package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;
import seedu.address.storage.AddressBookStorage;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;
import seedu.address.ui.Ui;
import seedu.address.ui.UiManager;

public class MainAppTest {

    private static final String VALID_PERSON_JSON = """
            {
              "persons" : [ {
                "name" : "Janice",
                "phone" : "82345678",
                "gender" : "M",
                "dateOfBirth" : "22-04-2021",
                "email" : "janice@gmail.com",
                "type" : "Monthly",
                "remark" : "",
                "id" : "M029",
                "emergencyContact" : "85671234",
                "joinDate" : "01-03-2026",
                "expiryDate" : "01-04-2026"
              } ]
            }
            """;
    private static final String MISSING_EXPIRY_DATE_JSON = """
            {
              "persons" : [ {
                "name" : "Janice",
                "phone" : "82345678",
                "gender" : "M",
                "dateOfBirth" : "22-04-2021",
                "email" : "janice@gmail.com",
                "type" : "Monthly",
                "remark" : "",
                "id" : "M029",
                "emergencyContact" : "85671234",
                "joinDate" : "01-03-2026"
              } ]
            }
            """;
    private static final String DUPLICATE_PERSONS_JSON = """
            {
              "persons" : [ {
                "name" : "Janice",
                "phone" : "82345678",
                "gender" : "M",
                "dateOfBirth" : "22-04-2021",
                "email" : "janice@gmail.com",
                "type" : "Monthly",
                "remark" : "",
                "id" : "M029",
                "emergencyContact" : "85671234",
                "joinDate" : "01-03-2026",
                "expiryDate" : "01-04-2026"
              }, {
                "name" : "Janice",
                "phone" : "82345678",
                "gender" : "M",
                "dateOfBirth" : "22-04-2021",
                "email" : "janice@gmail.com",
                "type" : "Monthly",
                "remark" : "",
                "id" : "M029",
                "emergencyContact" : "85671234",
                "joinDate" : "01-03-2026",
                "expiryDate" : "01-04-2026"
              } ]
            }
            """;
    private static final String DUPLICATE_FIELD_JSON = """
            {
              "persons" : [ {
                "name" : "Janice",
                "phone" : "82345678",
                "gender" : "M",
                "dateOfBirth" : "22-04-2021",
                "email" : "janice@gmail.com",
                "type" : "Monthly",
                "remark" : "",
                "id" : "M029",
                "emergencyContact" : "85671234",
                "joinDate" : "01-03-2026",
                "expiryDate" : "01-04-2026",
                "expiryDate" : "01-05-2026"
              } ]
            }
            """;
    private static final String MISSING_REMARK_JSON = """
            {
              "persons" : [ {
                "name" : "Janice",
                "phone" : "82345678",
                "gender" : "M",
                "dateOfBirth" : "22-04-2021",
                "email" : "janice@gmail.com",
                "type" : "Monthly",
                "id" : "M029",
                "emergencyContact" : "85671234",
                "joinDate" : "01-03-2026",
                "expiryDate" : "01-04-2026"
              } ]
            }
            """;

    @TempDir
    public Path testFolder;

    @Test
    public void initModelManager_invalidField_replacesCorruptedFileWithEmptyAddressBook() throws Exception {
        for (Map.Entry<String, String> invalidField : invalidFieldValues().entrySet()) {
            Path addressBookFile = testFolder.resolve(invalidField.getKey() + "-addressbook.json");
            Path userPrefsFile = testFolder.resolve(invalidField.getKey() + "-userPrefs.json");
            Files.writeString(addressBookFile, VALID_PERSON_JSON.replace(
                    fieldNameValue(invalidField.getKey()), invalidField.getValue()));

            StorageManager storage = new StorageManager(
                    new JsonAddressBookStorage(addressBookFile),
                    new JsonUserPrefsStorage(userPrefsFile));

            Model model = new MainApp().initModelManager(storage, new UserPrefs());

            assertTrue(model.getAddressBook().getPersonList().isEmpty(), invalidField.getKey());
            assertTrue(storage.readAddressBook().isPresent(), invalidField.getKey());
            assertEquals(0, storage.readAddressBook().get().getPersonList().size(), invalidField.getKey());
        }
    }

    @Test
    public void initModelManager_missingExpiryDate_replacesCorruptedFileWithEmptyAddressBook() throws Exception {
        Path addressBookFile = testFolder.resolve("missing-expiry-addressbook.json");
        Path userPrefsFile = testFolder.resolve("missing-expiry-userPrefs.json");
        Files.writeString(addressBookFile, MISSING_EXPIRY_DATE_JSON);

        StorageManager storage = new StorageManager(
                new JsonAddressBookStorage(addressBookFile),
                new JsonUserPrefsStorage(userPrefsFile));

        MainApp mainApp = new MainApp();
        Model model = mainApp.initModelManager(storage, new UserPrefs());

        assertTrue(model.getAddressBook().getPersonList().isEmpty());
        assertTrue(storage.readAddressBook().isPresent());
        assertEquals(0, storage.readAddressBook().get().getPersonList().size());
        assertTrue(mainApp.getAddressBookLoadFailureMessage().contains("invalid"));
    }

