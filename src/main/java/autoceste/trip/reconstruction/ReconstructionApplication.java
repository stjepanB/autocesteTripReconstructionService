package autoceste.trip.reconstruction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReconstructionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReconstructionApplication.class, args);
	}

}
