package io.github.artsobol.fitnessclub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FitnessClubApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitnessClubApplication.class, args);
    }

}
