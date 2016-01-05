package io.github.tapestryminecraft.atchat;

import org.spongepowered.api.text.Text;

public class AtChatMessage {
	private String channel;
	private String body;
	
	public AtChatMessage(Text text) {
		String[] args = text.toPlain().split(" ");
		
		this.setChannel(args);
		this.setBody(args);
	}
	
	public String getBody() {
		return this.body;
	}
	
	public String getChannel() {
		return this.channel;
	}
	
	public boolean hasBody() {
		return this.body.length() > 0;
	}
	
	public boolean hasChannel() {
		return this.channel.length() > 0;
	}
	
	private void setBody(String[] args) {
		StringBuilder body = new StringBuilder();
		for (int i = this.hasChannel() ? 1 : 0; i < args.length; i++) {
			body.append(args[i]);
		}
		this.body = body.toString();
	}
	
	private void setChannel(String[] args) {
		this.channel = args[0].charAt(0) == '@' ?
				args[0].substring(1, args[0].length()) :
				"";
	}
}
