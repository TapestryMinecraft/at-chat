package io.github.tapestryminecraft.atchat;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

public class AtChatMessage {
	private String tag;
	private String rawMessage;
	private String[] args;
	
	public AtChatMessage(Text text) {
		this(text.toPlain());
	}
	
	public AtChatMessage(String rawMessage) {
		this.rawMessage = rawMessage;
		this.args = rawMessage.split(" ");
	}
	
	public String getArgument(int index) {
		return this.args[index];
	}
	
	public String[] getArguments() {
		return this.args;
	}
	
	public String getTag() {
		return this.tag == null ? this.parseTag() : this.tag;
	}
	
	public boolean includesChannel() {
		return this.getTag().length() > 0;
	}
	
	public boolean isAtChatMessage() {
		return this.rawMessage.charAt(0) == '@';
	}
	
	public Text toText(int argumentCount) {
		return Text.of(TextColors.RESET, TextStyles.RESET, this.parseBody(argumentCount));
	}
	
	private String parseBody(int argumentCount) {
		StringBuilder body = new StringBuilder();
		for (int i = argumentCount; i < this.args.length; i++) {
			body.append((i == argumentCount ? "" : " ") + this.args[i]);
		}
		return body.toString();
	}
	
	private String parseTag() {
		return this.tag = this.isAtChatMessage() ?
				this.args[0].substring(1, this.args[0].length()) :
				"";
	}
}
