---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# FitDesk User Guide

## App Overview
FitDesk is a **desktop app for front-desk receptionists** at small-to-medium private fitness gyms managing member registrations and daily check-ins. Tailored for **fast, keyboard-centric workflows**, FitDesk empowers receptionists to work more efficiently, reducing wait times and increasing productivity.

### Supported Operating Systems
1. macOS
2. Windows
3. Linux
<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Quick Start

### Setup Instructions

1. **Ensure you have Java `17` or above** installed on your computer.<br>
2. **Platform-specific installation guides:**
   - **Mac:** [Installation guide](https://se-education.org/guides/tutorials/javaInstallationMac.html)
   - **Windows:** [Installation guide](https://se-education.org/guides/tutorials/javaInstallationWindows.html)
   - **Linux:** [Installation guide](https://se-education.org/guides/tutorials/javaInstallationLinux.html)

1. **Download the latest `.jar` file** from [here](https://github.com/AY2526S2-CS2103T-W08-3/tp/releases).

1. **Set up your home folder:**

   Copy the `.jar` file to the folder you want to use as FitDesk's _home folder_.

1. **Run the application:**
   1. Open a **command terminal**:
      - **Windows**: Press `Windows key + R`, type `cmd`, and press Enter
      - **Mac/Linux**: Open the **Terminal** app
   2. Navigate to the folder where the `.jar` file was saved
      ```bash
      cd path/to/your/folder
   3. Run the application using:
      ```bash
      java -jar fitdesk.jar
   4. The application should launch shortly after.

A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>

![Ui](images/Ui.png)

--------------------------------------------------------------------------------------------------------------------

### Using the Application

1. Type a command into the **command box**.
2. Press **Enter** to execute it. 

    For example:
    ```bash
       help
    ```
   This opens the help window.
   > 💡 **Tip:** 
   > 
    >- Use the **Up** and **Down** arrow keys to cycle through previously entered commands.
---
**Try these example commands:**

   * List all members: `list`

   * Add a member named `John Doe` to FitDesk:

   ```bash
   add n/John Doe p/98765432 g/M d/19-01-2004 m/annual e/johnd@example.com ec/98723347
   ```

   * Delete the 3rd member shown in the current list: `delete 3`

   * Delete all members: `clear` 

   * Exit the app: `exit`

**Learn More**

Refer to the [**Features**](#features) section below for full details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</box>


### Viewing help : `help`

Shows a message explaining how to access the help page through a pop-up window.

Format: `help`

Example:
* `help`
![help message](images/screenshots/help_1.png)
Help message is shown 


### Adding a member: `add`

Adds a member to the member list.

Format: `add n/NAME p/PHONE_NUMBER g/GENDER d/DATE_OF_BIRTH m/MEMBERSHIP_TYPE e/EMAIL ec/EMERGENCY_CONTACT`

<box type="tip" seamless>

**Tip:** Membership types only include "Monthly" or "Annual"
</box>

* Each **phone number** and **email** must be unique in the member list (no two members may share the same phone or the same email).
* **Names** do not need to be unique; different members are allowed to have the same name as long as their phone and email differ.
* If you try to add someone whose phone or email matches an existing member, the command is rejected and the error message indicates which field is duplicated.

Example:
* `add n/John Doe p/98765432 g/M d/19-01-2004 m/annual e/johnd@example.com ec/98723347`
![added member](images/screenshots/add_1.png)
A new member `John Doe` is added to the member list


### Listing all members : `list`

Shows a list of all members in the list.

Format: `list`

Example:
* `list`
![members listed](images/screenshots/list_1.png)
All members are listed


### Editing a person : `edit`

Edits an existing member in the list.

Format: `edit INDEX [n/NAME] [p/PHONE_NUMBER] [g/GENDER] [d/DATE_OF_BIRTH] [m/MEMBERSHIP_TYPE] [j/JOIN_DATE] [e/EMAIL] [ec/EMERGENCY_CONTACT]`

* Edits the member at the specified `INDEX`. The index refers to the index number shown in the displayed member list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* After editing, the member’s **phone** and **email** must still be unique among all other members (same rules as `add`). **Names** may match another member’s name.

Example:
*  `edit 7 p/91234567 e/johndoe@example.com`
![result for 'edit 1 p/91234567 e/johndoe@example.com'](images/screenshots/edit_1.png)
The phone number and email address of the 7th member are edited to be `91234567` and `johndoe@example.com` respectively


### Locating members by keyword: `find`

Finds members whose fields contain the search query as a substring.

Format: `find QUERY`

* The search is case-insensitive. e.g `hans` will match `Hans`
* Only some text-based fields are searched: name, phone, email, emergency contact, membership type.
* Can take any input as the query, including special characters and spaces.
* The entire query is matched as a literal substring against each field.
  e.g. `find john doe` will only return members whose field contains `"john doe"`, not members with just `john` or just `doe`

Examples:
* `find john doe`
![result for 'find john doe'](images/screenshots/find_1.png)
Members whose name (or other field) contains `john doe` are listed
* `find 9123` returns members whose phone number or another field contains `9123`


### Filtering members by fields: `filter`

Filters member list and displays members who have fields matching the given attribute.

Format: `filter [s/STATUS] [g/GENDER] [m/MEMBERSHIP_TYPE] [age>/AGE] [age</AGE] [age=/AGE] [j>/DATE] [j</DATE] [exp>/DATE] [exp</DATE] [exp=/DATE]`

Example:
* `filter s/valid`
![result for 'filter s/valid'](images/screenshots/filter_1.png)
Members with valid memberships are listed


### Deleting a member : `delete`

Deletes the specified member from the list.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …

Example:
* `list` followed by `delete 2` deletes the 2nd person in the address book.
  1. `list`
  ![list](images/screenshots/delete_1.png)
  2. `delete 2` 
  ![deleted member](images/screenshots/delete_2.png)


### Renewing a membership: `renew`

Renews specified member's membership.

Format: `renew INDEX [m/MEMBERSHIP_TYPE]`

* Renews the member's membership at the specified `INDEX`. The index refers to the index number shown in the displayed member list. The index **must be a positive integer** 1, 2, 3, …
* `MEMBERSHIP_TYPE` is an optional field.
* The new expiry extends from the **current** expiry date: **annual** adds one year, **monthly** adds one month (from that date, not from today).
* If the membership has **already expired** (expiry date before today), `renew` is rejected; register the person again with `add`.
* Membership type will be updated if included in the command.

Examples:
* `renew 2`
![renewed member](images/screenshots/renew_1.png)
The 2nd member's expiry date is changed to `11-04-2026`
* `renew 1 m/monthly`
![renewed member with different membership type](images/screenshots/renew_2.png)
The 1st member's membership type is changed to `Monthly`, so the expiry date becomes `11-04-2027`


### Adding a remark to a member : `remark`

Adds or edits a remark for the specified member.

Format: `remark INDEX r/[REMARK]`

* Edits the remark of the member at the specified `INDEX`. The index refers to the index number shown in the displayed member list. The index **must be a positive integer** 1, 2, 3, …​
* Existing remark will be overwritten by the input.
* Providing an empty remark (i.e. `r/` with nothing after it) removes the remark from the member.

Examples:
* `remark 1 r/Likes to swim.` adds the remark `Likes to swim.` to the 1st member.
  ![remark added](images/screenshots/remark.png)
* `remark 2 r/` removes the remark from the 2nd member.

### Viewing the details of a person : `details`

Shows the details of the specified member from the list.

Format: `details INDEX`

* Shows the details of the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `details 1` shows the details of the 1st member in the address book.
  1. `list`
    
        ![list of members](images/screenshots/details_list1.png)
  
  2. `details 1`
  
        ![details of 1st member](images/screenshots/details_1.png)

* `find David` followed by `details 1` shows the details of the 1st member in the results of the `find` command.

  ![details of 1st member in find results](images/screenshots/details_2.png)


### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

### Undoing the last command : `undo`

Undoes the most recent undoable command (add, edit, delete, clear).

Format: `undo`

Example:
* `undo`

  ![before undo](images/screenshots/undo_before.png)

  ![after undo](images/screenshots/undo_after.png)

### Navigating command history

Allows you to quickly re-use previously entered commands using the arrow keys in the command box.

* Press the `Up` arrow key to navigate to the previous command in history.
* Press the `Down` arrow key to navigate to the next command in history.
* The cursor will be placed at the end of the text after navigating.
* Pressing `Down` past the most recent command clears the command box.

![navigating to previous command](images/screenshots/command_history_down.png)

![navigating to next command](images/screenshots/command_history_up.png)

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

FitDesk data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

FitDesk data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>

**Caution:**
If your changes to the data file makes its format invalid, FitDesk will discard all data and start with an empty data file at the next run.  Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause FitDesk to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</box>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous AddressBook home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action     | Format, Examples
-----------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------
**Add**    | `add n/NAME p/PHONE_NUMBER g/GENDER d/DATE_OF_BIRTH m/MEMBERSHIP_TYPE e/EMAIL ec/EMERGENCY_CONTACT ​` <br> e.g., `add n/James Ho p/82224444 g/M d/14-05-2001 m/annual e/jamesho@example.com ec/99502281`
**Clear**  | `clear`
**Delete** | `delete INDEX`<br> e.g., `delete 3`
**Edit**   | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [g/GENDER] [d/DATE_OF_BIRTH] [m/MEMBERSHIP_TYPE] [j/JOIN_DATE] [e/EMAIL] [ec/EMERGENCY_CONTACT] ​`<br> e.g.,`edit 2 n/James Lee m/monthly e/jameslee@example.com`
**Find**   | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**Filter**   | `filter [s/STATUS] [g/GENDER] [m/MEMBERSHIP_TYPE] [age>/AGE] [age</AGE] [age=/AGE] [j>/DATE] [j</DATE] [exp>/DATE] [exp</DATE] [exp=/DATE]`<br> e.g., `filter s/valid g/M`
**Remark**  | `remark INDEX r/[REMARK]`<br> e.g., `remark 1 r/Likes to swim.`
**Renew**   | `renew INDEX [m/MEMBERSHIP_TYPE] ]`<br> e.g., `renew 2 m/monthly`
**Details**   | `details INDEX`<br> e.g., `details 1`
**List**   | `list`
**Undo**   | `undo`
**Help**   | `help`
