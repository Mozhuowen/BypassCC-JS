/**
 * author mozhuowen
 * 
 * This tiny code shows a solution of bypass the defense of CC attack use javascript and cookie
 */
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class RunScript {
    public static void main(String args[])
    {
    	//here Simulation the defense of javascript code depend on cookie
	    String script = "<html><head><meta http-equiv=\"window-target\" content=\"_top\"></head><body><script language=\"javascript\">"
	    		+"function GCK(c){if(document.cookie.length>0)"
				+"	{"
				+"		s=document.cookie.indexOf(c+'=');"
				+"		if(s!=-1){"
				+"			s=s+c.length+1;e=document.cookie.indexOf(';',s);"
				+"			if(e==-1)e=document.cookie.length;"
				+"			return unescape(document.cookie.substring(s,e))"
				+"			;"
				+"		}"
				+"	}"
				+"	return '';"
				+"}"
			+"function SCK(c,v)"
			+"	{"
			+"		document.cookie=c+'='+escape(v);"
			+"	}"
			+"	var v=GCK('__hs_cc_cookie_');"
			+"	v=v+'0214';"
			+"	SCK('__hs_cc_cookie_',v);"
			+"window.location.reload(true);"
			+"</script></body></html>";   
	    
	    System.out.println(getCookie("__hs_cc_cookie_=0214;",script));
    }
    
    public static String getCookie(String lastcookie,String script) {
    	if (script.contains("javascript") && script.contains("window.location.reload")) {
    		script = getScript(script);
    	}
    	System.out.println(script);
    	script = "var document = new Object();document.cookie = '" + lastcookie + "';" + script;
    	String result = "";
    	Context cx = Context.enter();
    	try {
            Scriptable scope = cx.initStandardObjects();
            cx.evaluateString(scope, script, "<cmd>", 1, null);          
            Object x = scope.get("document", scope);
            if (x == Scriptable.NOT_FOUND) {
                System.out.println("document.cookie is not defined.");
            } else {
            	Scriptable test = Context.toObject(x, scope);
            	result = test.get("cookie", test).toString();
            }
    	} finally {
    		Context.exit();
    	}
    	return result;
    }
    
    public static String getScript(String code) {
    	String result = "";
        Pattern p = Pattern.compile("language=\"javascript\">(.*?)window.location.reload");
        Matcher m = p.matcher(code);
        while (m.find()) {
            result = m.group(1);            
        } 
    	return result;
    }
}