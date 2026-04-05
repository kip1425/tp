package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AGE_EQUAL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AGE_GREATER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_AGE_LESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXPIRY_DATE_AFTER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXPIRY_DATE_BEFORE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXPIRY_DATE_EQUALS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOIN_DATE_AFTER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOIN_DATE_BEFORE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOIN_DATE_EQUALS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEMBERSHIP_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEMBERSTATUS;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
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
import seedu.address.model.person.Person;
import seedu.address.model.person.StatusMatchesPredicate;

/**
 * Parses input arguments and creates a new FilterCommand object.
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FilterCommand
     * and returns an FilterCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FilterCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_MEMBERSTATUS, PREFIX_GENDER, PREFIX_MEMBERSHIP_TYPE,
                        PREFIX_AGE_GREATER, PREFIX_AGE_LESS, PREFIX_AGE_EQUAL,
                        PREFIX_JOIN_DATE_AFTER, PREFIX_JOIN_DATE_BEFORE, PREFIX_JOIN_DATE_EQUALS,
                        PREFIX_EXPIRY_DATE_AFTER, PREFIX_EXPIRY_DATE_BEFORE, PREFIX_EXPIRY_DATE_EQUALS);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_MEMBERSTATUS, PREFIX_GENDER, PREFIX_MEMBERSHIP_TYPE,
                PREFIX_AGE_GREATER, PREFIX_AGE_LESS, PREFIX_AGE_EQUAL,
                PREFIX_JOIN_DATE_AFTER, PREFIX_JOIN_DATE_BEFORE, PREFIX_JOIN_DATE_EQUALS,
                PREFIX_EXPIRY_DATE_AFTER, PREFIX_EXPIRY_DATE_BEFORE, PREFIX_EXPIRY_DATE_EQUALS);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        List<Predicate<Person>> predicates = new ArrayList<>();

        // Filter based on membership status
        if (argMultimap.getValue(PREFIX_MEMBERSTATUS).isPresent()) {
            String status = argMultimap.getValue(PREFIX_MEMBERSTATUS).get().trim();
            if (status.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
            }

            if (!MemberStatus.isValidStatus(status)) {
                throw new ParseException(MemberStatus.MESSAGE_CONSTRAINTS);
            }
            predicates.add(new StatusMatchesPredicate(status));
        }

        // Filter based on gender
        if (argMultimap.getValue(PREFIX_GENDER).isPresent()) {
            String gender = argMultimap.getValue(PREFIX_GENDER).get().trim();

            if (gender.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
            }

            if (!Gender.isValidGender(gender)) {
                throw new ParseException((Gender.MESSAGE_CONSTRAINTS));
            }
            predicates.add(new GenderMatchesPredicate(gender));
        }

        // Filter based on membership type
        if (argMultimap.getValue(PREFIX_MEMBERSHIP_TYPE).isPresent()) {
            String membershipType = argMultimap.getValue(PREFIX_MEMBERSHIP_TYPE).get().trim();
            if (membershipType.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
            }

            if (!MembershipType.isValidType(membershipType)) {
                throw new ParseException(MembershipType.MESSAGE_CONSTRAINTS);
            }
            predicates.add(new MembershipTypeMatchesPredicate(membershipType));
        }

        // Filter based on age
        if (argMultimap.getValue(PREFIX_AGE_GREATER).isPresent()) {

            int age = parseAge(argMultimap.getValue(PREFIX_AGE_GREATER).get());

            if (argMultimap.getValue(PREFIX_AGE_EQUAL).isPresent()) {
                int otherAge = parseAge(argMultimap.getValue(PREFIX_AGE_EQUAL).get());
                if (otherAge != age) {
                    throw new ParseException(
                            String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
                }
                age -= 1; // Adjust age for greater than or equal to
            }

            predicates.add(new AgeGreaterThanPredicate(age));
        } else if (argMultimap.getValue(PREFIX_AGE_LESS).isPresent()) {
            int age = parseAge(argMultimap.getValue(PREFIX_AGE_LESS).get());

            if (argMultimap.getValue(PREFIX_AGE_EQUAL).isPresent()) {
                int otherAge = parseAge(argMultimap.getValue(PREFIX_AGE_EQUAL).get());
                if (otherAge != age) {
                    throw new ParseException(
                            String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
                }
                age += 1; // Adjust age for less than or equal to
            }

            predicates.add(new AgeLessThanPredicate(age));
        } else if (argMultimap.getValue(PREFIX_AGE_EQUAL).isPresent()) {
            int age = parseAge(argMultimap.getValue(PREFIX_AGE_EQUAL).get());
            predicates.add(new AgeEqualsPredicate(age));
        }

        // Filter based on join date
        if (argMultimap.getValue(PREFIX_JOIN_DATE_AFTER).isPresent()) {
            String joinDate = argMultimap.getValue(PREFIX_JOIN_DATE_AFTER).get().trim();
            verifyJoinDate(joinDate);

            if (argMultimap.getValue(PREFIX_JOIN_DATE_EQUALS).isPresent()) {
                String otherJoinDate = argMultimap.getValue(PREFIX_JOIN_DATE_EQUALS).get().trim();
                verifyJoinDate(otherJoinDate);
                if (!joinDate.equals(otherJoinDate)) {
                    throw new ParseException(
                            String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
                }
                predicates.add(new JoinDateAfterPredicate(ParserUtil.parseJoinDate(joinDate)
                        .getDate().minusDays(1)));
            } else {
                predicates.add(new JoinDateAfterPredicate(ParserUtil.parseJoinDate(joinDate).getDate()));
            }

        } else if (argMultimap.getValue(PREFIX_JOIN_DATE_BEFORE).isPresent()) {
            String joinDate = argMultimap.getValue(PREFIX_JOIN_DATE_BEFORE).get().trim();
            verifyJoinDate(joinDate);

            if (argMultimap.getValue(PREFIX_JOIN_DATE_EQUALS).isPresent()) {
                String otherJoinDate = argMultimap.getValue(PREFIX_JOIN_DATE_EQUALS).get().trim();
                verifyJoinDate(otherJoinDate);
                if (!joinDate.equals(otherJoinDate)) {
                    throw new ParseException(
                            String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
                }
                predicates.add(new JoinDateBeforePredicate(ParserUtil.parseJoinDate(joinDate)
                        .getDate().plusDays(1)));
            } else {
                predicates.add(new JoinDateBeforePredicate(ParserUtil.parseJoinDate(joinDate).getDate()));
            }

        } else if (argMultimap.getValue(PREFIX_JOIN_DATE_EQUALS).isPresent()) {
            String joinDate = argMultimap.getValue(PREFIX_JOIN_DATE_EQUALS).get().trim();
            verifyJoinDate(joinDate);
            predicates.add(new JoinDateEqualsPredicate(ParserUtil.parseJoinDate(joinDate).getDate()));
        }

        // Filter based on expiry date
        if (argMultimap.getValue(PREFIX_EXPIRY_DATE_AFTER).isPresent()) {
            String expiryDate = argMultimap.getValue(PREFIX_EXPIRY_DATE_AFTER).get().trim();
            verifyExpiryDate(expiryDate);

            if (argMultimap.getValue(PREFIX_EXPIRY_DATE_EQUALS).isPresent()) {
                String otherExpiryDate = argMultimap.getValue(PREFIX_EXPIRY_DATE_EQUALS).get().trim();
                verifyExpiryDate(otherExpiryDate);
                if (!expiryDate.equals(otherExpiryDate)) {
                    throw new ParseException(
                            String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
                }
                predicates.add(new ExpiryDateAfterPredicate(ParserUtil.parseExpiryDate(expiryDate)
                                                                .getExpiryDate().minusDays(1)));
            } else {
                predicates.add(new ExpiryDateAfterPredicate(ParserUtil.parseExpiryDate(expiryDate).getExpiryDate()));
            }

        } else if (argMultimap.getValue(PREFIX_EXPIRY_DATE_BEFORE).isPresent()) {
            String expiryDate = argMultimap.getValue(PREFIX_EXPIRY_DATE_BEFORE).get().trim();
            verifyExpiryDate(expiryDate);

            if (argMultimap.getValue(PREFIX_EXPIRY_DATE_EQUALS).isPresent()) {
                String otherExpiryDate = argMultimap.getValue(PREFIX_EXPIRY_DATE_EQUALS).get().trim();
                verifyExpiryDate(otherExpiryDate);
                if (!expiryDate.equals(otherExpiryDate)) {
                    throw new ParseException(
                            String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
                }
                predicates.add(new ExpiryDateBeforePredicate(ParserUtil.parseExpiryDate(expiryDate)
                                                                .getExpiryDate().plusDays(1)));
            } else {
                predicates.add(new ExpiryDateBeforePredicate(ParserUtil.parseExpiryDate(expiryDate).getExpiryDate()));
            }

        } else if (argMultimap.getValue(PREFIX_EXPIRY_DATE_EQUALS).isPresent()) {
            String expiryDate = argMultimap.getValue(PREFIX_EXPIRY_DATE_EQUALS).get().trim();
            if (expiryDate.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
            }

            if (!MembershipExpiryDate.isValidExpiryDate(expiryDate)) {
                throw new ParseException(MembershipExpiryDate.MESSAGE_CONSTRAINTS);
            }
            predicates.add(new ExpiryDateEqualsPredicate(ParserUtil.parseExpiryDate(expiryDate).getExpiryDate()));
        }

        if (predicates.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        Predicate<Person> combinedPredicate = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            combinedPredicate = combinedPredicate.and(predicates.get(i));
        }

        return new FilterCommand(combinedPredicate);
    }

    /**
     * Helper function to verify the validity of the join date string.
     * @param joinDate
     * @throws ParseException
     */
    public void verifyJoinDate(String joinDate) throws ParseException {
        if (joinDate.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        if (!MembershipJoinDate.isValidJoinDate(joinDate)) {
            throw new ParseException(MembershipJoinDate.MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Helper function to verify the validity of the expiry date string.
     * @param expiryDate
     * @throws ParseException
     */
    public void verifyExpiryDate(String expiryDate) throws ParseException {
        if (expiryDate.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        if (!MembershipExpiryDate.isValidExpiryDate(expiryDate)) {
            throw new ParseException(MembershipExpiryDate.MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Helper function to parse string to integer.
     * @param age string representation of age
     * @throws ParseException if the user input does not conform the expected format
     */
    private int parseAge(String age) throws ParseException {
        String trimmedAge = age.trim();
        if (trimmedAge.isEmpty() || !StringUtil.isNonZeroUnsignedInteger(trimmedAge)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }
        return Integer.parseInt(trimmedAge);
    }
}
