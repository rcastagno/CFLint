package com.cflint.plugins.core;
import ro.fortsoft.pf4j.Extension;
import net.htmlparser.jericho.Element;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.script.CFScriptStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScanner;
import com.cflint.plugins.Context;
import com.cflint.tools.CFSeverity;
import com.cflint.tools.CFMLTag;

@Extension
public class SelectStarChecker implements CFLintScanner {
	final String selectStar = "select*";
	final String messageCode = "AVOID_SELECT_*_IN_QUERY";
	final String message = "Avoid using 'select *' in a query. It may cause performance issues.";
	final String severity = CFSeverity.MAJOR.getValue();

	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		
	}

	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
	
	}
	
	public void element(final Element element, final Context context, final BugList bugs) {
		String tagName = element.getName();
		if (tagName.equals(CFMLTag.CFQUERY.getValue())){
			
			String queryGuts = element.getContent().toString().replaceAll("\\s+","");
			queryGuts = queryGuts.toLowerCase();

			if (queryGuts.contains(selectStar)){
				int beginLine = element.getSource().getRow(element.getBegin());
				bugs.add(new BugInfo.BugInfoBuilder().setLine(beginLine).setMessageCode(messageCode)
					.setSeverity(severity).setFilename(context.getFilename())
					.setMessage(message)
					.build());
				}
			}
		}		
	}


