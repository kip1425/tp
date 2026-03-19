package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class JoinDateBeforePredicateTest {

    @Test
    public void equals() {
        JoinDateBeforePredicate firstPredicate = new JoinDateBeforePredicate(LocalDate.of(2026, 3, 12));
        JoinDateBeforePredicate secondPredicate = new JoinDateBeforePredicate(LocalDate.of(2026, 3, 11));

        assertTrue(firstPredicate.equals(firstPredicate));
        assertTrue(firstPredicate.equals(new JoinDateBeforePredicate(LocalDate.of(2026, 3, 12))));
        assertFalse(firstPredicate.equals(secondPredicate));
        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
    }

    @Test
    public void test_joinDateBefore_returnsTrue() {
        Person person = new PersonBuilder().withDateOfBirth("01-01-1990").build();
        JoinDateBeforePredicate predicate = new JoinDateBeforePredicate(LocalDate.of(2026, 3, 12));
        assertTrue(predicate.test(person));
    }

    @Test
    public void test_joinDateBefore_returnsFalse() {
        Person person = new PersonBuilder().withDateOfBirth("01-01-1990").build();
        JoinDateBeforePredicate predicate = new JoinDateBeforePredicate(LocalDate.of(2026, 3, 10));
        assertFalse(predicate.test(person));
    }

    @Test
    public void toStringMethod() {
        JoinDateBeforePredicate predicate = new JoinDateBeforePredicate(LocalDate.of(2026, 3, 10));
        assertTrue(predicate.toString().contains("joinDateBefore"));
    }
}
