package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATEOFBIRTH_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMERGENCY_CONTACT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GENDER_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TYPE_BOB;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonTest {
    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same phone, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).withEmail(VALID_EMAIL_BOB)
                .withEmergencyContact(VALID_EMERGENCY_CONTACT_BOB).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // different phone, all other attributes same -> returns true (same email)
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // different phone and different email, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertTrue(BOB.isSamePerson(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertTrue(BOB.isSamePerson(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));
        assertEquals(ALICE.hashCode(), aliceCopy.hashCode());

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different gender -> returns false
        editedAlice = new PersonBuilder(ALICE).withGender(VALID_GENDER_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different dateOfBirth -> returns false
        editedAlice = new PersonBuilder(ALICE).withDateOfBirth(VALID_DATEOFBIRTH_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different type -> returns false
        editedAlice = new PersonBuilder(ALICE).withType(VALID_TYPE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmergencyContact(VALID_EMERGENCY_CONTACT_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different join date -> returns false
        editedAlice = new PersonBuilder(ALICE).withJoinDate("02-02-2026").build();
        assertFalse(ALICE.equals(editedAlice));

        // different expiry date -> returns false
        editedAlice = new PersonBuilder(ALICE).withExpiryDate("02-02-2027").build();
        assertFalse(ALICE.equals(editedAlice));

        // different remark -> returns false
        editedAlice = new PersonBuilder(ALICE).withRemark("Prefers morning classes").build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void constructor_joinDateBeforeDateOfBirth_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new PersonBuilder(ALICE).withJoinDate("01-01-1989").build());
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName()
                + "{member id=" + ALICE.getId()
                + ", name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", gender=" + ALICE.getGender() + ", date of birth=" + ALICE.getDateOfBirth()
                + ", type=" + ALICE.getMembershipType()
                + ", email=" + ALICE.getEmail()
                + ", emergency contact=" + ALICE.getEmergencyContact() + "}";
        assertEquals(expected, ALICE.toString());
    }
}
