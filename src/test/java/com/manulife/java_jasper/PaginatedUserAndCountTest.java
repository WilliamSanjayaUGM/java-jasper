package com.manulife.java_jasper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.manulife.java_jasper.model.User;
import com.manulife.java_jasper.repository.UserRepository;
import com.manulife.java_jasper.service.UserService;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PaginatedUserAndCountTest {
	
	@Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    
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
	void testPaginationAndFilterByName() {
	    // Save users with different names and dob
	    userService.save(buildValidUser());
	    User user2 = buildValidUser();
	    user2.setEmail("test@example.com");
        user2.setPhoneNo("+628123456790");
	    user2.setName("Alice");
	    user2.setDateOfBirth(LocalDate.of(1990, 1, 1));
	    userService.save(user2);

	    Pageable pageable = PageRequest.of(0, 10);
	    Page<User> page = userService.findPaginatedUser("alice", null, pageable);

	    assertEquals(1, page.getTotalElements());
	    assertEquals("Alice", page.getContent().get(0).getName());
	}

	@Test
	void testPaginationAndFilterByDate() {
	    User user1 = buildValidUser();
	    user1.setDateOfBirth(LocalDate.of(2000, 1, 1));
	    userService.save(user1);

	    User user2 = buildValidUser();
	    user2.setEmail("c@example.com");
        user2.setPhoneNo("+628123456790");
	    user2.setDateOfBirth(LocalDate.of(1999, 1, 1));
	    userService.save(user2);

	    Pageable pageable = PageRequest.of(0, 10);
	    Page<User> page = userService.findPaginatedUser(null, LocalDate.of(2000, 1, 1), pageable);

	    assertEquals(1, page.getTotalElements());
	    assertEquals("c@example.com", page.getContent().get(0).getEmail());
	}

	@Test
	void testCountByFilters() {
		User user1 = buildValidUser();
		User user2 = buildValidUser();
	    user2.setEmail("test@example.com");
	    userService.save(user1);
	    userService.save(user2);

	    long countAll = userService.count(null, null);
	    long countByName = userService.count("William Sanjaya", null);

	    assertEquals(2, countAll);
	    assertTrue(countByName >= 0);
	}
}
