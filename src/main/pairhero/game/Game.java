package pairhero.game;

import pairhero.PairHeroToolWindowFactory;
import pairhero.time.Timer;

public class Game {

    private final Timer timer;
    private final PairHeroToolWindowFactory view;
    private final Programmer leftProgrammer;
    private final Programmer rightProgrammer;
    private final Scoreboard scoreboard;

    public Game(PairHeroToolWindowFactory view, Timer timer, Programmer leftProgrammer, Programmer rightProgrammer,
                Scoreboard scoreboard) {
        this.view = view;
        this.timer = timer;
        this.leftProgrammer = leftProgrammer;
        this.rightProgrammer = rightProgrammer;
        this.scoreboard = scoreboard;
    }

    public void start() {
        leftProgrammer.drive();
        rightProgrammer.observe();
        timer.start(this);
    }

    public void stop() {
        timer.stop();
    }

    public void onTimeChange(int seconds) {
        leftProgrammer.onTimeChange();
        rightProgrammer.onTimeChange();
        scoreboard.onTimeChange();
        view.onTimeChange(seconds);
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

    public boolean isOnGoing() {
        return timer != null && timer.isRunning();
    }
}
