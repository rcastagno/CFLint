package com.cflint.tools;
/**
* Referenced inside plain.xsl at 158 and cflint-to-findbugs.xsl at 100
*/

public enum CFSeverity {
	COSMETIC("Cosmetic"),		// low priority
	INFO("Info"),				// low priority
	WARNING("Warning"),			// medium priority
	MAJOR("Major"),				// high priority
	SEVERE("Severe"),			// high priority
	CRITICAL("Critical"),		// high priority
	FATAL("Fatal");				// high priority

	private final String value;

	private CFSeverity(String value) {
	    this.value = value;
	}

	public String getName() {
	    return name();
	}

	public String getValue() {
	    return value;
	}

}