package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_GENDER = new Prefix("g/");
    public static final Prefix PREFIX_DATEOFBIRTH = new Prefix("d/");
    public static final Prefix PREFIX_MEMBERSTATUS = new Prefix("s/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_MEMBERSHIP_TYPE = new Prefix("m/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_JOIN_DATE = new Prefix("j/");
    public static final Prefix PREFIX_EMERGENCY_CONTACT = new Prefix("ec/");
    public static final Prefix PREFIX_AGE_GREATER = new Prefix("age>/");
    public static final Prefix PREFIX_AGE_LESS = new Prefix("age</");
    public static final Prefix PREFIX_AGE_EQUAL = new Prefix("age=/");
    public static final Prefix PREFIX_JOIN_DATE_AFTER = new Prefix("j>/");
    public static final Prefix PREFIX_JOIN_DATE_BEFORE = new Prefix("j</");
}
