package com.manulife.java_jasper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manulife.java_jasper.dto.InquiryUserRequestDto;
import com.manulife.java_jasper.dto.UserRequestByIdDto;
import com.manulife.java_jasper.model.User;
import com.manulife.java_jasper.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRestTest {
	
	@Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        user = new User();
        user.setEmail("william.sanjaya@mail.ugm.ac.id");
	    user.setPhoneNo("+628123456789");
	    user.setName("William");
	    user.setAddress("Jakarta");
	    user.setMale(true);
	    user.setDateOfBirth(LocalDate.of(1996, 7, 21));
        user = userRepository.save(user);
    }

    @Test
    void testSaveUser() throws Exception {
        User newUser = new User();
        newUser.setName("Sanjay");
        newUser.setEmail("Sanjay@example.com");
        newUser.setPhoneNo("+628987654321");
        newUser.setMale(false);
        newUser.setAddress("Bandung");
        newUser.setDateOfBirth(LocalDate.of(1998, 7, 21));

        mockMvc.perform(post("/api/user/saveUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Sanjay"));
    }

    @Test
    void testGetUserById() throws Exception {
        UserRequestByIdDto dto = new UserRequestByIdDto(user.getId());

        mockMvc.perform(post("/api/user/getUserById")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("Sanjay@example.com"));
    }

    @Test
    void testDeleteUser() throws Exception {
        UserRequestByIdDto dto = new UserRequestByIdDto(user.getId());

        mockMvc.perform(post("/api/user/deleteUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(content().string(user.getId() + " Succesfully deleted"));
    }

    @Test
    void testGetUserInquiry() throws Exception {
        InquiryUserRequestDto dto = new InquiryUserRequestDto();
        dto.setPageIdx(0);
        dto.setSize(10);
        dto.setAsc(true);
        dto.setSortBy("name");
        dto.setName("William");

        mockMvc.perform(post("/api/user/getUserInquiry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].name").value("William"));
    }

    @Test
    void testDownloadUserReport() throws Exception {
        mockMvc.perform(get("/api/user/"))
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Disposition", "attachment; filename=UserReport.pdf"))
            .andExpect(content().contentType(MediaType.APPLICATION_PDF));
    }
}
