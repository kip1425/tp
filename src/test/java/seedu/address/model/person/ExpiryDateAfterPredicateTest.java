package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class ExpiryDateAfterPredicateTest {

    @Test
    public void equals() {
        ExpiryDateAfterPredicate firstPredicate = new ExpiryDateAfterPredicate(LocalDate.of(2026, 3, 10));
        ExpiryDateAfterPredicate secondPredicate = new ExpiryDateAfterPredicate(LocalDate.of(2026, 3, 11));

        assertTrue(firstPredicate.equals(firstPredicate));
        assertTrue(firstPredicate.equals(new ExpiryDateAfterPredicate(LocalDate.of(2026, 3, 10))));
        assertFalse(firstPredicate.equals(secondPredicate));
        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
    }

    @Test
    public void test_expiryDateAfter_returnsTrue() {
        Person person = new PersonBuilder().build();
        ExpiryDateAfterPredicate predicate = new ExpiryDateAfterPredicate(LocalDate.of(2026, 3, 10));
        assertTrue(predicate.test(person));
    }

    @Test
    public void test_expiryDateAfter_returnsFalse() {
        Person person = new PersonBuilder().build();
        ExpiryDateAfterPredicate predicate = new ExpiryDateAfterPredicate(LocalDate.of(2027, 3, 12));
        assertFalse(predicate.test(person));
    }

    @Test
    public void toStringMethod() {
        ExpiryDateAfterPredicate predicate = new ExpiryDateAfterPredicate(LocalDate.of(2027, 3, 12));
        assertTrue(predicate.toString().contains("expiryDateAfter"));
    }
}
