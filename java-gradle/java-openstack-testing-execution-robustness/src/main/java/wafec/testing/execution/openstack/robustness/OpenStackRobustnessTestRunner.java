package wafec.testing.execution.openstack.robustness;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wafec.testing.core.JsonReBuilder;
import wafec.testing.core.JsonUtils;
import wafec.testing.execution.openstack.OpenStackTestDriver;
import wafec.testing.execution.robustness.*;
import wafec.testing.support.rabbitmq.RabbitMqDataInterception;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

@Component
public class OpenStackRobustnessTestRunner extends AbstractRobustnessTestRunner {
    @Autowired
    private OneRoundTamper dataTamper;

    public OpenStackRobustnessTestRunner(OpenStackTestDriver openStackTestDriver,
                                         RabbitMqDataInterception rabbitMqDataInterception,
                                         OpenStackDataParser openStackDataParser) {
        super(openStackTestDriver, rabbitMqDataInterception, openStackDataParser);
    }

    @Override
    protected ApplicationData adulterate(ApplicationData appData, RobustnessTestExecution robustnessTestExecution)
            throws CouldNotApplyOperatorException {
        if (appData instanceof OpenStackApplicationData) {
            return visit((OpenStackApplicationData) appData, robustnessTestExecution);
        } else {
            throw new CouldNotApplyOperatorException("appData not instance of OpenStackApplicationData");
        }
    }

    private ApplicationData visit(OpenStackApplicationData appData,
                                  RobustnessTestExecution robustnessTestExecution) throws
            CouldNotApplyOperatorException {
        JsonNode bodyNode = appData.getBodyNode();
        JsonNode messageNode = bodyNode.get("oslo.message");
        if (messageNode != null) {
            String messageContent = messageNode.textValue();
            messageContent = visit(messageContent, robustnessTestExecution);
            var bodyObject = (ObjectNode) bodyNode;
            bodyObject.remove("oslo.message");
            bodyObject.put("oslo.message", messageContent);
            return ApplicationData.of(generateBody(bodyObject));
        }
        else {
            throw new CouldNotApplyOperatorException("Invalid OSLO object");
        }
    }

    private String visit(String messageContent, final RobustnessTestExecution robustnessTestExecution)
            throws CouldNotApplyOperatorException {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            var messageNode = mapper.readTree(messageContent);
            final var methodNode = messageNode.get("method");
            var argsNode = messageNode.get("args");
            if (methodNode != null && argsNode != null) {
                var jsonReBuilder = new JsonReBuilder(argsNode);
                jsonReBuilder.rebuild((object) -> {
                    return mapper.convertValue(
                            dataTamper.adulterate(JsonUtils.getValue(object.getResult()),
                                    methodNode.asText(),
                                    object.getQualifiedContext(),
                                    robustnessTestExecution),
                            JsonNode.class
                    );
                });
                ObjectNode messageObject = (ObjectNode) messageNode;
                messageObject.remove("args");
                messageObject.set("args", argsNode);
            }
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(messageNode);
        } catch (IOException exc) {
            throw new CouldNotApplyOperatorException(exc.getMessage(), exc);
        }
    }

    private byte[] generateBody(JsonNode jsonNode) throws CouldNotApplyOperatorException {
        try (Writer writer = new StringWriter()) {
            JsonFactory factory = new JsonFactory();
            JsonGenerator generator = factory.createGenerator(writer);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeTree(generator, jsonNode);
            return writer.toString().getBytes();
        } catch (IOException exc) {
            throw new CouldNotApplyOperatorException(exc.getMessage(), exc);
        }
    }
}
