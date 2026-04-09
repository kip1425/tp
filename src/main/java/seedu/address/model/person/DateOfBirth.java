package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Represents a Person's date of birth in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDateOfBirth(String)}
 */
public class DateOfBirth {

    public static final String MESSAGE_CONSTRAINTS =
            "Date of Birth should be in the format DD-MM-YYYY and should be a valid date.";
    public static final String VALIDATION_REGEX = "^((0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-((19|20)\\d\\d))$";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public final LocalDate dateOfBirth;

    /**
     * Constructs a {@code DateOfBirth}.
     *
     * @param dateOfBirth A valid date of birth string in DD-MM-YYYY format.
     */
    public DateOfBirth(String dateOfBirth) {
        requireNonNull(dateOfBirth);
        checkArgument(isValidDateOfBirth(dateOfBirth), MESSAGE_CONSTRAINTS);
        this.dateOfBirth = LocalDate.parse(dateOfBirth, FORMATTER);
    }

    public LocalDate getDate() {
        return dateOfBirth;
    }

    /**
     * Returns true if a given string is a valid date of birth.
     */
    public static boolean isValidDateOfBirth(String test) {
        //if (!test.matches(VALIDATION_REGEX)) {
        //    return false;
        //}

        try {
            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("dd-MM-uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);
            LocalDate.parse(test, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.dateOfBirth.format(FORMATTER);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DateOfBirth)) {
            return false;
        }

        DateOfBirth otherDateOfBirth = (DateOfBirth) other;
        return this.dateOfBirth.equals(otherDateOfBirth.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return this.dateOfBirth.hashCode();
    }
}
