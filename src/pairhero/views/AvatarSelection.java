package pairhero.views;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class AvatarSelection {
	public static final String ICONS_PATH = "/pairhero/icons/";
	private JButton explorator;
	private JButton king;
	private JButton robin;
	private JButton wizard;

	public AvatarSelection(JComponent composite) {
		buildUI(composite);
	}

	protected void buildUI(JComponent composite) {
		JComponent group = new JPanel();
		composite.add(group);
		group.setLayout(new GridLayout(1, 4));

		explorator = new JButton(getImageIcon("explorator"));
		explorator.addActionListener(listener);
		group.add(explorator);

		king = new JButton(getImageIcon("king"));
		king.addActionListener(listener);
		group.add(king);

		robin = new JButton(getImageIcon("robin"));
		robin.addActionListener(listener);
		group.add(robin);

		wizard = new JButton(getImageIcon("wizard"));
		wizard.addActionListener(listener);
		group.add(wizard);
	}

	ActionListener listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			deSelectAllAvatars();
			((JButton)actionEvent.getSource()).setSelected(true);
		}
	};

	private void deSelectAllAvatars() {
		explorator.setSelected(false);
		king.setSelected(false);
		robin.setSelected(false);
		wizard.setSelected(false);
	}

	public String getSelection() {
		if (explorator.isSelected()) {
			return "explorator";
		}

		if (king.isSelected()) {
			return "king";
		}

		if (robin.isSelected()) {
			return "robin";
		}

		if (wizard.isSelected()) {
			return "wizard";
		}

		return "no-avatar";
	}

	private ImageIcon getImageIcon(String imageName) {
		return new ImageIcon(getClass().getResource(ICONS_PATH + imageName + ".png"));
	}
}
