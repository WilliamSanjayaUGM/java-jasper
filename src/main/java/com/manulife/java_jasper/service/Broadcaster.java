package com.manulife.java_jasper.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import org.springframework.stereotype.Component;

import com.vaadin.flow.shared.Registration;

@Component
public class Broadcaster {
	private final List<Consumer<Void>> listeners=new CopyOnWriteArrayList<>();
	
	public Registration register(Consumer<Void> listener) {
		listeners.add(listener);
		return () -> listeners.remove(listener);
	}
	
	public void broadcast() {
		for (Consumer<Void> listener : listeners) {
            listener.accept(null);
        }
	}
}
