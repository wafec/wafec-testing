package wafec.testing.driver.openstack.client;

import java.util.Optional;

public class ServerUtils {
    private ServerUtils() {

    }

    public static boolean hasChangedState(Server a, Server b) {
        return !Optional.ofNullable(a)
                .map(Server::getPowerState)
                .equals(Optional.ofNullable(b).map(Server::getPowerState)) ||
                !Optional.ofNullable(b)
                .map(Server::getVmState)
                .equals(Optional.ofNullable(b).map(Server::getVmState)) ||
                !Optional.ofNullable(a)
                .map(Server::getStatus)
                .equals(Optional.ofNullable(b).map(Server::getStatus)) ||
                !Optional.ofNullable(a)
                .map(Server::getTaskState)
                .equals(Optional.ofNullable(b).map(Server::getTaskState));
    }
}
