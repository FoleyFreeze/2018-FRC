package org.usfirst.frc.team910.robot.auton.steps;

import java.util.ArrayList;

public class ParallelSet extends AutonSet {

	private ArrayList<AutonStep> steps;
	private ArrayList<Boolean> stepDones;

	public ParallelSet() {
		steps = new ArrayList<>();
		stepDones = new ArrayList<>();
	}
	
	@Override
	public void addStep(AutonStep newStep) {

		steps.add(newStep);
		stepDones.add(false);

	}

	@Override
	public void init() {

		// Looping and initializing all steps at same time
		for (int i = 0; i < steps.size(); i++) {
			steps.get(i).init();
		}
	}

	@Override
	public void run() {

		for (int i = 0; i < steps.size(); i++) {
			if (stepDones.get(i) == false) {
				steps.get(i).run();
			}

		}

	}

	@Override
	public boolean isDone() {

		boolean allDone = true;
		// Checking if steps are all done then returning allDone if complete
		for (int i = 0; i < steps.size(); i++) { // Checks for step completeness

			if (stepDones.get(i) == false) { // Continues to run steps until complete

				if (steps.get(i).isDone()) {
					stepDones.set(i, true);

				} else {
					allDone = false; // If even one step isn't done, allDone is false

				}
			}
		}

		return allDone;
	}

}
