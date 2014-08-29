package com.cflint.main;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.xml.transform.TransformerException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import cflint.com.CFLintOptions;

import com.cflint.CFLint;
import com.cflint.CFLintDefaults;
import com.cflint.HTMLOutput;
import com.cflint.TextOutput;
import com.cflint.Version;
import com.cflint.XMLOutput;
import com.cflint.tools.CFLintFilter;

public class CFLintMain {

	List<String> includeRule = new ArrayList<String>();
	List<String> excludeRule = new ArrayList<String>();
	List<String> folder = new ArrayList<String>();
	String filterFile = null;
	String textOutFile = null;
	String[] includeCodes = null;
	String[] excludeCodes = null;
	private String extensions;

	public static void main(final String[] args) throws ParseException, IOException, TransformerException {

		final CommandLineParser parser = new GnuParser();
		final CommandLine cmd = parser.parse(CFLintOptions.options, args);
		if (cmd.hasOption('h') || cmd.hasOption("help")) {
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("cflint", CFLintOptions.options);
			return;
		}
		if(cmd.hasOption("version")){
			System.out.println("CFLint " + Version.getVersion());
			return;
		}
		final CFLintMain main = new CFLintMain();
		CFLintDefaults.verbose = (cmd.hasOption('v') || cmd.hasOption("verbose"));
		CFLintDefaults.quiet = (cmd.hasOption('q') || cmd.hasOption("quiet"));
		CFLintDefaults.xmlOutput = cmd.hasOption("xml") || cmd.hasOption("xmlstyle") || cmd.hasOption("xmlfile");
		CFLintDefaults.textOutput = cmd.hasOption("text") || cmd.hasOption("textfile");
		if (cmd.hasOption("ui")) {
			main.ui();
		}
		// If an output is specified, htmlOutput is not defaulted to true.
		if (CFLintDefaults.xmlOutput || CFLintDefaults.textOutput) {
			CFLintDefaults.htmlOutput = cmd.hasOption("html") || cmd.hasOption("htmlstyle") || cmd.hasOption("htmlfile");
		}

		if (cmd.hasOption("folder")) {
			main.folder.addAll(Arrays.asList(cmd.getOptionValue("folder").split(",")));
		}
		if (cmd.hasOption("file")) {
			main.folder.addAll(Arrays.asList(cmd.getOptionValue("file").split(",")));
		}
		if (cmd.hasOption("htmlstyle")) {
			CFLintDefaults.htmlStyle = cmd.getOptionValue("htmlstyle");
			if (!CFLintDefaults.htmlStyle.endsWith(".xsl") && !CFLintDefaults.htmlStyle.endsWith(".xslt")) {
				CFLintDefaults.htmlStyle = CFLintDefaults.htmlStyle + ".xsl";
			}
		}
		if (cmd.hasOption("xmlstyle")) {
			CFLintDefaults.xmlstyle = cmd.getOptionValue("xmlstyle");
		}
		if (cmd.hasOption("filterFile")) {
			main.filterFile = cmd.getOptionValue("filterFile");
		}
		if (cmd.hasOption("xmlfile")) {
			CFLintDefaults.xmlOutFile = cmd.getOptionValue("xmlfile");
		}
		if (cmd.hasOption("htmlfile")) {
			CFLintDefaults.htmlOutFile = cmd.getOptionValue("htmlfile");
		}
		if (cmd.hasOption("textfile")) {
			main.textOutFile = cmd.getOptionValue("textfile");
		}
		if (cmd.hasOption("extensions")) {
			main.extensions = cmd.getOptionValue("extensions");
		}
		
		if (cmd.hasOption("includeRule")) {
			main.includeRule = Arrays.asList(cmd.getOptionValue("includeRule").split(","));
		}
		if (cmd.hasOption("excludeRule")) {
			main.excludeRule = Arrays.asList(cmd.getOptionValue("excludeRule").split(","));
		}
		CFLintDefaults.showprogress=cmd.hasOption("showprogress") || (!cmd.hasOption("showprogress") && cmd.hasOption("ui"));
		CFLintDefaults.progressUsesThread=!cmd.hasOption("singlethread");
//		for (final Option option : cmd.getOptions()) {
//			if(main.verbose){
//				System.out.println("Option " + option.getOpt() + " => " + option.getValue());
//			}
//		}
		if (main.isValid()) {
			main.execute();
			if (cmd.hasOption("ui")) {
				main.open();
			}
		} else {
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("cflint", CFLintOptions.options);
		}
	}

