package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ModelManager;

public class CommandDefaultTest {

    @Test
    public void defaultUndo_notUndoableAndThrows() {
        Command command = new Command() {
            @Override
            public CommandResult execute(seedu.address.model.Model model) {
                return new CommandResult("ok");
            }
        };

        assertFalse(command.isUndoable());
        assertThrows(CommandException.class, "This command cannot be undone.", () -> command.undo(new ModelManager()));
    }

    @Test
    public void defaultRedo_throwsCommandException() {
        Command command = new Command() {
            @Override
            public CommandResult execute(seedu.address.model.Model model) {
                return new CommandResult("ok");
            }
        };

        assertThrows(CommandException.class, "This command cannot be redone.", () -> command.redo(new ModelManager()));
    }
}
