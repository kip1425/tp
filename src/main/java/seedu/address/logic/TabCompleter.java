package seedu.address.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.IntSupplier;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DetailsCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.logic.commands.RenewCommand;
import seedu.address.logic.commands.UndoCommand;

/**
 * Provides tab completion suggestions for the command box.
 *
 * <p>Completion is context-sensitive:
 * <ul>
 *   <li>No space yet → complete the command word</li>
 *   <li>Cursor immediately after a known prefix → complete the value</li>
 *   <li>Index commands (delete, details) → complete index only</li>
 *   <li>Index+prefix commands (edit, remark, renew) → complete index, then field prefixes</li>
 *   <li>add → no completion until at least one prefix+value has been entered</li>
 *   <li>filter → complete prefix tokens</li>
 * </ul>
 *
 * <p>Each returned string is the full replacement text for the input field.
 */
public class TabCompleter {

    private static final List<String> COMMAND_WORDS;
    private static final Map<String, List<String>> COMMAND_PREFIXES;
    private static final Map<String, List<String>> INDEX_THEN_PREFIX_COMMANDS;
    private static final Set<String> INDEX_ONLY_COMMANDS;
    private static final Map<String, List<String>> PREFIX_VALUES;

    static {
        List<String> cmdWords = new ArrayList<>(Arrays.asList(
                AddCommand.COMMAND_WORD,
                ClearCommand.COMMAND_WORD,
                DeleteCommand.COMMAND_WORD,
                DetailsCommand.COMMAND_WORD,
                EditCommand.COMMAND_WORD,
                ExitCommand.COMMAND_WORD,
                FilterCommand.COMMAND_WORD,
                FindCommand.COMMAND_WORD,
                HelpCommand.COMMAND_WORD,
                ListCommand.COMMAND_WORD,
                RemarkCommand.COMMAND_WORD,
                RenewCommand.COMMAND_WORD,
                UndoCommand.COMMAND_WORD
        ));
        Collections.sort(cmdWords);
        COMMAND_WORDS = Collections.unmodifiableList(cmdWords);

        // Commands where prefix completion begins immediately after the command word.
        // add: only completes after at least one prefix+value is already present (handled separately).
        Map<String, List<String>> prefixMap = new HashMap<>();
        prefixMap.put(AddCommand.COMMAND_WORD,
                Arrays.asList("n/", "p/", "g/", "d/", "e/", "m/", "ec/", "r/"));
        prefixMap.put(FilterCommand.COMMAND_WORD,
                Arrays.asList("s/", "g/", "m/", "age>/", "age</", "age=/",
                        "j>/", "j</", "j=/", "exp>/", "exp</", "exp=/"));
        COMMAND_PREFIXES = Collections.unmodifiableMap(prefixMap);

        // Commands where INDEX comes first, then optional field prefixes.
        Map<String, List<String>> indexThenPrefixMap = new HashMap<>();
        indexThenPrefixMap.put(EditCommand.COMMAND_WORD,
                Arrays.asList("n/", "p/", "g/", "d/", "e/", "ec/", "r/"));
        indexThenPrefixMap.put(RemarkCommand.COMMAND_WORD,
                Arrays.asList("r/"));
        indexThenPrefixMap.put(RenewCommand.COMMAND_WORD,
                Arrays.asList("m/"));
        INDEX_THEN_PREFIX_COMMANDS = Collections.unmodifiableMap(indexThenPrefixMap);

        // Commands where INDEX is the only argument — no fields after.
        Set<String> indexOnlySet = new HashSet<>();
        indexOnlySet.add(DeleteCommand.COMMAND_WORD);
        indexOnlySet.add(DetailsCommand.COMMAND_WORD);
        INDEX_ONLY_COMMANDS = Collections.unmodifiableSet(indexOnlySet);

        Map<String, List<String>> valueMap = new HashMap<>();
        valueMap.put("g/", Arrays.asList("m", "f"));
        valueMap.put("m/", Arrays.asList("monthly", "annual"));
        valueMap.put("s/", Arrays.asList("valid", "invalid"));
        PREFIX_VALUES = Collections.unmodifiableMap(valueMap);
    }

    private final IntSupplier listSizeSupplier;

