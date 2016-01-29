package io.github.tapestryminecraft.atchat;

import java.util.Collection;
import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.TabCompleteEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;

import io.github.tapestryminecraft.atchat.channels.PlayerChannel;
import io.github.tapestryminecraft.atchat.channels.RangedChannel;

@Plugin(id = "atchat", name = "AtChat", version = "0")
public class AtChat {
	
	@Listener(order = Order.EARLY)
	public void onServerStart(GameStartedServerEvent event) {
		// TODO load or setup database
		// TODO disable default channels in config
		// TODO use controllers to match previous channels ?
		this.registerPlayerChannelController();
		this.registerRangedChannelController();
	}
	
	@Listener
	public void onMessage(MessageChannelEvent.Chat event) {
		new AtChatRouter(event);
	}
	
	@Listener
	public void onPlayerJoin(ClientConnectionEvent.Join event) {
		Player sender = event.getTargetEntity();
		// TODO send this message after "[Player] joined the game"; (order = Order.POST) does not work
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
				if (player.getName().toLowerCase().startsWith(message.getChannel().toLowerCase())) {
					completions.add("@" + player.getName());
				}
			}
		}
	}
	
	public void registerChannel(AtChatChannelController factory) {
		AtChatRouter.registerController(factory);
	}
	
	private void registerPlayerChannelController() {
		this.registerChannel(new AtChatChannelController(){
			@Override
			public String matcher() { return "[a-zA-Z0-9_]{4,16}"; }

			@Override
			public AtChatChannel channel(Player sender, String channelString) {
				return new PlayerChannel(sender, channelString);
			}
		});
	}
	
	private void registerRangedChannelController() {
		this.registerChannel(new AtChatChannelController(){
			@Override
			public String matcher() { return "\\d{1,3}"; }

			@Override
			public AtChatChannel channel(Player sender, String channelString) {
				return new RangedChannel(sender, channelString);
			}
		});
	}
}
