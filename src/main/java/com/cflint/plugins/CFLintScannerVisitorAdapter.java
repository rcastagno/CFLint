package com.cflint.plugins;

import net.htmlparser.jericho.Element;

import com.cflint.BugList;

import cfml.CFSCRIPTParserBaseVisitor;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.script.CFScriptStatement;

public class CFLintScannerVisitorAdapter extends CFSCRIPTParserBaseVisitor<Object> implements CFLintScanner{

	protected BugList bugs;
	protected String filename;
	
	@Override
	public void expression(CFExpression expression, Context context,
			BugList bugs) {
	}

	@Override
	public void element(Element element, Context context, BugList bugs) {
	}

	@Override
	public void expression(CFScriptStatement expression, Context context,
			BugList bugs) {
	}

	@Override
	public void setParameter(String name, String value) {
	}

	@Override
	public void startFile(String fileName, BugList bugs) {
		this.bugs=bugs;
		this.filename = filename;
	}

	@Override
	public void endFile(String fileName, BugList bugs) {
	}

	@Override
	public void startComponent(Context context, BugList bugs) {
	}

	@Override
	public void endComponent(Context context, BugList bugs) {
	}

	@Override
	public void startFunction(Context context, BugList bugs) {
	}

	@Override
	public void endFunction(Context context, BugList bugs) {
	}

}
