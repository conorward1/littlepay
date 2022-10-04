package com.conorward.littlepaydemo;

import com.conorward.littlepaydemo.models.Trip;
import com.opencsv.bean.CsvToBeanBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@SpringBootTest(args = {"src/test/resources/test_taps.csv"})
class LittlePayDemoApplicationTests {

	@Test
	void applicationRunsAgainstTestData() throws FileNotFoundException {
		List<Trip> expectedTrips = new CsvToBeanBuilder(new FileReader("src/test/resources/expected_trips.csv")).withType(Trip.class).build().parse();
		List<Trip> trips = new CsvToBeanBuilder(new FileReader("trips.csv")).withType(Trip.class).build().parse();
		Assertions.assertEquals(expectedTrips, trips);
	}

}
