package io.github.tapestryminecraft.atchat;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;
import io.github.tapestryminecraft.atchat.channels.AtChatChannel;

public class AtChatRouter {
	
	// TODO save to database
	static Map<MessageReceiver, AtChatChannel> previousChannels = new HashMap<MessageReceiver, AtChatChannel>();
	
	AtChatChannel channel;
	AtChatMessage rawMessage;
	Player sender;
	
	public AtChatRouter(MessageChannelEvent.Chat event) {
		event.setCancelled(true);
		
		Optional<Player> player = event.getCause().first(Player.class);
		if (!player.isPresent()) {
			return;
		}
		this.sender = player.get();
		
		this.rawMessage = new AtChatMessage(event.getRawMessage());
		
		if (this.rawMessage.hasBody() && this.rawMessage.hasChannel()) {
			
			this.channel = AtChatChannel.fromChannelString(this.sender, this.rawMessage.getChannel());
			this.sendMessage();
			
		} else if (this.rawMessage.hasBody()) {
			
			this.channel = recallChannel(this.sender);
			this.sendMessage();
			
		} else if (this.rawMessage.hasChannel()) {
			
			this.channel = AtChatChannel.fromChannelString(this.sender, this.rawMessage.getChannel());
			recordChannel(this.sender, this.channel);
			
		}
	}
	
	private void sendMessage() {
		this.channel.send(this.buildMessage());
	}
	
	private Text buildMessage() {
		return this.channel.toBuilder()
				.append(this.rawMessage.toText())
				.build();
	}
	
	
	
	private static AtChatChannel recallChannel(MessageReceiver sender) {
		return previousChannels.get(sender);
	}	
	
	private static void recordChannel(MessageReceiver sender, AtChatChannel channel) {
		previousChannels.put(sender, channel);
	}
}
