package seedu.address.model.person;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * Represents the start date of a member's membership
 */
public class MembershipExpiryDate {
    public static final String MESSAGE_CONSTRAINTS =
            "Membership expiry date should be in the format DD-MM-YYYY and should be a valid date.";
    public static final String VALIDATION_REGEX = "^((0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-((19|20)\\d\\d))$";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public final LocalDate expiryDate;
    /**
     * represents the expiry date of member's membership
     * @param date date the member joined the gym
     * @param type refers to the type of membership a gym member has chosen
     */
    public MembershipExpiryDate(LocalDate date, MembershipType type) {
        if (type.toString().equalsIgnoreCase("annual")) {
            this.expiryDate = date.plusYears(1);
        } else {
            this.expiryDate = date.plusMonths(1);
        }
    }

    /**
     *
     */
    public MembershipExpiryDate(String date) {
        this.expiryDate = LocalDate.parse(date, FORMATTER);
    }

    /**
     *
     */
    public MembershipExpiryDate(LocalDate date) {
        this.expiryDate = date;
    }

    public LocalDate getExpiryDate() {
        return this.expiryDate;
    }

    /**
     * Returns true if a given string is a valid membership expiry date.
     */
    public static boolean isValidExpiryDate(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return expiryDate.format(FORMATTER);
    }
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MembershipExpiryDate)) {
            return false;
        }

        MembershipExpiryDate otherExpiryDate = (MembershipExpiryDate) other;
        return this.expiryDate.equals(otherExpiryDate.expiryDate);
    }

    @Override
    public int hashCode() {
        return expiryDate.hashCode();
    }
}
