package com.conorward.littlepaydemo.models;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Tap {
    @CsvBindByName(column = "ID", required = true)
    private String id;

    @CsvDate(value = "dd-MM-yyyy' 'HH:mm:ss")
    @CsvBindByName(column = "DateTimeUTC", required = true)
    private LocalDateTime date;

    @CsvBindByName(column = "TapType", required = true)
    private TapType type;

    @CsvBindByName(column = "StopId", required = true)
    private Stop stop;

    @CsvBindByName(column = "CompanyId", required = true)
    private String companyId;

    @CsvBindByName(column = "BusId", required = true)
    private String busId;

    @CsvBindByName(column = "PAN", required = true)
    private String pan;
}
