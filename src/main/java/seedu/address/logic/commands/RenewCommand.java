package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEMBERSHIP_TYPE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.DateOfBirth;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyContact;
import seedu.address.model.person.Gender;
import seedu.address.model.person.MemberId;
import seedu.address.model.person.MembershipExpiryDate;
import seedu.address.model.person.MembershipJoinDate;
import seedu.address.model.person.MembershipType;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;

/**
 * Edits the details of an existing person in the address book.
 */
public class RenewCommand extends Command {

    public static final String COMMAND_WORD = "renew";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Renews membership of member identified "
            + "by the index number used in the displayed list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_MEMBERSHIP_TYPE + "MEMBERSHIP_TYPE]\n"
            + "Example: " + COMMAND_WORD + " 1 ";

    public static final String MESSAGE_RENEW_PERSON_SUCCESS = "Renewed membership for: %1$s";

    public static final String MESSAGE_MEMBERSHIP_EXPIRED =
            "This membership has already expired. Renewal is not allowed."
            + " To re-register, delete this member first using 'delete', then add them again using 'add'.";

    private final Index index;
    private final RenewPersonDescriptor renewPersonDescriptor;
    private Person originalPerson;
    private Person renewedPerson;

    /**
     * @param index of the person in the filtered person list to edit
     * @param renewPersonDescriptor details to edit the person with
     */
    public RenewCommand(Index index, RenewPersonDescriptor renewPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(renewPersonDescriptor);

        this.index = index;
        this.renewPersonDescriptor = new RenewPersonDescriptor(renewPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToRenew = lastShownList.get(index.getZeroBased());
        LocalDate expiry = personToRenew.getExpiryDate().getExpiryDate();
        if (expiry.isBefore(LocalDate.now())) {
            throw new CommandException(MESSAGE_MEMBERSHIP_EXPIRED);
        }

        Person renwedPerson = createRenewedPerson(personToRenew, renewPersonDescriptor);

        model.setPerson(personToRenew, renwedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        originalPerson = personToRenew;
        renewedPerson = renwedPerson;

        return new CommandResult(String.format(MESSAGE_RENEW_PERSON_SUCCESS, Messages.format(renwedPerson)));
    }

    @Override
    public boolean isUndoable() {
        return true;
    }

    @Override
    public void undo(Model model) throws CommandException {
        requireNonNull(model);

        if (originalPerson == null || renewedPerson == null) {
            throw new CommandException("Cannot undo renew: original data is missing.");
        }

        if (!model.hasPerson(renewedPerson)) {
            throw new CommandException("Cannot undo renew: the renewed member is no longer in the address book.");
        }

        model.setPerson(renewedPerson, originalPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createRenewedPerson(Person personToRenew, RenewPersonDescriptor renewPersonDescriptor) {
        assert personToRenew != null;

        MemberId memberId = personToRenew.getId();
        Name name = personToRenew.getName();
        Phone phone = personToRenew.getPhone();
        Gender gender = personToRenew.getGender();
        DateOfBirth dateOfBirth = personToRenew.getDateOfBirth();
        Email email = personToRenew.getEmail();
        EmergencyContact emergencyContact = personToRenew.getEmergencyContact();
        MembershipType updatedType = renewPersonDescriptor.getType().orElse(personToRenew.getMembershipType());
        MembershipJoinDate joinDate = personToRenew.getJoinDate();
        Remark remark = personToRenew.getRemark();
        // Extend membership from current expiry date (execute() rejects if already expired).
        LocalDate currentExpiry = personToRenew.getExpiryDate().getExpiryDate();
        LocalDate newExpiry;
        if (updatedType.toString().equalsIgnoreCase("annual")) {
            newExpiry = currentExpiry.plusYears(1);
        } else {
            newExpiry = currentExpiry.plusMonths(1);
        }
        MembershipExpiryDate expiryDate = new MembershipExpiryDate(newExpiry);

        return new Person(memberId, name, phone, gender, dateOfBirth, email,
                emergencyContact, updatedType, joinDate, expiryDate, remark);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RenewCommand)) {
            return false;
        }

        RenewCommand otherRenewCommand = (RenewCommand) other;
        return index.equals(otherRenewCommand.index)
                && renewPersonDescriptor.equals(otherRenewCommand.renewPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("renewPersonDescriptor", renewPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class RenewPersonDescriptor {
        private MembershipType type;

        public RenewPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public RenewPersonDescriptor(RenewPersonDescriptor toCopy) {
            setMembershipType(toCopy.type);
        }

        public void setMembershipType(MembershipType type) {
            this.type = type;
        }

        public Optional<MembershipType> getType() {
            return Optional.ofNullable(type);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof RenewPersonDescriptor)) {
                return false;
            }

            RenewPersonDescriptor otherRenewPersonDescriptor = (RenewPersonDescriptor) other;
            return Objects.equals(type, otherRenewPersonDescriptor.type);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("type", type)
                    .toString();
        }
    }
}
