package uk.co.mruoc.wso2;

import org.testcontainers.containers.output.OutputFrame;

import java.util.function.Consumer;

public class StartupCheckLogConsumer implements Consumer<OutputFrame> {

    private static final int DEFAULT_TIMEOUT = 60000;
    private static final int DEFAULT_DELAY = 1000;

    private final Sleeper sleeper = new Sleeper();
    private final String startupMessage;

    private boolean started = false;

    public StartupCheckLogConsumer(String startupMessage) {
        this.startupMessage = startupMessage;
    }

    @Override
    public void accept(OutputFrame outputFrame) {
        String output = outputFrame.getUtf8String();
        if (isStartMessage(output))
            started = true;
    }

    public boolean waitForStartupMessageInLog() {
        return waitForStartupMessageInLog(DEFAULT_TIMEOUT, DEFAULT_DELAY);
    }

    public boolean waitForStartupMessageInLog(int timeout, int delay) {
        int totalWait = 0;
        while(!isStarted() && totalWait < timeout) {
            sleeper.sleep(delay);
            totalWait += delay;
        }
        if (!isStarted())
            throw new StartupTimeoutException(startupMessage, timeout);
        return true;
    }

    private boolean isStartMessage(String value) {
        if (value != null)
            System.out.print(value);
        return value.contains(startupMessage);
    }

    private boolean isStarted() {
        return started;
    }

}
