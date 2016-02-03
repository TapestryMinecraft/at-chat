package io.github.tapestryminecraft.atchat;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

public abstract class AtChatChannel implements MessageChannel{

	public final Text senderText() {
		return Text.of(
			this.senderColor(),
			TextStyles.RESET,
			TextActions.suggestCommand("@" + this.getSender().getName() + " "),
			"[" + this.getSender().getName() + "] "
		);
	}
	
	public final Text channelText() {
		String channelString = "@" + this.getChannelString() + " ";
		return Text.of(
			this.channelColor(),
			TextStyles.ITALIC,
			TextActions.suggestCommand(channelString),
			channelString
		);
	}
	
	public void sendMessage(Text message) {
		this.send(message);
	}

	protected abstract String getChannelString();
	
	protected abstract Player getSender();
	
	protected TextColor channelColor() {
		return TextColors.RESET;
	}
	
	protected TextColor senderColor() {
		return TextColors.RESET;
	}
}
