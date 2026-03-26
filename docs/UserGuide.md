---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# FitDesk User Guide

FitDesk is a **desktop app for front-desk receptionists** at small-to-medium private fitness gyms managing member registrations and daily check-ins. Tailored for **fast, keyboard-centric workflows**, FitDesk empowers receptionists to work more efficiently, reducing wait times and increasing productivity.

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/se-edu/addressbook-level3/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar addressbook.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a contact named `John Doe` to the Address Book.

   * `delete 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

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

Shows a message explaining how to access the help page.

![help message](images/helpMessage.png)

Format: `help`


### Adding a member: `add`

Adds a member to the member list.

Format: `add n/NAME p/PHONE_NUMBER g/GENDER d/DATE_OF_BIRTH m/MEMBERSHIP_TYPE e/EMAIL ec/EMERGENCY_CONTACT`

<box type="tip" seamless>

**Tip:** Membership types only include "Monthly" or "Annual"
</box>

Examples:
* `add n/John Doe p/98765432 g/M d/19-01-2004 m/annual e/johnd@example.com a/98723347`

![add new member](images/add_member.png)

A new member `John Doe` is added to the member list

![added member](images/added.png)

* `add n/Betsy Crowe m/monthly a/93349011 e/betsycrowe@example.com g/F d/28-01-2002 p/1234567`

### Listing all persons : `list`

Shows a list of all members in the list.

Format: `list`

Example:
![list](images/list.png)

### Editing a person : `edit`

Edits an existing member in the list.

Format: `edit INDEX [n/NAME] [p/PHONE_NUMBER] [g/GENDER] [d/DATE_OF_BIRTH] [m/MEMBERSHIP_TYPE] [j/JOIN_DATE] [e/EMAIL] [ec/EMERGENCY_CONTACT]`

* Edits the member at the specified `INDEX`. The index refers to the index number shown in the displayed member list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st member to be `91234567` and `johndoe@example.com` respectively.
  ![result for 'edit 1 p/91234567 e/johndoe@example.com'](images/Edit_Example_1.png)
*  `edit 2 n/Betsy Crower m/annual` Edits the name and membership type of the 2nd member to be `Betsy Crower`and `annual` respectively.
  ![result for 'edit 2 n/Betsy Crower m/annual'](images/Edit_Example_2.png)

### Locating members by keyword: `find`

Finds members whose fields contain the search query as a substring.

Format: `find QUERY`

* The search is case-insensitive. e.g `hans` will match `Hans`
* Only text-based fields are searched: name, phone, email, gender, emergency contact, membership type, and membership status.
  Date-based fields (date of birth, join date, expiry date) are not searchable.
* The entire query is matched as a literal substring against each field.
  e.g. `find john doe` will only return members whose field contains `"john doe"`, not members with just `john` or just `doe`

Examples:
* `find John` returns members with `John` in any field
* `find john doe` returns members whose name (or other field) contains `"john doe"`<br>
  ![result for 'find john doe'](images/findResult.png)
* `find 9123` returns members whose phone number or other field contains `9123`
* `find annual` returns members with `annual` membership type

### Filtering members by fields: `filter`

Filters member list and displays members who have fields matching the given attribute.

Format: `filter [s/STATUS] [g/GENDER] [m/MEMBERSHIP_TYPE] [age>/AGE] [age</AGE] [age=/AGE] [j>/DATE] [j</DATE]`

Examples:
* `filter s/valid` returns list of members with valid memberships
  ![result for 'filter s/valid'](images/filterResult.png)

### Deleting a person : `delete`

Deletes the specified member from the list.

Format: `delete INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd person in the address book.
  1. `list`
  
     ![list](images/list_2.png)
  
  2. `delete 2`
  
  ![deleted member](images/delete_2.png)
  
* `find Alex` followed by `delete 1` deletes the 1st person in the results of the `find` command.

### Viewing the details of a person : `details`

Shows the details of the specified member from the list.

Format: `details INDEX`

* Shows the details of the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `details 1` shows the details of the 1st member in the address book.
  1. `list`
    
        ![list of members](images/details_list1.png)
  
  2. `details 1`
  
        ![details of 1st member](images/details_1.png)

* `find David` followed by `details 1` shows the details of the 1st member in the results of the `find` command.

  ![details of 1st member in find results](images/details_2.png)


### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

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
**Filter**   | `filter [s/STATUS] [g/GENDER] [m/MEMBERSHIP_TYPE] [age>/AGE] [age</AGE] [age=/AGE] [j>/DATE] [j</DATE]`<br> e.g., `filter s/valid g/M`
**Details**   | `details INDEX`<br> e.g., `details 1`
**List**   | `list`
**Help**   | `help`
