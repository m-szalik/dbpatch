package org.jsoftware.simpleparser;

public interface SimpleParserCallback {
	
	void documentStarts();
	
	void tokenFound(SimpleParserCallbackContext ctx, String token);

	void documentEnds(SimpleParserCallbackContext ctx);
}
