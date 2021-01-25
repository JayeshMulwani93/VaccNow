
VaccNow:

As a cautious action after Covid-19, VaccNow is an healthcare organization managing the process of Covid-19 vaccine to public,
so that the VaccNow is planning to build multi their digital channels for consuming a modern API for basic features. This is
planned to be API first approach, of well-tested functions and enabling agility of later modifications.


Technical Specifications:
1. Java 1.8
2. Spring Boot 2.4
3. H2 Database
4. Spring Data JPA
5. JUnit
6. Mockito
7. Swagger

API Contract:

You can find more details about the API contract using swagger:
http://localhost:8080/swagger-ui.html

Static Data:
Created UserIds: 1,2,3,4
Please use availability controller endpoints to get the Vaccination centre & vaccine details.

How to Run:
Step 1: mvn clean install
Step 2: java -jar VaccNow-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev