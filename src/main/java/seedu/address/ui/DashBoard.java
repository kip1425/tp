package seedu.address.ui;

import java.time.LocalDate;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.logic.Logic;

/**
 * Dashboard UI for FitDesk
 * Gives a quick overview of the gym's statistics
 */
public class DashBoard extends UiPart<Region> {
    private static final String FXML = "Dashboard.fxml";

    private final Logic logic;

    @FXML
    private Label memberCount;
    @FXML
    private Label expiringMemberships;
    @FXML
    private Label annualMembers;
    @FXML
    private Label monthlyMembers;
    @FXML
    private Label newMembers;

    /**
     *
     */
    public DashBoard(Logic logic) {
        super(FXML);
        this.logic = logic;
        update();
    }
    private void update() {
        memberCount.textProperty().bind(
                Bindings.size(logic.getAddressBook().getPersonList()).asString()
        );
        annualMembers.textProperty().bind(
                Bindings.size(logic.getAddressBook().getPersonList().filtered(
                        p -> p.getMembershipType().value.equalsIgnoreCase("annual"))).asString()
        );
        monthlyMembers.textProperty().bind(
                Bindings.size(logic.getAddressBook().getPersonList().filtered(
                        p -> p.getMembershipType().value.equalsIgnoreCase("monthly"))).asString()
        );
        expiringMemberships.textProperty().bind(
                Bindings.size((logic.getAddressBook().getPersonList().filtered(
                        p -> p.getExpiryDate().getExpiryDate()
                                .isBefore(LocalDate.now().plusWeeks(1))
                )).filtered(p -> p.getExpiryDate().getExpiryDate().isAfter(LocalDate.now()))
                ).asString()
        );
        newMembers.textProperty().bind(
                Bindings.size(logic.getAddressBook().getPersonList().filtered(
                                p -> p.getJoinDate().getDate().isEqual(LocalDate.now())
                        )
                ).asString()
        );
    }
}
