package io.github.tapestryminecraft.atchat;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;
import io.github.tapestryminecraft.atchat.channels.AtChatChannel;

public class AtChatRouter {
	
	// TODO save to database
	static Map<UUID, AtChatChannel> previousChannels = new HashMap<UUID, AtChatChannel>();
	
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
			this.sendChannelUpdateNotice();
			recordChannel(this.sender, this.channel);
			
		}
	}
	
	private void sendChannelUpdateNotice() {
		this.sender.sendMessage(Text.builder("Now chatting ").append(this.channel.channelText()).build());
	}
	
	private void sendMessage() {
		this.channel.send(this.buildMessage());
	}
	
	private Text buildMessage() {
		return Text.builder()
				.append(this.channel.senderText())
				.append(this.channel.channelText())
				.append(this.rawMessage.toText())
				.build();
	}
	
	private static AtChatChannel recallChannel(Player sender) {
		AtChatChannel channel = previousChannels.get(sender.getUniqueId());
		if (channel == null) {
			recordChannel(sender, channel = defaultChannel(sender));
		}
		return channel;
	}	
	
	private static void recordChannel(Player sender, AtChatChannel channel) {
		previousChannels.put(sender.getUniqueId(), channel);
	}
	
	private static AtChatChannel defaultChannel(Player sender) {
		return AtChatChannel.fromChannelString(sender, "5");
	}
}
