package com.manulife.java_jasper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.manulife.java_jasper.model.User;
import com.manulife.java_jasper.service.UserService;
import com.manulife.java_jasper.view.UserForm;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.data.binder.BinderValidationStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserFormTest {
	@Autowired
    private UserService userService;
	
	@Test
    void shouldShowValidationErrorOnInvalidEmail() {
        UI ui = new UI();
        UI.setCurrent(ui);

        UserForm form = new UserForm(userService);

        TestUtil.setFieldValue(form, "email", "invalid-email");
        TestUtil.setFieldValue(form, "name", "A");
        form.getSave().click();

        BinderValidationStatus<User> status = form.getBinder().validate();
        Assertions.assertTrue(status.hasErrors(), "Validation should fail on invalid email");
    }
}
