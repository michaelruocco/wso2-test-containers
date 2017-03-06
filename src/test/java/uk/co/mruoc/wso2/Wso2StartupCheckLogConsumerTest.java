package uk.co.mruoc.wso2;

import org.junit.Test;
import org.testcontainers.containers.output.OutputFrame;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class Wso2StartupCheckLogConsumerTest {

    private static final int TIMEOUT = 500;
    private static final int DELAY = 250;
    private static final String STARTUP_MESSAGE = "CarbonUIServiceComponent Mgt Console URL  :";

    private final OutputFrame outputFrame = mock(OutputFrame.class);
    private final StartupCheckLogConsumer consumer = new Wso2StartupCheckLogConsumer();

    @Test
    public void shouldReturnTrueIfStartupMessageIsConsumed() {
        given(outputFrame.getUtf8String()).willReturn(STARTUP_MESSAGE);

        consumer.accept(outputFrame);

        assertThat(consumer.waitForStartupMessageInLog(TIMEOUT, DELAY)).isTrue();
    }

    @Test
    public void shouldHandleNullMessages() {
        given(outputFrame.getUtf8String()).willReturn(null, STARTUP_MESSAGE);

        consumer.accept(outputFrame);
        consumer.accept(outputFrame);

        assertThat(consumer.waitForStartupMessageInLog(TIMEOUT, DELAY)).isTrue();
    }

    @Test(expected = StartupTimeoutException.class)
    public void shouldThrowExceptionIfStartupMessageIsNotConsumed() {
        consumer.waitForStartupMessageInLog(TIMEOUT, DELAY);
    }

}
