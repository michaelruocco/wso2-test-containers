package uk.co.mruoc.wso2;

public class Wso2StartupCheckLogConsumer extends StartupCheckLogConsumer {

    private static final String STARTUP_MESSAGE = "CarbonUIServiceComponent Mgt Console URL  :";

    public Wso2StartupCheckLogConsumer() {
        super(STARTUP_MESSAGE);
    }

}
