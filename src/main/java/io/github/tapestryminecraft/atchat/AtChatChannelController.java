package io.github.tapestryminecraft.atchat;

import org.spongepowered.api.entity.living.player.Player;

public abstract class AtChatChannelController {
	
	// TODO channel name, tab completion

	public abstract String matcher();
	
	public abstract AtChatChannel channel(Player sender, String channelString);
}
