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
 * Created by IntelliJ IDEA.
 * User: Alexey.Chursin
 * Date: Aug 25, 2010
 * Time: 2:09:00 PM
 */
public class MyToolWindowFactory implements ToolWindowFactory {

	public static final String NO_RUN = "No run";
	public static final String BUTTON_LABEL_START = "Start";
	public static final String BUTTON_LABEL_STOP = "Stop";
	public static final String ICONS_PATH = "/myToolWindow/icons/";

	private int messageDelayCounter;

	private JButton startGameButton;

	private JPanel myToolWindowContent;
	private JPanel leftProgrammerPanel;
	private JPanel rightProgrammerPanel;
	private JPanel scorePanel;
	private JLabel scoreLabel;
	private JLabel timerLabel;
	private JLabel messageLabel;
	private ToolWindow myToolWindow;
	private Project myProject;

	private Game game;
	Scoreboard scoreboard;
	Programmer leftProgrammer;
	Programmer rightProgrammer;

	public MyToolWindowFactory() {

		startGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// myToolWindow.hide(null);
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
		myToolWindow = toolWindow;
		myProject = project;
		ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
		Content content = contentFactory.createContent(myToolWindowContent, "", false);
		toolWindow.getContentManager().addContent(content);

		leftProgrammer = new Programmer(leftProgrammerPanel);
		// myToolWindowContent.add(leftProgrammerPanel);

		rightProgrammer = new Programmer(rightProgrammerPanel);
		// myToolWindowContent.add(rightProgrammerPanel);

		scoreboard = new Scoreboard();
		createScoreArea(scorePanel);
		// myToolWindowContent.add(scorePanel);
	}








	private void createScoreArea(JPanel scorePanel) {
		// scoreAreaComposite = new Composite(parent, SWT.BORDER);
		// scoreAreaComposite.setLayout(createLayout());
		JComponent line1 = new JPanel(new GridLayout(0, 2));
		line1.add(new JLabel("Score: "));
		scoreLabel = new JLabel("0");
		line1.add(scoreLabel);
		scorePanel.add(line1);

		JComponent line2 = new JPanel(new GridLayout(0, 2));
		messageLabel = new JLabel(getImageIcon("blank"));
		line2.add(messageLabel);
		scorePanel.add(line2);

		JComponent line3 = new JPanel(new GridLayout(0, 2));
		line3.add(new JLabel("Time Left: "));
		timerLabel = new JLabel("25:00");
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
		// StartDialog dialog = new
		// StartDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		// dialog.open();

		// if (dialog.getReturnCode() == Dialog.OK) {
		leftProgrammer.resetStats();
		rightProgrammer.resetStats();
		leftProgrammer.setName("leftProgrammer"/* dialog.getPlayerOneName() */);
		rightProgrammer.setName("rightProgrammer"/* dialog.getPlayerTwoName() */);
		leftProgrammer.setAvatar("explorator"/* dialog.getPlayerOneAvatar() */);
		rightProgrammer.setAvatar("wizard"/* dialog.getPlayerTwoAvatar() */);
		scoreboard.resetStats();
		updateScore(scoreLabel, Long.toString(scoreboard.getScore()));

		return true;
		// }

		// return false;
	}

	public void onGameFinished() {
		// TODO: DIALOG
		// EndDialog dialog = new
		// EndDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
		// scoreboard);
		// dialog.open();
		startGameButton.setText(BUTTON_LABEL_START);
	}

	public void onTimeChange(int timeInSeconds) {
		updateScore(timerLabel, TimeFormatter.formatTime(timeInSeconds));
		updateMessageToDefault();
		game.onTimeChange(timeInSeconds);
	}

	private void updateScore(final JLabel label, final String text) {
		label.setText(text);
		// scoreAreaComposite.layout();
	}

	public void onStop() {
		// TODO: Add confirmation dialog
		// boolean response =
		// MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
		// "Pair Hero", "Are you sure you want to stop this session?");
		// if (response) {
		game.stop();
		startGameButton.setText(BUTTON_LABEL_START);
		// stopButton.setEnabled(false);
		// }
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
		return new ImageIcon(getClass().getResource(ICONS_PATH +
				imageName) + ".png");
	}

	private void createUIComponents() {
		// TODO: place custom component creation code here
		leftProgrammerPanel = new JPanel(new GridLayout(0, 4));
		rightProgrammerPanel = new JPanel(new GridLayout(0, 4));
		scorePanel = new JPanel(new GridLayout(0, 3));
	}
}
