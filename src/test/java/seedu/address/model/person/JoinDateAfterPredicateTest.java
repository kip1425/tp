package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class JoinDateAfterPredicateTest {

    @Test
    public void equals() {
        JoinDateAfterPredicate firstPredicate = new JoinDateAfterPredicate(LocalDate.of(2026, 3, 10));
        JoinDateAfterPredicate secondPredicate = new JoinDateAfterPredicate(LocalDate.of(2026, 3, 11));

        assertTrue(firstPredicate.equals(firstPredicate));
        assertTrue(firstPredicate.equals(new JoinDateAfterPredicate(LocalDate.of(2026, 3, 10))));
        assertFalse(firstPredicate.equals(secondPredicate));
        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
    }

    @Test
    public void test_joinDateAfter_returnsTrue() {
        Person person = new PersonBuilder().withDateOfBirth("01-01-1990").build();
        JoinDateAfterPredicate predicate = new JoinDateAfterPredicate(LocalDate.of(2026, 3, 10));
        assertTrue(predicate.test(person));
    }

    @Test
    public void test_joinDateAfter_returnsFalse() {
        Person person = new PersonBuilder().withDateOfBirth("01-01-1990").build();
        JoinDateAfterPredicate predicate = new JoinDateAfterPredicate(LocalDate.of(2026, 3, 12));
        assertFalse(predicate.test(person));
    }

    @Test
    public void toStringMethod() {
        JoinDateAfterPredicate predicate = new JoinDateAfterPredicate(LocalDate.of(2026, 3, 12));
        assertTrue(predicate.toString().contains("joinDateAfter"));
    }
}
