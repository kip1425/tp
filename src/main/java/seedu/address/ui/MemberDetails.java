package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class MemberDetails extends UiPart<Region> {

    private static final String FXML = "MemberDetails.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    private final Person person;

    @FXML
    private VBox memberCardPane;
    @FXML
    private Label memberId;
    @FXML
    private Label name;
    @FXML
    private Label phone;
    @FXML
    private Label gender;
    @FXML
    private Label dateOfBirth;
    @FXML
    private Label memberStatus;
    @FXML
    private Label emergencyContact;
    @FXML
    private Label email;
    @FXML
    private Label type;
    @FXML
    private Label joinDate;
    @FXML
    private Label expiryDate;
    @FXML
    private TextArea remarks;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public MemberDetails(Person person) {
        super(FXML);
        this.person = person;
        if (person != null) {
            setPerson(person);
        }
    }
    private void setPerson(Person person) {
        memberId.setText("ID: " + person.getId().toString());
        joinDate.setText("Start Date: " + person.getJoinDate().toString());
        expiryDate.setText("Expiry Date: " + person.getExpiryDate().toString());
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        gender.setText("Gender: " + person.getGender().gender);
        dateOfBirth.setText("Date of Birth: " + person.getDateOfBirth().toString());
        emergencyContact.setText("Emergency Contact: " + person.getEmergencyContact().value);
        email.setText(person.getEmail().value);
        type.setText(person.getMembershipType().toString());
        remarks.setText(person.getRemark().value);
        if (person.getMembershipType().value.equalsIgnoreCase("monthly")) {
            type.getStyleClass().add("type-month");
        } else {
            type.getStyleClass().add("type-year");
        }
        memberStatus.setText(person.getMemberStatus().memberStatus);
        if (person.getMemberStatus().memberStatus.equalsIgnoreCase("valid")) {
            memberStatus.getStyleClass().add("valid-tag");
        } else {
            memberStatus.getStyleClass().add("invalid-tag");
        }
    }
}
