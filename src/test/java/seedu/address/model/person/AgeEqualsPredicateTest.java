package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class AgeEqualsPredicateTest {

    @Test
    public void equals() {
        AgeEqualsPredicate firstPredicate = new AgeEqualsPredicate(10);
        AgeEqualsPredicate secondPredicate = new AgeEqualsPredicate(20);

        assertTrue(firstPredicate.equals(firstPredicate));
        assertTrue(firstPredicate.equals(new AgeEqualsPredicate(10)));
        assertFalse(firstPredicate.equals(secondPredicate));
        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
    }

    @Test
    public void test_ageEquals_returnsTrue() {
        Person person = new PersonBuilder().withDateOfBirth("01-01-1990").build();
        int personAge = calculateAge("01-01-1990");
        AgeEqualsPredicate predicate = new AgeEqualsPredicate(personAge);
        assertTrue(predicate.test(person));
    }

    @Test
    public void test_ageEquals_returnsFalse() {
        Person person = new PersonBuilder().withDateOfBirth("01-01-1990").build();
        int personAge = calculateAge("01-01-1990");
        AgeEqualsPredicate predicate = new AgeEqualsPredicate(personAge + 1);
        assertFalse(predicate.test(person));
    }

    @Test
    public void toStringMethod() {
        AgeEqualsPredicate predicate = new AgeEqualsPredicate(25);
        assertTrue(predicate.toString().contains("ageEquals"));
    }

    private int calculateAge(String dateOfBirth) {
        java.time.LocalDate dob = java.time.LocalDate.parse(dateOfBirth,
                java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return java.time.Period.between(dob, java.time.LocalDate.now()).getYears();
    }
}
