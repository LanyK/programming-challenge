package de.exxcellent.challenge;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * JUnit 5 Tests for {@link CSVDataLoader}
 * 
 * @author Yannick Kaiser <yannick-kaiser@gmx.de>
 */
class ChallengeTaskExecutorTest {

	@BeforeAll
	static void setup() {
		System.out.println(" -- ChallengtTaskExecutor Tests --");
	}

	@Test
	void challengeRegistrationAndCallTest() {

		System.out.println("  - Challenge Task Executor Generic Test -");

		ChallengeTaskExecutor executor = new ChallengeTaskExecutor();
		executor.registerTask("dummy", new DummyTask());
		executor.registerTask("dammy", new DummyTask());
		assertDoesNotThrow(() -> executor.executeTask(new String[] {"--dummy", "src/test/resources/de/exxellent/challenge/valid.csv"}));
	}
	
	@Test
	void weatherChallengeTest() {
		
		System.out.println("  - Weather Task Executor Generic Test -");

		ChallengeTaskExecutor executor = new ChallengeTaskExecutor();
		executor.registerTask("weather", new WeatherDataChallenge());
		assertDoesNotThrow(() -> executor.executeTask(new String[] {"--weather", "src/main/resources/de/exxcellent/challenge/weather.csv"}));
	}
	
	@Test
	void footballChallengeTest() {
		
		System.out.println("  - Football Task Executor Generic Test -");

		ChallengeTaskExecutor executor = new ChallengeTaskExecutor();
		executor.registerTask("football", new FootballChallenge());
		assertDoesNotThrow(() -> executor.executeTask(new String[] {"--football", "src/main/resources/de/exxcellent/challenge/football.csv"}));
	}
	
	class DummyTask implements ChallengeTask {

		@Override
		public void executeTask(FixedSizeTable dataTable) {
			System.out.println("  - DummyTask has been executed -");
		}
	}

}