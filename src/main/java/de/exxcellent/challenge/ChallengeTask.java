package de.exxcellent.challenge;

public interface ChallengeTask {
	
	/** Executes a (query) task on a data table. Implementions can be made for any given task,<br>
	 *  and then registered inside an instance of {@link ChallengeTaskExecutor},<br>
	 *  which can dispatch command line arguments to the right task executor depending on the arguments given.
	 * 
	 * @param dataTable the {@link FixedSizeTable} instance to base the task upon
	 */
	public void executeTask(FixedSizeTable dataTable);
}
