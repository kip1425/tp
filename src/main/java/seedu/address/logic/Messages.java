package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND =
            "Unknown command. Type 'help' to see available commands.";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format.\n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX =
            "The selected person index is invalid. Use the index shown in the current list.";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d person(s) shown.";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "The following field(s) are already used by another person: ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Gender: ")
                .append(person.getGender())
                .append("; Date of Birth: ")
                .append(person.getDateOfBirth())
                .append("; Membership Type: ")
                .append(person.getMembershipType())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Emergency Contact: ")
                .append(person.getEmergencyContact())
                .append("; Join Date: ")
                .append(person.getJoinDate())
                .append("; Expiry Date: ")
                .append(person.getExpiryDate())
                .append("; Status: ")
                .append(person.getMemberStatus())
                .append("; Remarks: ")
                .append(person.getRemark());
        return builder.toString();
    }

}
