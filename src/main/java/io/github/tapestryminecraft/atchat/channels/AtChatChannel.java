package io.github.tapestryminecraft.atchat.channels;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

public abstract class AtChatChannel implements MessageChannel{
	protected Player sender;
	
	public Text senderText() {
		return Text.of(
				this.senderColor(),
				TextStyles.RESET,
				TextActions.suggestCommand("@" + this.sender.getName() + " "),
				"[" + this.sender.getName() + "] "
				);
	}
	
	public Text channelText() {
		String channelString = "@" + this.channelString() + " ";
		return Text.of(
				this.channelColor(),
				TextStyles.ITALIC,
				TextActions.suggestCommand(channelString),
				channelString
				);
	}

	protected abstract String channelString();
	
	protected TextColor channelColor() {
		return TextColors.RESET;
	}
	
	protected TextColor senderColor() {
		return TextColors.RESET;
	}
}
