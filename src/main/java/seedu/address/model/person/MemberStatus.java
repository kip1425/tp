package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;

/**
 * Represents a Person's member status in the address book.
 */
public class MemberStatus {

    public static final String MESSAGE_CONSTRAINTS =
            "Membership status should only be 'Valid', 'Invalid' or 'Pending'";
    public static final String VALIDATION_REGEX = "(?i)^(Valid|Invalid|Pending)$";
    public final String memberStatus;

    /**
     * Constructs a {@code MemberStatus}.
     *
     * @param expiry the expiry date of a membership.
     * @param join the expiry date of a membership.
     */
    public MemberStatus(LocalDate expiry, LocalDate join) {
        requireAllNonNull(expiry, join);
        LocalDate today = LocalDate.now();
        if (!expiry.isAfter(today)) {
            this.memberStatus = "Invalid";
        } else if (join.isAfter(today)) {
            this.memberStatus = "Pending";
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


