package io.github.tapestryminecraft.atchat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	private static Map<UUID, AtChatChannel> lastUsedChannels = new HashMap<UUID, AtChatChannel>();
	private static List<AtChatChannelController> channelControllers = new ArrayList<AtChatChannelController>();
	
	AtChatChannelController controller;
	AtChatChannel channel;
	AtChatMessage message;
	Player sender;
	
	public AtChatRouter(MessageChannelEvent.Chat event) {
		event.setCancelled(true);
		
		Optional<Player> player = event.getCause().first(Player.class);
		if (!player.isPresent()) {
			return;
		}
		this.sender = player.get();
		
		this.message = new AtChatMessage(event.getRawMessage());

		if (this.message.isAtChatMessage()) {
			
			this.controller = matchChannelController(this.message.getTag());
			
			if (this.message.getArguments().length > this.controller.argumentCount()) {
				this.routeBodyAndChannel();
			} else if (this.message.getArguments().length < this.controller.argumentCount()){
				this.sender.sendMessage(Text.of("Not enough arguments for @" + message.getTag()));
			} else {
				this.routeChannel();
			}
			
		} else {
			this.routeBody();
		}
	}
	
	private void sendMessage() {
		this.channel.sendMessage(this.buildMessage());
	}
	
	private Text buildMessage() {
		return Text.builder()
				.append(this.channel.senderText())
				.append(this.channel.channelText())
				.append(this.message.toText(this.controller == null ? 0 : this.controller.argumentCount()))
				.build();
	}
	
	private void notifyInvalidChannel() {
		this.sender.sendMessage(Text.builder("Cannot chat ").append(this.channel.channelText()).build());
	}
	
	private void routeBody() {
		this.channel = recallChannel(this.sender);
		this.sendMessage();
	}
	
	private void routeBodyAndChannel() {
		this.channel = generateChannel(this.sender, this.message);
		if (this.channel instanceof InvalidChannel) {
			this.notifyInvalidChannel();
		} else {
			recordLastUsedChannel(this.sender, this.channel);
			this.sendMessage();
		}
	}
	
	private void routeChannel() {
		this.channel = generateChannel(this.sender, this.message);

		if (this.channel instanceof InvalidChannel) {
			this.notifyInvalidChannel();
		} else {
			recordChannel(this.sender, this.channel);
			notifyRecordedChannel(this.sender);
		}
	}
	
	
	
	
	
	private static AtChatChannel defaultChannel(Player sender) {
		// TODO set default channel in config
		return generateChannel(sender, new AtChatMessage("@5"));
	}

	private static AtChatChannel generateChannel(Player sender, AtChatMessage message) {
		String channelTag = message.getTag();
		AtChatChannelController controller = matchChannelController(channelTag);
		
		if (controller == null) {
			return new InvalidChannel(sender, channelTag, "Invalid channel tag");
		}
		
		// TODO catch and report channel-specific errors
		return controller.channel(sender, message);
	}
	
	public static AtChatChannelController matchChannelController(String channelTag) {
		for(AtChatChannelController controller : channelControllers) {
			if (channelTag.matches(controller.matcher())) {
				return controller;
			}
		}
		return null;
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

	public static AtChatChannel recallLastUsedChannel(Player sender) {
		AtChatChannel channel = lastUsedChannels.get(sender.getUniqueId());
		if (channel == null) {
			recordLastUsedChannel(sender, channel = defaultChannel(sender));
		}
		return channel;
	}
	
	private static void recordChannel(Player sender, AtChatChannel channel) {
		if (!(channel instanceof InvalidChannel)) {
			savedChannels.put(sender.getUniqueId(), channel);
		}
	}
	
	private static void recordLastUsedChannel(Player sender, AtChatChannel channel) {
		if (!(channel instanceof InvalidChannel)) {
			lastUsedChannels.put(sender.getUniqueId(), channel);
		}
	}
	
	public static void registerController(AtChatChannelController controller) {
		channelControllers.add(controller);
	}
}
