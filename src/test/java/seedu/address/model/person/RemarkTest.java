package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RemarkTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Remark(null));
    }

    @Test
    public void toString_validRemark_returnsValue() {
        Remark remark = new Remark("Some remark");
        assertEquals("Some remark", remark.toString());
    }

    @Test
    public void equals() {
        Remark remark = new Remark("Some remark");

        // same object -> returns true
        assertTrue(remark.equals(remark));

        // same value -> returns true
        assertTrue(remark.equals(new Remark("Some remark")));

        // null -> returns false
        assertFalse(remark.equals(null));

        // different type -> returns false
        assertFalse(remark.equals("Some remark"));

        // different value -> returns false
        assertFalse(remark.equals(new Remark("Other remark")));
    }

    @Test
    public void hashCode_sameValue_sameHashCode() {
        Remark remark = new Remark("Some remark");
        assertEquals(remark.hashCode(), new Remark("Some remark").hashCode());
    }
}
