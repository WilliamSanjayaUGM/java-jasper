package com.manulife.java_jasper.service.impl;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import org.springframework.stereotype.Component;

import com.manulife.java_jasper.service.BroadcasterService;
import com.vaadin.flow.shared.Registration;

@Component
public class BroadcasterServiceImpl implements BroadcasterService{
	private final List<Consumer<Void>> listeners=new CopyOnWriteArrayList<>();
	
	@Override
	public Registration register(Consumer<Void> listener) {
		listeners.add(listener);
		return () -> listeners.remove(listener);
	}
	
	@Override
	public void broadcast() {
		for (Consumer<Void> listener : listeners) {
            listener.accept(null);
        }
	}
}
