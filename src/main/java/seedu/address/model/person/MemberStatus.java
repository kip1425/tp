package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;

/**
 * Represents a Person's member status in the address book.
 */
public class MemberStatus {

    public static final String MESSAGE_CONSTRAINTS =
            "Membership status should only be 'Valid' or 'Invalid'";

    public static final String VALIDATION_REGEX = "(?i)^(Valid|Invalid)$";
    
    public final String memberStatus;

    /**
     * Constructs a {@code MemberStatus}.
     *
     * @param date the expiry date of a membership.
     */
    public MemberStatus(LocalDate date) {
        requireNonNull(date);
        if (date.isBefore(LocalDate.now())) {
            this.memberStatus = "Invalid";
        } else {
            this.memberStatus = "Valid";
        }
    }

    /**
     * Returns true if a given string is a valid status
     */
    public static boolean isValidStatus(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return this.memberStatus;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MemberStatus)) {
            return false;
        }

        MemberStatus otherStatus = (MemberStatus) other;
        return this.memberStatus.equals(otherStatus.memberStatus);
    }

    @Override
    public int hashCode() {
        return this.memberStatus.hashCode();
    }

}


