package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DATEOFBIRTH_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DATEOFBIRTH_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMERGENCY_CONTACT_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMERGENCY_CONTACT_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.GENDER_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.GENDER_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DATEOFBIRTH_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMERGENCY_CONTACT_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_GENDER_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_JOIN_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TYPE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.JOIN_DATE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.JOIN_DATE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.TYPE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TYPE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATEOFBIRTH_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMERGENCY_CONTACT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GENDER_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_JOIN_DATE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TYPE_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATEOFBIRTH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMERGENCY_CONTACT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOIN_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEMBERSHIP_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddCommand;
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

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @BeforeEach
    public void setUp() {
        GenerateMemberIds.initialize(0); // reset counter to 0
    }

    @Test
    public void parse_allFieldsPresent_success() {
        MemberId testId1 = new MemberId(1);
        MembershipJoinDate joinDate = new MembershipJoinDate();
        MembershipExpiryDate expiryDate =
                new MembershipExpiryDate(joinDate.getDate(), new MembershipType(VALID_TYPE_BOB));
        Person expectedPerson = new Person(testId1, BOB.getName(), BOB.getPhone(), BOB.getGender(),
                BOB.getDateOfBirth(), BOB.getEmail(), BOB.getEmergencyContact(), BOB.getMembershipType(),
                joinDate, expiryDate, BOB.getRemark());

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + TYPE_DESC_BOB + GENDER_DESC_BOB + DATEOFBIRTH_DESC_BOB
                + EMERGENCY_CONTACT_DESC_BOB, new AddCommand(expectedPerson));
    }

    @Test
    public void parse_optionalJoinDatePresent_success() {
        MemberId testId1 = new MemberId(1);
        MembershipJoinDate joinDate = new MembershipJoinDate(VALID_JOIN_DATE_BOB);
        MembershipExpiryDate expiryDate =
                new MembershipExpiryDate(joinDate.getDate(), new MembershipType(VALID_TYPE_BOB));
        Person expectedPerson = new Person(testId1, BOB.getName(), BOB.getPhone(), BOB.getGender(),
                BOB.getDateOfBirth(), BOB.getEmail(), BOB.getEmergencyContact(), BOB.getMembershipType(),
                joinDate, expiryDate, BOB.getRemark());

        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + TYPE_DESC_BOB + GENDER_DESC_BOB + DATEOFBIRTH_DESC_BOB
                + EMERGENCY_CONTACT_DESC_BOB + JOIN_DATE_DESC_BOB, new AddCommand(expectedPerson));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + GENDER_DESC_BOB + DATEOFBIRTH_DESC_BOB + TYPE_DESC_BOB
                + EMERGENCY_CONTACT_DESC_BOB;
        String validExpectedPersonStringWithJoinDate = validExpectedPersonString + JOIN_DATE_DESC_BOB;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));
        // multiple gender
        assertParseFailure(parser, GENDER_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_GENDER));
        // multiple date of birth
        assertParseFailure(parser, DATEOFBIRTH_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATEOFBIRTH));
        // multiple membership type
        assertParseFailure(parser, TYPE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_MEMBERSHIP_TYPE));
        // multiple join date
        assertParseFailure(parser, JOIN_DATE_DESC_AMY + validExpectedPersonStringWithJoinDate,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_JOIN_DATE));

        // multiple emails
        assertParseFailure(parser, EMAIL_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // multiple addresses
        assertParseFailure(parser, EMERGENCY_CONTACT_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMERGENCY_CONTACT));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedPersonString + PHONE_DESC_AMY + EMAIL_DESC_AMY + NAME_DESC_AMY + EMERGENCY_CONTACT_DESC_AMY
                        + GENDER_DESC_AMY + DATEOFBIRTH_DESC_AMY + TYPE_DESC_AMY + JOIN_DATE_DESC_AMY
                        + validExpectedPersonStringWithJoinDate,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_EMERGENCY_CONTACT, PREFIX_EMAIL,
                        PREFIX_PHONE, PREFIX_MEMBERSHIP_TYPE, PREFIX_GENDER, PREFIX_DATEOFBIRTH, PREFIX_JOIN_DATE));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, INVALID_EMAIL_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));
        // invalid gender
        assertParseFailure(parser, INVALID_GENDER_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_GENDER));
        // invalid date of birth
        assertParseFailure(parser, INVALID_DATEOFBIRTH_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATEOFBIRTH));
        // invalid membership type
        assertParseFailure(parser, INVALID_TYPE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_MEMBERSHIP_TYPE));
        // invalid join date
        assertParseFailure(parser, INVALID_JOIN_DATE_DESC + validExpectedPersonStringWithJoinDate,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_JOIN_DATE));

        // invalid emergency contact
        assertParseFailure(parser, INVALID_EMERGENCY_CONTACT_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMERGENCY_CONTACT));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, validExpectedPersonString + INVALID_EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, validExpectedPersonString + INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, validExpectedPersonString + INVALID_EMERGENCY_CONTACT_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMERGENCY_CONTACT));

        // invalid gender
        assertParseFailure(parser, validExpectedPersonString + INVALID_GENDER_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_GENDER));

        // invalid date of birth
        assertParseFailure(parser, validExpectedPersonString + INVALID_DATEOFBIRTH_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DATEOFBIRTH));

        // invalid membership type
        assertParseFailure(parser, validExpectedPersonString + INVALID_TYPE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_MEMBERSHIP_TYPE));
        // invalid join date
        assertParseFailure(parser, validExpectedPersonStringWithJoinDate + INVALID_JOIN_DATE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_JOIN_DATE));

    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + GENDER_DESC_BOB + DATEOFBIRTH_DESC_BOB
                + TYPE_DESC_BOB + EMAIL_DESC_BOB + EMERGENCY_CONTACT_DESC_BOB, expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + GENDER_DESC_BOB + DATEOFBIRTH_DESC_BOB
                + TYPE_DESC_BOB + EMAIL_DESC_BOB + EMERGENCY_CONTACT_DESC_BOB, expectedMessage);

        // missing gender prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_GENDER_BOB + DATEOFBIRTH_DESC_BOB
                + TYPE_DESC_BOB + EMAIL_DESC_BOB + EMERGENCY_CONTACT_DESC_BOB, expectedMessage);

        // missing date of birth prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + GENDER_DESC_BOB + VALID_DATEOFBIRTH_BOB
                + TYPE_DESC_BOB + EMAIL_DESC_BOB + EMERGENCY_CONTACT_DESC_BOB, expectedMessage);

        // missing membership type prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + GENDER_DESC_BOB + DATEOFBIRTH_DESC_BOB
                + VALID_TYPE_BOB + EMAIL_DESC_BOB + EMERGENCY_CONTACT_DESC_BOB, expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + GENDER_DESC_BOB + DATEOFBIRTH_DESC_BOB
                + TYPE_DESC_BOB + VALID_EMAIL_BOB + EMERGENCY_CONTACT_DESC_BOB, expectedMessage);

        // missing emergency contact prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + GENDER_DESC_BOB + DATEOFBIRTH_DESC_BOB
                + TYPE_DESC_BOB + EMAIL_DESC_BOB + VALID_EMERGENCY_CONTACT_BOB, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB + VALID_EMERGENCY_CONTACT_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + GENDER_DESC_BOB
                + DATEOFBIRTH_DESC_BOB + TYPE_DESC_BOB + EMAIL_DESC_BOB + EMERGENCY_CONTACT_DESC_BOB,
                Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + GENDER_DESC_BOB
                + DATEOFBIRTH_DESC_BOB + TYPE_DESC_BOB + EMAIL_DESC_BOB + EMERGENCY_CONTACT_DESC_BOB,
                Phone.MESSAGE_CONSTRAINTS);

        // invalid gender
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_GENDER_DESC
                + DATEOFBIRTH_DESC_BOB + TYPE_DESC_BOB + EMAIL_DESC_BOB + EMERGENCY_CONTACT_DESC_BOB,
                Gender.MESSAGE_CONSTRAINTS);

        // invalid date of birth
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + GENDER_DESC_BOB
                + INVALID_DATEOFBIRTH_DESC + TYPE_DESC_BOB + EMAIL_DESC_BOB + EMERGENCY_CONTACT_DESC_BOB,
                DateOfBirth.MESSAGE_CONSTRAINTS);

        // invalid membership type
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + GENDER_DESC_BOB
                + DATEOFBIRTH_DESC_BOB + INVALID_TYPE_DESC + EMAIL_DESC_BOB + EMERGENCY_CONTACT_DESC_BOB,
                MembershipType.MESSAGE_CONSTRAINTS);
        // invalid join date
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + GENDER_DESC_BOB
                + DATEOFBIRTH_DESC_BOB + TYPE_DESC_BOB + EMAIL_DESC_BOB + EMERGENCY_CONTACT_DESC_BOB
                + INVALID_JOIN_DATE_DESC, MembershipJoinDate.MESSAGE_CONSTRAINTS);

        // join date before date of birth
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + GENDER_DESC_BOB
                + DATEOFBIRTH_DESC_BOB + TYPE_DESC_BOB + EMAIL_DESC_BOB + EMERGENCY_CONTACT_DESC_BOB
                + " " + PREFIX_JOIN_DATE + "02-03-2002", Person.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + GENDER_DESC_BOB
                + DATEOFBIRTH_DESC_BOB + TYPE_DESC_BOB + INVALID_EMAIL_DESC + EMERGENCY_CONTACT_DESC_BOB,
                Email.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + GENDER_DESC_BOB
                + DATEOFBIRTH_DESC_BOB + TYPE_DESC_BOB + EMAIL_DESC_BOB + INVALID_EMERGENCY_CONTACT_DESC,
                EmergencyContact.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + GENDER_DESC_BOB
                + DATEOFBIRTH_DESC_BOB + TYPE_DESC_BOB + EMAIL_DESC_BOB + INVALID_EMERGENCY_CONTACT_DESC,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + GENDER_DESC_BOB + DATEOFBIRTH_DESC_BOB + TYPE_DESC_BOB + EMERGENCY_CONTACT_DESC_BOB,
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
