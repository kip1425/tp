package seedu.address.model.person;

import java.time.LocalDate;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s membership expiry date is equal to the given date.
 */
public class ExpiryDateEqualsPredicate implements Predicate<Person> {
    private final LocalDate expiryDate;

    public ExpiryDateEqualsPredicate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean test(Person person) {
        return person.getExpiryDate().getExpiryDate().isEqual(expiryDate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ExpiryDateEqualsPredicate otherPredicate)) {
            return false;
        }

        return expiryDate.equals(otherPredicate.expiryDate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("expiryDateEquals", expiryDate).toString();
    }
}
