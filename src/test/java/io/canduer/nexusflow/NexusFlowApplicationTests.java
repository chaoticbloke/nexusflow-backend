package io.canduer.nexusflow;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@SpringBootTest(
		properties = {
				"spring.kafka.bootstrap-servers=localhost:9092",
				"spring.datasource.url=jdbc:postgresql://localhost:5432/nexusflow"
		}
)
class NexusFlowApplicationTests {

	@Disabled
	@Test
	void contextLoads() {
	}

}
