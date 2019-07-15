package pairhero.views;

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;
import pairhero.UiHelper;

import javax.swing.*;
import java.awt.*;

public class StartDialog extends DialogWrapper {
    private String playerOneName;
    private String playerTwoName;
    private String playerOneAvatar;
    private String playerTwoAvatar;

    private JTextField playerTwoText;
    private JTextField playerOneText;

    private AvatarSelection playerOneAvatarSelection;
    private AvatarSelection playerTwoAvatarSelection;
    private UiHelper uiHelper;

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JComponent composite = new JPanel(new GridLayout(2, 10));
        showLogo(composite);
        showPlayerOneControls(composite);
        showBarSeparation(composite);
        showPlayerTwoControls(composite);

        return composite;
    }

    private static Dimension textDataLayout = new Dimension(200, 15);

    public StartDialog() {
        super(false);
        uiHelper = new UiHelper();
        init();
    }

    private void showPlayerOneControls(JComponent composite) {
        JLabel player1Label = new JLabel();
        composite.add(player1Label);
        player1Label.setText("Player 1:");

        playerOneText = new JTextField();
        composite.add(playerOneText);
        playerOneText.setMaximumSize(textDataLayout);

        playerOneAvatarSelection = new AvatarSelection(composite);
    }

    private void showPlayerTwoControls(JComponent composite) {
        JLabel player2Label = new JLabel();
        composite.add(player2Label);
        player2Label.setText("Player 2:");

        playerTwoText = new JTextField();
        composite.add(playerTwoText);
        playerTwoText.setMaximumSize(textDataLayout);

        playerTwoAvatarSelection = new AvatarSelection(composite);
    }

    private void showBarSeparation(JComponent composite) {
        composite.add(new JLabel(uiHelper.getImageIcon("divbar")));
    }

    public void buttonPressed(int dialogExitCode) {
        if (dialogExitCode == OK_EXIT_CODE) {
            playerOneName = playerOneText.getText();
            playerTwoName = playerTwoText.getText();
            playerOneAvatar = playerOneAvatarSelection.getSelection();
            playerTwoAvatar = playerTwoAvatarSelection.getSelection();
        }
    }

    public String getPlayerOneName() {
        return playerOneName;
    }

    public String getPlayerTwoName() {
        return playerTwoName;
    }

    public String getPlayerOneAvatar() {
        return playerOneAvatar;
    }

    public String getPlayerTwoAvatar() {
        return playerTwoAvatar;
    }

    private void showLogo(JComponent composite) {
        composite.add(new JLabel(uiHelper.getImageIcon("logo")));
    }
}
