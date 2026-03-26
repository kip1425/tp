package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class DateOfBirthTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DateOfBirth(null));
    }

    @Test
    public void constructor_invalidDateOfBirth_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new DateOfBirth(""));
        assertThrows(IllegalArgumentException.class, () -> new DateOfBirth("2000-01-01"));
        assertThrows(IllegalArgumentException.class, () -> new DateOfBirth("32-01-2000"));
    }

    @Test
    public void isValidDateOfBirth() {
        assertFalse(DateOfBirth.isValidDateOfBirth(""));
        assertFalse(DateOfBirth.isValidDateOfBirth("2000-01-01"));
        assertFalse(DateOfBirth.isValidDateOfBirth("32-01-2000"));

        assertTrue(DateOfBirth.isValidDateOfBirth("01-01-2000"));
        assertTrue(DateOfBirth.isValidDateOfBirth("31-12-1999"));
    }

    @Test
    public void toString_validDate_returnsFormattedString() {
        DateOfBirth dob = new DateOfBirth("15-06-1990");
        assertEquals("15-06-1990", dob.toString());
    }

    @Test
    public void equals() {
        DateOfBirth dob = new DateOfBirth("01-01-2000");

        // same object -> returns true
        assertTrue(dob.equals(dob));

        // same value -> returns true
        assertTrue(dob.equals(new DateOfBirth("01-01-2000")));

        // null -> returns false
        assertFalse(dob.equals(null));

        // different type -> returns false
        assertFalse(dob.equals("01-01-2000"));

        // different value -> returns false
        assertFalse(dob.equals(new DateOfBirth("02-01-2000")));
    }

    @Test
    public void hashCode_sameValue_sameHashCode() {
        assertEquals(new DateOfBirth("01-01-2000").hashCode(), new DateOfBirth("01-01-2000").hashCode());
    }
}
