package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_FILTER_RANGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AGE_EQUAL;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.person.AgeEqualsPredicate;
import seedu.address.model.person.AgeGreaterThanPredicate;
import seedu.address.model.person.AgeLessThanPredicate;
import seedu.address.model.person.ExpiryDateAfterPredicate;
import seedu.address.model.person.ExpiryDateBeforePredicate;
import seedu.address.model.person.ExpiryDateEqualsPredicate;
import seedu.address.model.person.Gender;
import seedu.address.model.person.GenderMatchesPredicate;
import seedu.address.model.person.JoinDateAfterPredicate;
import seedu.address.model.person.JoinDateBeforePredicate;
import seedu.address.model.person.JoinDateEqualsPredicate;
import seedu.address.model.person.MemberStatus;
import seedu.address.model.person.MembershipExpiryDate;
import seedu.address.model.person.MembershipJoinDate;
import seedu.address.model.person.MembershipType;
import seedu.address.model.person.MembershipTypeMatchesPredicate;
import seedu.address.model.person.StatusMatchesPredicate;

public class FilterCommandParserTest {

    private FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_ageEquals_success() {
        FilterCommand expectedCommand = new FilterCommand(new AgeEqualsPredicate(21));
        assertParseSuccess(parser, " age=/21", expectedCommand);
    }

    @Test
    public void parse_multipleFilters_success() {
        assertDoesNotThrow(() -> parser.parse(" age=/21 j>/01-01-2024 exp</01-01-2026"));
    }

    @Test
    public void parse_status_success() {
        FilterCommand expectedCommand = new FilterCommand(new StatusMatchesPredicate("valid"));
        assertParseSuccess(parser, " s/valid", expectedCommand);
    }

    @Test
    public void parse_gender_success() {
        FilterCommand expectedCommand = new FilterCommand(new GenderMatchesPredicate("F"));
        assertParseSuccess(parser, " g/F", expectedCommand);
    }

    @Test
    public void parse_membershipType_success() {
        FilterCommand expectedCommand = new FilterCommand(new MembershipTypeMatchesPredicate("Annual"));
        assertParseSuccess(parser, " m/Annual", expectedCommand);
    }

    @Test
    public void parse_ageGreater_success() {
        FilterCommand expectedCommand = new FilterCommand(new AgeGreaterThanPredicate(30));
        assertParseSuccess(parser, " age>/30", expectedCommand);
    }

    @Test
    public void parse_ageLess_success() {
        FilterCommand expectedCommand = new FilterCommand(new AgeLessThanPredicate(30));
        assertParseSuccess(parser, " age</30", expectedCommand);
    }

    @Test
    public void parse_ageRange_success() {
        assertDoesNotThrow(() -> parser.parse(" age>/20 age</30"));
    }

    @Test
    public void parse_ageLessEquals_success() {
        assertDoesNotThrow(() -> parser.parse(" age</30 age=/30"));
    }

    @Test
    public void parse_ageGreaterEquals_success() {
        assertDoesNotThrow(() -> parser.parse(" age>/20 age=/20"));
    }

