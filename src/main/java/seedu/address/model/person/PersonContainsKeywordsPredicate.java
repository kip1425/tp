package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that any of a {@code Person}'s fields contain the given query as a literal substring (case-insensitive).
 */
public class PersonContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public PersonContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        return keywords.stream().anyMatch(keyword -> {
            String lowerKeyword = keyword.toLowerCase();
            return person.getName().fullName.toLowerCase().contains(lowerKeyword)
                    || person.getPhone().value.toLowerCase().contains(lowerKeyword)
                    || person.getEmail().value.toLowerCase().contains(lowerKeyword)
                    || person.getGender().gender.toLowerCase().contains(lowerKeyword)
                    || person.getEmergencyContact().value.toLowerCase().contains(lowerKeyword)
                    || person.getMembershipType().value.toLowerCase().contains(lowerKeyword)
                    || person.getMemberStatus().memberStatus.toLowerCase().contains(lowerKeyword)
                    || person.getId().toString().toLowerCase().contains(lowerKeyword);
        });
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PersonContainsKeywordsPredicate)) {
            return false;
        }

        PersonContainsKeywordsPredicate otherPredicate = (PersonContainsKeywordsPredicate) other;
        return keywords.equals(otherPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
