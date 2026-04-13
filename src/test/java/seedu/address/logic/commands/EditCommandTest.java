package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.MemberId;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, new EditPersonDescriptor());
        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCommand, model,
                EditCommand.MESSAGE_DUPLICATE_FIELDS + "phone and email");
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit person in filtered list into a duplicate in address book
        Person personInList = model.getAddressBook().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder(personInList).build());

        assertCommandFailure(editCommand, model,
                EditCommand.MESSAGE_DUPLICATE_FIELDS + "phone and email");
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_joinDateBeforeDateOfBirth_failure() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withDateOfBirth("01-04-2026").build());

        assertCommandFailure(editCommand, model, Person.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void execute_duplicatePhoneOnly_throwsCommandException() {
        Person personA = new PersonBuilder()
                .withId(new MemberId(1))
                .withName("Alice A")
                .withPhone("85355255")
                .withEmail("alicea@gmail.com")
                .build();
        Person personB = new PersonBuilder()
                .withId(new MemberId(2))
                .withName("Bob B")
                .withPhone("94351253")
                .withEmail("bobb@gmail.com")
                .build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(personA);
        addressBook.addPerson(personB);

        Model model = new ModelManager(addressBook, new UserPrefs());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withPhone(personA.getPhone().toString())
                .build();
        EditCommand editCommand = new EditCommand(Index.fromOneBased(2), descriptor);

        assertCommandFailure(editCommand, model,
                EditCommand.MESSAGE_DUPLICATE_FIELDS + "phone");
    }

    @Test
    public void execute_duplicateEmailOnly_throwsCommandException() {
        Person personA = new PersonBuilder()
                .withId(new MemberId(1))
                .withName("Alice A")
                .withPhone("85355255")
                .withEmail("alicea@gmail.com")
                .build();
        Person personB = new PersonBuilder()
                .withId(new MemberId(2))
                .withName("Bob B")
                .withPhone("94351253")
                .withEmail("bobb@gmail.com")
                .build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(personA);
        addressBook.addPerson(personB);

        Model model = new ModelManager(addressBook, new UserPrefs());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withEmail(personA.getEmail().toString())
                .build();
        EditCommand editCommand = new EditCommand(Index.fromOneBased(2), descriptor);

        assertCommandFailure(editCommand, model,
                EditCommand.MESSAGE_DUPLICATE_FIELDS + "email");
    }

    @Test
    public void execute_duplicatePhoneAndEmail_throwsCommandException() {
        Person personA = new PersonBuilder()
                .withId(new MemberId(1))
                .withName("Alice A")
                .withPhone("85355255")
                .withEmail("alicea@gmail.com")
                .build();
        Person personB = new PersonBuilder()
                .withId(new MemberId(2))
                .withName("Bob B")
                .withPhone("94351253")
                .withEmail("bobb@gmail.com")
                .build();

        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(personA);
        addressBook.addPerson(personB);

        Model model = new ModelManager(addressBook, new UserPrefs());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withPhone(personA.getPhone().toString())
                .withEmail(personA.getEmail().toString()).build();
        EditCommand editCommand = new EditCommand(Index.fromOneBased(2), descriptor);

        assertCommandFailure(editCommand, model,
                EditCommand.MESSAGE_DUPLICATE_FIELDS + "phone and email");
    }

    @Test
    public void executeUndo_editRestoresModel() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName("Undo Edit").build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        editCommand.execute(model);
        editCommand.undo(model);

        assertTrue(editCommand.isUndoable());
        assertEquals(expectedModel, model);
    }

    @Test
    public void undo_withoutExecute_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName("Undo Edit").build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        assertThrows(CommandException.class,
                "Cannot undo edit: original data is missing.", () -> editCommand.undo(model));
    }

    @Test
    public void undo_editedPersonMissing_throwsCommandException() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName("Undo Edit").build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        editCommand.execute(model);
        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        model.deletePerson(editedPerson);

        String expectedMessage = "Cannot undo edit: the updated person is no longer in the address book.";
        assertThrows(
                CommandException.class,
                expectedMessage, () -> editCommand.undo(model));
    }

    @Test
    public void undo_missingEditedPersonReference_throwsCommandException() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName("Undo Edit").build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        editCommand.execute(model);

        Field editedPersonField = EditCommand.class.getDeclaredField("editedPerson");
        editedPersonField.setAccessible(true);
        editedPersonField.set(editCommand, null);

        assertThrows(CommandException.class,
                "Cannot undo edit: original data is missing.", () -> editCommand.undo(model));
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        EditCommand editCommand = new EditCommand(index, editPersonDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{index=" + index + ", editPersonDescriptor="
                + editPersonDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

    @Test
    public void redo_afterExecute_reeditsCorrectPerson() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName("Redo Edit").build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        editCommand.execute(expectedModel);
        editCommand.execute(model);

        // Undo restores original
        editCommand.undo(model);

        // Redo re-applies the edit
        editCommand.redo(model);
        assertEquals(expectedModel, model);
    }

    @Test
    public void redo_withoutExecute_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName("Redo Edit").build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        assertThrows(CommandException.class,
                "Unable to redo edit: missing data.", () ->
                        editCommand.redo(model));
    }

}
