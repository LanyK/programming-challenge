package de.exxcellent.challenge;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**Static class manageing exxcellent challenge tasks.<br><br>
 * 
 * This class is a controller dispatching command line arguments towards one or more registered tasks.<br>
 * Register a {@link ChallengeTask} via calling {@link ChallengeTaskExecutor#registerTask(String, ChallengeTask)}.
 * 
 * @author Kaiser
 *
 */
public class ChallengeTaskExecutor {
	
	private Map<String, ChallengeTask> registeredTasks = new HashMap<>();
	
	public ChallengeTaskExecutor() {}
	
	/** Register a ChallengeTask object under a given command name <code>name</code>.<br>
	 *  {@link #executeTask(String...)} will run the task if provided with <code>--name</code> as an argument.
	 *  Commands expect a reference to a data source as their second argument.
	 * 
	 * @param commandLineArgument
	 * @param challengeTask
	 */
	public void registerTask(String commandLineArgument, ChallengeTask challengeTask) {
		this.registeredTasks.put(commandLineArgument, challengeTask);
	}
	
	/** Execute the appropriate registered {@link ChallengeTask} depending on the command line arguments given.
	 * 
	 * @param args - expects the command line arguments of the application 
	 * @throws IOException
	 */
	public void executeTask(String... args) throws IOException {
		
		
		if (args.length == 0) {
			System.out.println("[ChallengeTaskExecutor] No args provided, exiting.");
			return;
		}
		
		String command = args[0];
		
		if (!isTaskCommand(command)) {
			System.out.println("[ChallengeTaskExecutor] First launch argument not a --command, exiting.");
			return;
		}
		
		command = command.substring(2); // remove the "--" from the command
		
		if (!this.registeredTasks.containsKey(command)) {
			System.out.println("[ChallengeTaskExecutor] Command not registered, exiting.");
			return;
		}
		
		if (args.length < 2) {
			System.out.println("[ChallengeTaskExecutor] No data source argument in position 2, exiting.");
			return;
		}
		
		String dataSource = args[1];
		
		// Use a TableDataLoader implementation to generate a FixedSizeTable for the ChallengeTask to access
		TableDataLoader dataLoader = new CSVDataLoader(Path.of(dataSource), null, true);
		FixedSizeTable table = new FixedSizeTable();
		
		Optional<String[]> headerLine = dataLoader.getHeaderLine();
		
		if (headerLine.isPresent()) {
			table.setColumnHeaders(headerLine.get());
		}
		
		dataLoader.streamOfRows().forEach(row -> table.addRow(row));
		
		this.execute(command, table);
	}
	
	/**Tries to execute the given command. Does nothing if no command of this name was registered in this {@link ChallengeTaskExecutor}
	 * 
	 * @param command
	 * @param dataTable
	 * 
	 * @throws NullPointerException if the command is not registered
	 */
	private void execute(String command, FixedSizeTable dataTable) {
		this.registeredTasks.get(command).executeTask(dataTable);
	}
	
	/**Helper method testing whether a given <code>String</code> starts with "--" two minus signs.<br>
	 * This marks it as a task command.
	 * 
	 * @param arg
	 * @return true if the argument starts with exactly "--"
	 */
	private static boolean isTaskCommand(String arg) {
		return arg.substring(0, 2).compareTo("--") == 0;
	}
}
