package pairhero;

import javax.swing.*;

public class UiHelper {
    private static final String ICONS_PATH = "/icons/";

    public ImageIcon getImageIcon(String imageName) {
        return new ImageIcon(getClass().getResource(ICONS_PATH + imageName + ".png"));
    }
}
