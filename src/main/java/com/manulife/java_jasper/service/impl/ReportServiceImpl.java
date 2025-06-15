package com.manulife.java_jasper.service.impl;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.manulife.java_jasper.dto.UserReportDto;
import com.manulife.java_jasper.exception.ReportGenerateException;
import com.manulife.java_jasper.model.User;
import com.manulife.java_jasper.service.ReportService;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReportServiceImpl implements ReportService{
	private static final Logger LOGGER=LoggerFactory.getLogger(ReportServiceImpl.class);
	private final UserServiceImpl userService;
	
	public ReportServiceImpl(UserServiceImpl userService) {
		this.userService=userService;
	}
	
	@Override
	public byte[] generateReport() throws JRException{
		List<User> userLists=userService.findAll();
		
		if(userLists==null || userLists.isEmpty() || userLists.size()==0) {
			LOGGER.error("No user available to generate report.");
			throw new ReportGenerateException("No user available to generate report.");
		}
		
		List<UserReportDto> reportUsers = userLists.stream()
			    .map(UserReportDto::new)
			    .toList();
		
		try {
			//Load compile jasper report
			InputStream jrxmlStream = getClass().getResourceAsStream("/user_report.jrxml");
			if (jrxmlStream == null) {
			    throw new ReportGenerateException("JRXML template not found in resources.");
			}

			JasperReport report = JasperCompileManager.compileReport(jrxmlStream);
			JRBeanCollectionDataSource usersDataSource = new JRBeanCollectionDataSource(reportUsers);

			LocalDateTime now = LocalDateTime.now();
			String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("requestedDate", formattedDate);

			JasperPrint print = JasperFillManager.fillReport(report, parameters, usersDataSource);
			return JasperExportManager.exportReportToPdf(print);
		} catch (JRException e) {
		    LOGGER.error("JRException while generating report", e); // <-- Full stacktrace
		    throw new ReportGenerateException("Failed to generate Jasper Report.", e);
		} catch (Exception ex) {
		    LOGGER.error("Unexpected exception", ex); // <-- Just in case
		    throw new ReportGenerateException("Unexpected error occurred while generating report.", ex);
		}

	}
}
