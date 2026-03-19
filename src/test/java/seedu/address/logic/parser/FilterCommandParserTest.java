package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AGE_EQUAL;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.person.AgeEqualsPredicate;
import seedu.address.model.person.AgeGreaterThanPredicate;
import seedu.address.model.person.AgeLessThanPredicate;
import seedu.address.model.person.GenderMatchesPredicate;
import seedu.address.model.person.JoinDateAfterPredicate;
import seedu.address.model.person.JoinDateBeforePredicate;
import seedu.address.model.person.MembershipJoinDate;
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
        assertDoesNotThrow(() -> parser.parse(" age=/21 j>/01-01-2024"));
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
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }
}
