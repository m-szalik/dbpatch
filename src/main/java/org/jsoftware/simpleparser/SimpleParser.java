package org.jsoftware.simpleparser;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleParser implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<String> tokens;
	
	public SimpleParser(String... tokens){
		this.tokens = Arrays.asList(tokens);
	}

    public void parse(String input, SimpleParserCallback callback) {
		SimpleParserCallbackContext ctx = new SimpleParserCallbackContext(input);
		callback.documentStarts();
		if (tokens.isEmpty()) {
			ctx.setEndp(input.length());
			callback.documentEnds(ctx);
			return;
		} 
		Pattern pattern = preparePattern();
		Matcher matcher = pattern.matcher(input);
		int ind = 0;
		while(matcher.find(ind)) {
			int start = matcher.start();
			String token = matcher.group();
			ctx.setEndp(start);
			callback.tokenFound(ctx, token);
			ind = start + token.length();
			ctx.setStartp(ind);
		}
		ctx.setStartp(ind);
		ctx.setEndp(input.length());
		callback.documentEnds(ctx);
	}

	private Pattern preparePattern() {
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		for(int i=0; i<tokens.size(); i++) {
			sb.append(tokens.get(i));
			if (i+1 < tokens.size()) sb.append('|');
		}
		sb.append(')');
		return Pattern.compile(sb.toString());
	}
	
}
