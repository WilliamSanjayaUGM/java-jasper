package com.manulife.java_jasper.config;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.shared.communication.PushMode;

@Push(PushMode.AUTOMATIC)
public class AppShellConfig implements AppShellConfigurator {
    // Optional: Add headers, meta tags if needed later
}
