package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class MembershipExpiryDateTest {

    @Test
    public void constructor_stringDate_parsesCorrectly() {
        MembershipExpiryDate date = new MembershipExpiryDate("01-01-2025");
        assertEquals("01-01-2025", date.toString());
    }

    @Test
    public void constructor_localDate_storesCorrectly() {
        LocalDate localDate = LocalDate.of(2025, 6, 15);
        MembershipExpiryDate date = new MembershipExpiryDate(localDate);
        assertEquals(localDate, date.getExpiryDate());
    }

    @Test
    public void constructor_annualType_addsOneYear() {
        LocalDate joinDate = LocalDate.of(2024, 1, 1);
        MembershipType annual = new MembershipType("annual");
        MembershipExpiryDate expiry = new MembershipExpiryDate(joinDate, annual);
        assertEquals(LocalDate.of(2025, 1, 1), expiry.getExpiryDate());
    }

    @Test
    public void constructor_monthlyType_addsOneMonth() {
        LocalDate joinDate = LocalDate.of(2024, 1, 1);
        MembershipType monthly = new MembershipType("monthly");
        MembershipExpiryDate expiry = new MembershipExpiryDate(joinDate, monthly);
        assertEquals(LocalDate.of(2024, 2, 1), expiry.getExpiryDate());
    }

    @Test
    public void isValidExpiryDate() {
        assertFalse(MembershipExpiryDate.isValidExpiryDate(""));
        assertFalse(MembershipExpiryDate.isValidExpiryDate("2025-01-01"));

        assertTrue(MembershipExpiryDate.isValidExpiryDate("01-01-2025"));
    }

    @Test
    public void equals() {
        MembershipExpiryDate date = new MembershipExpiryDate("01-01-2025");

        // same object -> returns true
        assertTrue(date.equals(date));

        // same value -> returns true
        assertTrue(date.equals(new MembershipExpiryDate("01-01-2025")));

        // null -> returns false
        assertFalse(date.equals(null));

        // different type -> returns false
        assertFalse(date.equals("01-01-2025"));

        // different value -> returns false
        assertFalse(date.equals(new MembershipExpiryDate("02-01-2025")));
    }

    @Test
    public void hashCode_sameValue_sameHashCode() {
        assertEquals(
                new MembershipExpiryDate("01-01-2025").hashCode(),
                new MembershipExpiryDate("01-01-2025").hashCode());
    }
}
