package seedu.address.model.person;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s age is greater than the given value.
 */
public class AgeGreaterThanPredicate implements Predicate<Person> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final int age;

    public AgeGreaterThanPredicate(int age) {
        this.age = age;
    }

    @Override
    public boolean test(Person person) {
        LocalDate dateOfBirth = LocalDate.parse(person.getDateOfBirth().dateOfBirth, FORMATTER);
        int personAge = Period.between(dateOfBirth, LocalDate.now()).getYears();
        return personAge > age;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AgeGreaterThanPredicate otherPredicate)) {
            return false;
        }

        return age == otherPredicate.age;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("ageGreaterThan", age).toString();
    }
}