	private void open() throws IOException {
		if (CFLintDefaults.xmlOutput) {
			Desktop.getDesktop().open(new File(CFLintDefaults.xmlOutFile));
			return;
		}
		if (CFLintDefaults.textOutput && textOutFile != null) {
			Desktop.getDesktop().open(new File(textOutFile));
			return;
		}
		if (CFLintDefaults.htmlOutput) {
			Desktop.getDesktop().open(new File(CFLintDefaults.htmlOutFile));
			return;
		}
	}

	private void ui() {
		final JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Select target directory");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		final int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			folder.add(chooser.getSelectedFile().getAbsolutePath());
		} else {
			return;
		}

		final String[] slist = new String[] { "xml", "html", "text" };
		final JList list = new JList(slist);
		JOptionPane.showMessageDialog(null, list, "Output Type", JOptionPane.PLAIN_MESSAGE);

		final int[] indxs = list.getSelectedIndices();
		// If selected set htmlOutput to false
		for (final int indx : indxs) {
			if (indx == 0) {
				CFLintDefaults.xmlOutput = true;
			}
			if (indx == 1) {
				CFLintDefaults.htmlOutput = true;
			}
			if (indx == 2) {
				CFLintDefaults.textOutput = true;
			}
		}
	}

	private void execute() throws IOException, TransformerException {
		final CFLint cflint = new CFLint();
		cflint.setVerbose(CFLintDefaults.verbose);
		cflint.setQuiet(CFLintDefaults.quiet);
		cflint.setShowProgress(CFLintDefaults.showprogress);
		cflint.setProgressUsesThread(CFLintDefaults.progressUsesThread);
		if(extensions != null && extensions.trim().length() > 0){
			try{
				cflint.setAllowedExtensions(Arrays.asList(extensions.trim().split(",")));
			}catch(Exception e){
				System.out.println("Unable to use extensions (" + extensions + ") using default instead. " + e.getMessage());
			}
		}
		CFLintFilter filter = CFLintFilter.createFilter();
		if(filterFile != null){
			File ffile = new File(filterFile);
			if(ffile.exists()){
				FileInputStream fis = new FileInputStream(ffile);
				byte b[] = new byte[fis.available()];
				fis.read(b);
				filter = CFLintFilter.createFilter(new String(b));
			}
		}
				
		if (excludeCodes != null && excludeCodes.length > 0) {
			filter.excludeCode(excludeCodes);
		}
		if (includeCodes != null && includeCodes.length > 0) {
			filter.includeCode(includeCodes);
		}
		cflint.getBugs().setFilter(filter);
		for (final String scanfolder : folder) {
			cflint.scan(scanfolder);
			// for(BugInfo bi: cflint.getBugs()){
			// System.out.println(bi);
			// }
		}
		if (CFLintDefaults.xmlOutput) {
			if(CFLintDefaults.verbose){
				System.out.println("Style:" + CFLintDefaults.xmlstyle);
			}
			if ("findbugs".equalsIgnoreCase(CFLintDefaults.xmlstyle)) {
				if(CFLintDefaults.verbose){
					System.out.println("Writing findbugs style to " + CFLintDefaults.xmlOutFile);
				}
				new XMLOutput().outputFindBugs(cflint.getBugs(), new FileWriter(CFLintDefaults.xmlOutFile));
			} else {
				if(CFLintDefaults.verbose){
					System.out.println("Writing " + CFLintDefaults.xmlOutFile);
				}
				new XMLOutput().output(cflint.getBugs(), new FileWriter(CFLintDefaults.xmlOutFile));
			}
		}
		if (CFLintDefaults.textOutput) {
			if(textOutFile != null){
				if(CFLintDefaults.verbose){
					System.out.println("Writing " + textOutFile);
				}
			}
			Writer textwriter = textOutFile != null?new FileWriter(textOutFile):new OutputStreamWriter(System.out);
			new TextOutput().output(cflint.getBugs(), textwriter);
			
		}
		if (CFLintDefaults.htmlOutput) {
			try {
				if(CFLintDefaults.verbose){
					System.out.println("Writing " + CFLintDefaults.htmlOutFile);
				}
				new HTMLOutput(CFLintDefaults.htmlStyle).output(cflint.getBugs(), new FileWriter(CFLintDefaults.htmlOutFile));
			} catch (final TransformerException e) {
				throw new IOException(e);
			}
		}
		if (includeCodes != null) {
			cflint.getBugs().getFilter().includeCode(includeCodes);
		}
		if (excludeCodes != null) {
			cflint.getBugs().getFilter().excludeCode(excludeCodes);
		}
	}

	private boolean isValid() {
		if (folder.isEmpty()) {
			System.err.println("Set -scanFolder");
			return false;
		}
		return true;
	}
}
