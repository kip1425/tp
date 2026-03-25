package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DATEOFBIRTH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMERGENCY_CONTACT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOIN_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEMBERSHIP_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.Person;

/**
 * A utility class for Person.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code person}.
     */
    public static String getAddCommand(Person person) {
        return AddCommand.COMMAND_WORD + " " + getPersonDetails(person);
    }

    /**
     * Returns the part of command string for the given {@code person}'s details.
     */
    public static String getPersonDetails(Person person) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + person.getName().fullName + " ");
        sb.append(PREFIX_PHONE + person.getPhone().value + " ");
        sb.append(PREFIX_GENDER + person.getGender().gender + " ");
        sb.append(PREFIX_DATEOFBIRTH + person.getDateOfBirth().dateOfBirth + " ");
        sb.append(PREFIX_EMAIL + person.getEmail().value + " ");
        sb.append(PREFIX_MEMBERSHIP_TYPE + person.getMembershipType().value + " ");
        sb.append(PREFIX_EMERGENCY_CONTACT + person.getEmergencyContact().value + " ");
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditPersonDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        descriptor.getGender().ifPresent(gender -> sb.append(PREFIX_GENDER).append(gender.gender).append(" "));
        descriptor.getDateOfBirth().ifPresent(
                dateOfBirth -> sb.append(PREFIX_DATEOFBIRTH).append(dateOfBirth.dateOfBirth).append(" "));
        descriptor.getEmail().ifPresent(email -> sb.append(PREFIX_EMAIL).append(email.value).append(" "));
        descriptor.getEmergencyContact().ifPresent(
                contact -> sb.append(PREFIX_EMERGENCY_CONTACT).append(contact.value).append(" "));
        descriptor.getJoinDate().ifPresent(join -> sb.append(PREFIX_JOIN_DATE).append(join.value).append(" "));
        descriptor.getType().ifPresent(
                membershipType -> sb.append(PREFIX_MEMBERSHIP_TYPE).append(membershipType.value).append(" "));
        return sb.toString();
    }
}
