package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class AgeLessThanPredicateTest {

    @Test
    public void equals() {
        AgeLessThanPredicate firstPredicate = new AgeLessThanPredicate(10);
        AgeLessThanPredicate secondPredicate = new AgeLessThanPredicate(20);

        assertTrue(firstPredicate.equals(firstPredicate));
        assertTrue(firstPredicate.equals(new AgeLessThanPredicate(10)));
        assertFalse(firstPredicate.equals(secondPredicate));
        assertFalse(firstPredicate.equals(1));
        assertFalse(firstPredicate.equals(null));
    }

    @Test
    public void test_ageLessThan_returnsTrue() {
        Person person = new PersonBuilder().withDateOfBirth("01-01-1990").build();
        int personAge = calculateAge("01-01-1990");
        AgeLessThanPredicate predicate = new AgeLessThanPredicate(personAge + 1);
        assertTrue(predicate.test(person));
    }

    @Test
    public void test_ageLessThan_returnsFalse() {
        Person person = new PersonBuilder().withDateOfBirth("01-01-1990").build();
        int personAge = calculateAge("01-01-1990");
        AgeLessThanPredicate predicate = new AgeLessThanPredicate(personAge);
        assertFalse(predicate.test(person));
    }

    @Test
    public void toStringMethod() {
        AgeLessThanPredicate predicate = new AgeLessThanPredicate(25);
        assertTrue(predicate.toString().contains("ageLessThan"));
    }

    private int calculateAge(String dateOfBirth) {
        java.time.LocalDate dob = java.time.LocalDate.parse(dateOfBirth,
                java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        return java.time.Period.between(dob, java.time.LocalDate.now()).getYears();
    }
}
