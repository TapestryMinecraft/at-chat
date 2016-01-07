package io.github.tapestryminecraft.atchat;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

public class AtChatMessage {
	private String channel;
	private String body;
	private String rawMessage;
	private String[] args;
	
	public AtChatMessage(Text text) {
		this(text.toPlain());
	}
	
	public AtChatMessage(String rawMessage) {
		this.rawMessage = rawMessage;
		this.args = rawMessage.split(" ");
	}
	
	public String getBody() {
		return this.body == null ? this.parseBody() : this.body;
	}
	
	public String getChannel() {
		return this.channel == null ? this.parseChannel() : this.channel;
	}
	
	public boolean includesBody() {
		return this.getBody().length() > 0;
	}
	
	public boolean includesChannel() {
		return this.getChannel().length() > 0;
	}
	
	public boolean signalsBody() {
		return this.rawMessage.contains(" ");
	}
	
	public boolean signalsChannel() {
		return this.rawMessage.charAt(0) == '@';
	}
	
	public Text toText() {
		return Text.of(TextColors.RESET, TextStyles.RESET, this.body);
	}
	
	private String parseBody() {
		StringBuilder body = new StringBuilder();
		int start = this.signalsChannel() ? 1 : 0;
		for (int i = start; i < this.args.length; i++) {
			body.append((i == start ? "" : " ") + this.args[i]);
		}
		return this.body = body.toString();
	}
	
	private String parseChannel() {
		return this.channel = this.signalsChannel() ?
				this.args[0].substring(1, this.args[0].length()) :
				"";
	}
}
