package com.cflint.plugins.core;

import ro.fortsoft.pf4j.Extension;
import net.htmlparser.jericho.Element;
import cfml.parsing.cfscript.CFExpression;
import cfml.parsing.cfscript.CFFunctionExpression;
import cfml.parsing.cfscript.script.CFScriptStatement;

import com.cflint.BugInfo;
import com.cflint.BugList;
import com.cflint.plugins.CFLintScanner;
import com.cflint.plugins.Context;


@Extension
public class CFWriteOutputChecker implements CFLintScanner {
	final String CFML_TAG_REJECT = "writeDump";
	final String SEVERITY = "WARNING";
	final String MESSAGE_CODE = "AVOID_USING_" + CFML_TAG_REJECT.toUpperCase() + "_TAG";
	final String MESSAGE = "Avoid Leaving<" + 
							"CFML_TAG_REJECT" +
							"> tags in committed code. Debug information should be ommited from release code";
	
	public void expression(final CFExpression expression, final Context context, final BugList bugs) {
		if (expression instanceof CFFunctionExpression) {
				bugs.add(new BugInfo.BugInfoBuilder().setLine(11).setColumn(11)
						.setMessageCode(MESSAGE_CODE).setSeverity(SEVERITY)
						.setFilename(context.getFilename()).setFunction(context.getFunctionName())
						.setMessage(MESSAGE)
						.build());
			
		}
	}

	public void expression(final CFScriptStatement expression, final Context context, final BugList bugs) {
	}
	
	public void element(final Element element, final Context context, final BugList bugs) {
	}
}

			

				
			