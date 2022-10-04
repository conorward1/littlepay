package com.conorward.littlepaydemo.generator;

import com.conorward.littlepaydemo.models.Tap;
import com.conorward.littlepaydemo.models.TapType;
import com.conorward.littlepaydemo.models.Trip;
import com.conorward.littlepaydemo.models.TripStatus;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TripGenerator {

    /***
     * Generates trips from the input taps.
     *
     * @param taps List of Tap objects that describe where people tapped on and off
     * @return List of Trip objects which is generated from the taps param
     */
    public List<Trip> generateTrips(List<Tap> taps) {
        Map<String, Tap> panIdentifiedTapOns = new HashMap<>();
        List<Trip> trips = new ArrayList<>();
        for (Tap currentTap : taps) {
            if (panIdentifiedTapOns.containsKey(currentTap.getPan())) {
                Tap tapOn = panIdentifiedTapOns.get(currentTap.getPan());
                if (currentTap.getType() == TapType.ON) {
                    trips.add(createIncompleteTrip(tapOn));
                    panIdentifiedTapOns.put(currentTap.getPan(), currentTap);
                } else if (currentTap.getType() == TapType.OFF) {
                    trips.add(createTrip(tapOn, currentTap));
                    panIdentifiedTapOns.remove(currentTap.getPan());
                }
            } else if (currentTap.getType() == TapType.ON) {
                panIdentifiedTapOns.put(currentTap.getPan(), currentTap);
            }
        }
        for (Tap inCompleteTrip : panIdentifiedTapOns.values()) {
            trips.add(createIncompleteTrip(inCompleteTrip));
        }
        return trips;
    }

    private Trip createIncompleteTrip(Tap tap) {
        return Trip.builder()
                .started(tap.getDate())
                .fromStopId(tap.getStop())
                .chargeAmount(tap.getStop().calculateIncompleteTripCharge())
                .companyId(tap.getCompanyId())
                .busId(tap.getBusId())
                .pan(tap.getPan())
                .status(TripStatus.INCOMPLETE)
                .build();
    }

    private Trip createTrip(Tap tapOn, Tap tapOff) {
        if (!tapOn.getBusId().equals(tapOff.getBusId())) {
            return createIncompleteTrip(tapOn);
        }
        double chargeAmount = tapOn.getStop().calculateChargeToStop(tapOff.getStop());
        TripStatus tripStatus = chargeAmount == 0 ? TripStatus.CANCELLED : TripStatus.COMPLETED;
        return Trip.builder()
                .started(tapOn.getDate())
                .finished(tapOff.getDate())
                .durationSecs(Duration.between(tapOn.getDate(), tapOff.getDate()).getSeconds())
                .fromStopId(tapOn.getStop())
                .toStopId(tapOff.getStop())
                .chargeAmount(chargeAmount)
                .companyId(tapOn.getCompanyId())
                .busId(tapOn.getBusId())
                .pan(tapOn.getPan())
                .status(tripStatus)
                .build();
    }
}
