package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CommandHistoryTest {

    @Test
    public void pushPopPeekSize_capacityAndOrder() {
        CommandHistory history = new CommandHistory();
        assertTrue(history.isEmpty());
        assertEquals(0, history.size());
        assertEquals(null, history.peek());

        Command first = new StubCommand("first");
        history.push(first);
        assertFalse(history.isEmpty());
        assertEquals(1, history.size());
        assertEquals(first, history.peek());

        Command[] commands = new Command[21];
        commands[0] = first;
        for (int i = 1; i < commands.length; i++) {
            commands[i] = new StubCommand("c" + i);
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
        assertTrue(history.isEmpty());
    }

    @Test
    public void redoStack_pushRedoCanRedoPopRedo() {
        CommandHistory history = new CommandHistory();
        assertFalse(history.canRedo());

        Command cmd = new StubCommand("redo-cmd");
        history.pushRedo(cmd);
        assertTrue(history.canRedo());
        assertEquals(cmd, history.popRedo());
        assertFalse(history.canRedo());
    }

    @Test
    public void push_clearsRedoStack() {
        CommandHistory history = new CommandHistory();
        history.pushRedo(new StubCommand("r1"));
        assertTrue(history.canRedo());

        // push() should clear redo stack
        history.push(new StubCommand("u1"));
        assertFalse(history.canRedo());
    }

    @Test
    public void pushToUndo_doesNotClearRedoStack() {
        CommandHistory history = new CommandHistory();
        history.pushRedo(new StubCommand("r1"));
        assertTrue(history.canRedo());

        // pushToUndo() should NOT clear redo stack
        history.pushToUndo(new StubCommand("u1"));
        assertTrue(history.canRedo());
        assertEquals(1, history.size());
    }

    @Test
    public void pushToUndo_respectsCapacity() {
        CommandHistory history = new CommandHistory();
        for (int i = 0; i < 20; i++) {
            history.pushToUndo(new StubCommand("c" + i));
        }
        assertEquals(20, history.size());

        // Adding one more should evict the oldest
        history.pushToUndo(new StubCommand("overflow"));
        assertEquals(20, history.size());
    }

    @Test
    public void peek_emptyHistory_returnsNull() {
        CommandHistory history = new CommandHistory();
        assertNull(history.peek());
    }

    @Test
    public void toStringMethod_includesSizeAndCapacity() {
        CommandHistory history = new CommandHistory();
        history.push(new StubCommand("first"));

        String expected = CommandHistory.class.getCanonicalName() + "{historySize=1, capacity=20}";
        assertEquals(expected, history.toString());
    }

    private static class StubCommand extends Command {
        private final String id;

        private StubCommand(String id) {
            this.id = id;
        }

        @Override
        public CommandResult execute(seedu.address.model.Model model) {
            return new CommandResult(id);
        }
    }
}
