package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TypicalPersons;

public class AddCommandTest {

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddCommand(null));
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validPerson = new PersonBuilder().build();

        CommandResult commandResult = new AddCommand(validPerson).execute(modelStub);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, Messages.format(validPerson)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validPerson), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person validPerson = new PersonBuilder().build();
        AddCommand addCommand = new AddCommand(validPerson);
        ModelStub modelStub = new ModelStubWithPerson(validPerson);

        assertThrows(CommandException.class,
                AddCommand.MESSAGE_DUPLICATE_FIELDS + "phone and email", () ->
                        addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicatePhoneOnly_throwsCommandException() {
        Person existingPerson = new PersonBuilder().build();
        Person personToAdd = new PersonBuilder(existingPerson)
                .withName("Different Name")
                .withEmail("different@gmail.com")
                .build();
        AddCommand addCommand = new AddCommand(personToAdd);
        ModelStub modelStub = new ModelStubWithPerson(existingPerson);
        String expectedMessage = AddCommand.MESSAGE_DUPLICATE_FIELDS + "phone";
        assertThrows(CommandException.class, expectedMessage, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicateEmailOnly_throwsCommandException() {
        Person existingPerson = new PersonBuilder().build();
        Person personToAdd = new PersonBuilder(existingPerson)
                .withName("Different Name")
                .withPhone("98765432")
                .build();
        AddCommand addCommand = new AddCommand(personToAdd);
        ModelStub modelStub = new ModelStubWithPerson(existingPerson);
        String expectedMessage = AddCommand.MESSAGE_DUPLICATE_FIELDS + "email";
        assertThrows(CommandException.class, expectedMessage, () -> addCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicatePhoneAndEmail_throwsCommandException() {
        Person existingPerson = new PersonBuilder().build();
        Person personToAdd = new PersonBuilder(existingPerson)
                .withName("Different Name")
                .build();
        AddCommand addCommand = new AddCommand(personToAdd);
        ModelStub modelStub = new ModelStubWithPerson(existingPerson);
        String expectedMessage = AddCommand.MESSAGE_DUPLICATE_FIELDS + "phone and email";
        assertThrows(CommandException.class, expectedMessage, () -> addCommand.execute(modelStub));
    }

    @Test
    public void executeUndo_addRestoresModel() throws Exception {
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        Person newPerson = new PersonBuilder().withName("Undo Add").withEmail("undoadd@example.com").build();
        AddCommand addCommand = new AddCommand(newPerson);

        addCommand.execute(model);
        addCommand.undo(model);

        assertTrue(addCommand.isUndoable());
        assertEquals(expectedModel, model);
    }

    @Test
    public void undo_withoutExecute_throwsCommandException() {
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        Person newPerson = new PersonBuilder().withName("Undo Add").withEmail("undoadd@example.com").build();
        AddCommand addCommand = new AddCommand(newPerson);

        assertThrows(
                CommandException.class,
                "Cannot undo add: the added person is no longer in the address book.", () -> addCommand.undo(model));
    }

    @Test
    public void undo_personMissingAfterExecute_throwsCommandException() throws Exception {
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        Person newPerson = new PersonBuilder().withName("Undo Add Missing").withEmail("undoadd2@example.com").build();
        AddCommand addCommand = new AddCommand(newPerson);

        addCommand.execute(model);
        model.deletePerson(newPerson);

        assertThrows(
                CommandException.class,
                "Cannot undo add: the added person is no longer in the address book.", () -> addCommand.undo(model));
    }

    @Test
    public void redo_afterUndoAdd_readdsCorrectPerson() throws Exception {
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        Person newPerson = new PersonBuilder().withName("Redo Add").withEmail("redoadd@example.com").build();
        AddCommand addCommand = new AddCommand(newPerson);

        addCommand.execute(model);

        // Undo removes the person
        addCommand.undo(model);
        assertFalse(model.hasPerson(newPerson));

        // Redo re-adds the person
        addCommand.redo(model);
        assertTrue(model.hasPerson(newPerson));
    }

    @Test
    public void redo_withoutExecute_throwsCommandException() {
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        Person newPerson = new PersonBuilder().withName("Redo Add").withEmail("redoadd@example.com").build();
        AddCommand addCommand = new AddCommand(newPerson);

        assertThrows(CommandException.class,
                "Unable to redo add: missing person data.", () ->
                        addCommand.redo(model));
    }

    @Test
    public void equals() {
        Person alice = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different person -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        AddCommand addCommand = new AddCommand(ALICE);
        String expected = AddCommand.class.getCanonicalName() + "{toAdd=" + ALICE + "}";
        assertEquals(expected, addCommand.toString());
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Person person;

        ModelStubWithPerson(Person person) {
            requireNonNull(person);
            this.person = person;
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            AddressBook addressBook = new AddressBook();
            addressBook.addPerson(person);
            return addressBook;
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return this.person.isSamePerson(person);
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return personsAdded.stream().anyMatch(person::isSamePerson);
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
