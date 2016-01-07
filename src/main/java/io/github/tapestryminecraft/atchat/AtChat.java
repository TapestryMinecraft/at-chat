package io.github.tapestryminecraft.atchat;

import java.util.Collection;
import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.command.TabCompleteEvent;
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
	
	@Listener
	public void onTabComplete(TabCompleteEvent.Chat event) {
		AtChatMessage message = new AtChatMessage(event.getRawMessage());
		
		if (message.signalsChannel() && !message.signalsBody() ) {
			List<String> completions = event.getTabCompletions();
			completions.clear();
			Collection<Player> players = Sponge.getServer().getOnlinePlayers();
			
			for (Player player : players) {
				completions.add("@" + player.getName());
			}
			
			// TODO filter by already-typed characters
		}
	}
}
