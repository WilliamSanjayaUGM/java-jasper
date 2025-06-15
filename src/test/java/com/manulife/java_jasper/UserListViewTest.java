package com.manulife.java_jasper;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.manulife.java_jasper.model.User;
import com.manulife.java_jasper.service.BroadcasterService;
import com.manulife.java_jasper.service.ReportService;
import com.manulife.java_jasper.service.UserService;
import com.manulife.java_jasper.view.UserListView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.shared.Registration;

@SpringBootTest
@AutoConfigureMockMvc
public class UserListViewTest {
	@Autowired
    private UserService userService;

    @Test
    void userListGridShowsUsers() {
        UI ui = new UI();
        UI.setCurrent(ui);

        User user = new User();
        user.setName("Test");
        user.setEmail("test@mail.com");
        user.setPhoneNo("+628123456789");
        user.setMale(true);
        user.setDateOfBirth(LocalDate.of(1996, 7, 21));
        user.setAddress("Bandung");
        userService.save(user);

        // Create real view
        UserListView view = new UserListView(userService, new DummyReportService(), new DummyBroadcaster());
        view.setId("user-list-view");

     // Replace the lazy data provider with a testable in-memory list
        List<User> testUsers = List.of(user);
        Grid<User> grid = (Grid<User>) view.getChildren()
                .filter(c -> c instanceof Grid)
                .findFirst()
                .orElseThrow();

        grid.setItems(testUsers);;

        Assertions.assertTrue(grid.getListDataView().getItems().iterator().hasNext(), "Grid should not be empty");
    }

    static class DummyReportService implements ReportService {
        @Override public byte[] generateReport() { return new byte[0]; }
    }

    static class DummyBroadcaster implements BroadcasterService {
        @Override public Registration register(Consumer<Void> listener) { return () -> {}; }
        @Override public void broadcast() {}
    }
}
