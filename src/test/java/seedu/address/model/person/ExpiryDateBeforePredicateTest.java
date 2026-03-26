package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class ExpiryDateBeforePredicateTest {

    @Test
    public void equals() {
        ExpiryDateBeforePredicate firstPredicate = new ExpiryDateBeforePredicate(LocalDate.of(2027, 3, 12));
        ExpiryDateBeforePredicate secondPredicate = new ExpiryDateBeforePredicate(LocalDate.of(2027, 3, 11));

        assertTrue(firstPredicate.equals(firstPredicate));
        assertTrue(firstPredicate.equals(new ExpiryDateBeforePredicate(LocalDate.of(2027, 3, 12))));
        assertFalse(firstPredicate.equals(secondPredicate));
        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
    }

    @Test
    public void test_expiryDateBefore_returnsTrue() {
        Person person = new PersonBuilder().build();
        ExpiryDateBeforePredicate predicate = new ExpiryDateBeforePredicate(LocalDate.of(2027, 3, 12));
        assertTrue(predicate.test(person));
    }

    @Test
    public void test_expiryDateBefore_returnsFalse() {
        Person person = new PersonBuilder().build();
        ExpiryDateBeforePredicate predicate = new ExpiryDateBeforePredicate(LocalDate.of(2027, 3, 10));
        assertFalse(predicate.test(person));
    }

    @Test
    public void toStringMethod() {
        ExpiryDateBeforePredicate predicate = new ExpiryDateBeforePredicate(LocalDate.of(2027, 3, 10));
        assertTrue(predicate.toString().contains("expiryDateBefore"));
    }
}
