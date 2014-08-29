package cflint.com;

import org.apache.commons.cli.Options;

public class CFLintOptions {
	
	public static Options options = new Options();
	
	public CFLintOptions() {
		options.addOption("includeRule", true, "specify rules to include");
		options.addOption("excludeRule", true, "specify rules to exclude");
		options.addOption("folder", true, "folder(s) to scan");
		options.addOption("file", true, "file(s) to scan");
		options.addOption("filterFile", true, "filter file");
		options.addOption("v", false, "verbose");
		options.addOption("version", false, "show the version number");
		options.addOption("ui", false, "show UI");
		options.addOption("verbose", false, "verbose");
		options.addOption("showprogress", false, "show progress bar");
		options.addOption("singlethread", false, "show progress bar");
		
		options.addOption("q", false, "quiet");
		options.addOption("quiet", false, "quiet");
		options.addOption("h", false, "display this help");
		options.addOption("help", false, "display this help");
		options.addOption("xml", false, "output in xml format");
		options.addOption("xmlfile", true, "specify the output xml file (default: cflint-results.xml)");
		options.addOption("xmlstyle", true, "cflint,findbugs");
		options.addOption("html", false, "output in html format (default)");
		options.addOption("htmlfile", true, "specify the output html file (default: cflint-results.html)");
		options.addOption("htmlstyle", true, "default,plain");// fancy,fancy-hist,summary
		options.addOption("text", false, "output in plain text");
		options.addOption("textfile", true, "specify the output text file (default: cflint-results.txt)");
		options.addOption("extensions", true, "specify the extensions of the CF source files (default: .cfm,.cfc)");
	}
}