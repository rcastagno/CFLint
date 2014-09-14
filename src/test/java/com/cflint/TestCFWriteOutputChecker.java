package com.cflint;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.cflint.CFLint;
import com.cflint.StackHandler;
import com.cflint.plugins.core.CFWriteOutputChecker;

import cfml.parsing.cfscript.ParseException;
import static org.junit.Assert.*;

public class TestCFWriteOutputChecker {

	@Test
	public void test_BAD() throws ParseException, IOException {
		final String cfcSrc = 
								"filename = \"log.txt\";" +
								"	    try { " +
								"	    result = FileOpen(expandpath(filename)); " +
								"	    WriteDump(result); " +
								"	} " +
								"	catch(Expression exception) { " +
								"	    WriteOutput(\"<p>An Expression exception was thrown.</p>\"); " +
								"	    WriteOutput(\"<p>#exception.message#</p>\"); " +
								"	    WriteLog(type=\"Error\", file=\"myapp.log\", text=\"[exception.type] " +
								"	    #exception.message#\"); " +
								"	    } "
							;
		final CFLint cfBugs = new CFLint(new CFWriteOutputChecker());
		cfBugs.process(cfcSrc, "test");
		assertEquals(1, cfBugs.getBugs().getBugList().size());
	}

}
