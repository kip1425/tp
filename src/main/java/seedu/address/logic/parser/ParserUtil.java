package seedu.address.logic.parser;


import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.DateOfBirth;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyContact;
import seedu.address.model.person.Gender;
import seedu.address.model.person.MembershipExpiryDate;
import seedu.address.model.person.MembershipJoinDate;
import seedu.address.model.person.MembershipType;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index must be a positive whole number.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String gender} into a {@code Gender}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code gender} is invalid.
     */
    public static Gender parseGender(String gender) throws ParseException {
        requireNonNull(gender);
        String trimmedGender = gender.trim();
        if (!Gender.isValidGender(trimmedGender)) {
            throw new ParseException(Gender.MESSAGE_CONSTRAINTS);
        }
        return new Gender(trimmedGender);
    }

    /**
     * Parses a {@code String dateOfBirth} into a {@code DateOfBirth}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code dateOfBirth} is invalid.
     */
    public static DateOfBirth parseDateOfBirth(String dateOfBirth) throws ParseException {
        requireNonNull(dateOfBirth);
        String trimmedDateOfBirth = dateOfBirth.trim();
        if (!DateOfBirth.isValidDateOfBirth(trimmedDateOfBirth)) {
            throw new ParseException(DateOfBirth.MESSAGE_CONSTRAINTS);
        }
        return new DateOfBirth(trimmedDateOfBirth);
    }

    //    /**
    //     * Parses a {@code String memberStatus} into a {@code MemberStatus}.
    //     * Leading and trailing whitespaces will be trimmed.
    //     *
    //     * @throws ParseException if the given {@code memberStatus} is invalid.
    //     */
    //    public static MemberStatus parseMemberStatus(String memberStatus) throws ParseException {
    //        requireNonNull(memberStatus);
    //        String trimmedMemberStatus = memberStatus.trim();
    //        if (!MemberStatus.isValidMemberStatus(trimmedMemberStatus)) {
    //            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
    //        }
    //        return new MemberStatus(trimmedMemberStatus);
    //    }
    /**
     * Parses a {@code String emergencyContact} into an {@code EmergencyContact}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code emergencyContact} is invalid.
     */
    public static EmergencyContact parseEmergencyContact(String emergencyContact) throws ParseException {
        requireNonNull(emergencyContact);
        String trimmedEmergencyContact = emergencyContact.trim();
        if (!EmergencyContact.isValidEmergencyContact(trimmedEmergencyContact)) {
            throw new ParseException(EmergencyContact.MESSAGE_CONSTRAINTS);
        }
        return new EmergencyContact(trimmedEmergencyContact);
    }

    /**
     * Parses a {@code String joinDate} into a {@code MembershipJoinDate}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code joinDate} is invalid.
     */
    public static MembershipJoinDate parseJoinDate(String joinDate) throws ParseException {
        requireNonNull(joinDate);
        String trimmedJoinDate = joinDate.trim();
        if (!MembershipJoinDate.isValidJoinDate(trimmedJoinDate)) {
            throw new ParseException(MembershipJoinDate.MESSAGE_CONSTRAINTS);
        }
        return new MembershipJoinDate(trimmedJoinDate);
    }

    /**
     * Parses a {@code String expiryDate} into a {@code MembershipExpiryDate}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code expiryDate} is invalid.
     */
    public static MembershipExpiryDate parseExpiryDate(String expiryDate) throws ParseException {
        requireNonNull(expiryDate);
        String trimmedExpiryDate = expiryDate.trim();
        if (!MembershipExpiryDate.isValidExpiryDate(trimmedExpiryDate)) {
            throw new ParseException(MembershipExpiryDate.MESSAGE_CONSTRAINTS);
        }
        return new MembershipExpiryDate(trimmedExpiryDate);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }
    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static MembershipType parseType(String type) throws ParseException {
        requireNonNull(type);
        String trimmedType = type.trim();
        if (!MembershipType.isValidType(trimmedType)) {
            throw new ParseException(MembershipType.MESSAGE_CONSTRAINTS);
        }
        return new MembershipType(trimmedType);
    }

}
