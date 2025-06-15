package com.manulife.java_jasper.service;

import net.sf.jasperreports.engine.JRException;

public interface ReportService {
	public byte[] generateReport() throws JRException;
}
