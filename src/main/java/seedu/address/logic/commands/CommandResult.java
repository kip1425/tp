package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;

    private final Person personToSelect;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** The application should exit. */
    private final boolean exit;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.showHelp = showHelp;
        this.exit = exit;
        this.personToSelect = null;
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false);
    }

    /**
     * Constructs a {@code CommandResult} that includes the specified feedback and person to select.
     *
     * @param feedbackToUser feedback text for the user
     * @param personToSelect the person to select in the UI (or null if not applicable)
     */
    public CommandResult(String feedbackToUser, Person personToSelect) {
        this.feedbackToUser = feedbackToUser;
        this.personToSelect = personToSelect;
        this.showHelp = false;
        this.exit = false;
    }

    /**
     * Returns the {@code Person} that should be selected in the UI.
     *
     * @return the person to select (or null if no selection is required)
     */
    public Person getPersonToSelect() {
        return personToSelect;
    }

    /**
     * Returns the feedback message to be shown to the user.
     *
     * @return feedback text
     */
    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    /**
     * Returns whether help information should be shown to the user.
     *
     * @return true if help should be shown, false otherwise
     */
    public boolean isShowHelp() {
        return showHelp;
    }

    /**
     * Returns whether the application should exit after this command.
     *
     * @return true if the application should exit, false otherwise
     */
    public boolean isExit() {
        return exit;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && showHelp == otherCommandResult.showHelp
                && exit == otherCommandResult.exit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("showHelp", showHelp)
                .add("exit", exit)
                .toString();
    }

}
