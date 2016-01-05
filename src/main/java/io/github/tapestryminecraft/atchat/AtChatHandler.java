package io.github.tapestryminecraft.atchat;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MutableMessageChannel;

public class AtChatHandler {

	public AtChatHandler(MessageChannelEvent.Chat event) {
		
		AtChatMessage chat = new AtChatMessage(event.getRawMessage());
		
		System.out.println(event.getMessage());
		System.out.println(event.getRawMessage());
		
		Cause sender = event.getCause();
		
		if (chat.hasBody() && chat.hasChannel()) {
			// send a one-time message to specified channel, but do not remember it
			event.setChannel(newChannel(chat.getChannel()));
			event.setMessage(Text.of(chat.getBody()));
		} else if (chat.hasBody()) {
			// send a message to remembered or default channel
			event.setChannel(recallChannel(sender));
		} else if (chat.hasChannel()) {
			// remember new channel
			MessageChannel channel = newChannel(chat.getChannel());
			memorizeChannel(sender, channel);
			event.setChannel(MessageChannel.TO_NONE);
		} else {
			// ???
		}
	}
	
	private MessageChannel newChannel(String channelName) {
		// TODO determine player vs ranged channel
		return newPlayerChannel(channelName);
	}
	
	private MessageChannel newPlayerChannel(String channelName) {
		Optional<Player> playerContainer = Sponge.getGame().getServer().getPlayer(channelName);
		MutableMessageChannel channel = emptyChannel();
		
		Player player = playerContainer.get();
		if (player != null) {
			channel.addMember(player);
		}
		return channel;
	}
	
	private MutableMessageChannel emptyChannel() {
		return MessageChannel.TO_NONE.asMutable();
	}
	
	private void memorizeChannel(Cause sender, MessageChannel channel) {
		AtChat.previousChannels.put(sender, channel);
	}
	
	private MessageChannel recallChannel(Cause sender) {
		return AtChat.previousChannels.getOrDefault(sender, MessageChannel.TO_NONE);
	}	
}
