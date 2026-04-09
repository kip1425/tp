package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TabCompleterTest {

    private static final int TEST_LIST_SIZE = 3;
    private TabCompleter tabCompleter;

    @BeforeEach
    public void setUp() {
        tabCompleter = new TabCompleter(() -> TEST_LIST_SIZE);
    }

    // --- Command word completion ---

    @Test
    public void getCompletions_emptyInput_allCommands() {
        List<String> results = tabCompleter.getCompletions("");
        assertEquals(13, results.size());
        assertTrue(results.contains("add"));
        assertTrue(results.contains("filter"));
    }

    @Test
    public void getCompletions_partialCommandWord_matchingCommands() {
        List<String> results = tabCompleter.getCompletions("fi");
        assertEquals(2, results.size());
        assertTrue(results.contains("filter"));
        assertTrue(results.contains("find"));
    }

    @Test
    public void getCompletions_noMatchingCommand_empty() {
        List<String> results = tabCompleter.getCompletions("xyz");
        assertTrue(results.isEmpty());
    }

    // --- add: no completion until first prefix+value entered ---

    @Test
    public void getCompletions_addWithSpaceOnly_noCompletion() {
        assertTrue(tabCompleter.getCompletions("add ").isEmpty());
    }

    @Test
    public void getCompletions_addAfterName_suggestsAllPrefixes() {
        List<String> results = tabCompleter.getCompletions("add Alice ");
        assertEquals(8, results.size());
        assertTrue(results.contains("add Alice n/"));
        assertTrue(results.contains("add Alice p/"));
        assertTrue(results.contains("add Alice g/"));
    }

    @Test
    public void getCompletions_addAfterNameAndPrefix_suggestsRemainingPrefixes() {
        List<String> results = tabCompleter.getCompletions("add Alice n/Alice ");
        assertNoneMatch(results, "add Alice n/Alice n/");
        assertTrue(results.contains("add Alice n/Alice p/"));
    }

    // --- filter: plain prefix completion ---

    @Test
    public void getCompletions_filterWithSpace_suggestsAllPrefixes() {
        List<String> results = tabCompleter.getCompletions("filter ");
        assertEquals(12, results.size());
        assertTrue(results.contains("filter s/"));
        assertTrue(results.contains("filter g/"));
        assertTrue(results.contains("filter age>/"));
    }

    @Test
    public void getCompletions_filterUsedPrefixExcluded() {
        List<String> results = tabCompleter.getCompletions("filter g/m ");
        assertNoneMatch(results, "filter g/m g/");
        assertTrue(results.contains("filter g/m s/"));
    }

    // --- Index-only commands (delete, details) ---

    @Test
    public void getCompletions_deleteWithSpace_suggestsAllIndices() {
        List<String> results = tabCompleter.getCompletions("delete ");
        assertEquals(TEST_LIST_SIZE, results.size());
        assertTrue(results.contains("delete 1"));
        assertTrue(results.contains("delete 2"));
        assertTrue(results.contains("delete 3"));
    }

    @Test
    public void getCompletions_deletePartialIndex_filteredIndices() {
        TabCompleter large = new TabCompleter(() -> 12);
        List<String> results = large.getCompletions("delete 1");
        // "1".startsWith("1") → 1, "10".startsWith("1") → 10, "11" → 11, "12" → 12
        assertEquals(4, results.size());
        assertTrue(results.contains("delete 1"));
        assertTrue(results.contains("delete 10"));
    }

    @Test
    public void getCompletions_deleteAfterIndex_noCompletion() {
        assertTrue(tabCompleter.getCompletions("delete 1 ").isEmpty());
    }

    @Test
    public void getCompletions_detailsWithSpace_suggestsAllIndices() {
        List<String> results = tabCompleter.getCompletions("details ");
        assertEquals(TEST_LIST_SIZE, results.size());
        assertTrue(results.contains("details 1"));
    }

    // --- Index-then-prefix commands (edit, remark, renew) ---

    @Test
    public void getCompletions_editWithSpace_suggestsAllIndices() {
        List<String> results = tabCompleter.getCompletions("edit ");
        assertEquals(TEST_LIST_SIZE, results.size());
        assertTrue(results.contains("edit 1"));
        assertTrue(results.contains("edit 3"));
    }

    @Test
    public void getCompletions_editAfterIndex_suggestsFieldPrefixes() {
        List<String> results = tabCompleter.getCompletions("edit 1 ");
        assertEquals(7, results.size());
        assertTrue(results.contains("edit 1 n/"));
        assertTrue(results.contains("edit 1 g/"));
        assertNoneMatch(results, "edit 1 m/");
    }

    @Test
    public void getCompletions_editUsedPrefixExcluded() {
        List<String> results = tabCompleter.getCompletions("edit 2 n/Bob ");
        assertNoneMatch(results, "edit 2 n/Bob n/");
        assertTrue(results.contains("edit 2 n/Bob p/"));
    }

    @Test
    public void getCompletions_remarkAfterIndex_suggestsRemarkPrefix() {
        List<String> results = tabCompleter.getCompletions("remark 1 ");
        assertEquals(1, results.size());
        assertEquals("remark 1 r/", results.get(0));
    }

    @Test
    public void getCompletions_renewAfterIndex_suggestsMembershipPrefix() {
        List<String> results = tabCompleter.getCompletions("renew 1 ");
        assertEquals(1, results.size());
        assertEquals("renew 1 m/", results.get(0));
    }

    // --- Value completion ---

    @Test
    public void getCompletions_genderPrefixTyped_suggestsBothValues() {
        List<String> results = tabCompleter.getCompletions("add g/");
        assertEquals(2, results.size());
        assertTrue(results.contains("add g/m"));
        assertTrue(results.contains("add g/f"));
    }

    @Test
    public void getCompletions_partialValue_filteredMatch() {
        List<String> results = tabCompleter.getCompletions("filter s/v");
        assertEquals(1, results.size());
        assertEquals("filter s/valid", results.get(0));
    }

    @Test
    public void getCompletions_editValueCompletion_works() {
        List<String> results = tabCompleter.getCompletions("edit 1 g/");
        assertEquals(2, results.size());
        assertTrue(results.contains("edit 1 g/m"));
        assertTrue(results.contains("edit 1 g/f"));
    }

    @Test
    public void getCompletions_nullInput_empty() {
        assertTrue(tabCompleter.getCompletions(null).isEmpty());
    }

    // Helper
    private void assertNoneMatch(List<String> results, String unexpected) {
        assertTrue(results.stream().noneMatch(r -> r.equals(unexpected)),
                "Did not expect \"" + unexpected + "\" in completions but found it");
    }
}
