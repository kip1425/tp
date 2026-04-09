package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Cleared the member list.";

    private AddressBook previousAddressBook;

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        previousAddressBook = new AddressBook(model.getAddressBook());
        model.setAddressBook(new AddressBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean isUndoable() {
        return true;
    }

    @Override
    public void undo(Model model) throws CommandException {
        requireNonNull(model);

        if (previousAddressBook == null) {
            throw new CommandException("Cannot undo clear: no previous state was saved.");
        }

        model.setAddressBook(new AddressBook(previousAddressBook));
    }
}
