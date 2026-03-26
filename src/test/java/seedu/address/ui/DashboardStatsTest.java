package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class DashboardStatsTest {
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
}
