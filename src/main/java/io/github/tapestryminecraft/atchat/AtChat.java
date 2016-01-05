package io.github.tapestryminecraft.atchat;

import java.util.HashMap;
import java.util.Map;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.channel.MessageChannel;

@Plugin(id = "atchat", name = "AtChat", version = "0")
public class AtChat {
	
	// TODO save to database
	static Map<Cause, MessageChannel> previousChannels = new HashMap<Cause, MessageChannel>();
	
	@Listener
	public void onServerStart(GameStartedServerEvent event) {
		// TODO load or setup database
	}
	
	@Listener
	public void onMessage(MessageChannelEvent.Chat event) {
		new AtChatHandler(event);
	}
}
