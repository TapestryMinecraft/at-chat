package io.github.tapestryminecraft.atchat.channels;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import io.github.tapestryminecraft.atchat.AtChatChannel;

public class RangedChannel extends AtChatChannel {
	private int range;
	
	public RangedChannel(Player sender, String channelString) {
		int range = Integer.parseInt(channelString);
		this.sender = sender;
		this.range = Math.abs(range);
	}

	public Collection<MessageReceiver> getMembers() {
		Set<MessageReceiver> members = new HashSet<MessageReceiver>();
		
		World sourceWorld = ((Entity) this.sender).getWorld();
		Location<?> sourceLocation = ((Entity) this.sender).getLocation();
		int x1 = sourceLocation.getBlockX();
		int z1 = sourceLocation.getBlockZ();
		
		Collection<Player> players = Sponge.getServer().getOnlinePlayers();
		
		
		for (Player player: players) {
			World destinationWorld = player.getWorld();
			if (destinationWorld.equals(sourceWorld)){
				Location<?> destinationLocation = player.getLocation();
				int x2 = destinationLocation.getBlockX();
				int z2 = destinationLocation.getBlockZ();
				
				if (distance(x1, x2, z1, z2) <= range * 16){
					members.add(player);
				}
			}
		}
		return members;
	}

	@Override
	public String channelString() {
		return Integer.toString(this.range);
	}
	
	private double distance(int x1, int x2, int z1, int z2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(z1 - z2, 2));
	}
	
	@Override
	protected TextColor channelColor() {
		if (this.range == 1) {
			return TextColors.AQUA;
		} else if (this.range <= 3) {
			return TextColors.DARK_AQUA;
		} else if (this.range <= 10) {
			return TextColors.BLUE;
		} else {
			return TextColors.DARK_BLUE;
		}
	}
}
