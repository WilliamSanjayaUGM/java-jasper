package com.manulife.java_jasper.view;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.manulife.java_jasper.model.User;
import com.manulife.java_jasper.service.BroadcasterService;
import com.manulife.java_jasper.service.ReportService;
import com.manulife.java_jasper.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.security.PermitAll;

@CssImport("./styles/shared-styles.css")
@Route(value="", layout=MainLayout.class)
@PermitAll
public class UserListView extends VerticalLayout{
	private final UserService userService;
	private final ReportService reportService;
	
	private final Grid<User> grid =new Grid<>();
	private final TextField nameFilter=new TextField();
	private final DatePicker dobFilter = new DatePicker();
	
	private final BroadcasterService broadcaster;
	private Registration broadcasterRegistration;
	
	public UserListView(UserService userService,ReportService reportService, BroadcasterService broadcaster) {
		addClassName("user-list");
		this.userService=userService;
		this.reportService=reportService;
		this.broadcaster=broadcaster;
		
		//Add filter and action button
		configureFilter();
		configureGrid();
		
        add(configureToolbar(),grid);
	}
	
	@PostConstruct
	private void init() {
		broadcasterRegistration=broadcaster.register(event->{
			UI ui = UI.getCurrent();
	        if (ui != null) {
	            ui.access(() -> grid.getDataProvider().refreshAll());
	        }
		});
	}
	
	@PreDestroy
	private void destroy() {
	    if (broadcasterRegistration != null) {
	        broadcasterRegistration.remove();
	    }
	}
	
	private void configureFilter() {
		nameFilter.setPlaceholder("Search Name");
		nameFilter.setClearButtonVisible(true);
		nameFilter.setWidth("300px");
		
		dobFilter.setPlaceholder("Filter by Date");
		dobFilter.setWidth("200px");
		nameFilter.addValueChangeListener(e -> grid.getDataProvider().refreshAll());
        dobFilter.addValueChangeListener(e -> grid.getDataProvider().refreshAll());
	}
	
	private HorizontalLayout configureToolbar() {
        Button addButton = new Button("Add User", e -> UI.getCurrent().navigate("user-form"));
        Button searchButton = new Button("Search", e -> grid.getDataProvider().refreshAll());
        Button reportButton = new Button("Download Report", e -> {
            try {
                byte[] pdf = reportService.generateReport();
                StreamResource resource = new StreamResource("user_info.pdf", () -> new ByteArrayInputStream(pdf));
                Anchor download = new Anchor(resource, "Download PDF");
                download.getElement().setAttribute("download", true);
                add(download);
            } catch (Exception ex) {
                Notification.show("Error generating report: " + ex.getMessage());
            }
        });

        HorizontalLayout toolbar = new HorizontalLayout(nameFilter, dobFilter,searchButton, addButton, reportButton);
        toolbar.setAlignItems(Alignment.BASELINE);
        return toolbar;
    }
	
	private void configureGrid() {
		grid.setPageSize(10);
		grid.addColumn(User::getName).setHeader("Name");
        grid.addColumn(User::getEmail).setHeader("Email");
        grid.addColumn(User::getPhoneNo).setHeader("Phone");
        grid.addColumn(user -> user.isMale() ? "Male" : "Female").setHeader("Gender");
        grid.addColumn(user->user.getDateOfBirth()!=null ? 
        		user.getDateOfBirth().toString():"").setHeader("Date of Birth");
        grid.addColumn(User::getAddress).setHeader("Address");
        
        grid.addComponentColumn(user->{
        	Button edit = new Button("Edit", e -> UI.getCurrent().navigate("user-form/" + user.getId()));
        	Button delete = new Button("Delete",e->{
        		userService.deleteById(user.getId());
        		grid.getDataProvider().refreshAll();
        		Notification.show("User deleted");
        	});
        	return new HorizontalLayout(edit,delete);
        }).setHeader("Actions");
        
        String name = nameFilter.getValue();
        LocalDate dob = dobFilter.getValue();
        
        grid.setDataProvider(DataProvider.fromCallbacks(
				//fetch callback
				query->{
					int page = query.getOffset() / query.getLimit();
                    Pageable pageable = PageRequest.of(page, query.getLimit());
					return userService.findPaginatedUser(name, dob, pageable).stream();
				}, query -> (int) userService.count(name,dob)));
	}

	//--------------Start Getter & Setter ------------------------------
	public Grid<User> getGrid() {
		return grid;
	}

	public TextField getNameFilter() {
		return nameFilter;
	}

	public DatePicker getDobFilter() {
		return dobFilter;
	}
	
	
}
