package io.github.tapestryminecraft.atchat;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.text.Text;

import io.github.tapestryminecraft.atchat.channels.InvalidChannel;

public class AtChatRouter {
	
	// TODO save to database
	private static Map<UUID, AtChatChannel> savedChannels = new HashMap<UUID, AtChatChannel>();
	// TODO save lastUsedChannels for @@ or @re or @_ syntax
//	private static Map<UUID, AtChatChannel> lastUsedChannels = new HashMap<UUID, AtChatChannel>();
	// TODO matchers should be stored in order
	private static Map<String, Class<? extends AtChatChannel>> matchers = new LinkedHashMap<String, Class<? extends AtChatChannel>>();
	
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
		this.channel.sendMessage(this.buildMessage());
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
	
	public static void registerChannel(Class<? extends AtChatChannel> channel, String matcher) {
		matchers.put(matcher, channel);
	}
	
	private static AtChatChannel recallChannel(Player sender) {
		AtChatChannel channel = savedChannels.get(sender.getUniqueId());
		if (channel == null) {
			recordChannel(sender, channel = defaultChannel(sender));
		}
		return channel;
	}	
	
	private static void recordChannel(Player sender, AtChatChannel channel) {
		if (!(channel instanceof InvalidChannel)) {
			savedChannels.put(sender.getUniqueId(), channel);
		}
	}
	
	private static AtChatChannel defaultChannel(Player sender) {
		return fromChannelString(sender, "5");
	}

	private static AtChatChannel fromChannelString(Player sender, String channelString) {
		Class<?> c = matchChannel(channelString);
		
		try {
			return (AtChatChannel) c.getDeclaredConstructor(Player.class, String.class).newInstance(sender, channelString);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO catch channel-specific errors
			e.printStackTrace();
			return new InvalidChannel(sender, channelString);
		}
	}
	
	private static Class<? extends AtChatChannel> matchChannel(String channelString) {
		for(String matcher : matchers.keySet()) {
			if (channelString.matches(matcher)) {
				return matchers.get(matcher);
			}
		}
		return InvalidChannel.class;
	}
}
