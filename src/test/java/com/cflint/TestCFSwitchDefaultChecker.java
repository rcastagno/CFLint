package com.cflint;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cflint.CFLint;
import com.cflint.StackHandler;
import com.cflint.plugins.core.CFSwitchDefaultChecker;
import com.cflint.plugins.core.CFUpdateTagChecker;

import cfml.parsing.cfscript.ParseException;
import static org.junit.Assert.*;

public class TestCFSwitchDefaultChecker {

	StackHandler handler = null;

	@Before
	public void setUp() {
		handler = new StackHandler();
	}

	@Test
	public void test_OK() throws ParseException, IOException {
		final String cfcSrc = "<cfswitch expression=\"#score#\">" +
			    "<cfcase value=\"10\">" +
			    "    <cfset grade=\"A\">" +
			    "</cfcase>" +
			    "<cfcase value=\"9;8\" delimiters=\";\">" +
			        "<cfset grade=\"B\">" +
			    "</cfcase>" +
			    "<cfcase value=\"7;6\" delimiters=\";\">" +
			        "<cfset grade=\"C\">" +
			    "</cfcase>" +
			    "<cfcase value=\"5;4;\" delimiters=\";\">" +
			        "<cfset grade=\"D\">" +
			    "</cfcase>" +
			    "<cfdefaultcase>" +
			        "<cfset grade=\"F\">" +
			    "</cfdefaultcase>" +
			"</cfswitch>";
		final CFLint cfBugs = new CFLint(new CFSwitchDefaultChecker());
		cfBugs.process(cfcSrc, "test");
		assertEquals(0, cfBugs.getBugs().getBugList().size());
	}

	@Test
	public void testBAD() throws ParseException, IOException {
		final String cfcSrc = "<cfswitch expression=\"#score#\">" +
			    "<cfcase value=\"10\">" +
			    "    <cfset grade=\"A\">" +
			    "</cfcase>" +
			    "<cfcase value=\"9;8\" delimiters=\";\">" +
			        "<cfset grade=\"B\">" +
			    "</cfcase>" +
			    "<cfcase value=\"7;6\" delimiters=\";\">" +
			        "<cfset grade=\"C\">" +
			    "</cfcase>" +
			    "<cfcase value=\"5;4;\" delimiters=\";\">" +
			        "<cfset grade=\"D\">" +
			    "</cfcase>" +
			"</cfswitch>";
		final CFLint cfBugs = new CFLint(new CFSwitchDefaultChecker());
		cfBugs.process(cfcSrc, "test");
		assertEquals(1, cfBugs.getBugs().getBugList().size());
	}

}
