package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class ExpiryDateEqualsPredicateTest {

    @Test
    public void equals() {
        ExpiryDateEqualsPredicate firstPredicate = new ExpiryDateEqualsPredicate(LocalDate.of(2027, 3, 12));
        ExpiryDateEqualsPredicate secondPredicate = new ExpiryDateEqualsPredicate(LocalDate.of(2027, 3, 11));

        assertTrue(firstPredicate.equals(firstPredicate));
        assertTrue(firstPredicate.equals(new ExpiryDateEqualsPredicate(LocalDate.of(2027, 3, 12))));
        assertFalse(firstPredicate.equals(secondPredicate));
        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
    }

    @Test
    public void toStringMethod() {
        ExpiryDateEqualsPredicate predicate = new ExpiryDateEqualsPredicate(LocalDate.of(2027, 3, 12));
        assertTrue(predicate.toString().contains("expiryDateEquals"));
    }
}
