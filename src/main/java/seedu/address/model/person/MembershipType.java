package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;


/**
 * Represents a Members's membership type in FitDesk.
 * Guarantees: immutable; is valid as declared in {@link #isValidType(String)}
 */
public class MembershipType {

    public static final String MESSAGE_CONSTRAINTS =
            "Membership types should only be 'monthly' or 'annual' (case-insensitive)";
    public static final String VALIDATION_REGEX = "(?i)^(Monthly|Annual)$";
    public final String value;

    /**
     * Constructs a {@code Membership Type}.
     *
     * @param type A valid type.
     */
    public MembershipType(String type) {
        requireNonNull(type);
        checkArgument(isValidType(type), MESSAGE_CONSTRAINTS);
        if (type.equalsIgnoreCase("annual")) {
            this.value = "Annual";
        } else {
            this.value = "Monthly";
        }
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidType(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MembershipType)) {
            return false;
        }

        MembershipType otherType = (MembershipType) other;
        return value.equals(otherType.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
