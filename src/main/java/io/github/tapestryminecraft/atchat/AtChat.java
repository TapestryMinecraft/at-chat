package io.github.tapestryminecraft.atchat;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.message.MessageChannelEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.channel.MutableMessageChannel;

@Plugin(id = "atchat", name = "AtChat", version = "0")
public class AtChat {

	// TODO save to database
	Map<Cause, MessageChannel> previousChannels = new HashMap<Cause, MessageChannel>();
	
	@Listener
	public void onServerStart(GameStartedServerEvent event) {
		// TODO load or setup database
	}
	
	@Listener
	public void onMessage(MessageChannelEvent.Chat event) {
		Text message = event.getRawMessage();
		Cause sender = event.getCause();
		
		MessageChannel channel = rememberOrCreateChannel(message, sender);
		
		event.setChannel(channel);
	}
	
	private MessageChannel rememberOrCreateChannel(Text message, Cause sender) {
		return isAtMessage(message) ? createChannel(message, sender) : rememberChannel(sender);
	}
	
	private boolean isAtMessage(Text message) {
		return message.toPlain().charAt(0) == '@';
	}
	
	private MessageChannel createChannel(Text message, Cause sender) {
		String reference = getReference(message);
		MessageChannel channel = createPlayerChannel(reference); 
		this.previousChannels.put(sender, channel);
		return channel;
	}
	
	public MessageChannel createPlayerChannel(String name) {
		Optional<Player> player = Sponge.getGame().getServer().getPlayer(name);
		MutableMessageChannel channel = MessageChannel.TO_NONE.asMutable();
		channel.addMember(player.get());
		return channel;
	}
	
	private MessageChannel rememberChannel(Cause sender) {
		return this.previousChannels.getOrDefault(sender, MessageChannel.TO_NONE);
	}
	
	private String getReference(Text message) {
		// TODO return part between the @ and the first space
		String reference = message.toPlain().split(" ")[0];
		System.out.println(reference);
		return reference.substring(1, reference.length());
	}
}
