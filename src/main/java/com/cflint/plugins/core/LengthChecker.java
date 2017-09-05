package com.cflint.plugins.core;

import com.cflint.BugList;
import com.cflint.plugins.CFLintScannerAdapter;
import com.cflint.plugins.Context;

/**
 * Check the length of a block of code.
 */
public class LengthChecker extends CFLintScannerAdapter {

    /**
     *  Check the length of a block of code.
     *
     * @param maxLength maximimum length.
     * @param message message to display.
     * @param context current context.
     * @param atLine  current line.
     * @param linesLength no of lines.
     * @param bugs list of bugs.
     */
    protected void checkSize(final int maxLength, final String message, final Context context, final int atLine, final int linesLength, final BugList bugs) {
        final String lengthThreshold = getParameter("length");
        int length = maxLength;

        if (lengthThreshold != null) {
            length = Integer.parseInt(lengthThreshold);
        }

        if (linesLength > length) {
            context.addMessage(message, Integer.toString(linesLength), this, atLine);

        }
    }
}