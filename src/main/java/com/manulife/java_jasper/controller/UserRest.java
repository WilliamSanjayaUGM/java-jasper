package com.manulife.java_jasper.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.manulife.java_jasper.dto.InquiryUserRequestDto;
import com.manulife.java_jasper.dto.UserRequestByIdDto;
import com.manulife.java_jasper.exception.ReportGenerateException;
import com.manulife.java_jasper.model.User;
import com.manulife.java_jasper.service.ReportService;
import com.manulife.java_jasper.service.UserService;

import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/api/user/")
public class UserRest {
	private final UserService userService;
	private final ReportService reportService;
	
	public UserRest(UserService userService,ReportService reportService) {
		this.userService=userService;
		this.reportService=reportService;
	}
	
	@RequestMapping(value="saveUser",method = RequestMethod.POST)
	public ResponseEntity<User> saveUser(@RequestBody User user){
		return ResponseEntity.ok(userService.save(user));
	}
	
	@RequestMapping(value="getUserInquiry",method = RequestMethod.POST)
	public ResponseEntity<Page<User>> getUserInquiry(@RequestBody InquiryUserRequestDto inquiryRequest){
		Sort sort = !inquiryRequest.isAsc() ? Sort.by(inquiryRequest.getSortBy()).descending() : Sort.by(inquiryRequest.getSortBy()).ascending();
		int page=inquiryRequest.getPageIdx()*inquiryRequest.getSize();
		int size=inquiryRequest.getSize();
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<User> users=userService.findPaginatedUser(inquiryRequest.getName(),inquiryRequest.getDateOfBirth(),pageable);
		return ResponseEntity.ok().body(users);
	}
	
	@RequestMapping(value="getUserById",method = RequestMethod.POST)
	public ResponseEntity<User> getUserById(@RequestBody UserRequestByIdDto request){
		long userId=request.getId();
		return ResponseEntity.ok().body(userService.getUserById(userId));
	}
	
	@RequestMapping(value="deleteUser",method = RequestMethod.POST)
	public ResponseEntity<String> deleteUser(@RequestBody UserRequestByIdDto request){
		long userId=request.getId();
		long responseId=userService.deleteById(userId);
		return ResponseEntity.ok(responseId +" Succesfully deleted");
	}
	
	@GetMapping
	public ResponseEntity<byte[]> downloadUserReport(){
		try {
            byte[] pdfBytes = reportService.generateReport();

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=UserReport.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (JRException | ReportGenerateException e) {
            return ResponseEntity.internalServerError()
                    .body(("Failed to generate report: " + e.getMessage()).getBytes());
        }
	}
}
