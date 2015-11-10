package com.cflint.plugins.visitors;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import cfml.CFSCRIPTParser.CompoundStatementContext;

import com.cflint.BugInfo;
import com.cflint.plugins.CFLintScannerVisitorAdapter;

public class BracketsStyleChecker extends CFLintScannerVisitorAdapter {

	@Override
	public Object visitCompoundStatement(CompoundStatementContext ctx) {
		// Left brace should be on the same line as the token before it.
		if(ctx.LEFTCURLYBRACKET().getSymbol().getLine() !=
				getPrecedingNode(ctx).getLine()){
			bugs.add(new BugInfo.BugInfoBuilder().setLine(ctx.LEFTCURLYBRACKET().getSymbol().getLine())
					.setMessageCode("BRACKETS_STYLE")
					.setSeverity("INFO").setFilename(filename)
					.setMessage("Brackets should be formatted consistently.")
					.build());
		}
		// Right brace should be the last token on the line.
		if(ctx.RIGHTCURLYBRACKET().getSymbol().getLine() ==
				getNextNode(ctx).getLine()){
			bugs.add(new BugInfo.BugInfoBuilder().setLine(ctx.RIGHTCURLYBRACKET().getSymbol().getLine())
					.setMessageCode("BRACKETS_STYLE")
					.setSeverity("INFO").setFilename(filename)
					.setMessage("Brackets should be formatted consistently.")
					.build());
		}

		return super.visitCompoundStatement(ctx);
	}

	private Token getPrecedingNode(ParserRuleContext ctx) {
		if (ctx == null) {
			return null;
		}
		int idx = ctx.getParent().children.indexOf(ctx);
		if (idx > 0) {
			ParseTree parseTree = ctx.getParent().children.get(idx - 1);
			if (parseTree instanceof TerminalNode) {
				return ((TerminalNode) parseTree).getSymbol();
			} else if (parseTree instanceof ParserRuleContext) {
				return ((ParserRuleContext) parseTree).getStop();
			}
		}
		return getPrecedingNode(ctx.getParent());
	}

	private Token getNextNode(ParserRuleContext ctx) {
		if (ctx == null) {
			return null;
		}
		int idx = ctx.getParent().children.indexOf(ctx);
		if (idx < ctx.getParent().children.size() - 1) {
			ParseTree parseTree = ctx.getParent().children.get(idx + 1);
			if (parseTree instanceof TerminalNode) {
				return ((TerminalNode) parseTree).getSymbol();
			} else if (parseTree instanceof ParserRuleContext) {
				return ((ParserRuleContext) parseTree).getStart();
			}
		}
		return getNextNode(ctx.getParent());
	}

}
