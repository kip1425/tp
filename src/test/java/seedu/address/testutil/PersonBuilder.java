package seedu.address.testutil;

import seedu.address.model.person.DateOfBirth;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyContact;
import seedu.address.model.person.Gender;
import seedu.address.model.person.MemberId;
import seedu.address.model.person.MembershipExpiryDate;
import seedu.address.model.person.MembershipJoinDate;
import seedu.address.model.person.MembershipType;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final Integer DEFAULT_MEMBER_ID = 1;
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_GENDER = "F";
    public static final String DEFAULT_DATEOFBIRTH = "02-02-2000";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_EMERGENCY_CONTACT = "91236811";
    public static final String DEFAULT_TYPE = "annual";
    public static final String DEFAULT_JOIN_DATE = "11-03-2026";
    public static final String DEFAULT_EXPIRY_DATE = "11-03-2027";

    private MemberId id;
    private Name name;
    private Phone phone;
    private Gender gender;
    private DateOfBirth dateOfBirth;
    private Email email;
    private EmergencyContact emergencyContact;
    private MembershipType type;
    private MembershipJoinDate joinDate;
    private MembershipExpiryDate expiryDate;
    private Remark remark;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        id = new MemberId(DEFAULT_MEMBER_ID);
        phone = new Phone(DEFAULT_PHONE);
        gender = new Gender(DEFAULT_GENDER);
        dateOfBirth = new DateOfBirth(DEFAULT_DATEOFBIRTH);
        email = new Email(DEFAULT_EMAIL);
        emergencyContact = new EmergencyContact(DEFAULT_EMERGENCY_CONTACT);
        type = new MembershipType(DEFAULT_TYPE);
        joinDate = new MembershipJoinDate(DEFAULT_JOIN_DATE);
        expiryDate = new MembershipExpiryDate(DEFAULT_EXPIRY_DATE);
        remark = new Remark("");
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        id = personToCopy.getId();
        phone = personToCopy.getPhone();
        gender = personToCopy.getGender();
        dateOfBirth = personToCopy.getDateOfBirth();
        email = personToCopy.getEmail();
        emergencyContact = personToCopy.getEmergencyContact();
        type = personToCopy.getMembershipType();
        joinDate = personToCopy.getJoinDate();
        expiryDate = personToCopy.getExpiryDate();
        remark = personToCopy.getRemark();
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code EmergencyContact} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmergencyContact(String emergencyContact) {
        this.emergencyContact = new EmergencyContact(emergencyContact);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Gender} of the {@code Person} that we are building.
     */
    public PersonBuilder withGender(String gender) {
        this.gender = new Gender(gender);
        return this;
    }
    /**
     * Sets the {@code Id} of the {@code Person} that we are building.
     */
    public PersonBuilder withId(MemberId id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the {@code DateOfBirth} of the {@code Person} that we are building.
     */
    public PersonBuilder withDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = new DateOfBirth(dateOfBirth);
        return this;
    }
    /**
     * Sets the {@code MembershipType} of the {@code Person} that we are building.
     */
    public PersonBuilder withType(String type) {
        this.type = new MembershipType(type);
        return this;
    }
    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code Remark} of the {@code Person} that we are building.
     */
    public PersonBuilder withRemark(String remark) {
        this.remark = new Remark(remark);
        return this;
    }

    /**
     * Builds and returns the {@code Person}.
     */
    public Person build() {
        return new Person(id, name, phone, gender, dateOfBirth, email, emergencyContact, type, joinDate, expiryDate,
                remark);
    }

}
