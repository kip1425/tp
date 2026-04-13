---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# FitDesk Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* This project is based on the [AddressBook Level 3 (AB3)](https://github.com/se-edu/addressbook-level3) project created by the [SE-EDU initiative](https://se-education.org/).
* Architecture design, component structure, and documentation templates are adapted from AB3.
* [JavaFX 17.0.7](https://openjfx.io/) — UI framework
* [Jackson Databind 2.7.0](https://github.com/FasterXML/jackson-databind) — JSON serialization/deserialization
* [Jackson Datatype JSR310 2.7.4](https://github.com/FasterXML/jackson-modules-java8) — Java 8 date/time support for Jackson
* [JUnit Jupiter 5.4.0](https://junit.org/junit5/) — unit and integration testing

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the app.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside components being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="800" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="700" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Undo/redo feature

#### Implementation

The undo/redo mechanism is facilitated by `CommandHistory` (in `logic/commands/CommandHistory.java`) and the `Command` base class. Each undoable command stores the data needed to reverse or reapply itself, and implements `undo(Model)` and `redo(Model)` methods.

`CommandHistory` maintains two stacks:
* **Undo stack** — bounded `Deque<Command>` of capacity 20, holding commands that can be undone.
* **Redo stack** — `Deque<Command>`, holding commands that were undone and can be redone.

The following operations are the key interactions:
* `CommandHistory#push(command)` — Adds a command to the undo stack and clears the redo stack (a new action invalidates redo history).
* `CommandHistory#pushToUndo(command)` — Adds a command back to the undo stack without clearing the redo stack (used by `RedoCommand`).
* `CommandHistory#pushRedo(command)` — Moves a command to the redo stack after it is undone.
* `CommandHistory#popRedo()` — Retrieves the most recently undone command for redoing.

Given below is an example usage scenario:

Step 1. The user executes `delete 5`. `LogicManager` calls `commandHistory.push(deleteCommand)`, adding it to the undo stack. Redo stack is cleared.

Step 2. The user executes `undo`. `UndoCommand` calls `deleteCommand.undo(model)` to restore the deleted person, then moves `deleteCommand` from the undo stack to the redo stack via `commandHistory.pushRedo(deleteCommand)`.

Step 3. The user executes `redo`. `RedoCommand` calls `commandHistory.popRedo()` to retrieve `deleteCommand`, then calls `deleteCommand.redo(model)` to re-delete the person, and pushes it back to the undo stack via `commandHistory.pushToUndo(deleteCommand)`.

Step 4. The user executes a new undoable command (e.g. `add`). `commandHistory.push(addCommand)` adds it to the undo stack and **clears the redo stack**, as it no longer makes sense to redo the previous delete.

<box type="info" seamless>

**Note:** If the undo stack is empty, `undo` returns an error. If the redo stack is empty, `redo` returns an error.

</box>

The following sequence diagram illustrates the interactions within the `Logic` component when `undo` is executed (assuming `delete 1` was the previous command):

<puml src="diagrams/UndoSequenceDiagram.puml" alt="Undo Sequence Diagram" />

The following sequence diagram illustrates the interactions within the `Logic` component when `redo` is executed (after the `undo` above):

<puml src="diagrams/RedoSequenceDiagram.puml" alt="Redo Sequence Diagram" />

Each undoable command stores the minimal state needed to reverse and reapply itself:

| Command | Data stored | `undo()` | `redo()` |
|---------|-------------|----------|----------|
| `add` | `addedPerson` | `model.deletePerson(addedPerson)` | `model.addPerson(addedPerson)` |
| `edit` | `originalPerson`, `editedPerson` | `model.setPerson(editedPerson, originalPerson)` | `model.setPerson(originalPerson, editedPerson)` |
| `delete` | `deletedPerson`, `previousAddressBook` | `model.setAddressBook(previousAddressBook)` | `model.deletePerson(deletedPerson)` |
| `clear` | `previousAddressBook` | `model.setAddressBook(previousAddressBook)` | `model.setAddressBook(new AddressBook())` |
| `renew` | `originalPerson`, `renewedPerson` | `model.setPerson(renewedPerson, originalPerson)` | `model.setPerson(originalPerson, renewedPerson)` |
| `remark` | `originalPerson`, `editedPerson` | `model.setPerson(editedPerson, originalPerson)` | `model.setPerson(originalPerson, editedPerson)` |

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1:** Saves the entire address book for every command.
  * Pros: Easy to implement uniformly.
  * Cons: High memory usage; saving the full state for every command is wasteful.

* **Alternative 2 (current choice):** Each command stores only the data it needs to undo/redo itself.
  * Pros: Memory-efficient; each command only saves what changed.
  * Cons: Each command must correctly implement `undo()` and `redo()`.

**Aspect: Granularity of undo history:**

* **Alternative 1 (current choice):** Every state-modifying command is individually undoable.
  * Pros: Fine-grained control; users can undo exactly one step at a time.
  * Cons: Requires many undo steps to reverse a sequence of bulk changes.

* **Alternative 2:** Group related commands into a single undoable transaction (e.g. a bulk-import).
  * Pros: More intuitive for batch operations.
  * Cons: More complex to implement; requires defining transaction boundaries.


### Tab completion feature

#### Implementation

Tab completion is implemented across three classes:

* `TabCompleter` (`logic/`) — pure logic class that computes completion candidates from the current input string
* `CommandBox` (`ui/`) — handles the `Tab` key press and cycles through candidates
* `MainWindow` (`ui/`) — provides the current filtered list size to `TabCompleter` via an `IntSupplier`

**`TabCompleter`** determines completions based on context, checked in this order:

1. **Value completion** — if the cursor is immediately after a known prefix (`g/`, `m/`, `s/`) with no space after it, suggests valid values (e.g. `filter s/v` → `filter s/valid`)
2. **Command word completion** — if no space has been typed yet, matches partial command words against all known commands
3. **Index-only commands** (`delete`, `details`) — suggests indices 1 to the current filtered list size; no further completion after the index
4. **Index-then-prefix commands** (`edit`, `remark`, `renew`) — suggests indices first, then field prefixes once the index and a space are present
5. **`add`** — suppresses completion until a space follows the first word (the name), then suggests unused field prefixes
6. **`filter`** — suggests field prefixes immediately after the command word

**`CommandBox`** uses `addEventFilter` on the `TextField` to intercept `Tab` before JavaFX's focus traversal system. On each `Tab` press:
* If no candidates exist, computes a fresh candidate list via `TabCompleter`, filtering out any candidate identical to the current text, and selects the first
* Otherwise, advances the index and applies the next candidate (cycling back to the first after the last)
* Any non-`Tab` key press resets the candidate list

<box type="info" seamless>

**Why `addEventFilter` instead of `setOnKeyPressed`?** JavaFX handles `Tab` focus traversal via an event *filter* registered on the scene, which fires before event handlers. Using `addEventFilter` on the `TextField` intercepts `Tab` earlier in the dispatch chain, preventing focus from leaving the field.

</box>

**`MainWindow`** passes `() -> logic.getFilteredPersonList().size()` as an `IntSupplier` to `CommandBox`, which forwards it to `TabCompleter`. This allows index completions to dynamically reflect the current filtered list size without coupling `TabCompleter` directly to the `Logic` or `Model` layers.

The following sequence diagram illustrates a `Tab` press when the user has typed `filter `:

<puml src="diagrams/TabCompletionSequenceDiagram.puml" alt="Tab completion sequence diagram" />


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* is a front-desk receptionist at a small-to-medium private fitness gym
* has a need to manage member registrations and daily check-ins
* prefer desktop apps over other types
* prefer fast keyboard-based workflows
* is reasonably comfortable using CLI apps

**Value proposition**: manage member details, track membership status, and store notes faster—making day-to-day operations smoother and reducing errors

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​ | I can/want to …​                                                   | So that I can…​                                      |
|----------|---------|--------------------------------------------------------------------|------------------------------------------------------|
| `* * *`  | user    | view all members                                                   | browse the database                                  |
| `* * *`  | user    | add a new member                                                   | register new members to the database                 |
| `* * *`  | user    | delete a member                                                    | remove invalid records                               |
| `* * *`  | user    | view full details of a member                                      | manage members' accounts effectively                 |
| `* * *`  | user    | find members by name                                               | locate details without going through the entire list |
| `* *`    | user    | edit a member's details                                            | keep member records updated                          |
| `* *`    | user    | track membership validity and status                               | get a quick overview of active and valid memberships |
| `* *`    | user    | filter members by specific fields (e.g. gender, age, status, etc.) | view members in specific groups                      |
| `* *`    | user    | access a help command                                              | review the list of commands                          |
| `* *`    | user    | view command history                                               | check past activity                                  |
| `* *`    | user    | auto-save data                                                     | avoid losing changes if the app closes               |
| `*`      | user    | renew memberships                                                  | extend their membership validity                     |
| `*`      | user    | view number of memberships expiring soon                           | remind members to renew memberships                  |
| `*`      | user    | track the number of recently added members                         | follow up on new member requests                     |
| `*`      | user    | add remarks to a member                                            | record special requests                              |
| `*`      | user    | clear the address book                                             | delete all members at once                           |

### Use cases

(For all use cases below, the **System** is the `FitDesk` and the **Actor** is the `Receptionist`, unless specified otherwise)


**Use case: UC01 - Add Member**

**MSS**

1. Receptionist requests to add a member
2. FitDesk prompts for required member details
3. Receptionist fills in the respective member details
4. FitDesk add the member

    Use case ends.

**Extensions**

* 3a. The given inputs are invalid.
    * 3a1. FitDesk shows an error message.

      Use case resumes at step 2.


**Use case: UC02 - Delete Member**

**MSS**

1.  Receptionist requests to list members
2.  FitDesk shows a list of members
3.  Receptionist requests to delete a specific member in the list
4.  FitDesk deletes the person

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.


* 3a. The given index is invalid.
    * 3a1. FitDesk shows an error message.

      Use case resumes at step 2.


**Use case: UC03 - Find Member**

**MSS**

1.  Receptionist requests to find members by keyword(s)
2.  FitDesk searches for members whose field contains the given keyword substring
3.  FitDesk shows a list of matching members

    Use case ends.

**Extensions**

* 1a. Receptionist provides no keywords.
    * 1a1. FitDesk shows an error message.

      Use case ends.


* 3a. No members match the given keyword(s).
    * 3a1. FitDesk shows an empty list.

      Use case ends.


**Use case: UC04 - Edit Member**

**MSS**

1. Receptionist requests to edit a specific member
2. FitDesk displays the current stored information of the selected member
3. Receptionist modifies one or more fields of the member’s information
4. Receptionist saves the changes
5. FitDesk updates the member’s information and confirms the update

    Use case ends.

**Extensions**

* 3a. Invalid input provided
    * 3a1. FitDesk detects invalid input. 
    * 3a2. FitDesk displays an error message and requests correct input. 
    * 3a3. Receptionist corrects the input.

      Use case resumes at step 4.


* 3b. Receptionist cancels the edit operation
    * 3b1. Receptionist cancels the edit request.
    * 3b2. FitDesk discards the changes and returns to the main member list.

      Use case ends.


**Use case: UC05 - Filter Member List by Status**

**MSS**

1.  Receptionist requests to filter members by status (Valid/Invalid/Pending)
2.  FitDesk filters member list by specified status
3.  FitDesk displays filtered member list

    Use case ends.

**Use case: UC06 - View Details of a Specific Member**

**MSS**

1. Receptionist requests to list members
2. FitDesk shows a list of members
3. Receptionist requests to view the details of a specific member
4. FitDesk displays the full details of the selected member

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.
    * 3a1. FitDesk shows an error message.

      Use case resumes at step 2.


**Use case: UC07 - Add a Remark to a Member**

**MSS**

1. Receptionist requests to list members
2. FitDesk shows a list of members
3. Receptionist requests to add or edit a remark for a specific member
4. FitDesk updates the remark for the member and confirms the update

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.
    * 3a1. FitDesk shows an error message.

      Use case resumes at step 2.

* 3b. Receptionist provides an empty remark (i.e. `r/` with nothing after it).
    * 3b1. FitDesk removes the existing remark from the member.

      Use case ends.


**Use case: UC08 - Undo a Command**

**MSS**

1. Receptionist executes a command that modifies data (e.g. `add`, `edit`, `delete`, `clear`)
2. Receptionist requests to undo the last command
3. FitDesk reverts the most recent change and confirms the undo

    Use case ends.

**Extensions**

* 2a. There is no undoable command in history.
    * 2a1. FitDesk shows an error message indicating nothing to undo.

      Use case ends.


**Use case: UC09 - Redo a Command**

**MSS**

1. Receptionist has previously undone a command
2. Receptionist requests to redo the last undone command
3. FitDesk reapplies the undone change and confirms the redo

    Use case ends.

**Extensions**

* 2a. There is no undone command to redo (either nothing was undone, or a new command was executed after the undo).
    * 2a1. FitDesk shows an error message indicating nothing to redo.

      Use case ends.


**Use case: UC10 - Access Command History**

**MSS**

1. Receptionist has previously entered one or more commands
2. Receptionist presses the `Up` arrow key in the command box
3. FitDesk displays the most recently entered command in the command box
4. Receptionist presses `Up` or `Down` to navigate through command history
5. Receptionist presses `Enter` to re-execute the selected command

    Use case ends.

**Extensions**

* 2a. There is no command history.
    * 2a1. FitDesk does not change the command box.

      Use case ends.

* 4a. Receptionist presses `Down` past the most recent command.
    * 4a1. FitDesk clears the command box.

      Use case ends.


**Use case: UC11 - Renew a Member's Membership**

**MSS**

1.  Receptionist requests to list members
2.  FitDesk shows a list of members
3.  Receptionist requests to renew a specific member in the list
4.  Receptionist can choose to modify the membership type field or not
5.  FitDesk renews the membership of the member

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.
    * 3a1. FitDesk shows an error message.

      Use case resumes at step 2.

* 4a. Invalid membership type provided
    * 4a1. FitDesk detects invalid input. 
    * 4a2. FitDesk displays an error message and requests correct input.
    * 4a3. Receptionist corrects the input.

      Use case resumes at step 5.


### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2. Should be able to hold up to 1000 members without a noticeable sluggishness in performance for typical usage.
3. A user with above average typing speed for regular English text should be able to accomplish most tasks faster using commands than using the mouse.
4. Should be usable by a receptionist with no prior _CLI_ experience after reading the user guide.
5. Should work fully offline without requiring an internet connection.
6. Private contact details such as health information and emergency contacts should not be transmitted to any external server.
7. The system is not required to support more than one user at a time.
8. The product is not required to support multi-branch gym operations.


### Glossary

* **Mainstream OS**: Windows, Linux, Unix, macOS
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **Member**: A registered gym patron whose details are stored and managed in FitDesk
* **Membership status**: The current standing of a member's membership, which can be one of the following — valid, invalid, or pending
* **Emergency contact**: A person designated by the member to be contacted in the event of a medical or safety emergency

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. Shutting down via the `exit` command

   1. Type `exit` in the command box and press Enter.

      Expected: The application closes. Window size and position are saved and will be restored on next launch.

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. Deleting a person after filtering the list

   1. Prerequisites: Use the `filter` command (e.g. `filter s/valid`) to show a subset of members. At least one member in the filtered list.

   1. Test case: `delete 1`<br>
      Expected: First member in the *filtered* list is deleted. The filtered view is refreshed. Details of the deleted member shown in the status message.

   1. Test case: `list` followed by `delete 1`<br>
      Expected: Deletes the first member in the full unfiltered list.

### Saving data

1. Dealing with a missing data file

   1. Delete the `data/fitdesk.json` file (or move it elsewhere).

   1. Launch the application.<br>
      Expected: FitDesk starts with a fresh set of sample members. A new `data/fitdesk.json` file is created automatically on the next data-modifying command.

1. Dealing with a corrupted data file

   1. Open `data/fitdesk.json` in a text editor and introduce invalid JSON (e.g. delete a closing brace `}` or set a field to an invalid value such as `"gender": "X"`).

   1. Launch the application.<br>
      Expected: FitDesk starts with an empty member list (no sample data). A warning may be logged. The corrupted file is not overwritten until a data-modifying command is executed.

1. Dealing with missing/incorrect preference file

   1. Delete or corrupt the `preferences.json` file.

   1. Launch the application.<br>
      Expected: FitDesk starts with default window size and position. A new `preferences.json` is created with default values.

