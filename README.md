# Little Pay Coding Exercise

### Description
This application parses a specified CSV file with Tap On and Tap Off data and 
outputs a CSV file with the generated Trip information for each Tap On and Off with the
charge amount.

### Assumptions
1. Input file is well-formed and not missing any data.
2. The data in the input file is ordered by Date.
3. If a Tap Off comes before a Tap On then the Tap Off is disregarded.
4. If the Tap On Bus ID does not match the Tap Off Bus ID then this will count as one
   INCOMPLETE trip and the Tap Off will be disregarded.

### Requirements
Java 17 or later. I haven't used anything specifically released with Java 17 
but just used the latest LTS version. Java 12's switch statements is probably the latest updates I've used.
I use `jenv` to switch between different versions of Java.

### Build
It's possible to build the jar by running `mvn clean install`
This will store the jar in the `target` directory.

### Run
To run the Application using maven. Run the following command:

```
mvn spring-boot:run -Dspring-boot.run.arguments=src/test/resources/test_taps.csv
```

This uses a test file called `test_taps.csv` and generates a file at the project root called `trip.csv`

You can also run the application using the jar built from running `mvn clean install`. Run the following:

```
java -jar target/little-pay-demo-0.0.1.jar src/test/resources/test_taps.csv
```
I have also added the jar to the project root so the following can also be run:
```
java -jar little-pay-demo-0.0.1.jar src/test/resources/test_taps.csv
```

### To Do (Slight issues)
1. Update headers of output file so that they are not all Upper Case.
2. Update the order of the headers so that they aren't alphabetical.

### Possible Improvements
1. Instead of disregarding a Tap Off if it comes before a Tap On, it may have been better to charge the maximum amount and create an INCOMPLETE trip from it.
2. Instead of hardcoding the Charge Amounts in the Stop Enums, I could have made it configurable by creating separate classes that can make use of the `@Value` annotation
   
   Change in Stop enum:
   ```
    STOP1(new Stop1Charges()),
    STOP2(new Stop2Charges()),
    STOP3(new Stop3Charges());

    private final StopCharges stopCharges;
   ```
   And one of the StopCharges classes would look like the following:
   ```
   public class Stop1Charges extends StopCharges {
       private final double chargeToStop1 = 0;
       
       @Value("${charges.stop1.to.stop2:3.25}")
       private final double chargeToStop2;
       
       @Value("${charges.stop1.to.stop3:7.30}")
       private final double chargeToStop3;
   }
   ```
   And the application.properties would look like
   ```
   charges.stop1.to.stop2=3.25
   charges.stop2.to.stop3=5.50
   charges.stop1.to.stop3=7.30
   ```
3. Add some exception handling to the main run method, even though it can be assumed that the input is well-formed and not missing data.
4. Possibly no need to add the busId logic as users may swap buses on their way from Stop1 to Stop3 for example.