package myToolWindow.time;

import com.intellij.util.ui.UIUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import myToolWindow.game.Game;

public class Timer implements Runnable {

	private static final int ONE_SECOND = 1000;
	public static final int _25_MINS = 1500;

	private Game game;
	private int countdownInSeconds = _25_MINS;
	private boolean stopTimerSignal;

	public void start(Game game) {
		this.game = game;
		run();
	}

	@Override
	public void run() {
		countdownInSeconds--;

		game.onTimeChange(countdownInSeconds);

		if (!stopTimerSignal) {
			reRunInASecond();
		}
	}

	void reRunInASecond() {
		javax.swing.Timer timer = UIUtil.createNamedTimer("reRunInASecond timer", ONE_SECOND, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				run();
			}
		});
		timer.setRepeats(false);
		timer.start();
		// PlatformUI.getWorkbench().getDisplay().timerExec(ONE_SECOND, this);
	}

	public void stop() {
		stopTimerSignal = true;
	}

}
