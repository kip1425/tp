package seedu.address.model;

import seedu.address.model.person.MemberId;

/**
 * Generates a unique membership ID for a new gym member
 */
public class GenerateMemberIds {
    private static int currentMaxId = 0;
    private GenerateMemberIds() {}

    public static void initialize(int maxId) {
        currentMaxId = maxId;
    }

    /**
     * Ensures no gym members share the same ID
     * @return an id representing the next unique available membership
     */
    public static MemberId generateNextId() {
        currentMaxId++;
        return new MemberId(currentMaxId);
    }

    /**
     * Decrements current max ID by one.
     */
    public static void decrementMaxId() {
        if (currentMaxId >= 0) {
            currentMaxId--;
        }
    }

    public static int getCurrentMaxId() {
        return currentMaxId;
    }
}
