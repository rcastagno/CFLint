component test {
	filename = "log.txt"; 
	    try { 
	    result = FileOpen(expandpath(filename)); 
	    WriteDump(result); 
	} 
	catch(Expression exception) { 
	    WriteOutput("<p>An Expression exception was thrown.</p>"); 
	    WriteOutput("<p>#exception.message#</p>"); 
	    WriteLog(type="Error", file="myapp.log", text="[exception.type] 
	    #exception.message#"); 
	    } 

}