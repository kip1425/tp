package seedu.address.storage;



import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.GenerateMemberIds;
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
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String id;
    private final String name;
    private final String phone;
    private final String gender;
    private final String dateOfBirth;
    private final String email;
    private final String emergencyContact;
    private final String type;
    private final String joinDate;
    private final String expiryDate;
    private final String remark;

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator

    public JsonAdaptedPerson(@JsonProperty("member id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("phone") String phone,
            @JsonProperty("gender") String gender,
            @JsonProperty("dateOfBirth") String dateOfBirth,
            @JsonProperty("email") String email,
            @JsonProperty("emergency contact") String emergencyContact,
            @JsonProperty("type") String type,
            @JsonProperty("join date") String joinDate,
            @JsonProperty("expiry date") String expiryDate,
            @JsonProperty("remark") String remark) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.emergencyContact = emergencyContact;
        this.type = type;
        this.joinDate = joinDate;
        this.expiryDate = expiryDate;
        this.remark = remark;
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        id = source.getId().toString();
        name = source.getName().fullName;
        phone = source.getPhone().value;
        gender = source.getGender().gender;
        dateOfBirth = source.getDateOfBirth().toString();
        email = source.getEmail().value;
        emergencyContact = source.getEmergencyContact().value;
        type = source.getMembershipType().toString();
        joinDate = source.getJoinDate().toString();
        expiryDate = source.getExpiryDate().toString();
        remark = source.getRemark().value;
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final MemberId modelId;
        if (id != null) {
            if (!isValidMemberId(id)) {
                throw new IllegalValueException("MemberId should be in the format M###.");
            }
            int idNumber = Integer.parseInt(id.substring(1));
            modelId = new MemberId(idNumber);
        } else {
            modelId = GenerateMemberIds.generateNextId();
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (gender == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Gender.class.getSimpleName()));
        }
        if (!Gender.isValidGender(gender)) {
            throw new IllegalValueException(Gender.MESSAGE_CONSTRAINTS);
        }
        final Gender modelGender = new Gender(gender);

        if (dateOfBirth == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    DateOfBirth.class.getSimpleName()));
        }
        if (!DateOfBirth.isValidDateOfBirth(dateOfBirth)) {
            throw new IllegalValueException(DateOfBirth.MESSAGE_CONSTRAINTS);
        }
        final DateOfBirth modelDateOfBirth = new DateOfBirth(dateOfBirth);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (emergencyContact == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, EmergencyContact.class.getSimpleName()));
        }
        if (!EmergencyContact.isValidEmergencyContact(emergencyContact)) {
            throw new IllegalValueException(EmergencyContact.MESSAGE_CONSTRAINTS);
        }
        final EmergencyContact modelEmergencyContact = new EmergencyContact(emergencyContact);

        if (type == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, MembershipType.class.getSimpleName()));
        }
        if (!MembershipType.isValidType(type)) {
            throw new IllegalValueException(MembershipType.MESSAGE_CONSTRAINTS);
        }
        final MembershipType modelType = new MembershipType(type);

        final MembershipJoinDate modelJoinDate;
        if (joinDate != null) {
            if (!MembershipJoinDate.isValidJoinDate(joinDate)) {
                throw new IllegalValueException(MembershipJoinDate.MESSAGE_CONSTRAINTS);
            }
            modelJoinDate = new MembershipJoinDate(joinDate);
        } else {
            modelJoinDate = new MembershipJoinDate();
        }

        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    MembershipExpiryDate.class.getSimpleName()));
        }

        final MembershipExpiryDate modelExpiryDate;
        MembershipExpiryDate derivedExpiry = new MembershipExpiryDate(modelJoinDate.getDate(), modelType);
        String trimmedExpiry = expiryDate.trim();
        if (!MembershipExpiryDate.isValidExpiryDate(trimmedExpiry)) {
            throw new IllegalValueException(MembershipExpiryDate.MESSAGE_CONSTRAINTS);
        }
        MembershipExpiryDate parsedStored = new MembershipExpiryDate(trimmedExpiry);
        LocalDate anchor = derivedExpiry.getExpiryDate();
        LocalDate storedDate = parsedStored.getExpiryDate();
        if (isRenewalExtensionOf(anchor, storedDate, modelType)) {
            modelExpiryDate = parsedStored;
        } else {
            modelExpiryDate = derivedExpiry;
        }
        if (remark == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Remark.class.getSimpleName()));
        }
        final Remark modelRemark = new Remark(remark);

        try {
            return new Person(modelId, modelName, modelPhone, modelGender, modelDateOfBirth,
                    modelEmail, modelEmergencyContact, modelType, modelJoinDate, modelExpiryDate, modelRemark);
        } catch (IllegalArgumentException e) {
            throw new IllegalValueException(e.getMessage(), e);
        }
    }

    /**
     * True if {@code stored} is {@code anchor} or a date reached by repeatedly extending
     * the membership by one period (matching the renew command), for the given type.
     */
    private static boolean isRenewalExtensionOf(LocalDate anchor, LocalDate stored, MembershipType type) {
        if (stored.isBefore(anchor)) {
            return false;
        }
        LocalDate cursor = anchor;
        for (int i = 0; i < 1000; i++) {
            if (cursor.equals(stored)) {
                return true;
            }
            if (cursor.isAfter(stored)) {
                return false;
            }
            cursor = stepRenewal(cursor, type);
        }
        return false;
    }

    private static LocalDate stepRenewal(LocalDate currentExpiry, MembershipType type) {
        if (type.toString().equalsIgnoreCase("annual")) {
            return currentExpiry.plusYears(1);
        }
        return currentExpiry.plusMonths(1);
    }

    private static boolean isValidMemberId(String test) {
        return test.matches("M\\d{3}");
    }

}
