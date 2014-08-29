package com.cflint;

import org.apache.commons.cli.Options;

public class CFLintDefaults {
	
	
	// defaults
	public static boolean inFunction = false;
	public static boolean inAssignment = false;
	public static boolean inComponent = false;
	public static boolean verbose = false;
	public static boolean quiet = false;
	public static boolean showProgress = false;
	public static boolean progressUsesThread = true;
	public static boolean reportParseErrors = false;
	
	// CFLintMain defaults
	public static boolean xmlOutput = false;
	public static boolean htmlOutput = true;
	public static boolean textOutput = false;
	public static boolean showprogress= false;
	public static String xmlOutFile = "cflint-result.xml";
	public static String xmlstyle = "cflint";
	public static String htmlOutFile = "cflint-result.html";
	public static String htmlStyle = "plain.xsl";
	
}