package io.github.tapestryminecraft.atchat.channels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.channel.MessageReceiver;

import io.github.tapestryminecraft.atchat.AtChatChannel;

public class InvalidChannel extends AtChatChannel {
	String channelName;
	
	public InvalidChannel(Player sender, String channelName) {
		this.channelName = channelName;
		this.sender = sender;
	}

	public Collection<MessageReceiver> getMembers() {
		List<MessageReceiver> list = new ArrayList<MessageReceiver>();
		list.add(this.sender);
		return list;
	}

	@Override
	protected String channelString() {
		return this.channelName;
	}

}
