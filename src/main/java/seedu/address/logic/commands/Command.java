package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    public abstract CommandResult execute(Model model) throws CommandException;

    /**
     * Returns true if the command supports undo.
     */
    public boolean isUndoable() {
        return false;
    }

    /**
     * Undoes the command.
     *
     * @param model {@code Model} which the command should operate on.
     * @throws CommandException If an error occurs during undo.
     */
    public void undo(Model model) throws CommandException {
        throw new CommandException("This command cannot be undone.");
    }

    /**
     * Redoes the command.
     *
     * @param model {@code Model} which the command should operate on.
     * @throws CommandException If an error occurs during redo.
     */
    public void redo(Model model) throws CommandException {
        throw new CommandException("This command cannot be redone.");
    }
}
