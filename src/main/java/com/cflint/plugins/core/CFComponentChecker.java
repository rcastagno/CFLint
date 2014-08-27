package com.cflint.plugins.core;

import ro.fortsoft.pf4j.Extension;
import net.htmlparser.jericho.Element;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.script.CFScriptStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScanner;
import com.cflint.plugins.Context;
import com.cflint.tools.CFMLDeclaration;
import com.cflint.tools.CFSeverity;

@Extension
public class CFComponentChecker implements CFLintScanner {
	final String cfmlDeclarationCheck = CFMLDeclaration.CFCOMPONENT.getValue();
	final String severity = CFSeverity.INFO.getValue();
	final String messageCode = "CFSCRIPT_FOR_COMPONENT_RECOMMENDED";
	final String message = cfmlDeclarationCheck + " is becoming deprecated. Preferred use is a separate CFScript file (.cfc).";
	
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		
	}

	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
	}
	
	public void element(final Element element, final Context context, final BugList bugs) {
		String tagName = element.getName();
		if (tagName.equalsIgnoreCase(cfmlDeclarationCheck)) {
			int begLine = element.getSource().getRow(element.getBegin());
			bugs.add(new BugInfo.BugInfoBuilder().setLine(begLine).setMessageCode(messageCode)
				.setSeverity(severity).setFilename(context.getFilename())
				.setMessage(message)
				.build());
		}
	}
}