package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class MembershipJoinDateTest {

    @Test
    public void constructor_noArgs_usesCurrentDate() {
        MembershipJoinDate date = new MembershipJoinDate();
        assertNotNull(date.getDate());
    }

    @Test
    public void constructor_stringDate_parsesCorrectly() {
        MembershipJoinDate date = new MembershipJoinDate("01-01-2024");
        assertEquals("01-01-2024", date.toString());
    }

    @Test
    public void isValidJoinDate() {
        assertFalse(MembershipJoinDate.isValidJoinDate(""));
        assertFalse(MembershipJoinDate.isValidJoinDate("2024-01-01"));
        assertFalse(MembershipJoinDate.isValidJoinDate("31-02-2025"));
        assertFalse(MembershipJoinDate.isValidJoinDate("31-04-2025"));
        assertFalse(MembershipJoinDate.isValidJoinDate("29-02-2025"));

        assertTrue(MembershipJoinDate.isValidJoinDate("01-01-2024"));
        assertTrue(MembershipJoinDate.isValidJoinDate("29-02-2024"));
    }

    @Test
    public void equals() {
        MembershipJoinDate date = new MembershipJoinDate("01-01-2024");

        // same object -> returns true
        assertTrue(date.equals(date));

        // same value -> returns true
        assertTrue(date.equals(new MembershipJoinDate("01-01-2024")));

        // null -> returns false
        assertFalse(date.equals(null));

        // different type -> returns false
        assertFalse(date.equals("01-01-2024"));

        // different value -> returns false
        assertFalse(date.equals(new MembershipJoinDate("02-01-2024")));
    }

    @Test
    public void hashCode_sameValue_sameHashCode() {
        assertEquals(
                new MembershipJoinDate("01-01-2024").hashCode(),
                new MembershipJoinDate("01-01-2024").hashCode());
    }
}
