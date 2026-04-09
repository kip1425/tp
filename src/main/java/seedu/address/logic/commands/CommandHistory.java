package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayDeque;
import java.util.Deque;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Stores recently executed undoable commands for use by {@link UndoCommand}.
 */
public class CommandHistory {
    private static final int DEFAULT_CAPACITY = 20;

    private final Deque<Command> history;
    private final Deque<Command> redoStack;
    private final int capacity;

    /**
     * Creates an empty command history with a fixed capacity.
     */
    public CommandHistory() {
        this.history = new ArrayDeque<>();
        this.redoStack = new ArrayDeque<>();
        this.capacity = DEFAULT_CAPACITY;
    }

    /**
     * Adds a command to history, evicting the oldest command when full.
     * Clears the redo stack since a new undoable command invalidates any redoable state.
     *
     * @param command command to store.
     */
    public void push(Command command) {
        requireNonNull(command);

        if (history.size() == capacity) {
            history.removeFirst();
        }

        history.addLast(command);
        redoStack.clear();
    }

    /**
     * Adds a command back to the undo history after a redo, without clearing the redo stack.
     *
     * @param command command to restore to undo history.
     */
    public void pushToUndo(Command command) {
        requireNonNull(command);

        if (history.size() == capacity) {
            history.removeFirst();
        }

        history.addLast(command);
    }

    /**
     * Adds a command to the redo stack after it has been undone.
     *
     * @param command command to store for potential redo.
     */
    public void pushRedo(Command command) {
        requireNonNull(command);
        redoStack.addLast(command);
    }

    /**
     * Returns whether there are commands available to redo.
     *
     * @return {@code true} if there is at least one command to redo.
     */
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    /**
     * Removes and returns the most recently undone command for redoing.
     *
     * @return last command in redo stack.
     */
    public Command popRedo() {
        return redoStack.removeLast();
    }

    /**
     * Returns whether there are no commands to undo.
     *
     * @return {@code true} if history has no commands.
     */
    public boolean isEmpty() {
        return history.isEmpty();
    }

    /**
     * Returns the number of stored commands.
     *
     * @return number of commands in history.
     */
    public int size() {
        return history.size();
    }

    /**
     * Returns the most recently stored command without removing it.
     *
     * @return last command in history.
     */
    public Command peek() {
        return history.peekLast();
    }

    /**
     * Removes and returns the most recently stored command.
     *
     * @return last command in history.
     */
    public Command pop() {
        return history.removeLast();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("historySize", history.size())
                .add("capacity", capacity)
                .toString();
    }
}