    public TabCompleter(IntSupplier listSizeSupplier) {
        this.listSizeSupplier = listSizeSupplier;
    }

    /**
     * Returns a list of possible completions (full replacement texts) for the given input.
     * Returns an empty list if no completions are available.
     */
    public List<String> getCompletions(String input) {
        if (input == null) {
            return new ArrayList<>();
        }

        // 1. Value completion: cursor is immediately after a known prefix with no space
        for (Map.Entry<String, List<String>> entry : PREFIX_VALUES.entrySet()) {
            String prefix = entry.getKey();
            int prefixIndex = input.lastIndexOf(prefix);
            if (prefixIndex == -1) {
                continue;
            }
            String afterPrefix = input.substring(prefixIndex + prefix.length());
            if (afterPrefix.contains(" ")) {
                continue;
            }
            List<String> matches = new ArrayList<>();
            String base = input.substring(0, prefixIndex + prefix.length());
            for (String value : entry.getValue()) {
                if (value.toLowerCase().startsWith(afterPrefix.toLowerCase())) {
                    matches.add(base + value);
                }
            }
            return matches;
        }

        int spaceIndex = input.indexOf(' ');

        // 2. Command word completion: no space typed yet
        if (spaceIndex == -1) {
            List<String> matches = new ArrayList<>();
            for (String cmd : COMMAND_WORDS) {
                if (cmd.startsWith(input.toLowerCase())) {
                    matches.add(cmd);
                }
            }
            return matches;
        }

        String commandWord = input.substring(0, spaceIndex).toLowerCase();
        String args = input.substring(spaceIndex + 1);

        // 3. Index-only commands (delete, details): complete index, nothing after
        if (INDEX_ONLY_COMMANDS.contains(commandWord)) {
            if (!args.contains(" ")) {
                return getIndexCompletions(input.substring(0, spaceIndex + 1), args);
            }
            return new ArrayList<>();
        }

        // 4. Index-then-prefix commands (edit, remark, renew): index first, then field prefixes
        if (INDEX_THEN_PREFIX_COMMANDS.containsKey(commandWord)) {
            int indexSpaceIdx = args.indexOf(' ');
            if (indexSpaceIdx == -1) {
                // Still typing the index
                return getIndexCompletions(input.substring(0, spaceIndex + 1), args);
            }
            // Index done — complete field prefixes
            String afterIndex = args.substring(indexSpaceIdx + 1);
            String lastToken = getLastToken(afterIndex);
            String base = input.substring(0, input.length() - lastToken.length());
            List<String> prefixes = INDEX_THEN_PREFIX_COMMANDS.get(commandWord);
            List<String> matches = new ArrayList<>();
            for (String prefix : prefixes) {
                if (prefix.startsWith(lastToken) && !afterIndex.contains(prefix)) {
                    matches.add(base + prefix);
                }
            }
            return matches;
        }

        // 5. add: no completion while the user is still typing the first word (the name).
        //    Once they type a space after the name, suggest field prefixes.
        if (AddCommand.COMMAND_WORD.equals(commandWord) && !args.contains(" ")) {
            return new ArrayList<>();
        }

        // 6. Prefix completion (add with existing args, filter)
        List<String> prefixes = COMMAND_PREFIXES.get(commandWord);
        if (prefixes == null || prefixes.isEmpty()) {
            return new ArrayList<>();
        }

        String lastToken = getLastToken(args);
        String base = input.substring(0, input.length() - lastToken.length());
        List<String> matches = new ArrayList<>();
        for (String prefix : prefixes) {
            if (prefix.startsWith(lastToken) && !args.contains(prefix)) {
                matches.add(base + prefix);
            }
        }
        return matches;
    }

    private List<String> getIndexCompletions(String base, String partialIndex) {
        int listSize = listSizeSupplier.getAsInt();
        List<String> matches = new ArrayList<>();
        for (int i = 1; i <= listSize; i++) {
            String idx = String.valueOf(i);
            if (idx.startsWith(partialIndex)) {
                matches.add(base + idx);
            }
        }
        return matches;
    }

    private String getLastToken(String args) {
        int lastSpace = args.lastIndexOf(' ');
        return lastSpace == -1 ? args : args.substring(lastSpace + 1);
    }
}
