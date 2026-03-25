package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        PersonContainsKeywordsPredicate firstPredicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList("first"));
        PersonContainsKeywordsPredicate secondPredicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList("second"));

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PersonContainsKeywordsPredicate firstPredicateCopy =
                new PersonContainsKeywordsPredicate(Collections.singletonList("first"));
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different query -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_fieldContainsQuery_returnsTrue() {
        // Name match
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList("Amy"));
        assertTrue(predicate.test(new PersonBuilder().withName("Amy Bee").build()));

        // Phone match
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("8535"));
        assertTrue(predicate.test(new PersonBuilder().withPhone("85355255").build()));

        // Email match
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("amy@gmail"));
        assertTrue(predicate.test(new PersonBuilder().withEmail("amy@gmail.com").build()));

        // Gender match
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("F"));
        assertTrue(predicate.test(new PersonBuilder().withGender("F").build()));

        // Emergency contact match
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("9123"));
        assertTrue(predicate.test(new PersonBuilder().withEmergencyContact("91236811").build()));

        // Membership type match
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("annual"));
        assertTrue(predicate.test(new PersonBuilder().withType("annual").build()));

        // Membership status match
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("valid"));
        assertTrue(predicate.test(new PersonBuilder().build()));

        // Case-insensitive match
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("AMY BEE"));
        assertTrue(predicate.test(new PersonBuilder().withName("Amy Bee").build()));

        // Literal multi-word substring match
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("Amy Bee"));
        assertTrue(predicate.test(new PersonBuilder().withName("Amy Bee").build()));
    }

    @Test
    public void test_fieldDoesNotContainQuery_returnsFalse() {
        // Non-matching query
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(Collections.singletonList("Carol"));
        assertFalse(predicate.test(new PersonBuilder().withName("Amy Bee").build()));

        // Multi-word query not present as literal substring
        predicate = new PersonContainsKeywordsPredicate(Collections.singletonList("Amy Carol"));
        assertFalse(predicate.test(new PersonBuilder().withName("Amy Bee").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1");
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(keywords);

        String expected = PersonContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
