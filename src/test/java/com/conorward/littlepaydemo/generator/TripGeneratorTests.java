package com.conorward.littlepaydemo.generator;

import com.conorward.littlepaydemo.models.Stop;
import com.conorward.littlepaydemo.models.Tap;
import com.conorward.littlepaydemo.models.TapType;
import com.conorward.littlepaydemo.models.Trip;
import com.conorward.littlepaydemo.models.TripStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

class TripGeneratorTests {
    private static final String PAN = "5555555555554444";
    private final String companyId = "Some Company";
    private final String busId = "Bus42";

    private final TripGenerator tripGenerator = new TripGenerator();
    private final LocalDateTime tapOnDate = LocalDateTime.of(2022, 10, 4, 12, 25);
    private final LocalDateTime tapOffDate = LocalDateTime.of(2022, 10, 4, 12, 20);
    private final Tap tapOn = Tap.builder()
            .id("1")
            .pan(PAN)
            .type(TapType.ON)
            .busId(busId)
            .stop(Stop.STOP1)
            .companyId(companyId)
            .date(tapOnDate)
            .build();
    private final Tap tapOff = Tap.builder()
            .id("2")
            .pan(PAN)
            .type(TapType.OFF)
            .busId(busId)
            .stop(Stop.STOP2)
            .companyId(companyId)
            .date(tapOffDate)
            .build();

    @Test
    void tapOffFirstIsDisregarded() {
        LocalDateTime tapOff2Date = LocalDateTime.of(2022, 10, 4, 12, 30);
        Tap tapOff2 = tapOff.toBuilder().date(tapOff2Date).build();
        List<Tap> taps = List.of(tapOff, tapOn, tapOff2);

        List<Trip> trips = tripGenerator.generateTrips(taps);

        Trip expectedTrip = Trip.builder()
                .started(tapOnDate)
                .finished(tapOff2Date)
                .durationSecs(300)
                .fromStopId(Stop.STOP1)
                .toStopId(Stop.STOP2)
                .busId(busId)
                .companyId(companyId)
                .status(TripStatus.COMPLETED)
                .pan(PAN)
                .chargeAmount(3.25)
                .build();
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(trips.size()).isEqualTo(1);
            softly.assertThat(trips.get(0)).isEqualTo(expectedTrip);
        });
    }

    @Test
    void tapOnTapOffCreatesCompletedTrips() {
        Tap tapOn2 = tapOn.toBuilder().stop(Stop.STOP2).build();
        Tap tapOff2 = tapOff.toBuilder().stop(Stop.STOP3).build();
        List<Tap> taps = List.of(tapOn, tapOff, tapOn2, tapOff2);

        List<Trip> trips = tripGenerator.generateTrips(taps);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(trips.size()).isEqualTo(2);
            softly.assertThat(trips.get(0).getFromStopId()).isEqualTo(Stop.STOP1);
            softly.assertThat(trips.get(0).getToStopId()).isEqualTo(Stop.STOP2);
            softly.assertThat(trips.get(0).getStatus()).isEqualTo(TripStatus.COMPLETED);
            softly.assertThat(trips.get(1).getFromStopId()).isEqualTo(Stop.STOP2);
            softly.assertThat(trips.get(1).getToStopId()).isEqualTo(Stop.STOP3);
            softly.assertThat(trips.get(1).getStatus()).isEqualTo(TripStatus.COMPLETED);
        });
    }

    @Test
    void tapOnTapOnCreatesInCompleteTrips() {
        Tap tapOn2 = tapOn.toBuilder().stop(Stop.STOP2).build();
        List<Tap> taps = List.of(tapOn, tapOn2);

        List<Trip> trips = tripGenerator.generateTrips(taps);

        Assertions.assertEquals(2, trips.size());
        Assertions.assertEquals(TripStatus.INCOMPLETE, trips.get(0).getStatus());
        Assertions.assertEquals(TripStatus.INCOMPLETE, trips.get(1).getStatus());
    }

    @Test
    void tapOnTapOffWithDifferentPansCreatesOneIncompleteTrip() {
        Tap tapOff2 = tapOff.toBuilder().pan("5019717010103742").build();
        List<Tap> taps = List.of(tapOn, tapOff2);

        List<Trip> trips = tripGenerator.generateTrips(taps);

        Assertions.assertEquals(1, trips.size());
        Assertions.assertEquals(TripStatus.INCOMPLETE, trips.get(0).getStatus());
    }

    @Test
    void tapOnTapOffWithDifferentBusIdsCreatesOneIncompleteTrip() {
        Tap tapOff2 = tapOff.toBuilder().busId("Different Bus").build();
        List<Tap> taps = List.of(tapOn, tapOff2);

        List<Trip> trips = tripGenerator.generateTrips(taps);

        Assertions.assertEquals(1, trips.size());
        Assertions.assertEquals(TripStatus.INCOMPLETE, trips.get(0).getStatus());
    }

    @Test
    void tapOnTapOffInTheSameStopCreatesACancelledTrip() {
        Tap tapOff2 = tapOff.toBuilder().stop(Stop.STOP1).build();
        List<Tap> taps = List.of(tapOn, tapOff2);

        List<Trip> trips = tripGenerator.generateTrips(taps);

        Assertions.assertEquals(1, trips.size());
        Assertions.assertEquals(TripStatus.CANCELLED, trips.get(0).getStatus());
    }
}
