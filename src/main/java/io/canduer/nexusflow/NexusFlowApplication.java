package io.canduer.nexusflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class NexusFlowApplication {

	public static void main(String[] args) {
		SpringApplication.run(NexusFlowApplication.class, args);
	}

}
