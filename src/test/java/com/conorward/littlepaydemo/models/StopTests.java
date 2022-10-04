package com.conorward.littlepaydemo.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class StopTests {

    @ParameterizedTest
    @MethodSource(value = "incompleteChargeAmount")
    void validateIncompleteChargeForEachStop(Stop stop, double expectedIncompleteCharge) {
        Assertions.assertEquals(expectedIncompleteCharge, stop.calculateIncompleteTripCharge());
    }

    @ParameterizedTest
    @MethodSource(value = "completeChargeAmount")
    void validateCompleteChargeForEachStop(Stop fromStop, Stop toStop, double expectedIncompleteCharge) {
        Assertions.assertEquals(expectedIncompleteCharge, fromStop.calculateChargeToStop(toStop));
    }

    private static Stream<Arguments> incompleteChargeAmount() {
        return Stream.of(
                Arguments.of(Stop.STOP1, 7.30),
                Arguments.of(Stop.STOP2, 5.50),
                Arguments.of(Stop.STOP3, 7.30)
        );
    }

    private static Stream<Arguments> completeChargeAmount() {
        return Stream.of(
                Arguments.of(Stop.STOP1, Stop.STOP1, 0),
                Arguments.of(Stop.STOP1, Stop.STOP2, 3.25),
                Arguments.of(Stop.STOP1, Stop.STOP3, 7.30),
                Arguments.of(Stop.STOP2, Stop.STOP1, 3.25),
                Arguments.of(Stop.STOP2, Stop.STOP2, 0),
                Arguments.of(Stop.STOP2, Stop.STOP3, 5.50),
                Arguments.of(Stop.STOP3, Stop.STOP1, 7.30),
                Arguments.of(Stop.STOP3, Stop.STOP2, 5.50),
                Arguments.of(Stop.STOP3, Stop.STOP3, 0)
        );
    }
}
