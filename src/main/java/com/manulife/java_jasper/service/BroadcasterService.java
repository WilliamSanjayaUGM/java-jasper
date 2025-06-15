package com.manulife.java_jasper.service;

import java.util.function.Consumer;

import com.vaadin.flow.shared.Registration;

public interface BroadcasterService {
	public Registration register(Consumer<Void> listener);
	
	public void broadcast();
}
