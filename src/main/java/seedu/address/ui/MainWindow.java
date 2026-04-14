package seedu.address.ui;

import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;
    private final String initialFeedbackToUser;

    // Independent Ui parts residing in this Ui container
    private PersonListPanel personListPanel;
    private MemberDetails currMemberDetails;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;
    private DashBoard dashBoard;
    private CommandBox commandBox;

    @FXML
    private HBox headerBar;

    @FXML
    private MenuBar menuBar;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private MenuItem helpMenuItem;
    @FXML
    private StackPane personListPanelPlaceholder;
    @FXML
    private StackPane memberDetailsPlaceholder;
    @FXML
    private StackPane dashboardPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        this(primaryStage, logic, null);
    }

    /**
     * Creates a {@code MainWindow} with the given {@code Stage}, {@code Logic}, and initial feedback text.
     */
    public MainWindow(Stage primaryStage, Logic logic, String initialFeedbackToUser) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;
        this.initialFeedbackToUser = initialFeedbackToUser;

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
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        personListPanel = new PersonListPanel(logic.getFilteredPersonList());
        personListPanelPlaceholder.getChildren().add(personListPanel.getRoot());

        headerBar.setOnMouseClicked(event -> handleHome());

        dashBoard = new DashBoard(logic);
        memberDetailsPlaceholder.getChildren().add(dashBoard.getRoot());

        personListPanel.getListView().getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        currMemberDetails = new MemberDetails(newValue);
                        memberDetailsPlaceholder.getChildren().setAll(currMemberDetails.getRoot());
                    } else {
                        memberDetailsPlaceholder.getChildren().setAll(dashBoard.getRoot());
                    }
                });

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());
        if (initialFeedbackToUser != null && !initialFeedbackToUser.isBlank()) {
            resultDisplay.setFeedbackToUser(initialFeedbackToUser);
        }

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getAddressBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        commandBox = new CommandBox(this::executeCommand, () -> logic.getFilteredPersonList().size());
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());

        getRoot().addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (commandBox.isFocused()) {
                return;
            }
            String character = event.getCharacter();
            if (character.isEmpty() || character.charAt(0) < 32 || character.charAt(0) == 127) {
                return;
            }
            commandBox.focusAndType(character);
            event.consume();
        });

        getRoot().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                personListPanel.getListView().getSelectionModel().clearSelection();
            }
        });

        getRoot().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            Node target = (Node) event.getTarget();
            if (isDescendantOf(target, personListPanelPlaceholder)
                    || isDescendantOf(target, memberDetailsPlaceholder)
                    || isDescendantOf(target, commandBoxPlaceholder)
                    || isDescendantOf(target, resultDisplayPlaceholder)
                    || isDescendantOf(target, menuBar)
                    || isDescendantOf(target, headerBar)) {
                return;
            }
            personListPanel.getListView().getSelectionModel().clearSelection();
        });
    }

    /**
     * Checks if the given node is a descendant of the specified ancestor.
     */
    private boolean isDescendantOf(Node node, Node ancestor) {
        Node current = node;
        while (current != null) {
            if (current == ancestor) {
                return true;
            }
            current = current.getParent();
        }
        return false;
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
        //        if (!helpWindow.isShowing()) {
        helpWindow.show();
        //        } else {
        //            helpWindow.focus();
        //        }
    }

    /**
     * Resets the view to show all members and the dashboard.
     */
    private void handleHome() {
        try {
            executeCommand("list");
        } catch (CommandException | ParseException e) {
            logger.info("Home action failed: " + e.getMessage());
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    public PersonListPanel getPersonListPanel() {
        return personListPanel;
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);

            personListPanel.getListView().getSelectionModel().clearSelection();
            memberDetailsPlaceholder.getChildren().setAll(dashBoard.getRoot());

            if (commandResult.getPersonToSelect() != null) {
                personListPanel.selectPerson(commandResult.getPersonToSelect());
            }

            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());
            if (dashBoard != null) {
                dashBoard.update();
            }

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("An error occurred while executing command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
