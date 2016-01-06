package io.github.tapestryminecraft.atchat.channels;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

public abstract class AtChatChannel implements MessageChannel{
	protected String channelName;
	protected Player sender;
	
	public Text.Builder toBuilder() {
		return Text.builder()
				.append(Text.of(this.senderColor(), TextStyles.RESET, "[" + ((Player) this.sender).getName() + "] "))
				.append(Text.of(this.channelColor(), TextStyles.ITALIC, "@" + this.textString() + " "))
				;
	}

	protected abstract String textString();
	
	protected TextColor channelColor() {
		return TextColors.RESET;
	}
	
	protected TextColor senderColor() {
		return TextColors.RESET;
	}

	public static AtChatChannel fromChannelString(Player sender, String channelString) {
		if (channelString.length() > 3) {
			Optional<Player> player = Sponge.getServer().getPlayer(channelString);
			if (player.isPresent()) {
				return new PlayerChannel(sender, channelString);
			} else {
				return new InvalidChannel(sender, channelString);
			}
		} else {
			try {
				return new RangedChannel(sender, Integer.parseInt(channelString));
			} catch(NumberFormatException e) {
				return new InvalidChannel(sender, channelString);
			}
		}
	}
}
