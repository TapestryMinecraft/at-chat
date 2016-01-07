package io.github.tapestryminecraft.atchat.channels;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

public class PlayerChannel extends AtChatChannel{
	private String recipientName;
	private UUID recipientId;
	
	public PlayerChannel(Player sender, Player recipient) {
		this.sender = sender;
		this.recipientId = recipient.getUniqueId();
		this.recipientName = recipient.getName();
	}

	public Collection<MessageReceiver> getMembers() {
		Set<MessageReceiver> members = new HashSet<MessageReceiver>();
		
		// TODO error checking, player is offline
		Player player = Sponge.getServer().getPlayer(this.recipientId).get();
		
		members.add(player);
		
		return members;
	}

	@Override
	public String channelString() {
		return this.recipientName;
	}
	
//	public boolean isValid() {
//		return this.recipient.isOnline();
//	}
	
	@Override
	protected TextColor channelColor() {
		return TextColors.LIGHT_PURPLE;
	}
	
	@Override
	protected TextColor senderColor() {
		return TextColors.LIGHT_PURPLE;
	}
}
