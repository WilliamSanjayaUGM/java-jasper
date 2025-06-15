package com.manulife.java_jasper;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.manulife.java_jasper.exception.ReportGenerateException;
import com.manulife.java_jasper.model.User;
import com.manulife.java_jasper.repository.UserRepository;
import com.manulife.java_jasper.service.ReportService;
import com.manulife.java_jasper.service.UserService;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserReportTest {
	@Autowired
    private ReportService reportService;
	
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
    void testGenerateReportSuccess() throws Exception {
        userService.save(buildValidUser());

        byte[] pdf = reportService.generateReport();
        assertNotNull(pdf);
        assertTrue(pdf.length > 100); // should not be empty
    }
	
	@Test
	void testGenerateReportThrowsOnEmptyData() {
		ReportGenerateException ex = assertThrows(ReportGenerateException.class, () -> {
			reportService.generateReport();
	    });
	    assertTrue(ex.getMessage().contains("No user available"));
	}
	
	// Rename/move the file "user_report.jrxmlr" from resources to simulate missing template
	@Test
    void testGenerateReportThrowsWhenTemplateMissing() {
		userService.save(buildValidUser());

        InputStream templateStream = getClass().getResourceAsStream("/user_report.jrxml");
        assumeTrue(templateStream == null, "Test requires jrxml file to be missing");

        ReportGenerateException ex = assertThrows(ReportGenerateException.class, () -> {
            reportService.generateReport();
        });

        assertTrue(ex.getMessage().contains("template not found"));
    }
}
