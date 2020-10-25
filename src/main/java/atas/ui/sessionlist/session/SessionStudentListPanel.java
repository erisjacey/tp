package atas.ui.sessionlist.session;

import java.util.logging.Logger;

import atas.commons.core.LogsCenter;
import atas.model.session.Attributes;
import atas.ui.UiPart;
import atas.ui.studentlist.StudentListPanel;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

public class SessionStudentListPanel extends UiPart<Region> {
    private static final String FXML = "SessionStudentListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(StudentListPanel.class);

    @FXML
    private ListView<Attributes> sessionStudentListView;

    /**
     * Creates a {@code StudentListPanel} with the given {@code ObservableList}.
     */
    public SessionStudentListPanel(ObservableList<Attributes> attributesList) {
        super(FXML);
        sessionStudentListView.setItems(attributesList);
        sessionStudentListView.setCellFactory(listView -> new SessionStudentListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Student} using a {@code StudentCard}.
     */
    class SessionStudentListViewCell extends ListCell<Attributes> {
        @Override
        protected void updateItem(Attributes attributes, boolean empty) {
            super.updateItem(attributes, empty);

            if (empty || attributes == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new SessionStudentCard(attributes, getIndex() + 1).getRoot());
            }
        }
    }
}