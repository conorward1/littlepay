package com.conorward.littlepaydemo.models;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Trip {
    @CsvDate(value = "dd-MM-yyyy' 'HH:mm:ss")
    @CsvBindByName(column = "Started")
    private LocalDateTime started;

    @CsvDate(value = "dd-MM-yyyy' 'HH:mm:ss")
    @CsvBindByName(column = "Finished")
    private LocalDateTime finished;

    @CsvBindByName(column = "DurationSecs")
    private long durationSecs;

    @CsvBindByName(column = "FromStopId")
    private Stop fromStopId;

    @CsvBindByName(column = "ToStopId")
    private Stop toStopId;

    @CsvNumber(value = "#0.00")
    @CsvBindByName(column = "ChargeAmount")
    private double chargeAmount;

    @CsvBindByName(column = "CompanyId")
    private String companyId;

    @CsvBindByName(column = "BusId")
    private String busId;

    @CsvBindByName(column = "PAN")
    private String pan;

    @CsvBindByName(column = "Status")
    private TripStatus status;
}
