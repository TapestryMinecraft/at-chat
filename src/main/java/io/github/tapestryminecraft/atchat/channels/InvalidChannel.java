package io.github.tapestryminecraft.atchat.channels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;

import io.github.tapestryminecraft.atchat.AtChatChannel;

public class InvalidChannel extends AtChatChannel {
	private Player sender;
	private String channelName;
	private String error;
	
	public InvalidChannel(Player sender, String channelName, String error) {
		this.channelName = channelName;
		this.sender = sender;
		this.error = error;
	}

	public Collection<MessageReceiver> getMembers() {
		List<MessageReceiver> list = new ArrayList<MessageReceiver>();
		list.add(this.sender);
		return list;
	}

	@Override
	protected String getChannelString() {
		return this.channelName;
	}

	@Override
	protected Player getSender() {
		return this.sender;
	}

	@Override
	public void sendMessage(Text message) {
		this.send(Text.of(this.error));
	}
}