    @Test
    public void initModelManager_duplicatePersons_replacesCorruptedFileWithEmptyAddressBook() throws Exception {
        Path addressBookFile = testFolder.resolve("duplicate-persons-addressbook.json");
        Path userPrefsFile = testFolder.resolve("duplicate-persons-userPrefs.json");
        Files.writeString(addressBookFile, DUPLICATE_PERSONS_JSON);

        StorageManager storage = new StorageManager(
                new JsonAddressBookStorage(addressBookFile),
                new JsonUserPrefsStorage(userPrefsFile));

        MainApp mainApp = new MainApp();
        Model model = mainApp.initModelManager(storage, new UserPrefs());

        assertTrue(model.getAddressBook().getPersonList().isEmpty());
        assertTrue(storage.readAddressBook().isPresent());
        assertEquals(0, storage.readAddressBook().get().getPersonList().size());
        assertTrue(mainApp.getAddressBookLoadFailureMessage().contains("invalid"));
    }

    @Test
    public void initModelManager_duplicateField_replacesCorruptedFileWithEmptyAddressBook() throws Exception {
        Path addressBookFile = testFolder.resolve("duplicate-field-addressbook.json");
        Path userPrefsFile = testFolder.resolve("duplicate-field-userPrefs.json");
        Files.writeString(addressBookFile, DUPLICATE_FIELD_JSON);

        StorageManager storage = new StorageManager(
                new JsonAddressBookStorage(addressBookFile),
                new JsonUserPrefsStorage(userPrefsFile));

        MainApp mainApp = new MainApp();
        Model model = mainApp.initModelManager(storage, new UserPrefs());

        assertTrue(model.getAddressBook().getPersonList().isEmpty());
        assertTrue(storage.readAddressBook().isPresent());
        assertEquals(0, storage.readAddressBook().get().getPersonList().size());
        assertTrue(mainApp.getAddressBookLoadFailureMessage().contains("invalid"));
    }

    @Test
    public void initModelManager_missingRemark_replacesCorruptedFileWithEmptyAddressBook() throws Exception {
        Path addressBookFile = testFolder.resolve("missing-remark-addressbook.json");
        Path userPrefsFile = testFolder.resolve("missing-remark-userPrefs.json");
        Files.writeString(addressBookFile, MISSING_REMARK_JSON);

        StorageManager storage = new StorageManager(
                new JsonAddressBookStorage(addressBookFile),
                new JsonUserPrefsStorage(userPrefsFile));

        MainApp mainApp = new MainApp();
        Model model = mainApp.initModelManager(storage, new UserPrefs());

        assertTrue(model.getAddressBook().getPersonList().isEmpty());
        assertTrue(storage.readAddressBook().isPresent());
        assertEquals(0, storage.readAddressBook().get().getPersonList().size());
        assertTrue(mainApp.getAddressBookLoadFailureMessage().contains("invalid"));
    }

    @Test
    public void initModelManager_afterPreviousFailure_successfulReadClearsLoadFailureMessage() throws Exception {
        MainApp mainApp = new MainApp();
        Storage failingStorage = new StorageStub(
                Path.of("failing.json"),
                Optional.empty(),
                new DataLoadingException(new IOException("broken")),
                false);
        mainApp.initModelManager(failingStorage, new UserPrefs());

        Path addressBookFile = testFolder.resolve("valid-addressbook.json");
        Path userPrefsFile = testFolder.resolve("valid-userPrefs.json");
        Files.writeString(addressBookFile, VALID_PERSON_JSON);
        StorageManager validStorage = new StorageManager(
                new JsonAddressBookStorage(addressBookFile),
                new JsonUserPrefsStorage(userPrefsFile));

        Model model = mainApp.initModelManager(validStorage, new UserPrefs());

        assertEquals(1, model.getAddressBook().getPersonList().size());
        assertNull(mainApp.getAddressBookLoadFailureMessage());
    }

    @Test
    public void initModelManager_loadFailureAndSaveFailure_returnsEmptyAddressBookAndSetsWarningMessage() {
        MainApp mainApp = new MainApp();
        Storage failingStorage = new StorageStub(
                Path.of("failing.json"),
                Optional.empty(),
                new DataLoadingException(new IOException("broken")),
                true);

        Model model = mainApp.initModelManager(failingStorage, new UserPrefs());

        assertTrue(model.getAddressBook().getPersonList().isEmpty());
        assertEquals("The data file at failing.json is invalid. FitDesk started with an empty data file instead.",
                mainApp.getAddressBookLoadFailureMessage());
    }

