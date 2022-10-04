package com.conorward.littlepaydemo;

import com.conorward.littlepaydemo.generator.TripGenerator;
import com.conorward.littlepaydemo.models.Tap;
import com.conorward.littlepaydemo.models.Trip;
import com.opencsv.ICSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.List;

@SpringBootApplication
public class LittlePayDemoApplication implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(LittlePayDemoApplication.class);
	@Autowired
	private TripGenerator tripGenerator;

	public static void main(String[] args) {
		SpringApplication.run(LittlePayDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (args.length > 0) {
			List<Tap> taps = new CsvToBeanBuilder(new FileReader(args[0]))
					.withType(Tap.class).build().parse();
			List<Trip> trips = tripGenerator.generateTrips(taps);
			try (Writer writer  = new FileWriter("trips.csv")) {
				StatefulBeanToCsv<Trip> sbc = new StatefulBeanToCsvBuilder<Trip>(writer)
						.withQuotechar(ICSVWriter.NO_QUOTE_CHARACTER)
						.withSeparator(ICSVWriter.DEFAULT_SEPARATOR)
						.build();
				sbc.write(trips);
			}
		} else {
			logger.error("Please provide the path to a valid CSV file containing the Tap information");
		}
	}
}
