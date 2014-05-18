package myToolWindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import myToolWindow.game.Game;
import myToolWindow.game.Programmer;
import myToolWindow.game.Scoreboard;
import myToolWindow.time.TimeFormatter;
import myToolWindow.time.Timer;

/**
 * IntelliJ version of the <a href="http://www.happyprog.com/pairhero/">PairHero
 * plugin</a>. Ported by <a href="https://github.com/marybel">Marybel Archer</a>
 */
public class PairHeroToolWindowFactory implements ToolWindowFactory {
	public static final String BUTTON_LABEL_START = "Start";
	public static final String BUTTON_LABEL_STOP = "Stop";
	public static final String ICONS_PATH = "icons/";

	private JButton startGameButton;
	private JPanel myToolWindowContent;
	private JPanel leftProgrammerPanel;
	private JPanel rightProgrammerPanel;
	private JPanel scorePanel;
	private JLabel scoreLabel;
	private JLabel timerLabel;
	private JLabel messageLabel;

	private Game game;
	private Scoreboard scoreboard;
	private Programmer leftProgrammer;
	private Programmer rightProgrammer;
	private int messageDelayCounter;

	public PairHeroToolWindowFactory() {
		scoreboard = new Scoreboard();

		startGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (startGameButton.getText() == BUTTON_LABEL_START) {
					onStart();
				} else {
					onStop();
				}
			}
		});
	}

	// Create the tool window content.
	public void createToolWindowContent(Project project, ToolWindow toolWindow) {
		ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
		Content content = contentFactory.createContent(myToolWindowContent, "", false);
		toolWindow.getContentManager().addContent(content);

		leftProgrammer = new Programmer(leftProgrammerPanel);
		rightProgrammer = new Programmer(rightProgrammerPanel);
		createScoreArea(scorePanel);
	}

	private void createScoreArea(JPanel scorePanel) {
		JComponent line1 = new JPanel(new GridLayout(2, 1));
		line1.add(new JLabel("Score: "));
		scoreLabel = new JLabel("0");
		line1.add(scoreLabel);
		scorePanel.add(line1);

		JComponent line2 = new JPanel(new GridLayout(2, 1));
		messageLabel = new JLabel(getImageIcon("blank"));
		line2.add(messageLabel);
		scorePanel.add(line2);

		JComponent line3 = new JPanel(new GridLayout(2, 1));
		line3.add(new JLabel("Time Left: "));
		this.timerLabel = new JLabel("25:00");
		line3.add(timerLabel);
		scorePanel.add(line3);
	}

	private void onStart() {
		if (ableToCreatePlayers()) {
			startGame();
			startGameButton.setText(BUTTON_LABEL_STOP);
		}
	}

	private void startGame() {
		game = new Game(this, new Timer(), leftProgrammer, rightProgrammer, scoreboard);
		game.start();
	}

	private boolean ableToCreatePlayers() {
		// TODO: Add dialog to set names and player avatars
		leftProgrammer.resetStats();
		rightProgrammer.resetStats();
		leftProgrammer.setName("Explorer");
		rightProgrammer.setName("Wizard"/* dialog.getPlayerTwoName() */);
		leftProgrammer.setAvatar("explorator"/* dialog.getPlayerOneAvatar() */);
		rightProgrammer.setAvatar("wizard"/* dialog.getPlayerTwoAvatar() */);
		scoreboard.resetStats();
		updateScore(scoreLabel, Long.toString(scoreboard.getScore()));

		return true;
	}

	public void onGameFinished() {
		// TODO: Dialog to show score???
		startGameButton.setText(BUTTON_LABEL_START);
	}

	public void onTimeChange(int timeInSeconds) {
		updateScore(this.timerLabel, TimeFormatter.formatTime(timeInSeconds));
		updateMessageToDefault();
	}

	private void updateScore(JLabel label, final String text) {
		label.setText(text);
	}

	public void onStop() {
		// TODO: Add confirmation dialog
		game.stop();
		startGameButton.setText(BUTTON_LABEL_START);
	}

	public void onSwitchRole() {
		game.onSwitchRole();
		showMessageAndUpdateScore(getSwitchRoleImage(), scoreboard.getScore());
	}

	public void onRefactoring() {
		scoreboard.addRefactoring();
		showMessageAndUpdateScore("refactoring", scoreboard.getScore());
	}

	public void onGreenTest() {
		game.onGreenTest();
		showMessageAndUpdateScore("green", scoreboard.getScore());
	}

	private void showMessageAndUpdateScore(String imageKey, long score) {
		updateMessage(messageLabel, getImageIcon(imageKey));
		updateScore(score);
		messageDelayCounter = 3;
	}

	private void updateScore(long score) {
		updateScore(scoreLabel, String.format("%d", score));
	}

	private void updateMessageToDefault() {
		if (messageDelayCounter < 0) {
			updateMessage(messageLabel, getImageIcon("blank"));
		}
		messageDelayCounter--;
	}

	private void updateMessage(final JLabel label, final ImageIcon image) {
		label.setIcon(image);
	}

	private String getSwitchRoleImage() {
		int multiplier = scoreboard.getLastMultiplier();
		if (multiplier == Scoreboard.MULTIPLIER_4X) {
			return "switch-4x";
		} else if (multiplier == Scoreboard.MULTIPLIER_2X) {
			return "switch-2x";
		} else {
			return "switch";
		}
	}

	private ImageIcon getImageIcon(String imageName) {
		return new ImageIcon(getClass().getResource(ICONS_PATH + imageName + ".png"));
	}

	private void createUIComponents() {
		// TODO: place custom component creation code here
		leftProgrammerPanel = new JPanel(new GridLayout(4, 1));
		rightProgrammerPanel = new JPanel(new GridLayout(4, 1));
		scorePanel = new JPanel(new GridLayout(3, 1));
	}

	public boolean isGameOngoing() {
		return game != null && game.isOnGoing();
	}
}
