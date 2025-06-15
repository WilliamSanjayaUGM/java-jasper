package com.manulife.java_jasper;

import java.lang.reflect.Field;

import com.vaadin.flow.component.HasValue;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TestUtil {
	public static void setFieldValue(Object target, String fieldName, Object value) {
        try {
        	Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            HasValue fieldComponent = (HasValue) field.get(target);
            fieldComponent.setValue(value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field value", e);
        }
    }
}
