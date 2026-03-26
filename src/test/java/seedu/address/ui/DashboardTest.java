package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class DashboardTest {
    private DashBoard dashBoard;

    @Test
    public void getTotal_returnsCorrectCount() {
        ObservableList<Person> list = FXCollections.observableArrayList();
        list.add(new PersonBuilder().build());
        assertEquals(1, DashboardStats.getTotal(list));
    }
    @Test
    public void getAnnualAndMonthly_returnsCorrectCount() {
        ObservableList<Person> list = FXCollections.observableArrayList();
        list.add(new PersonBuilder().withType("annual").build());
        list.add(new PersonBuilder().withType("annual").build());
        list.add(new PersonBuilder().withType("monthly").build());
        assertEquals(2, DashboardStats.getAnnual(list));
        assertEquals(1, DashboardStats.getMonthly(list));
    }
    @Test
    public void getNewMember_thisWeek_returnsCorrectCount() {
        ObservableList<Person> list = FXCollections.observableArrayList();
        LocalDate nextMon = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
        String dayThisWeek = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        list.add(new PersonBuilder().withJoinDate(dayThisWeek).build());
        assertEquals(1, DashboardStats.getNewMembers(list));
    }
    @Test
    public void getNewMember_beforeThisWeek_returnsZero() {
        ObservableList<Person> list = FXCollections.observableArrayList();
        list.add(new PersonBuilder().withJoinDate("11-03-2026").build());
        assertEquals(0, DashboardStats.getNewMembers(list));
    }
    @Test
    public void getNewMember_afterNextWeek_returnsZero() {
        ObservableList<Person> list = FXCollections.observableArrayList();
        String day = LocalDate.now().plusWeeks(2).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        list.add(new PersonBuilder().withJoinDate(day).build());
        assertEquals(0, DashboardStats.getNewMembers(list));
    }
    @Test
    public void getExpiry_beforeToday_returnsZero() {
        ObservableList<Person> list = FXCollections.observableArrayList();
        list.add(new PersonBuilder().withExpiryDate("11-03-2026").build());
        assertEquals(0, DashboardStats.getExpiring(list));
    }
    @Test
    public void getExpiringMembersInNextWeek_returnsCorrectCount() {
        ObservableList<Person> list = FXCollections.observableArrayList();
        String dayNextWeek = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        list.add(new PersonBuilder().withExpiryDate(dayNextWeek).build());
        assertEquals(1, DashboardStats.getExpiring(list));
    }
    @Test
    public void getExpiringMembersAfterNextWeek_returnsZero() {
        ObservableList<Person> list = FXCollections.observableArrayList();
        String day = LocalDate.now().plusWeeks(3).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        list.add(new PersonBuilder().withExpiryDate(day).build());
        assertEquals(0, DashboardStats.getExpiring(list));
    }
}
