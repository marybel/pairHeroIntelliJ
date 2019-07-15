package pairhero;

import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowEP;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import pairhero.game.Game;
import pairhero.game.Programmer;
import pairhero.game.Scoreboard;
import pairhero.time.TimeFormatter;
import pairhero.time.Timer;
import pairhero.views.StartDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * IntelliJ version of the <a href="http://www.happyprog.com/pairhero/">PairHero
 * plugin</a>. Ported by <a href="https://github.com/marybel">Marybel Archer</a>
 */
public class PairHeroToolWindowFactory implements ToolWindowFactory {
    public static final String BUTTON_LABEL_START = "Start";
    public static final String BUTTON_LABEL_STOP = "Stop";

    private final UiHelper uiHelper;
    private JButton startGameButton;
    private JPanel myToolWindowContent;
    private JPanel leftProgrammerPanel;
    private JPanel rightProgrammerPanel;
    private JPanel scorePanel;
    private JLabel leftPlayerName;
    private JLabel rightPlayerName;
    private JLabel leftAvatar;
    private JLabel rightAvatar;
    private JLabel leftRoleLabel;
    private JLabel rightRoleLabel;
    private JLabel rightTimeAtKeyboardLabel;
    private JLabel leftTimeAtKeyboardLabel;
    private JPanel line1;
    private JPanel line2;
    private JPanel line3;
    private JLabel scoreLabel;
    private JLabel timerLabel;
    private JLabel messageLabel;

    private Game game;
    private Scoreboard scoreboard;
    private Programmer leftProgrammer;
    private Programmer rightProgrammer;
    private int messageDelayCounter;

    public PairHeroToolWindowFactory() {
        uiHelper = new UiHelper();
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

    public static PairHeroToolWindowFactory getToolWindowFactory() {
        try {
            ToolWindowEP[] toolWindowExtensionPoints = Extensions.getExtensions(ToolWindowEP.EP_NAME);
            for (final ToolWindowEP toolWindowEP : toolWindowExtensionPoints) {
                ToolWindowFactory toolWindowFactory = toolWindowEP.getToolWindowFactory();
                if (toolWindowFactory instanceof PairHeroToolWindowFactory) {
                    return (PairHeroToolWindowFactory) toolWindowFactory;
                }
            }
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    // Create the tool window content.
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(myToolWindowContent, "", false);
        toolWindow.getContentManager().addContent(content);

        leftProgrammer = new Programmer(leftProgrammerPanel, leftPlayerName, leftAvatar, leftRoleLabel,
                leftTimeAtKeyboardLabel);
        rightProgrammer = new Programmer(rightProgrammerPanel, rightPlayerName, rightAvatar, rightRoleLabel,
                rightTimeAtKeyboardLabel);
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
        StartDialog dialog = new StartDialog();
        dialog.show();

        if (dialog.getExitCode() == DialogWrapper.OK_EXIT_CODE) {
            dialog.buttonPressed(dialog.getExitCode());
            leftProgrammer.resetStats();
            rightProgrammer.resetStats();
            leftProgrammer.setName(dialog.getPlayerOneName());
            rightProgrammer.setName(dialog.getPlayerTwoName());
            leftProgrammer.setAvatar(dialog.getPlayerOneAvatar());
            rightProgrammer.setAvatar(dialog.getPlayerTwoAvatar());
            scoreboard.resetStats();
            updateScore(scoreboard.getScore());

            return true;
        }

        return false;
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
        showMessageAndUpdateScore("refactor", scoreboard.getScore());
    }

    public void onGreenTest() {
        game.onGreenTest();
        showMessageAndUpdateScore("green", scoreboard.getScore());
    }

    private void showMessageAndUpdateScore(String imageKey, long score) {
        updateMessage(messageLabel, uiHelper.getImageIcon(imageKey));
        updateScore(score);
        messageDelayCounter = 3;
    }

    private void updateScore(long score) {
        updateScore(scoreLabel, String.format("%d", score));
    }

    private void updateMessageToDefault() {
        if (messageDelayCounter < 0) {
            updateMessage(messageLabel, uiHelper.getImageIcon("blank"));
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

    public boolean isGameOngoing() {
        return game != null && game.isOnGoing();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        myToolWindowContent = new JPanel();
        myToolWindowContent.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        leftProgrammerPanel = new JPanel();
        leftProgrammerPanel.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        myToolWindowContent.add(leftProgrammerPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        leftAvatar = new JLabel();
        leftAvatar.setIcon(new ImageIcon(getClass().getResource("/icons/no-avatar.png")));
        leftAvatar.setText("");
        leftProgrammerPanel.add(leftAvatar, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        leftRoleLabel = new JLabel();
        leftRoleLabel.setIcon(new ImageIcon(getClass().getResource("/icons/red-keyboard.png")));
        leftRoleLabel.setText("");
        leftProgrammerPanel.add(leftRoleLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        leftProgrammerPanel.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        leftPlayerName = new JLabel();
        leftPlayerName.setText("Player");
        leftProgrammerPanel.add(leftPlayerName, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        leftTimeAtKeyboardLabel = new JLabel();
        leftTimeAtKeyboardLabel.setText("00:00");
        leftProgrammerPanel.add(leftTimeAtKeyboardLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rightProgrammerPanel = new JPanel();
        rightProgrammerPanel.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        myToolWindowContent.add(rightProgrammerPanel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        rightProgrammerPanel.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        rightAvatar = new JLabel();
        rightAvatar.setIcon(new ImageIcon(getClass().getResource("/icons/no-avatar.png")));
        rightAvatar.setText("");
        rightProgrammerPanel.add(rightAvatar, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rightRoleLabel = new JLabel();
        rightRoleLabel.setIcon(new ImageIcon(getClass().getResource("/icons/red-keyboard.png")));
        rightRoleLabel.setText("");
        rightProgrammerPanel.add(rightRoleLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rightTimeAtKeyboardLabel = new JLabel();
        rightTimeAtKeyboardLabel.setText("00:00");
        rightProgrammerPanel.add(rightTimeAtKeyboardLabel, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rightPlayerName = new JLabel();
        rightPlayerName.setText("Player");
        rightProgrammerPanel.add(rightPlayerName, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scorePanel = new JPanel();
        scorePanel.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        myToolWindowContent.add(scorePanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        line1 = new JPanel();
        line1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        scorePanel.add(line1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Score: ");
        line1.add(label1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scoreLabel = new JLabel();
        scoreLabel.setText("0");
        line1.add(scoreLabel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        line2 = new JPanel();
        line2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        scorePanel.add(line2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        messageLabel = new JLabel();
        messageLabel.setIcon(new ImageIcon(getClass().getResource("/icons/blank.png")));
        messageLabel.setText("");
        line2.add(messageLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        line3 = new JPanel();
        line3.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        scorePanel.add(line3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Time Left: ");
        line3.add(label2, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        timerLabel = new JLabel();
        timerLabel.setText("25:00");
        line3.add(timerLabel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        scorePanel.add(spacer3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        startGameButton = new JButton();
        startGameButton.setText("Start");
        scorePanel.add(startGameButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 1, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return myToolWindowContent;
    }
}
