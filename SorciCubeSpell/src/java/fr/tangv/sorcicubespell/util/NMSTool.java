package fr.tangv.sorcicubespell.util;

import java.lang.reflect.Field;

public class NMSTool {

    protected static void setField(Object packet, Field field, Object value) {
        field.setAccessible(true);
        try {
            field.set(packet, value);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        field.setAccessible(!field.isAccessible());
    }

    protected static Field getField(Class<?> classs, String fieldname) {
        try {
            return classs.getDeclaredField(fieldname);
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }
	
}
