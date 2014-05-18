package pairhero.game;

import java.awt.*;
import javax.swing.*;
import pairhero.time.TimeFormatter;

public class Programmer {
	public static final String ICONS_PATH = "/pairhero/icons/";
	private static Color DRIVER_COLOR;
	private static Color OBSERVER_COLOR;

	private JLabel nameLabel;
	private JLabel roleLabel;
	private JLabel timeAtKeyboardLabel;
	private JLabel avatar;
	private Role currentRole;
	private int timeAtKeyboard;
	private JPanel panel;

	enum Role {
		Driving, Observing
	}

	public Programmer(JPanel programmerPanel) {
		this.panel = programmerPanel;
		initializeUIControls(panel);
	}

	void initializeUIControls(JPanel programmerPanel) {
		DRIVER_COLOR = new Color(58, 170, 53);
		OBSERVER_COLOR = new Color(218, 218, 218);

		nameLabel = new JLabel("Player");
		programmerPanel.add(nameLabel);

		avatar = new JLabel(getImageIcon("no-avatar"));
		programmerPanel.add(avatar);

		roleLabel = new JLabel(getImageIcon("red-keyboard"));
		programmerPanel.add(roleLabel);
		
		timeAtKeyboardLabel = new JLabel("00:00");
		programmerPanel.add(timeAtKeyboardLabel);
	}

	private ImageIcon getImageIcon(String imageName) {
		return new ImageIcon(getClass().getResource(ICONS_PATH + imageName + ".png"));
	}

	public void drive() {
		currentRole = Role.Driving;
		updateRole(currentRole);
	}

	public void observe() {
		currentRole = Role.Observing;
		updateRole(currentRole);
	}

	public void setName(String playerName) {
		nameLabel.setText(playerName);
	}

	public void switchRole() {
		if (currentRole.equals(Role.Driving)) {
			observe();
		} else {
			drive();
		}
	}

	public void onTimeChange() {
		if (Role.Driving.equals(currentRole)) {
			timeAtKeyboard++;
			updateTimeAtKeyboard(timeAtKeyboard);
		}
	}

	void updateRole(final Role role) {
		if (role.equals(Role.Driving)) {
			roleLabel.setIcon(getImageIcon("green-keyboard"));
			panel.setBackground(DRIVER_COLOR);
		} else {
			roleLabel.setIcon(getImageIcon("red-keyboard"));
			panel.setBackground(OBSERVER_COLOR);
		}
	}

	void updateTimeAtKeyboard(final int seconds) {
		timeAtKeyboardLabel.setText(TimeFormatter.formatTime(seconds));
	}

	public void resetStats() {
		timeAtKeyboard = 0;
		updateTimeAtKeyboard(timeAtKeyboard);
	}

	public void setAvatar(final String avatarImage) {
		avatar.setIcon(getImageIcon(avatarImage));
	}
}
