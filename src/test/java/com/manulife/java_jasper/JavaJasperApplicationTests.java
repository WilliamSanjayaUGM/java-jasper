package com.manulife.java_jasper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.manulife.java_jasper.exception.UserNotFoundException;
import com.manulife.java_jasper.model.User;
import com.manulife.java_jasper.repository.UserRepository;
import com.manulife.java_jasper.service.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class JavaJasperApplicationTests {
	@Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private Validator validator;
    
	@BeforeEach
	void setup() {
		userRepository.deleteAll();
	}
	
	 private User buildValidUser() {
		 User user = new User();
	     user.setEmail("william.sanjaya@mail.ugm.ac.id");
	     user.setPhoneNo("+628123456789");
	     user.setName("William Sanjaya");
	     user.setAddress("Jakarta");
	     user.setMale(true);
	     user.setDateOfBirth(LocalDate.of(1996, 7, 21));
	     return user;
	}
	
	@Test
	void testSaveValidUser() {
		User user = buildValidUser();
        User saved = userService.save(user);
        assertNotNull(saved.getId());
        assertEquals(user.getEmail(), saved.getEmail());
	}
	
	@Test
	void testInvalidEmailValidation() {
		User user = buildValidUser();
        user.setEmail("invalid-email");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
	}
	
	@Test
    void testInvalidPhoneNumberValidation() {
        User user = buildValidUser();
        user.setPhoneNo("0812345678"); // does not start with +62

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("phoneNo")));
    }
	
	@Test
    void testBlankNameValidation() {
        User user = buildValidUser();
        user.setName(" ");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }
	
	@Test
    void testFutureDateOfBirthValidation() {
        User user = buildValidUser();
        user.setDateOfBirth(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dateOfBirth")));
    }
	
	@Test
    void testDeleteUser() {
        User user = buildValidUser();
        User saved = userService.save(user);
        long id = saved.getId();

        long deletedId = userService.deleteById(id);
        assertEquals(id, deletedId);
        assertFalse(userRepository.findById(id).isPresent());
    }
	
	@Test
    void testGetUserById() {
        User user = buildValidUser();
        User saved = userService.save(user);

        User found = userService.getUserById(saved.getId());
        assertNotNull(found);
        assertEquals(saved.getEmail(), found.getEmail());
    }
	
	@Test
    void testFindAllUsers() {
		User user1 = buildValidUser();
        User user2 = buildValidUser();
        user2.setEmail("test@example.com");
        user2.setPhoneNo("+628123456790");

        userService.save(user1);
        userService.save(user2);

        List<User> allUsers = userService.findAll();
        assertEquals(2, allUsers.size());
    }
	
	@Test
    void testUserNotFoundException() {
        long nonExistentId = 99999L;
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(nonExistentId);
        });
    }
}
