package autoceste.trip.reconstruction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
public class ReconstructionApplication {
    public static void main(String[] args) {
        try {
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }

        SpringApplication.run(ReconstructionApplication.class, args);
    }
}
