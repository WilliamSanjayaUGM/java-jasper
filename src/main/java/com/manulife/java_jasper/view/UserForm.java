package com.manulife.java_jasper.view;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.function.Consumer;

import com.manulife.java_jasper.model.User;
import com.manulife.java_jasper.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

@CssImport("./styles/shared-styles.css")
@Route(value = "user-form", layout = MainLayout.class)
public class UserForm extends FormLayout implements BeforeEnterObserver{
	private final Binder<User> binder=new Binder<>(User.class);
	
	private final EmailField email=new EmailField("Email");
	private final TextField name=new TextField("Name");
	private final TextField phoneNo=new TextField("Phone No.");
	private final TextArea address=new TextArea("address");
	private final DatePicker dob = new DatePicker("Date of Birth");
	private final RadioButtonGroup<Boolean> gender = new RadioButtonGroup<>();
	
	private final Button save = new Button("Save");
	private final Button cancel = new Button("Cancel");
	
	private final UserService userService;
    private final UI ui = UI.getCurrent();

    private User user = new User();
	
	public UserForm(UserService userService) {
		this.userService=userService;
		addClassName("user-form");
		
		initFormData();
		uiLayout();
		
		// Field binding with validation
		fieldBindingValidation();
		
		save.addClickListener(e->{
			User u = new User();
			if(binder.writeBeanIfValid(u)) {
				//Create backgroun thread
				new Thread(() ->{
					userService.save(u);
					ui.access(()->{
						Notification.show("User Saved");
						ui.navigate("");
					});
				}).start();
			}else {
				Notification.show("There are still incomplete field");
			}
		});
		
		cancel.addClickListener(e -> ui.navigate(""));
	}
	
	private void initFormData() {
		gender.setLabel("Gender");
		gender.setItems(true,false);
		gender.setItemLabelGenerator(value -> value ? "Male" : "Female");
	}
	
	private void uiLayout() {		
		// Create FormLayout for inputs
		FormLayout formLayout = new FormLayout();
		formLayout.setWidthFull();
		formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0px", 2));
		formLayout.add(name, email, phoneNo, dob, gender);
		formLayout.add(address);
		formLayout.setColspan(address, 2); //adress spans full width
		
		cancel.addClickListener(e ->clearForm());
		
		HorizontalLayout buttonLayout = new HorizontalLayout(save,cancel);
		buttonLayout.setWidthFull(); // Takes up full horizontal width
		buttonLayout.setJustifyContentMode(JustifyContentMode.START); // Aligns button to the left
		buttonLayout.setPadding(false);
		buttonLayout.setMargin(false);
		
		//Layouts to main view
		VerticalLayout layout = new VerticalLayout(formLayout, buttonLayout);
		layout.setWidthFull();
		layout.setAlignItems(Alignment.STRETCH);
		layout.setPadding(false);
		layout.setSpacing(true);
	    add(layout);
	}
	
	private void fieldBindingValidation() {
		binder.forField(email).asRequired("Email is required")
		.withValidator(new EmailValidator("Invalid email address"))
		.bind(User::getEmail, User::setEmail);
	
		binder.forField(name).asRequired("Name is required")
		.withValidator(n -> n.length() >= 2, "Name must be at least 2 characters")
			.bind(User::getName, User::setName);
		
		binder.forField(phoneNo).asRequired("Phone number is required")
	    	.withValidator(phone -> phone.startsWith("+62"), "Phone must start with +62")
	    	.bind(User::getPhoneNo, User::setPhoneNo);
		
		binder.forField(gender).bind(User::isMale, User::setMale);
		
		binder.forField(dob)
	      .withValidator(date -> date.isBefore(LocalDate.now()), "Date must be in the past")
	      .bind(
	              user -> user.getDateOfBirth() != null
	                  ? user.getDateOfBirth().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
	                  : null,
	              (user, localDate) -> user.setDateOfBirth(
	                  Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
	          );
	}
	
	private void clearForm() {
		name.clear();
		email.clear();
		phoneNo.clear();
		dob.clear();
		gender.clear();
		address.clear();
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		Optional<String> idOpt = event.getRouteParameters().get("id");
		if(idOpt.isPresent()) {
			try {
				long userId=Long.parseLong(idOpt.get());
				user=userService.getUserById(userId);
				binder.readBean(user);
			}catch (NumberFormatException e) {
                Notification.show("Invalid user ID");
            }
		}
	}
}
