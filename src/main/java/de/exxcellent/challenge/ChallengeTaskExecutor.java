package de.exxcellent.challenge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**Static class manageing exxcellent challenge tasks.<br><br>
 * 
 * This class is a controller dispatching command line arguments towards one or more registered tasks.<br>
 * Register a {@link ChallengeTask} via calling {@link ChallengeTaskExecutor#registerTask(String, ChallengeTask)}.
 * 
 * @author Kaiser
 *
 */
public class ChallengeTaskExecutor {
	
	private static Map<String, ChallengeTask> registeredTasks = new HashMap<>();
	
	private ChallengeTaskExecutor() {}
	
	public static void registerTask(String commandLineArgument, ChallengeTask challengeTask) {
		registeredTasks.put(commandLineArgument, challengeTask);
	}
	
	public static void executeTasks(String... args) {
		
		if (args.length == 0) {
			System.out.println("[ChallengeTaskExecutor] No args provided, exiting.");
			return;
		}
		
		String command = args[0];
		String dataSource = args[1];
		
		executeCommand(command, List.of(args[1]));
	}
	
	private static void executeCommand(String command, List<String> commandArgs) {
		if (registeredTasks.containsKey(command)) registeredTasks.get(command).executeTask(commandArgs);
	}
	
	private static boolean isTaskCommand(String arg) {
		return arg.substring(0, 2).compareTo("--") == 0;
	}
}
