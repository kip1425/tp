package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class AgeGreaterThanPredicateTest {

    @Test
    public void equals() {
        AgeGreaterThanPredicate firstPredicate = new AgeGreaterThanPredicate(10);
        AgeGreaterThanPredicate secondPredicate = new AgeGreaterThanPredicate(20);

        assertTrue(firstPredicate.equals(firstPredicate));
        assertTrue(firstPredicate.equals(new AgeGreaterThanPredicate(10)));
        assertFalse(firstPredicate.equals(secondPredicate));
        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
    }

    @Test
    public void test_ageGreaterThan_returnsTrue() {
        Person person = new PersonBuilder().withDateOfBirth("01-01-1990").build();
        int personAge = calculateAge("01-01-1990");
        AgeGreaterThanPredicate predicate = new AgeGreaterThanPredicate(personAge - 1);
        assertTrue(predicate.test(person));
    }

    @Test
    public void test_ageGreaterThan_returnsFalse() {
        Person person = new PersonBuilder().withDateOfBirth("01-01-1990").build();
        int personAge = calculateAge("01-01-1990");
        AgeGreaterThanPredicate predicate = new AgeGreaterThanPredicate(personAge);
        assertFalse(predicate.test(person));
    }

    @Test
    public void toStringMethod() {
        AgeGreaterThanPredicate predicate = new AgeGreaterThanPredicate(25);
        assertTrue(predicate.toString().contains("ageGreaterThan"));
    }

    private int calculateAge(String dateOfBirth) {
        java.time.LocalDate dob = java.time.LocalDate.parse(dateOfBirth,
                java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return java.time.Period.between(dob, java.time.LocalDate.now()).getYears();
    }
}
