package seedu.address.model.person;

import java.time.LocalDate;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s membership join date is equal to the given date.
 */
public class JoinDateEqualsPredicate implements Predicate<Person> {
    private final LocalDate joinDate;

    public JoinDateEqualsPredicate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    @Override
    public boolean test(Person person) {
        return person.getJoinDate().getDate().isEqual(joinDate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof JoinDateEqualsPredicate otherPredicate)) {
            return false;
        }

        return joinDate.equals(otherPredicate.joinDate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("joinDateEquals", joinDate).toString();
    }
}
