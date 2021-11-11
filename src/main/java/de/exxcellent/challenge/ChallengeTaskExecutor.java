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
		
		int cmdIndex = 0;
		int argIndex = 1;
		
		String command = null;
		List<String> taskArgs = null;
		
		outer:while (cmdIndex < args.length) {
			
			if (!isTaskCommand(args[cmdIndex])) continue;
			
			// found a task command (--command), now gather any arguments to it, 
			// which are args up until the end of the args array,
			// or up until the next task command.
			
			command = args[cmdIndex];
			taskArgs = new ArrayList<>();
			
			while (argIndex < args.length) {
				
				if(isTaskCommand(args[argIndex])) { // parse next task command upon finding one
					
					executeCommand(command, taskArgs);
					
					command = null;
					taskArgs = null;
					
					cmdIndex = argIndex;
					argIndex = argIndex + 1;
					continue outer;
				} // else
				
				taskArgs.add(args[argIndex]);
			}
			
			if (command != null) {
				executeCommand(command, taskArgs);
			}
		}
	}
	
	private static void executeCommand(String command, List<String> commandArgs) {
		if (registeredTasks.containsKey(command)) registeredTasks.get(command).executeTask(commandArgs);
	}
	
	private static boolean isTaskCommand(String arg) {
		return arg.substring(0, 2).compareTo("--") == 0;
	}
}
