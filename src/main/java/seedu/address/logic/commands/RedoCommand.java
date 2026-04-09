package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Redoes the most recently undone command.
 */
public class RedoCommand extends Command {
    public static final String COMMAND_WORD = "redo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Redoes the most recently undone command.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Redo successful!";
    public static final String MESSAGE_NOTHING_TO_REDO = "No commands to redo.";

    private final CommandHistory commandHistory;

    /**
     * Creates a RedoCommand with the given command history.
     */
    public RedoCommand(CommandHistory commandHistory) {
        requireNonNull(commandHistory);
        this.commandHistory = commandHistory;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (!commandHistory.canRedo()) {
            throw new CommandException(MESSAGE_NOTHING_TO_REDO);
        }

        Command lastUndoneCommand = commandHistory.popRedo();
        lastUndoneCommand.redo(model);
        commandHistory.pushToUndo(lastUndoneCommand);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        return other instanceof RedoCommand;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("redoStackSize", commandHistory.canRedo() ? "non-empty" : "empty")
                .toString();
    }
}
