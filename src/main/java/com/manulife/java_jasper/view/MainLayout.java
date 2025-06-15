package com.manulife.java_jasper.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class MainLayout extends AppLayout{
	public MainLayout() {
		createHeader();
	}
	
	private void createHeader() {
		H1 logo = new H1("User Management");
        logo.getStyle()
            .set("margin", "0")
            .set("font-size", "1.5em");
        
        HorizontalLayout header = new HorizontalLayout(logo);
        header.setWidthFull();
        header.setPadding(true);
        header.setSpacing(true);
        header.setAlignItems(Alignment.CENTER);
        header.expand(logo); // Push the button to the right

        addToNavbar(header);
	}
}
