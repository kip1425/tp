package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATEOFBIRTH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMERGENCY_CONTACT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
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
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.person.exceptions.DuplicatePersonException;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_GENDER + "GENDER] "
            + "[" + PREFIX_DATEOFBIRTH + "DATEOFBIRTH] "
            + "[" + PREFIX_EMERGENCY_CONTACT + "EMERGENCY_CONTACT] \n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Updated person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "Specify at least one field to update.";
    public static final String MESSAGE_DUPLICATE_FIELDS = Messages.MESSAGE_DUPLICATE_FIELDS;

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;
    private Person originalPerson;
    private Person editedPerson;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person updatedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        boolean isPhoneDuplicate = false;
        boolean isEmailDuplicate = false;

        for (Person existingPerson : model.getAddressBook().getPersonList()) {
            // Skip checking duplicates against the person being edited.
            if (existingPerson.getId().equals(personToEdit.getId())) {
                continue;
            }

            isPhoneDuplicate = isPhoneDuplicate || existingPerson.getPhone().equals(updatedPerson.getPhone());
            isEmailDuplicate = isEmailDuplicate || existingPerson.getEmail().equals(updatedPerson.getEmail());
        }

        if (isPhoneDuplicate || isEmailDuplicate) {
            throw new CommandException(MESSAGE_DUPLICATE_FIELDS
                    + formatDuplicateFields(isPhoneDuplicate, isEmailDuplicate));
        }

        try {
            model.setPerson(personToEdit, updatedPerson);
        } catch (DuplicatePersonException e) {
            // Convert model-level duplicate errors into user-facing messages with clear priority.
            throw new CommandException(getDuplicateMessage(personToEdit, updatedPerson, model));
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        originalPerson = personToEdit;
        editedPerson = updatedPerson;
        return new CommandResult(
                String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(updatedPerson)), editedPerson);
    }

    private static String getDuplicateMessage(Person personToEdit, Person updatedPerson, Model model) {
        boolean isPhoneDuplicate = false;
        boolean isEmailDuplicate = false;

        for (Person existingPerson : model.getAddressBook().getPersonList()) {
            if (existingPerson == personToEdit) {
                continue;
            }

            isPhoneDuplicate = isPhoneDuplicate || existingPerson.getPhone().equals(updatedPerson.getPhone());
            isEmailDuplicate = isEmailDuplicate || existingPerson.getEmail().equals(updatedPerson.getEmail());
        }

        return MESSAGE_DUPLICATE_FIELDS + formatDuplicateFields(isPhoneDuplicate, isEmailDuplicate);
    }

    private static String formatDuplicateFields(boolean isPhoneDuplicate, boolean isEmailDuplicate) {
        if (isPhoneDuplicate && isEmailDuplicate) {
            return "phone and email";
        }
        if (isPhoneDuplicate) {
            return "phone";
        }
        return "email";
    }

    @Override
    public boolean isUndoable() {
        return true;
    }

    @Override
    public void undo(Model model) throws CommandException {
        requireNonNull(model);

        if (originalPerson == null || editedPerson == null) {
            throw new CommandException("Cannot undo edit: original data is missing.");
        }

        if (!model.hasPerson(editedPerson)) {
            throw new CommandException("Cannot undo edit: the updated person is no longer in the address book.");
        }

        model.setPerson(editedPerson, originalPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        MemberId memberId = personToEdit.getId();
        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Gender updatedGender = editPersonDescriptor.getGender().orElse(personToEdit.getGender());
        DateOfBirth updatedDateOfBirth = editPersonDescriptor.getDateOfBirth().orElse(personToEdit.getDateOfBirth());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        EmergencyContact updatedEmergencyContact = editPersonDescriptor.getEmergencyContact()
                                                                        .orElse(personToEdit.getEmergencyContact());
        MembershipJoinDate joinDate = personToEdit.getJoinDate();
        MembershipExpiryDate expiryDate = personToEdit.getExpiryDate();
        Remark remark = personToEdit.getRemark();

        return new Person(memberId, updatedName, updatedPhone, updatedGender, updatedDateOfBirth, updatedEmail,
                updatedEmergencyContact, personToEdit.getMembershipType(), joinDate, expiryDate, remark);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Gender gender;
        private DateOfBirth dateOfBirth;
        private EmergencyContact emergencyContact;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setGender(toCopy.gender);
            setDateOfBirth(toCopy.dateOfBirth);
            setEmail(toCopy.email);
            setEmergencyContact(toCopy.emergencyContact);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(
                    name, phone, gender, dateOfBirth, email, emergencyContact);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setGender(Gender gender) {
            this.gender = gender;
        }

        public Optional<Gender> getGender() {
            return Optional.ofNullable(gender);
        }

        public void setDateOfBirth(DateOfBirth dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public Optional<DateOfBirth> getDateOfBirth() {
            return Optional.ofNullable(dateOfBirth);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setEmergencyContact(EmergencyContact emergencyContact) {
            this.emergencyContact = emergencyContact;
        }

        public Optional<EmergencyContact> getEmergencyContact() {
            return Optional.ofNullable(emergencyContact);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(gender, otherEditPersonDescriptor.gender)
                    && Objects.equals(dateOfBirth, otherEditPersonDescriptor.dateOfBirth)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(emergencyContact, otherEditPersonDescriptor.emergencyContact);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("gender", gender)
                    .add("date of birth", dateOfBirth)
                    .add("email", email)
                    .add("emergency contact", emergencyContact)
                    .toString();
        }
    }
}
