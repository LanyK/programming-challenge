package de.exxcellent.challenge;

import java.util.stream.Stream;

public class FootballChallenge implements ChallengeTask {

	@Override
	public void executeTask(FixedSizeTable dataTable) {
		
		// Task: 
		//
		// The `football.csv` file contains results of the
	    // English Premier League. The columns labeled ‘Goals’
	    // and ‘Goals Allowed’ contain the total number of goals scored
	    // by and against each team (so Arsenal scored
	    // 79 goals themselves and had 36 goals scored against them).
	    // Read the file, then print the name of the team with the smallest
	    // distance (absolute difference) between ‘Goals’ and ‘Goals Allowed’.
		
		int minDistance = Integer.MAX_VALUE;
		String minDistanceTeam = "";
		
		String[] team = dataTable.getColumn("Team");
		int[] goals = Stream.of(dataTable.getColumn("Goals")).mapToInt(elem -> Integer.parseInt(elem)).toArray();
		int[] goalsAllowed = Stream.of(dataTable.getColumn("Goals Allowed")).mapToInt(elem -> Integer.parseInt(elem)).toArray();
		
		for (int i = 0; i < team.length; ++i) {
			int distance = Math.abs(goals[i] - goalsAllowed[i]);
			
			if (distance < minDistance) {
				minDistance = distance;
				minDistanceTeam = team[i];
			}
		}
		
		System.out.printf("Team with smallest goal spread       : %s%n", minDistanceTeam);
	}
}
