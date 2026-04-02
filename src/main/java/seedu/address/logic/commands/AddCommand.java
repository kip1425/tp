package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATEOFBIRTH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMERGENCY_CONTACT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEMBERSHIP_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.GenerateMemberIds;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_GENDER + "GENDER "
            + PREFIX_DATEOFBIRTH + "DATEOFBIRTH "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_EMERGENCY_CONTACT + "EMERGENCY_CONTACT "
            + PREFIX_MEMBERSHIP_TYPE + "MEMBERSHIP_TYPE "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_GENDER + "M "
            + PREFIX_DATEOFBIRTH + "02-02-2002 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_EMERGENCY_CONTACT + "93110225 "
            + PREFIX_MEMBERSHIP_TYPE + "monthly";

    public static final String MESSAGE_SUCCESS = "New person added: %1$s";
    public static final String MESSAGE_DUPLICATE_FIELDS = "%1$s already existed in the address book.";

    private final Person toAdd;
    private Person addedPerson;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        boolean isPhoneDuplicate = false;
        boolean isEmailDuplicate = false;
        boolean isNameDuplicate = false;

        for (Person existingPerson : model.getAddressBook().getPersonList()) {
            isPhoneDuplicate = isPhoneDuplicate || existingPerson.getPhone().equals(toAdd.getPhone());
            isEmailDuplicate = isEmailDuplicate || existingPerson.getEmail().equals(toAdd.getEmail());
            isNameDuplicate = isNameDuplicate || existingPerson.getName().equals(toAdd.getName());
        }

        if (isPhoneDuplicate || isEmailDuplicate || isNameDuplicate) {
            GenerateMemberIds.decrementMaxId();
            throw new CommandException(String.format(MESSAGE_DUPLICATE_FIELDS,
                    formatDuplicateFields(isNameDuplicate, isPhoneDuplicate, isEmailDuplicate)));
        }

        model.addPerson(toAdd);
        addedPerson = toAdd;
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    @Override
    public boolean isUndoable() {
        return true;
    }

    @Override
    public void undo(Model model) throws CommandException {
        requireNonNull(model);

        if (addedPerson == null || !model.hasPerson(addedPerson)) {
            throw new CommandException("Unable to undo add: person not found.");
        }

        model.deletePerson(addedPerson);
        GenerateMemberIds.decrementMaxId();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }

    private static String formatDuplicateFields(boolean isNameDuplicate, boolean isPhoneDuplicate,
                                                    boolean isEmailDuplicate) {
        int duplicateCount = 0;
        if (isNameDuplicate) {
            duplicateCount++;
        }
        if (isPhoneDuplicate) {
            duplicateCount++;
        }
        if (isEmailDuplicate) {
            duplicateCount++;
        }

        if (duplicateCount == 1) {
            if (isNameDuplicate) {
                return "name";
            }
            if (isPhoneDuplicate) {
                return "phone";
            }
            return "email";
        }

        if (duplicateCount == 2) {
            if (isNameDuplicate && isPhoneDuplicate) {
                return "name and phone";
            }
            if (isNameDuplicate) {
                return "name and email";
            }
            return "phone and email";
        }

        return "name, phone and email";
    }
}
