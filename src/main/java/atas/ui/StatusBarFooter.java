package atas.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

/**
 * A ui for the status bar that is displayed at the footer of the application.
 */
public class StatusBarFooter extends UiPart<Region> {

    private static final String FXML = "StatusBarFooter.fxml";
    private static final String SPACE = "   ";

    @FXML
    private Label currentSessionLeftStatus;

    @FXML
    private Label currentSessionRightStatus;

    @FXML
    private Label emptyLeft;

    @FXML
    private Label emptyRight;

    /**
     * Creates a {@code StatusBarFooterLeft} with the given {@code Path}.
     */
    public StatusBarFooter(String sessionStatusLeft , String sessionStatusRight) {
        super(FXML);
        emptyLeft.setText(SPACE);
        currentSessionLeftStatus.setText(sessionStatusLeft);
        currentSessionRightStatus.setText(sessionStatusRight);
        emptyRight.setText(SPACE);
    }

}
