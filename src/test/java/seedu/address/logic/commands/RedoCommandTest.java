package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

public class RedoCommandTest {

    @Test
    public void execute_noRedoHistory_throwsCommandException() {
        Model model = new ModelManager();
        CommandHistory history = new CommandHistory();
        RedoCommand redoCommand = new RedoCommand(history);

        assertThrows(CommandException.class, RedoCommand.MESSAGE_NOTHING_TO_REDO, () -> redoCommand.execute(model));
    }

    @Test
    public void execute_withRedoHistory_redoesLastUndoneCommand() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        // Delete a person, then undo — model back to original
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        deleteCommand.execute(model);

        CommandHistory history = new CommandHistory();
        history.push(deleteCommand);

        UndoCommand undoCommand = new UndoCommand(history);
        undoCommand.execute(model);

        // model is now restored; redo should re-delete
        Person deletedPerson = expectedModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        expectedModel.deletePerson(deletedPerson);

        RedoCommand redoCommand = new RedoCommand(history);
        CommandResult result = redoCommand.execute(model);

        assertEquals(RedoCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_redoRestoredToUndoHistory() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        CommandHistory history = new CommandHistory();

        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        deleteCommand.execute(model);
        history.push(deleteCommand);

        new UndoCommand(history).execute(model);
        assertTrue(history.canRedo());
        assertTrue(history.isEmpty());

        new RedoCommand(history).execute(model);
        assertFalse(history.canRedo());
        assertFalse(history.isEmpty()); // deleteCommand is back in undo history
    }

    @Test
    public void execute_newCommandAfterUndoClearsRedo() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        CommandHistory history = new CommandHistory();

        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        deleteCommand.execute(model);
        history.push(deleteCommand);

        new UndoCommand(history).execute(model);
        assertTrue(history.canRedo());

        // Pushing a new command clears the redo stack
        history.push(deleteCommand);
        assertFalse(history.canRedo());
    }

    @Test
    public void equals() {
        CommandHistory history = new CommandHistory();
        RedoCommand redoCommand = new RedoCommand(history);

        assertTrue(redoCommand.equals(redoCommand));
        assertTrue(redoCommand.equals(new RedoCommand(new CommandHistory())));
        assertFalse(redoCommand.equals(1));
        assertFalse(redoCommand.equals(null));
    }
}