    @Test
    public void parse_ageLessEquals_mismatchFailure() {
        assertParseFailure(parser, " age</30 age=/31",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_ageGreaterEquals_mismatchFailure() {
        assertParseFailure(parser, " age>/20 age=/21",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_ageAllThree_conflictingPrefixes() {
        assertParseFailure(parser, " age>/20 age</30 age=/25",
                Messages.getErrorMessageForConflictingPrefixes(
                        CliSyntax.PREFIX_AGE_GREATER, CliSyntax.PREFIX_AGE_LESS, CliSyntax.PREFIX_AGE_EQUAL));
    }

    @Test
    public void parse_joinDateAfter_success() {
        FilterCommand expectedCommand =
                new FilterCommand(new JoinDateAfterPredicate(new seedu.address.model.person.MembershipJoinDate(
                        "01-01-2024").getDate()));
        assertParseSuccess(parser, " j>/01-01-2024", expectedCommand);
    }

    @Test
    public void parse_joinDateBefore_success() {
        FilterCommand expectedCommand =
                new FilterCommand(new JoinDateBeforePredicate(new seedu.address.model.person.MembershipJoinDate(
                        "01-01-2024").getDate()));
        assertParseSuccess(parser, " j</01-01-2024", expectedCommand);
    }

    @Test
    public void parse_joinDateRange_success() {
        assertDoesNotThrow(() -> parser.parse(" j>/01-01-2024 j</01-01-2025"));
    }

    @Test
    public void parse_joinDateBeforeEquals_success() {
        assertDoesNotThrow(() -> parser.parse(" j</01-01-2024 j=/01-01-2024"));
    }

    @Test
    public void parse_joinDateAfterEquals_success() {
        assertDoesNotThrow(() -> parser.parse(" j>/01-01-2024 j=/01-01-2024"));
    }

    @Test
    public void parse_joinDateBeforeEquals_mismatchFailure() {
        assertParseFailure(parser, " j</01-01-2024 j=/01-01-2025",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_joinDateAfterEquals_mismatchFailure() {
        assertParseFailure(parser, " j>/01-01-2024 j=/01-01-2025",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_joinDateAllThree_conflictingPrefixes() {
        assertParseFailure(parser, " j>/01-01-2024 j</01-01-2025 j=/01-01-2024",
                Messages.getErrorMessageForConflictingPrefixes(
                        CliSyntax.PREFIX_JOIN_DATE_AFTER, CliSyntax.PREFIX_JOIN_DATE_BEFORE,
                        CliSyntax.PREFIX_JOIN_DATE_EQUALS));
    }

    @Test
    public void parse_expiryDateAfter_success() {
        FilterCommand expectedCommand =
                new FilterCommand(new ExpiryDateAfterPredicate(new seedu.address.model.person.MembershipExpiryDate(
                        "01-01-2026").getExpiryDate()));
        assertParseSuccess(parser, " exp>/01-01-2026", expectedCommand);
    }

    @Test
    public void parse_expiryDateBefore_success() {
        FilterCommand expectedCommand =
                new FilterCommand(new ExpiryDateBeforePredicate(new seedu.address.model.person.MembershipExpiryDate(
                        "01-01-2026").getExpiryDate()));
        assertParseSuccess(parser, " exp</01-01-2026", expectedCommand);
    }

    @Test
    public void parse_expiryDateEquals_success() {
        FilterCommand expectedCommand =
                new FilterCommand(new ExpiryDateEqualsPredicate(new seedu.address.model.person.MembershipExpiryDate(
                        "01-01-2026").getExpiryDate()));
        assertParseSuccess(parser, " exp=/01-01-2026", expectedCommand);
    }

    @Test
    public void parse_expiryDateRange_success() {
        assertDoesNotThrow(() -> parser.parse(" exp>/01-01-2026 exp</01-01-2027"));
    }

    @Test
    public void parse_expiryDateBeforeEquals_success() {
        assertDoesNotThrow(() -> parser.parse(" exp</01-01-2026 exp=/01-01-2026"));
    }

    @Test
    public void parse_expiryDateAfterEquals_success() {
        assertDoesNotThrow(() -> parser.parse(" exp>/01-01-2026 exp=/01-01-2026"));
    }

    @Test
    public void parse_expiryDateBeforeEquals_mismatchFailure() {
        assertParseFailure(parser, " exp</01-01-2026 exp=/01-01-2027",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_expiryDateAfterEquals_mismatchFailure() {
        assertParseFailure(parser, " exp>/01-01-2026 exp=/01-01-2027",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_expiryDateAllThree_conflictingPrefixes() {
        assertParseFailure(parser, " exp>/01-01-2026 exp</01-01-2027 exp=/01-01-2026",
                Messages.getErrorMessageForConflictingPrefixes(
                        CliSyntax.PREFIX_EXPIRY_DATE_AFTER, CliSyntax.PREFIX_EXPIRY_DATE_BEFORE,
                        CliSyntax.PREFIX_EXPIRY_DATE_EQUALS));
    }

    @Test
    public void parse_invalidAge_failure() {
        assertParseFailure(parser, " age=/abc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_zeroAge_failure() {
        assertParseFailure(parser, " age=/0",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyAge_failure() {
        assertParseFailure(parser, " age=/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyJoinDate_failure() {
        assertParseFailure(parser, " j>/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidJoinDate_failure() {
        assertParseFailure(parser, " j>/1990-01-01", MembershipJoinDate.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_emptyExpiryDate_failure() {
        assertParseFailure(parser, " exp>/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyExpiryDateBefore_failure() {
        assertParseFailure(parser, " exp</",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyExpiryDateEquals_failure() {
        assertParseFailure(parser, " exp=/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidExpiryDate_failure() {
        assertParseFailure(parser, " exp>/1990-01-01", MembershipExpiryDate.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_emptyStatus_failure() {
        assertParseFailure(parser, " s/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyGender_failure() {
        assertParseFailure(parser, " g/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyMembershipType_failure() {
        assertParseFailure(parser, " m/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_preamble_failure() {
        assertParseFailure(parser, " preamble age=/21",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_duplicatePrefix_failure() {
        assertParseFailure(parser, " age=/21 age=/22",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_AGE_EQUAL));
    }

    @Test
    public void parse_emptyJoinDateBefore_failure() {
        assertParseFailure(parser, " j</",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_joinDateEquals_success() {
        FilterCommand expectedCommand =
                new FilterCommand(new JoinDateEqualsPredicate(new MembershipJoinDate("01-01-2024").getDate()));
        assertParseSuccess(parser, " j=/01-01-2024", expectedCommand);
    }

    @Test
    public void parse_emptyJoinDateEquals_failure() {
        assertParseFailure(parser, " j=/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void pare_invalidMemberStatus_throwsParseException() {
        assertParseFailure(parser, " s/inv", MemberStatus.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidMembershipType_throwsParseException() {
        assertParseFailure(parser, " m/Gold", MembershipType.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidGender_throwsParseException() {
        assertParseFailure(parser, " g/k", Gender.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_ageRangeInvalid_failure() {
        assertParseFailure(parser, " age>/25 age</20", MESSAGE_INVALID_FILTER_RANGE);
    }

    @Test
    public void parse_ageRangeEqual_failure() {
        assertParseFailure(parser, " age>/25 age</25", MESSAGE_INVALID_FILTER_RANGE);
    }

    @Test
    public void parse_joinDateRangeInvalid_failure() {
        assertParseFailure(parser, " j>/01-01-2025 j</01-01-2024", MESSAGE_INVALID_FILTER_RANGE);
    }

    @Test
    public void parse_joinDateRangeEqual_failure() {
        assertParseFailure(parser, " j>/01-01-2025 j</01-01-2025", MESSAGE_INVALID_FILTER_RANGE);
    }

    @Test
    public void parse_expiryDateRangeInvalid_failure() {
        assertParseFailure(parser, " exp>/01-01-2026 exp</01-01-2025", MESSAGE_INVALID_FILTER_RANGE);
    }

    @Test
    public void parse_expiryDateRangeEqual_failure() {
        assertParseFailure(parser, " exp>/01-01-2026 exp</01-01-2026", MESSAGE_INVALID_FILTER_RANGE);
    }
}
