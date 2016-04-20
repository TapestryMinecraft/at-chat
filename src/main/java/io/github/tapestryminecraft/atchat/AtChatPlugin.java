package io.github.tapestryminecraft.atchat;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.PluginManager;

import com.google.inject.Inject;

public abstract class AtChatPlugin {

	@Inject
	protected PluginManager pluginManager;
	protected AtChat atChat;
	
	@Listener
	public void onServerStart(GameStartedServerEvent event) {
		this.fetchAtChatInstance();
		this.registerChannelController();
	}
	
	
	protected final void fetchAtChatInstance(){
		this.atChat = (AtChat) pluginManager.getPlugin("atchat").orElse(null).getInstance().get();
	}
	
	protected abstract void registerChannelController();
}
