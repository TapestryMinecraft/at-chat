package io.github.tapestryminecraft.atchat;

import java.util.List;

import org.spongepowered.api.entity.living.player.Player;

public abstract class AtChatChannelController {
	
	// TODO channel name
	
	public abstract int argumentCount();
	
	public List<String> tabCompletions(List<String> completions, AtChatMessage message) {
		return completions;
	}

	public abstract String matcher();
	
	public abstract AtChatChannel channel(Player sender, AtChatMessage message);
}
