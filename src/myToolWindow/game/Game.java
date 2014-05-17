package myToolWindow.game;

import myToolWindow.MyToolWindowFactory;
import myToolWindow.time.Timer;

public class Game {

	private final Timer timer;
	private final MyToolWindowFactory view;
	private final Programmer leftProgrammer;
	private final Programmer rightProgrammer;
	// private final JUnitListener testSubscriber;
	// private final RefactoringSubscriber refactoringSubscriber;
	private final Scoreboard scoreboard;

	public Game(MyToolWindowFactory view, Timer timer, Programmer leftProgrammer, Programmer rightProgrammer,
			/* JUnitListener testSubscriber, *//*
												 * RefactoringSubscriber
												 * refactoringSubscriber,
												 */Scoreboard scoreboard) {
		this.view = view;
		this.timer = timer;
		this.leftProgrammer = leftProgrammer;
		this.rightProgrammer = rightProgrammer;
		// this.testSubscriber = testSubscriber;
		// this.refactoringSubscriber = refactoringSubscriber;
		this.scoreboard = scoreboard;
	}

	public void start() {
		// testSubscriber.subscribe(this);
		// refactoringSubscriber.subscribe(this);

		leftProgrammer.drive();
		rightProgrammer.observe();

		timer.start(this);
	}

	public void stop() {
		timer.stop();
		// testSubscriber.unregister();
		// refactoringSubscriber.unregister();
	}

	public void onTimeChange(int seconds) {
		leftProgrammer.onTimeChange();
		rightProgrammer.onTimeChange();
		scoreboard.onTimeChange();

		if (seconds <= 0) {
			stop();
			view.onGameFinished();
			return;
		}
	}

	public void onSwitchRole() {
		leftProgrammer.switchRole();
		rightProgrammer.switchRole();
		scoreboard.addSwitch();

	}

	public void onGreenTest() {
		scoreboard.addGreenTest();

	}

	public void onRefactoring() {
		scoreboard.addRefactoring();
		view.onRefactoring();
	}

}
