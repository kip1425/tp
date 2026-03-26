package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class JoinDateEqualsPredicateTest {

    @Test
    public void equals() {
        JoinDateEqualsPredicate firstPredicate =
                new JoinDateEqualsPredicate(LocalDate.of(2026, 3, 12));
        JoinDateEqualsPredicate secondPredicate =
                new JoinDateEqualsPredicate(LocalDate.of(2026, 3, 11));

        assertTrue(firstPredicate.equals(firstPredicate));
        assertTrue(firstPredicate.equals(
                                       new JoinDateEqualsPredicate(LocalDate.of(2026, 3, 12))));
        assertFalse(firstPredicate.equals(secondPredicate));
        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
    }

    @Test
    public void toStringMethod() {
        JoinDateEqualsPredicate predicate = new JoinDateEqualsPredicate(LocalDate.of(2026, 3, 10));
        assertTrue(predicate.toString().contains("joinDateEquals"));
    }
}