    @Test
    public void createLogic_returnsLogicManager() {
        MainApp mainApp = new MainApp();
        Storage storage = new StorageStub(Path.of("logic.json"), Optional.empty(), null, false);
        Model model = mainApp.initModelManager(storage, new UserPrefs());

        Logic logic = mainApp.createLogic(model, storage);

        assertTrue(logic instanceof LogicManager);
    }

    @Test
    public void createUi_propagatesStartupWarningMessage() throws Exception {
        MainApp mainApp = new MainApp();
        Storage failingStorage = new StorageStub(
                Path.of("warning.json"),
                Optional.empty(),
                new DataLoadingException(new IOException("broken")),
                false);
        Model model = mainApp.initModelManager(failingStorage, new UserPrefs());
        Logic logic = mainApp.createLogic(model, failingStorage);

        Ui ui = mainApp.createUi(logic, mainApp.getAddressBookLoadFailureMessage());

        assertTrue(ui instanceof UiManager);
        Field field = UiManager.class.getDeclaredField("startupWarningMessage");
        field.setAccessible(true);
        assertEquals(mainApp.getAddressBookLoadFailureMessage(), field.get(ui));
    }

    @Test
    public void initModelLogicAndUi_setsModelLogicAndUiFromFactoryMethods() {
        TestMainApp mainApp = new TestMainApp();
        UserPrefs userPrefs = new UserPrefs();
        AddressBookStorage addressBookStorage = new JsonAddressBookStorage(Path.of("model-logic-ui.json"));
        UserPrefsStorageStub userPrefsStorage = new UserPrefsStorageStub(Path.of("prefs.json"));

        mainApp.initModelLogicAndUi(addressBookStorage, userPrefsStorage, userPrefs);

        assertEquals(mainApp.modelToReturn, mainApp.model);
        assertEquals(mainApp.logicToReturn, mainApp.logic);
        assertEquals(mainApp.uiToReturn, mainApp.ui);
        assertTrue(mainApp.storage instanceof StorageManager);
        assertEquals(mainApp.modelToReturn, mainApp.modelPassedToCreateLogic);
        assertEquals(mainApp.storage, mainApp.storagePassedToCreateLogic);
        assertEquals(mainApp.logicToReturn, mainApp.logicPassedToCreateUi);
        assertNull(mainApp.messagePassedToCreateUi);
    }

    @Test
    public void initModelLogicAndUi_passesLoadFailureMessageToCreateUi() {
        TestMainApp mainApp = new TestMainApp();
        mainApp.loadFailureMessageToSet = "startup warning";
        AddressBookStorage addressBookStorage = new JsonAddressBookStorage(Path.of("warning-ui.json"));
        UserPrefsStorageStub userPrefsStorage = new UserPrefsStorageStub(Path.of("prefs.json"));

        mainApp.initModelLogicAndUi(addressBookStorage, userPrefsStorage, new UserPrefs());

        assertEquals("startup warning", mainApp.messagePassedToCreateUi);
    }

    private static Map<String, String> invalidFieldValues() {
        return Map.ofEntries(
                Map.entry("name", "\"\""),
                Map.entry("phone", "\"123\""),
                Map.entry("gender", "\"X\""),
                Map.entry("dateOfBirth", "\"31-02-2021\""),
                Map.entry("email", "\"not-an-email\""),
                Map.entry("type", "\"Weekly\""),
                Map.entry("id", "\"BAD\""),
                Map.entry("emergencyContact", "\"123\""),
                Map.entry("joinDate", "\"not-a-date\""),
                Map.entry("expiryDate", "\"31-02-2026\""));
    }

    private static String fieldNameValue(String fieldName) {
        return switch (fieldName) {
        case "name" -> "\"name\" : \"Janice\"";
        case "phone" -> "\"phone\" : \"82345678\"";
        case "gender" -> "\"gender\" : \"M\"";
        case "dateOfBirth" -> "\"dateOfBirth\" : \"22-04-2021\"";
        case "email" -> "\"email\" : \"janice@gmail.com\"";
        case "type" -> "\"type\" : \"Monthly\"";
        case "id" -> "\"id\" : \"M029\"";
        case "emergencyContact" -> "\"emergencyContact\" : \"85671234\"";
        case "joinDate" -> "\"joinDate\" : \"01-03-2026\"";
        case "expiryDate" -> "\"expiryDate\" : \"01-04-2026\"";
        default -> throw new IllegalArgumentException("Unknown field: " + fieldName);
        };
    }

