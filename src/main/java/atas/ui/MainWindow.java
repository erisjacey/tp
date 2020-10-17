package atas.ui;

import static atas.logic.commands.atas.SwitchCommand.MESSAGE_INVALID_TAB;

import java.util.logging.Logger;

import atas.commons.core.GuiSettings;
import atas.commons.core.LogsCenter;
import atas.logic.Logic;
import atas.logic.commands.CommandResult;
import atas.logic.commands.exceptions.CommandException;
import atas.logic.parser.exceptions.ParseException;
import atas.ui.sessionlist.SessionListPanel;
import atas.ui.sessionlist.session.SessionStudentListPanel;
import atas.ui.studentlist.StudentListPanel;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final int SLEEP_TIME = 50;
    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private StudentListPanel studentListPanel;
    private SessionListPanel sessionListPanel;
    private SessionStudentListPanel sessionStudentListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private StackPane personListPanelPlaceholder;

    @FXML
    private StackPane sessionListPanelPlaceholder;

    @FXML
    private StackPane sessionStudentListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    @FXML
    private TabPane tabPane;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        setAccelerators();

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setAccelerators() {
        setAccelerator(helpMenuItem, KeyCombination.valueOf("F1"));
    }

    /**
     * Sets the accelerator of a MenuItem.
     * @param keyCombination the KeyCombination value of the accelerator
     */
    private void setAccelerator(MenuItem menuItem, KeyCombination keyCombination) {
        menuItem.setAccelerator(keyCombination);

        /*
         * TODO: the code below can be removed once the bug reported here
         * https://bugs.openjdk.java.net/browse/JDK-8131666
         * is fixed in later version of SDK.
         *
         * According to the bug report, TextInputControl (TextField, TextArea) will
         * consume function-key events. Because CommandBox contains a TextField, and
         * ResultDisplay contains a TextArea, thus some accelerators (e.g F1) will
         * not work when the focus is in them because the key event is consumed by
         * the TextInputControl(s).
         *
         * For now, we add following event filter to capture such key events and open
         * help window purposely so to support accelerators even when focus is
         * in CommandBox or ResultDisplay.
         */
        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getTarget() instanceof TextInputControl && keyCombination.match(event)) {
                menuItem.getOnAction().handle(new ActionEvent());
                event.consume();
            }
        });

        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.TAB) {
                switchTab();
                event.consume();
            }
        });
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        studentListPanel = new StudentListPanel(logic.getFilteredStudentList());
        personListPanelPlaceholder.getChildren().add(studentListPanel.getRoot());

        sessionListPanel = new SessionListPanel(logic.getFilteredSessionList());
        sessionListPanelPlaceholder.getChildren().add(sessionListPanel.getRoot());

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getStudentListFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Sets the default size based on {@code guiSettings}.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setHeight(guiSettings.getWindowHeight());
        primaryStage.setWidth(guiSettings.getWindowWidth());
        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    private void switchTab() {
        int currentTabIndex = tabPane.getSelectionModel().getSelectedIndex();
        int toSwitchTabIndex = (currentTabIndex + 1) % Tab.values().length;
        tabPane.getSelectionModel().select(toSwitchTabIndex);
    }

    /**
     * Switches to the specified tab.
     *
     * @param tab tab to switch to.
     */
    @FXML
    private void handleSwitchTab(Tab tab) throws CommandException {
        //int currentTabIndex = tabPane.getSelectionModel().getSelectedIndex();
        int toSwitchTabIndex = tab.getIndex().getZeroBased();

        /* if (currentTabIndex == toSwitchTabIndex) {
            throw new CommandException(String.format(MESSAGE_ALREADY_ON_TAB, tab.toString().toLowerCase()));
        }
        */
        if (tab.equals(Tab.STUDENTS) || tab.equals(Tab.SESSIONS)) {
            tabPane.getSelectionModel().select(toSwitchTabIndex);
        } else if (tab.equals(Tab.CURRENT)) {
            tabPane.getSelectionModel().select(toSwitchTabIndex);
        } else {
            throw new CommandException(MESSAGE_INVALID_TAB);
        }
    }

    @FXML
    private void handleEnterSessionTab(Tab tab) throws CommandException {

        int toSwitchTabIndex = tab.getIndex().getZeroBased();
        logic.enableCurrentSession();
        sessionStudentListPanel = new SessionStudentListPanel(logic.getFilteredAttributesList());
        sessionStudentListPanelPlaceholder.getChildren().add(sessionStudentListPanel.getRoot());
        tabPane.getSelectionModel().select(toSwitchTabIndex);

    }

    @FXML
    private void handleExit() throws InterruptedException {
        Platform.runLater(() -> {
            GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
            logic.setGuiSettings(guiSettings);
            helpWindow.hide();
            primaryStage.hide();
        });
    }

    /**
     * Displays the command result feedback.
     * @param commandResult Command result of the user command.
     */
    public void displayResult(CommandResult commandResult) {
        logger.info("Result: " + commandResult.getFeedbackToUser());
        resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
    }

    /**
     * Executes the command and returns the result.
     *
     * @see Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws
            CommandException, ParseException, InterruptedException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            Task displayResultTask = new Task() {
                @Override
                protected Object call() throws Exception {
                    displayResult(commandResult);
                    return null;
                }
            };
            displayResultTask.run();
            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isSwitchTab()) {
                handleSwitchTab(commandResult.getTab());
            }

            if (commandResult.isEnterSession()) {
                handleEnterSessionTab(commandResult.getTab());
            }

            if (commandResult.isExit()) {
                Thread.sleep(SLEEP_TIME);
                handleExit();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
