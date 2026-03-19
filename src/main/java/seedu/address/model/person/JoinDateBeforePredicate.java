package seedu.address.model.person;

import java.time.LocalDate;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s membership join date is before the given date.
 */
public class JoinDateBeforePredicate implements Predicate<Person> {
    private final LocalDate joinDate;

    public JoinDateBeforePredicate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    @Override
    public boolean test(Person person) {
        return person.getJoinDate().getDate().isBefore(joinDate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof JoinDateBeforePredicate otherPredicate)) {
            return false;
        }

        return joinDate.equals(otherPredicate.joinDate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("joinDateBefore", joinDate).toString();
    }
}