    private static class StorageStub implements Storage {
        private final Path addressBookFilePath;
        private final Optional<ReadOnlyAddressBook> addressBook;
        private final DataLoadingException readAddressBookException;
        private final boolean saveAddressBookThrows;

        StorageStub(Path addressBookFilePath, Optional<ReadOnlyAddressBook> addressBook,
                    DataLoadingException readAddressBookException, boolean saveAddressBookThrows) {
            this.addressBookFilePath = addressBookFilePath;
            this.addressBook = addressBook;
            this.readAddressBookException = readAddressBookException;
            this.saveAddressBookThrows = saveAddressBookThrows;
        }

        @Override
        public Optional<UserPrefs> readUserPrefs() {
            return Optional.empty();
        }

        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) {}

        @Override
        public Path getUserPrefsFilePath() {
            return Path.of("prefs.json");
        }

        @Override
        public Path getAddressBookFilePath() {
            return addressBookFilePath;
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException {
            if (readAddressBookException != null) {
                throw readAddressBookException;
            }
            return addressBook;
        }

        @Override
        public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath) throws DataLoadingException {
            return readAddressBook();
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
            if (saveAddressBookThrows) {
                throw new IOException("cannot save");
            }
        }

        @Override
        public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
            saveAddressBook(addressBook);
        }
    }

    private static class UserPrefsStorageStub implements seedu.address.storage.UserPrefsStorage {
        private final Path filePath;

        UserPrefsStorageStub(Path filePath) {
            this.filePath = filePath;
        }

        @Override
        public Path getUserPrefsFilePath() {
            return filePath;
        }

        @Override
        public Optional<UserPrefs> readUserPrefs() {
            return Optional.empty();
        }

        @Override
        public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) {}
    }

    private static class TestMainApp extends MainApp {
        private final Model modelToReturn = new ModelStub();
        private final Logic logicToReturn = new LogicStub();
        private final Ui uiToReturn = new UiStub();
        private Model modelPassedToCreateLogic;
        private Storage storagePassedToCreateLogic;
        private Logic logicPassedToCreateUi;
        private String messagePassedToCreateUi;
        private String loadFailureMessageToSet;

        @Override
        protected Model initModelManager(Storage storage, ReadOnlyUserPrefs userPrefs) {
            try {
                Field field = MainApp.class.getDeclaredField("addressBookLoadFailureMessage");
                field.setAccessible(true);
                field.set(this, loadFailureMessageToSet);
            } catch (ReflectiveOperationException e) {
                throw new AssertionError(e);
            }
            return modelToReturn;
        }

        @Override
        protected Logic createLogic(Model model, Storage storage) {
            modelPassedToCreateLogic = model;
            storagePassedToCreateLogic = storage;
            return logicToReturn;
        }

        @Override
        protected Ui createUi(Logic logic, String startupWarningMessage) {
            logicPassedToCreateUi = logic;
            messagePassedToCreateUi = startupWarningMessage;
            return uiToReturn;
        }
    }

    private static class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new UnsupportedOperationException();
        }

        @Override
        public UserPrefs getUserPrefs() {
            throw new UnsupportedOperationException();
        }

        @Override
        public seedu.address.commons.core.GuiSettings getGuiSettings() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setGuiSettings(seedu.address.commons.core.GuiSettings guiSettings) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook addressBook) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasPerson(seedu.address.model.person.Person person) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deletePerson(seedu.address.model.person.Person target) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addPerson(seedu.address.model.person.Person person) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setPerson(seedu.address.model.person.Person target,
                              seedu.address.model.person.Person editedPerson) {
            throw new UnsupportedOperationException();
        }

        @Override
        public javafx.collections.ObservableList<seedu.address.model.person.Person> getFilteredPersonList() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void updateFilteredPersonList(
                java.util.function.Predicate<seedu.address.model.person.Person> predicate) {
            throw new UnsupportedOperationException();
        }
    }

    private static class LogicStub implements Logic {
        @Override
        public seedu.address.logic.commands.CommandResult execute(String commandText) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new UnsupportedOperationException();
        }

        @Override
        public javafx.collections.ObservableList<seedu.address.model.person.Person> getFilteredPersonList() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new UnsupportedOperationException();
        }

        @Override
        public seedu.address.commons.core.GuiSettings getGuiSettings() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setGuiSettings(seedu.address.commons.core.GuiSettings guiSettings) {
            throw new UnsupportedOperationException();
        }
    }

    private static class UiStub implements Ui {
        @Override
        public void start(javafx.stage.Stage primaryStage) {
            throw new UnsupportedOperationException();
        }
    }
}
