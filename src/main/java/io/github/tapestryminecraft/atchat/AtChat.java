package io.github.tapestryminecraft.atchat;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = "atchat", name = "AtChat", version = "0")
public class AtChat {
	
	@Listener
	public void onServerStart(GameStartedServerEvent event) {
		// TODO load or setup database
	}
	
	@Listener
	public void onMessage(MessageChannelEvent.Chat event) {
		new AtChatRouter(event);
	}
	
	@Listener 
	public void onPlayerJoin(ClientConnectionEvent.Join event) {
		Player sender = event.getTargetEntity();
		AtChatRouter.notifyRecordedChannel(sender);
	}
}
