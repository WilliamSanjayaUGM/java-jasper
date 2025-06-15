package com.manulife.java_jasper.service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.manulife.java_jasper.exception.ReportGenerateException;
import com.manulife.java_jasper.model.User;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReportService {
	private static final Logger LOGGER=LoggerFactory.getLogger(ReportService.class);
	private final UserService userService;
	
	public ReportService(UserService userService) {
		this.userService=userService;
	}
	
	public byte[] generateReport() throws JRException{
		List<User> userLists=userService.findAll();
		
		if(userLists==null || userLists.isEmpty() || userLists.size()==0) {
			LOGGER.error("No user available to generate report.");
			throw new ReportGenerateException("No user available to generate report.");
		}
		
		try {
			//Load compile jasper report
			InputStream inputStream=getClass().getResourceAsStream("/user_report.jasper");
			
			if (inputStream == null) {
				LOGGER.error("Jasper report template not found in resources.");
                throw new ReportGenerateException("Jasper report template not found in resources.");
            }
			JRBeanCollectionDataSource usersDataSource=new JRBeanCollectionDataSource(userLists);
			
			LocalDateTime now=LocalDateTime.now();
			DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String formattedDate=now.format(formatter);
			Map<String, Object> parameters = Map.of("requestedDate", formattedDate);
			
			JasperPrint print=JasperFillManager.fillReport(inputStream, parameters,usersDataSource);
			return JasperExportManager.exportReportToPdf(print);
		}catch(JRException e) {
			LOGGER.error(e.getMessage());
			throw new ReportGenerateException("Failed to generate Jasper Report.",e);
		}
	}
}
