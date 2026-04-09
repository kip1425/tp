package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.RENEW_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.RENEW_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TYPE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.lang.reflect.Field;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.RenewCommand.RenewPersonDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.RenewPersonDescriptorBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for RenewCommand.
 */
public class RenewCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_unfilteredList_success() {
        RenewCommand renewCommand = new RenewCommand(INDEX_FIRST_PERSON, new RenewPersonDescriptor());
        Person person = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String newExpiry = (person.getExpiryDate().getExpiryDate().plusYears(1))
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        Person expectedPerson = new PersonBuilder(person).withExpiryDate(newExpiry).build();

        String expectedMessage = String.format(
                RenewCommand.MESSAGE_RENEW_PERSON_SUCCESS, Messages.format(expectedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(person, expectedPerson);

        assertCommandSuccess(renewCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person person = new PersonBuilder(personInFilteredList).build();
        RenewCommand renewCommand = new RenewCommand(INDEX_FIRST_PERSON,
                new RenewPersonDescriptorBuilder().build());

        String newExpiry = (person.getExpiryDate().getExpiryDate().plusYears(1))
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        Person expectedPerson = new PersonBuilder(person).withExpiryDate(newExpiry).build();

        String expectedMessage = String.format(
                RenewCommand.MESSAGE_RENEW_PERSON_SUCCESS, Messages.format(expectedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), expectedPerson);

        assertCommandSuccess(renewCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_expiredRenewal_failure() {
        Person expiredPerson = new PersonBuilder().withExpiryDate("01-01-2020").withType("Annual").build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(expiredPerson);
        Model testModel = new ModelManager(addressBook, new UserPrefs());

        RenewCommand renewCommand = new RenewCommand(INDEX_FIRST_PERSON, new RenewPersonDescriptor());
        assertCommandFailure(renewCommand, testModel, RenewCommand.MESSAGE_MEMBERSHIP_EXPIRED);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        RenewPersonDescriptor descriptor = new RenewPersonDescriptorBuilder().withType(VALID_TYPE_BOB).build();
        RenewCommand renewCommand = new RenewCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(renewCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Renew filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        RenewCommand renewCommand = new RenewCommand(outOfBoundIndex,
                new RenewPersonDescriptorBuilder().withType(VALID_TYPE_BOB).build());

        assertCommandFailure(renewCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndo_renewRestoresModel() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        RenewPersonDescriptor descriptor = new RenewPersonDescriptorBuilder().withType(VALID_TYPE_BOB).build();
        RenewCommand renewCommand = new RenewCommand(INDEX_FIRST_PERSON, descriptor);

        renewCommand.execute(model);
        renewCommand.undo(model);

        assertTrue(renewCommand.isUndoable());
        assertEquals(expectedModel, model);
    }

    @Test
    public void undo_withoutExecute_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        RenewPersonDescriptor descriptor = new RenewPersonDescriptorBuilder().withType(VALID_TYPE_BOB).build();
        RenewCommand renewCommand = new RenewCommand(INDEX_FIRST_PERSON, descriptor);

        assertThrows(CommandException.class,
                "Cannot undo renew: original data is missing.", () -> renewCommand.undo(model));
    }

    @Test
    public void undo_renewedPersonMissing_throwsCommandException() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        RenewPersonDescriptor descriptor = new RenewPersonDescriptorBuilder().withType(VALID_TYPE_BOB).build();
        RenewCommand renewCommand = new RenewCommand(INDEX_FIRST_PERSON, descriptor);

        renewCommand.execute(model);
        Person renewedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        model.deletePerson(renewedPerson);

        String expectedMessage = "Cannot undo renew: the renewed member is no longer in the address book.";
        assertThrows(
                CommandException.class,
                expectedMessage, () -> renewCommand.undo(model));
    }

    @Test
    public void undo_missingEditedPersonReference_throwsCommandException() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        RenewPersonDescriptor descriptor = new RenewPersonDescriptorBuilder().withType(VALID_TYPE_BOB).build();
        RenewCommand renewCommand = new RenewCommand(INDEX_FIRST_PERSON, descriptor);

        renewCommand.execute(model);

        Field editedPersonField = RenewCommand.class.getDeclaredField("renewedPerson");
        editedPersonField.setAccessible(true);
        editedPersonField.set(renewCommand, null);

        assertThrows(CommandException.class,
                "Cannot undo renew: original data is missing.", () -> renewCommand.undo(model));
    }

    @Test
    public void equals() {
        final RenewCommand standardCommand = new RenewCommand(INDEX_FIRST_PERSON, RENEW_DESC_AMY);

        // same values -> returns true
        RenewPersonDescriptor copyDescriptor = new RenewPersonDescriptor(RENEW_DESC_AMY);
        RenewCommand commandWithSameValues = new RenewCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new RenewCommand(INDEX_SECOND_PERSON, RENEW_DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new RenewCommand(INDEX_FIRST_PERSON, RENEW_DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        RenewPersonDescriptor renewPersonDescriptor = new RenewPersonDescriptor();
        RenewCommand renewCommand = new RenewCommand(index, renewPersonDescriptor);
        String expected = RenewCommand.class.getCanonicalName() + "{index=" + index + ", renewPersonDescriptor="
                + renewPersonDescriptor + "}";
        assertEquals(expected, renewCommand.toString());
    }

    @Test
    public void redo_afterExecute_renenewsCorrectPerson() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        RenewPersonDescriptor descriptor = new RenewPersonDescriptorBuilder().withType(VALID_TYPE_BOB).build();
        RenewCommand renewCommand = new RenewCommand(INDEX_FIRST_PERSON, descriptor);

        renewCommand.execute(expectedModel);
        renewCommand.execute(model);

        // Undo restores original
        renewCommand.undo(model);

        // Redo re-applies the renew
        renewCommand.redo(model);
        assertEquals(expectedModel, model);
    }

    @Test
    public void redo_withoutExecute_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        RenewPersonDescriptor descriptor = new RenewPersonDescriptorBuilder().withType(VALID_TYPE_BOB).build();
        RenewCommand renewCommand = new RenewCommand(INDEX_FIRST_PERSON, descriptor);

        assertThrows(CommandException.class,
                "Unable to redo renew: missing data.", () ->
                        renewCommand.redo(model));
    }

}
