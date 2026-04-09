package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Undoes the most recent undoable command.
 */
public class UndoCommand extends Command {
    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Undoes the most recent undoable command.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Undid the previous command.";
    public static final String MESSAGE_NOTHING_TO_UNDO = "There are no commands to undo.";

    private final CommandHistory commandHistory;

    /**
     * Creates an UndoCommand with the given command history.
     */
    public UndoCommand(CommandHistory commandHistory) {
        requireNonNull(commandHistory);
        this.commandHistory = commandHistory;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (commandHistory.isEmpty()) {
            throw new CommandException(MESSAGE_NOTHING_TO_UNDO);
        }

        Command lastCommand = commandHistory.peek();
        if (lastCommand == null) {
            throw new CommandException(MESSAGE_NOTHING_TO_UNDO);
        }

        lastCommand.undo(model);
        commandHistory.pop();
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        return other instanceof UndoCommand;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("historySize", commandHistory.size())
                .toString();
    }
}
