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

public class UndoCommandTest {

    @Test
    public void execute_noHistory_throwsCommandException() {
        Model model = new ModelManager();
        CommandHistory history = new CommandHistory();
        UndoCommand undoCommand = new UndoCommand(history);

        assertThrows(CommandException.class, UndoCommand.MESSAGE_NOTHING_TO_UNDO, () -> undoCommand.execute(model));
    }

    @Test
    public void execute_withHistory_undoesLastCommand() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        deleteCommand.execute(model);

        CommandHistory history = new CommandHistory();
        if (deleteCommand.isUndoable()) {
            history.push(deleteCommand);
        }

        UndoCommand undoCommand = new UndoCommand(history);
        CommandResult commandResult = undoCommand.execute(model);

        assertEquals(UndoCommand.MESSAGE_SUCCESS, commandResult.getFeedbackToUser());
        assertTrue(history.isEmpty());
        assertEquals(expectedModel, model);
    }

    @Test
    public void execute_historyPeekNull_throwsCommandException() {
        Model model = new ModelManager();
        CommandHistory history = new CommandHistory() {
            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public Command peek() {
                return null;
            }
        };

        UndoCommand undoCommand = new UndoCommand(history);
        assertThrows(CommandException.class, UndoCommand.MESSAGE_NOTHING_TO_UNDO, () -> undoCommand.execute(model));
    }

    @Test
    public void equals_andToString() {
        CommandHistory history = new CommandHistory();
        history.push(newStubCommand("a"));
        UndoCommand undoCommand = new UndoCommand(history);

        assertTrue(undoCommand.equals(undoCommand));
        assertTrue(undoCommand.equals(new UndoCommand(new CommandHistory())));
        assertFalse(undoCommand.equals(1));

        String expected = UndoCommand.class.getCanonicalName() + "{historySize=1}";
        assertEquals(expected, undoCommand.toString());
    }

    @Test
    public void commandHistory_pushPopPeekSizeAndCapacity() {
        CommandHistory history = new CommandHistory();
        assertTrue(history.isEmpty());
        assertEquals(0, history.size());
        assertEquals(null, history.peek());

        Command first = newStubCommand("c1");
        history.push(first);
        assertFalse(history.isEmpty());
        assertEquals(1, history.size());
        assertEquals(first, history.peek());

        Command[] commands = new Command[21];
        commands[0] = first;
        for (int i = 1; i < commands.length; i++) {
            commands[i] = newStubCommand("c" + (i + 1));
            history.push(commands[i]);
        }

        assertEquals(20, history.size());
        assertEquals(commands[20], history.peek());
        assertEquals(commands[20], history.pop());

        Command lastPopped = null;
        for (int i = 0; i < 19; i++) {
            lastPopped = history.pop();
        }
        assertEquals(commands[1], lastPopped);
    }

    @Test
    public void execute_undoPushesCommandToRedoStack() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        CommandHistory history = new CommandHistory();

        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        deleteCommand.execute(model);
        history.push(deleteCommand);

        assertFalse(history.canRedo());

        new UndoCommand(history).execute(model);

        assertTrue(history.canRedo());
        assertTrue(history.isEmpty());
    }

    @Test
    public void commandDefaultUndo_throwsCommandException() {
        Command command = new Command() {
            @Override
            public CommandResult execute(Model model) {
                return new CommandResult("ok");
            }
        };

        assertFalse(command.isUndoable());
        assertThrows(CommandException.class, "This command cannot be undone.", () -> command.undo(new ModelManager()));
    }

    private Command newStubCommand(String id) {
        return new Command() {
            @Override
            public CommandResult execute(Model model) {
                return new CommandResult(id);
            }
        };
    }
}
