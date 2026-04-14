package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Gender;
import seedu.address.model.person.MembershipExpiryDate;
import seedu.address.model.person.MembershipJoinDate;
import seedu.address.model.person.MembershipType;
import seedu.address.model.person.Person;

public class JsonAdaptedPersonTest {

    private static final String VALID_ID = "M001";
    private static final String VALID_NAME = "Alice Pauline";
    private static final String VALID_PHONE = "94351253";
    private static final String VALID_GENDER = "F";
    private static final String VALID_DOB = "01-01-1990";
    private static final String VALID_EMAIL = "alice@example.com";
    private static final String VALID_EC = "81236811";

    private static JsonAdaptedPerson personWith(String type, String joinDate, String expiryDate) {
        return new JsonAdaptedPerson(VALID_ID, VALID_NAME, VALID_PHONE, VALID_GENDER, VALID_DOB,
                VALID_EMAIL, VALID_EC, type, joinDate, expiryDate, "");
    }

    private static MembershipExpiryDate expectedFirstPeriod(String joinDate, String type) {
        return new MembershipExpiryDate(new MembershipJoinDate(joinDate).getDate(), new MembershipType(type));
    }

    @Test
    public void toModelType_nullExpiry_throwsIllegalValueException() {
        JsonAdaptedPerson json = personWith("Annual", "11-03-2026", null);
        assertThrows(IllegalValueException.class,
                String.format(JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT,
                        MembershipExpiryDate.class.getSimpleName()),
                json::toModelType);
    }

    @Test
    public void toModelType_blankExpiry_throwsIllegalValueException() {
        JsonAdaptedPerson json = personWith("Monthly", "15-01-2026", "   ");
        assertThrows(IllegalValueException.class,
                String.format(JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT,
                        MembershipExpiryDate.class.getSimpleName()),
                json::toModelType);
    }

    @Test
    public void toModelType_expiryTrimmedAndMatching_keepsStored() throws Exception {
        JsonAdaptedPerson json = personWith("Annual", "11-03-2026", "  11-03-2027  ");
        Person model = json.toModelType();
        assertEquals(new MembershipExpiryDate("11-03-2027"), model.getExpiryDate());
    }

    @Test
    public void toModelType_expiryInconsistent_normalizesToDerived() throws Exception {
        JsonAdaptedPerson json = personWith("Annual", "11-03-2026", "01-06-2028");
        Person model = json.toModelType();
        assertEquals(expectedFirstPeriod("11-03-2026", "Annual"), model.getExpiryDate());
    }

    @Test
    public void toModelType_expiryBeforeAnchor_normalizesToDerived() throws Exception {
        JsonAdaptedPerson json = personWith("Annual", "11-03-2026", "11-03-2026");
        Person model = json.toModelType();
        assertEquals(expectedFirstPeriod("11-03-2026", "Annual"), model.getExpiryDate());
    }

    @Test
    public void toModelType_expiryNotOnRenewalChain_normalizesToDerived() throws Exception {
        // Anchor 15-02-2026; next step 15-03-2026; 20-02-2026 falls strictly between steps.
        JsonAdaptedPerson json = personWith("Monthly", "15-01-2026", "20-02-2026");
        Person model = json.toModelType();
        assertEquals(expectedFirstPeriod("15-01-2026", "Monthly"), model.getExpiryDate());
    }

    @Test
    public void toModelType_expirySecondRenewalAnnual_keepsStored() throws Exception {
        JsonAdaptedPerson json = personWith("Annual", "11-03-2025", "11-03-2027");
        Person model = json.toModelType();
        assertEquals(new MembershipExpiryDate("11-03-2027"), model.getExpiryDate());
    }

    @Test
    public void toModelType_expirySecondRenewalMonthly_keepsStored() throws Exception {
        JsonAdaptedPerson json = personWith("Monthly", "15-01-2026", "15-03-2026");
        Person model = json.toModelType();
        assertEquals(new MembershipExpiryDate("15-03-2026"), model.getExpiryDate());
    }

    @Test
    public void toModelType_invalidExpiryFormat_throwsIllegalValueException() {
        JsonAdaptedPerson json = personWith("Annual", "11-03-2026", "not-a-date");
        assertThrows(IllegalValueException.class, MembershipExpiryDate.MESSAGE_CONSTRAINTS, json::toModelType);
    }

    @Test
    public void toModelType_invalidMemberId_throwsIllegalValueException() {
        JsonAdaptedPerson json = new JsonAdaptedPerson("BAD", VALID_NAME, VALID_PHONE, VALID_GENDER, VALID_DOB,
                VALID_EMAIL, VALID_EC, "Annual", "11-03-2026", "11-03-2027", "");
        assertThrows(IllegalValueException.class, "MemberId should be in the format M###.", json::toModelType);
    }

    @Test
    public void toModelType_invalidJoinDate_throwsIllegalValueException() {
        JsonAdaptedPerson json = personWith("Annual", "not-a-date", "11-03-2027");
        assertThrows(IllegalValueException.class, MembershipJoinDate.MESSAGE_CONSTRAINTS, json::toModelType);
    }

    @Test
    public void toModelType_invalidGender_throwsGenderConstraintsMessage() {
        JsonAdaptedPerson json = new JsonAdaptedPerson(VALID_ID, VALID_NAME, VALID_PHONE, "Y", VALID_DOB,
                VALID_EMAIL, VALID_EC, "Annual", "11-03-2026", "11-03-2027", "");
        assertThrows(IllegalValueException.class, Gender.MESSAGE_CONSTRAINTS, json::toModelType);
    }
}
