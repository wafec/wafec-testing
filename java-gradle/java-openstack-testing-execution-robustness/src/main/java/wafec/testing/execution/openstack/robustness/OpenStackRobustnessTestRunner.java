package wafec.testing.execution.openstack.robustness;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import wafec.testing.core.JsonReBuilder;
import wafec.testing.core.JsonUtils;
import wafec.testing.execution.*;
import wafec.testing.execution.openstack.OpenStackTestDriver;
import wafec.testing.execution.robustness.*;
import wafec.testing.support.rabbitmq.RabbitMqDataInterception;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OpenStackRobustnessTestRunner extends AbstractRobustnessTestRunner {
    @Autowired
    private OneRoundTamper dataTamper;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private TestOutputRepository testOutputRepository;
    @Autowired
    private TestExecutionObservedOutputRepository testExecutionObservedOutputRepository;

    Logger logger = LoggerFactory.getLogger(OpenStackRobustnessTestRunner.class);

    public OpenStackRobustnessTestRunner(OpenStackTestDriver openStackTestDriver,
                                         RabbitMqDataInterception rabbitMqDataInterception,
                                         OpenStackDataParser openStackDataParser) {
        super(openStackTestDriver, rabbitMqDataInterception, openStackDataParser);
    }

    public void setInjectionOperatorType(OpenStackInjectionOperatorType operatorType) {
        if (operatorType.equals(OpenStackInjectionOperatorType.LARANJEIRO))
            dataTamper.setInjectionManager(null);
        else if (operatorType.equals(OpenStackInjectionOperatorType.KANSO))
            dataTamper.setInjectionManager(
                    applicationContext.getBean(KansoInjectionManager.class, environmentController)
            );
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
            createObservedOutput(messageNode, robustnessTestExecution);
            final var methodNode = messageNode.get("method");
            var argsNode = messageNode.get("args");
            if (methodNode != null && argsNode != null) {
                var jsonReBuilder = applicationContext.getBean(JsonReBuilder.class, argsNode);
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

    private void createObservedOutput(JsonNode messageNode, RobustnessTestExecution robustnessTestExecution) {
        var testExecution = robustnessTestExecution.getTestExecution();
        var currentTestExecutionInput = testExecution.getCurrentTestExecutionInput();
        if (currentTestExecutionInput == null) {
            logger.debug("Skipping. TestExecutionInput is null.");
            return;
        }
        if (currentTestExecutionInput.getStatus().equals(TestExecutionInput.STATUS_IN_USE)) {
            var testInput = currentTestExecutionInput.getTestInput();
            List<String> fieldValueList = new ArrayList<>();
            var iterator = messageNode.fields();
            while (iterator.hasNext()) {
                var entry = iterator.next();
                var value = entry.getValue();
                if (value.getNodeType().equals(JsonNodeType.STRING) || value.getNodeType().equals(JsonNodeType.NUMBER)) {
                    fieldValueList.add(String.format("%s=%s", entry.getKey(), value.textValue()));
                }
            }
            if (fieldValueList.size() > 0) {
                var names = String.join(", ", fieldValueList);
                var testOutput = new TestOutput();
                testOutput.setPosition(0);
                testOutput.setCreatedAt(new Date());
                testOutput.setOutput(names);
                testOutput.setSourceType("message");
                testOutput.setSource("test-runner");
                testOutputRepository.save(testOutput);
                TestExecutionObservedOutput observedOutput =
                        TestExecutionObservedOutput.of(testExecution, testInput, testOutput);
                testExecutionObservedOutputRepository.save(observedOutput);
            }
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
