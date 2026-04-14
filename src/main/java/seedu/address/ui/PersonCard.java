package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private HBox nameBox;
    @FXML
    private Label memberId;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label memberStatus;
    @FXML
    private Label expiryDate;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + "");
        memberId.setText(person.getId().toString());
        expiryDate.setText(person.getExpiryDate().toString());
        name.setText(person.getName().fullName);
        name.maxWidthProperty().bind(nameBox.widthProperty());
        memberStatus.setText(person.getMemberStatus().memberStatus);
        if (person.getMemberStatus().memberStatus.equalsIgnoreCase("valid")) {
            memberStatus.getStyleClass().add("valid-status");
        } else if (person.getMemberStatus().memberStatus.equalsIgnoreCase("invalid")) {
            memberStatus.getStyleClass().add("invalid-status");
        } else {
            memberStatus.getStyleClass().add("pending-status");
        }

    }
}
