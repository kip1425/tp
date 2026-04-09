package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final MemberId id;
    private final Name name;
    private final Phone phone;
    private final Email email;
    private final Gender gender;
    private final DateOfBirth dateOfBirth;
    private final MemberStatus memberStatus;

    // Data fields
    private final EmergencyContact emergencyContact;
    private final MembershipType membershipType;
    private final MembershipJoinDate joinDate;
    private final MembershipExpiryDate expiryDate;
    private final Remark remark;

    /**
     * Every field must be present and not null.
     */
    public Person(MemberId id, Name name, Phone phone, Gender gender,
                  DateOfBirth dateOfBirth, Email email, EmergencyContact emergencyContact,
                  MembershipType type, MembershipJoinDate joinDate, MembershipExpiryDate expiryDate) {
        this(id, name, phone, gender, dateOfBirth, email, emergencyContact, type, joinDate, expiryDate,
                new Remark(""));
    }

    /**
     * Every field must be present and not null.
     */
    public Person(MemberId id, Name name, Phone phone, Gender gender,
                  DateOfBirth dateOfBirth, Email email, EmergencyContact emergencyContact,
                  MembershipType type, MembershipJoinDate joinDate, MembershipExpiryDate expiryDate, Remark remark) {
        requireAllNonNull(id, name, phone, email, emergencyContact, type, joinDate, remark);
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.emergencyContact = emergencyContact;
        this.membershipType = type;
        this.joinDate = joinDate;
        this.expiryDate = expiryDate;
        this.remark = remark;
        this.memberStatus = new MemberStatus(this.expiryDate.getExpiryDate());
    }

    public MemberId getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public EmergencyContact getEmergencyContact() {
        return emergencyContact;
    }
    public MembershipType getMembershipType() {
        return membershipType;
    }
    public MembershipJoinDate getJoinDate() {
        return joinDate;
    }
    public MembershipExpiryDate getExpiryDate() {
        return expiryDate;
    }

    public Gender getGender() {
        return this.gender;
    }

    public DateOfBirth getDateOfBirth() {
        return this.dateOfBirth;
    }

    public MemberStatus getMemberStatus() {
        return this.memberStatus;
    }

    public Remark getRemark() {
        return this.remark;
    }

    /**
     * Returns true if both persons have the same phone number or email.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && (otherPerson.getPhone().equals(getPhone())
                || otherPerson.getEmail().equals(getEmail()));
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && gender.equals(otherPerson.gender)
                && dateOfBirth.equals(otherPerson.dateOfBirth)
                && memberStatus.equals(otherPerson.memberStatus)
                && emergencyContact.equals(otherPerson.emergencyContact)
                && membershipType.equals(otherPerson.membershipType)
                && joinDate.equals(otherPerson.joinDate)
                && expiryDate.equals(otherPerson.expiryDate)
                && remark.equals(otherPerson.remark)
                && id.equals(otherPerson.id);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(id, name, phone, email, gender, dateOfBirth,
                memberStatus, emergencyContact, membershipType, joinDate, expiryDate, remark);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("member id", id.toString())
                .add("name", name)
                .add("phone", phone)
                .add("gender", gender)
                .add("date of birth", dateOfBirth)
                .add("type", membershipType)
                //.add("member status", memberStatus)
                .add("email", email)
                .add("emergency contact", emergencyContact)
                //.add("join date", joinDate.toString())
                //.add("expiry date", expiryDate.toString())
                .toString();
    }

}
