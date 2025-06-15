package com.manulife.java_jasper.util;

import org.springframework.web.bind.annotation.RequestParam;

public class PagingRequest {
	private int pageIdx=0;
	private int size=10;
	private String sortBy="id";
	private boolean asc=true;
	public int getPageIdx() {
		return pageIdx;
	}
	public void setPageIdx(int pageIdx) {
		this.pageIdx = pageIdx;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	public boolean isAsc() {
		return asc;
	}
	public void setAsc(boolean asc) {
		this.asc = asc;
	}
}