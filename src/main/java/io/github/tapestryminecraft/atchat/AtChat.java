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
		this.registerLastUsedChannelController();
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
		
		if (!message.isAtChatMessage()) {
			return;
		}
		
		String channelTag = message.getTag();
		AtChatChannelController controller = AtChatRouter.matchChannelController(channelTag);
		List<String> completions = event.getTabCompletions();
		
		if (controller == null) {
			// by default, suggest player names
			completions.clear();

			Collection<Player> players = Sponge.getServer().getOnlinePlayers();
			
			for (Player player : players) {
				if (player.getName().toLowerCase().startsWith(channelTag.toLowerCase())) {
					completions.add("@" + player.getName());
				}
			}
		} else {
			controller.tabCompletions(completions, message);
		}
	}
	
	public void registerChannel(AtChatChannelController controller) {
		AtChatRouter.registerController(controller);
	}
	
	private void registerLastUsedChannelController() {
		this.registerChannel(new AtChatChannelController(){

			@Override
			public int argumentCount() { return 1; }

			@Override
			public String matcher() { return "@"; }

			@Override
			public AtChatChannel channel(Player sender, AtChatMessage message) {
				return AtChatRouter.recallLastUsedChannel(sender);
			}
			
		});
	}
	
	private void registerPlayerChannelController() {
		this.registerChannel(new AtChatChannelController(){
			@Override
			public int argumentCount() { return 1; }
			
			@Override
			public String matcher() { return "[a-zA-Z0-9_]{4,16}"; }

			@Override
			public AtChatChannel channel(Player sender, AtChatMessage message) {
				return new PlayerChannel(sender, message.getTag());
			}
		});
	}
	
	private void registerRangedChannelController() {
		this.registerChannel(new AtChatChannelController(){
			@Override
			public int argumentCount() { return 1; }
			
			@Override
			public String matcher() { return "\\d{1,3}"; }

			@Override
			public AtChatChannel channel(Player sender, AtChatMessage message) {
				return new RangedChannel(sender, message.getTag());
			}
		});
	}
}
