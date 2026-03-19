package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        // Support field-specific keyword search by prefix (e.g. "find n/foo bar")
        if (trimmedArgs.startsWith("n/")) {
            String afterPrefix = trimmedArgs.substring("n/".length()).trim();
            if (afterPrefix.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
            String[] keywords = afterPrefix.split("\\s+");
            return new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        }

        String[] keywords = trimmedArgs.split("\\s+");
        return new FindCommand(new PersonContainsKeywordsPredicate(Arrays.asList(keywords)));
    }

}
