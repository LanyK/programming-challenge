package de.exxcellent.challenge;

import java.io.IOException;

/**<code>App</code> solves the eXXcellent weather challenge. See <code>README</code> for details.
 *
 * @author Benjamin Schmid <benjamin.schmid@exxcellent.de>
 * @author Yannick Kaiser <yannick-kaiser@gmx.de>
 */
public final class App {

    /**
     * This is the main entry method of the program.<br>
     * {@link ChallengeTask}s can be registered with the {@link ChallengeTaskExecutor} 
     * to have them automatically be executed depending on the <code>--command</code> command line argument provided.
     * @param args The CLI arguments passed
     */
    public static void main(String... args) {

    	ChallengeTaskExecutor taskExecutor = new ChallengeTaskExecutor();
    	taskExecutor.registerTask("weather", new WeatherDataChallenge());
    	taskExecutor.registerTask("football", new FootballChallenge());
    	
    	try {
			taskExecutor.executeTask(args);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
