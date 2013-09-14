package org.jsoftware.config.dialect;

public class DialectFinder {

	public static Dialect find(String value) {
		String oldValue = value;
		value = value.trim().toLowerCase();
		if (value.length() == 0 || value.equalsIgnoreCase("default")) {
			return new DefaultDialect();
		}
		value = "org.jsoftware.config.dialect." + Character.toUpperCase(value.charAt(0)) + value.substring(1) + "Dialect";
		try {
			Class<?> cl = Class.forName(value);
			return (Dialect) cl.newInstance();
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Can not find class " + value + " for dialect " + oldValue);
		} catch (Exception e) {
			throw new RuntimeException("Can not instance dialect " + value);
		}
	}

}
