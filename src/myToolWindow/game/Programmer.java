package myToolWindow.game;

import java.awt.*;
import javax.swing.*;
import myToolWindow.time.TimeFormatter;

public class Programmer {

	public static final String ICONS_PATH = "/myToolWindow/icons/";
	private JLabel nameLabel;
	private JLabel roleLabel;
	private JLabel timeAtKeyboardLabel;
	private JLabel avatar;

	private Role currentRole;
	private int timeAtKeyboard;

	private static Color DRIVER_COLOR;
	private static Color OBSERVER_COLOR;
	private JPanel panel;

	enum Role {
		Driving, Observing
	}

	public Programmer(JPanel programmerPanel) {
		this.panel = programmerPanel;
		initializeUIControls(panel);
	}

	void initializeUIControls(JPanel programmerPanel) {
		DRIVER_COLOR = new Color(/* PlatformUI.getWorkbench().getDisplay(), */58, 170, 53);
		OBSERVER_COLOR = new Color(/* PlatformUI.getWorkbench().getDisplay(), */218, 218, 218);

		nameLabel = new JLabel("Press start to add Player");
		programmerPanel.add(nameLabel);

		// GridData nameGridData = new GridData(GridData.FILL_BOTH);
		// nameGridData.horizontalSpan = 2;
		// nameLabel.setLayoutData(nameGridData);

		avatar = new JLabel(getImageIcon("no-avatar.png"));
		programmerPanel.add(avatar);
		// avatar.setImage(Activator.getDefault().getImageFromKey("no-avatar"));
		// GridData avatarGridData = new GridData(GridData.FILL_BOTH);
		// avatarGridData.horizontalSpan = 2;
		// avatar.setLayoutData(avatarGridData);

		roleLabel = new JLabel(getImageIcon("red-keyboard.png"));
		programmerPanel.add(roleLabel);
		// roleLabel.setImage(Activator.getImageDescriptor("icons/red-keyboard.png").createImage());

		timeAtKeyboardLabel = new JLabel("00:00");
		programmerPanel.add(timeAtKeyboardLabel);
	}

	private ImageIcon getImageIcon(String imageName) {
		return new ImageIcon(getClass().getResource(ICONS_PATH +
				imageName) + ".png");
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
		// PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
		// @Override
		// public void run() {
		if (role.equals(Role.Driving)) {
			roleLabel.setIcon(getImageIcon("green-keyboard.png"));
			panel.setBackground(DRIVER_COLOR);
		} else {
			roleLabel.setIcon(getImageIcon("red-keyboard.png"));
			panel.setBackground(OBSERVER_COLOR);
		}
		// }
		// });
	}

	void updateTimeAtKeyboard(final int seconds) {
		// PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
		// @Override
		// public void run() {
		timeAtKeyboardLabel.setText(TimeFormatter.formatTime(seconds));
		// }
		// });
	}

	public void resetStats() {
		timeAtKeyboard = 0;
		updateTimeAtKeyboard(timeAtKeyboard);
	}

	public void setAvatar(final String avatarImage) {
		// PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
		// @Override
		// public void run() {
		avatar.setIcon(getImageIcon(avatarImage));
		// }
		// });
	}
}
