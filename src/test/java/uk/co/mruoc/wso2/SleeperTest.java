package uk.co.mruoc.wso2;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SleeperTest {

    private final Sleeper sleeper = new Sleeper();

    @Test
    public void shouldSleepForGivenNumberOfMilliseconds() {
        int milliseconds = 500;

        DateTime before = DateTime.now();
        sleeper.sleep(milliseconds);
        DateTime after = DateTime.now();

        assertThat(getDifferenceInMillis(before, after)).isGreaterThan(milliseconds);
    }

    private int getDifferenceInMillis(DateTime before, DateTime after) {
        Period period = new Period(before, after, PeriodType.millis());
        return period.getMillis();
    }
}
