package org.jsoftware.config;

public class ToStringBuilder {
	private final StringBuilder sb = new StringBuilder();
	
	public ToStringBuilder add(String name, Object value) {
		sb.append(name).append('=').append(value == null ? "-" : value).append('\n');
		return this;
	}
	
	@Override
	public String toString() {
		return sb.toString();
	}
	
}
