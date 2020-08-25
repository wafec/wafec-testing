package wafec.testing.execution.openstack.robustness.analysis;

public enum OpenStackStateDestinationTypes {
    CORRECT_STATE,
    CORRECT_STATE_BUT_DIFFERENT_PATH,
    INCORRECT_BUT_A_MODELED_STATE,
    INCORRECT_BUT_A_NOT_MODELED_STATE,
    INCONCLUSIVE,
    ERROR_STATE
}
