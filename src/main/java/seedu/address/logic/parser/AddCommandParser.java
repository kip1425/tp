package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATEOFBIRTH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMERGENCY_CONTACT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOIN_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEMBERSHIP_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.stream.Stream;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.GenerateMemberIds;
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

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_GENDER, PREFIX_DATEOFBIRTH,
                        PREFIX_EMAIL, PREFIX_EMERGENCY_CONTACT, PREFIX_MEMBERSHIP_TYPE, PREFIX_JOIN_DATE);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_PHONE, PREFIX_GENDER, PREFIX_DATEOFBIRTH,
                PREFIX_EMAIL, PREFIX_EMERGENCY_CONTACT, PREFIX_MEMBERSHIP_TYPE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_GENDER, PREFIX_DATEOFBIRTH,
                PREFIX_EMAIL, PREFIX_EMERGENCY_CONTACT, PREFIX_MEMBERSHIP_TYPE, PREFIX_JOIN_DATE);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Gender gender = ParserUtil.parseGender(argMultimap.getValue(PREFIX_GENDER).get());
        DateOfBirth dateOfBirth = ParserUtil.parseDateOfBirth(argMultimap.getValue(PREFIX_DATEOFBIRTH).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        EmergencyContact emergencyContact = ParserUtil.parseEmergencyContact(argMultimap
                                                                       .getValue(PREFIX_EMERGENCY_CONTACT).get());
        MembershipType membershipType = ParserUtil.parseType(argMultimap.getValue(PREFIX_MEMBERSHIP_TYPE).get());
        MembershipJoinDate joinDate = argMultimap.getValue(PREFIX_JOIN_DATE).isPresent()
                ? ParserUtil.parseJoinDate(argMultimap.getValue(PREFIX_JOIN_DATE).get())
                : new MembershipJoinDate();
        MemberId memberId = GenerateMemberIds.generateNextId();
        MembershipExpiryDate expiryDate = new MembershipExpiryDate(joinDate.getDate(), membershipType);
        Person person;
        try {
            person = new Person(memberId, name, phone, gender, dateOfBirth, email, emergencyContact,
                    membershipType, joinDate, expiryDate);
        } catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage(), e);
        }

        return new AddCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
