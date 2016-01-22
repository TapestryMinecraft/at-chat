package io.github.tapestryminecraft.atchat;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;

import io.github.tapestryminecraft.atchat.channels.AtChatChannel;
import io.github.tapestryminecraft.atchat.channels.InvalidChannel;
import io.github.tapestryminecraft.atchat.channels.PlayerChannel;
import io.github.tapestryminecraft.atchat.channels.RangedChannel;

public class AtChatRouter {
	
	// TODO save to database
	static Map<UUID, AtChatChannel> savedChannels = new HashMap<UUID, AtChatChannel>();
	static Map<UUID, AtChatChannel> lastUsedChannels = new HashMap<UUID, AtChatChannel>();
	static Map<Class<AtChatChannel>, String> channels = new HashMap<Class<AtChatChannel>, String>();
	
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
		
		if (this.rawMessage.includesBody() && this.rawMessage.includesChannel()) {
			
			this.channel = fromChannelString(this.sender, this.rawMessage.getChannel());
			if (this.channel instanceof InvalidChannel) {
				this.notifyInvalidChannel();
			} else {
				this.sendMessage();
			}
			
		} else if (this.rawMessage.includesBody()) {
			
			this.channel = recallChannel(this.sender);
			this.sendMessage();
			
		} else if (this.rawMessage.includesChannel()) {
			
			this.channel = fromChannelString(this.sender, this.rawMessage.getChannel());

			if (this.channel instanceof InvalidChannel) {
				this.notifyInvalidChannel();
			} else {
				recordChannel(this.sender, this.channel);
				notifyRecordedChannel(this.sender);
			}
			
		}
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
	
	private void notifyInvalidChannel() {
		this.sender.sendMessage(Text.builder("Cannot chat ").append(this.channel.channelText()).build());
	}
	
	public static void notifyRecordedChannel(Player sender) {
		AtChatChannel channel = recallChannel(sender);
		sender.sendMessage(Text.builder("Now chatting ").append(channel.channelText()).build());
	}
	
	private static AtChatChannel recallChannel(Player sender) {
		AtChatChannel channel = savedChannels.get(sender.getUniqueId());
		if (channel == null) {
			recordChannel(sender, channel = defaultChannel(sender));
		}
		return channel;
	}	
	
	private static void recordChannel(Player sender, AtChatChannel channel) {
		savedChannels.put(sender.getUniqueId(), channel);
	}
	
	private static AtChatChannel defaultChannel(Player sender) {
		return fromChannelString(sender, "5");
	}
	
//	public static void registerChannel(Class<AtChatChannel> channel, String matcher) {
//		channels.put((Class<AtChatChannel>) channel, matcher);
//	}

	public static AtChatChannel fromChannelString(Player sender, String channelString) {
		if (channelString.length() > 3) {
			Optional<Player> recipient = Sponge.getServer().getPlayer(channelString);
			if (recipient.isPresent()) {
				return new PlayerChannel(sender, recipient.get());
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
