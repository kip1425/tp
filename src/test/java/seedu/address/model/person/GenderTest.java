package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class GenderTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Gender(null));
    }

    @Test
    public void constructor_invalidGender_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Gender("X"));
        assertThrows(IllegalArgumentException.class, () -> new Gender(""));
    }

    @Test
    public void isValidGender() {
        assertFalse(Gender.isValidGender(""));
        assertFalse(Gender.isValidGender("X"));
        assertFalse(Gender.isValidGender("Male"));

        assertTrue(Gender.isValidGender("M"));
        assertTrue(Gender.isValidGender("m"));
        assertTrue(Gender.isValidGender("F"));
        assertTrue(Gender.isValidGender("f"));
    }

    @Test
    public void equals() {
        Gender gender = new Gender("M");

        // same object -> returns true
        assertTrue(gender.equals(gender));

        // same value -> returns true
        assertTrue(gender.equals(new Gender("M")));

        // null -> returns false
        assertFalse(gender.equals(null));

        // different type -> returns false
        assertFalse(gender.equals("M"));

        // different value -> returns false
        assertFalse(gender.equals(new Gender("F")));
    }

    @Test
    public void hashCode_sameValue_sameHashCode() {
        assertEquals(new Gender("M").hashCode(), new Gender("M").hashCode());
    }
}
