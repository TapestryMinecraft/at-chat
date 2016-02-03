# AtChat

Chat with Minecraft players using the internet-friendly "@" syntax.

## Channels

AtChat comes with two channels by default: player channels and ranged channels.

Use `@PlayerName` to send private messages to a player.

Use `@range`, where range is a number between 1 and 999, to send messages to anyone within a certain chunk radius.

See [Plugins][plugins] for a list of custom channels available as plugins for AtChat.

[plugins]: https://github.com/TapestryMinecraft/at-chat#plugins

## Interaction

Use the syntax `@ChannelName` to switch to a channel.  Subsequent messages will be sent to that channel.

Use the syntax `@ChannelName with a message` to send a single message to a channel.  The channel will not be saved, and subsequent messages will be sent to the previously saved channel.

To send an additional message to or switch to a previously-used but unsaved channel, use `@@ with a message` or `@@`, respectively.

Click on the channel tags as they appear in the chat window to fill in the necessary

### Example

Typing these lines into chat:
> @ItsNickBarry  
> Hello.  
> Lovely weather we're having.  
> @Nashor Hey, Nashor.  
> @50 Everyone nearby plz I need food.  
> How's the weather over there?  
> @@ Help, I'm about to starve.  
> Looks like rain.  
> @@  
> I'm beginning to starve.  
> I'm at half health.  
> I'm at one heart.  
> Thanks, guys.  
> Really, you're great.  

Will yield this result:
> You joined the game  
> Now chatting *@ItsNickBarry*  
> __[You] *@ItsNickBarry*__ Hello.  
> __[You] *@ItsNickBarry*__ Lovely weather we're having.  
> __[You] *@Nashor*__ Hey, Nashor.  
> __[You] *@50*__ Everyone nearby plz I need food.  
> __[You] *@ItsNickBarry*__ How's the weather over there?  
> __[You] *@50*__ Help, I'm about to starve.  
> __[You] *@ItsNickBarry*__ Looks like rain.  
> Now chatting *@50*  
> __[You] *@50*__ I'm beginning to starve.  
> __[You] *@50*__ I'm at half health.  
> __[ItsNickBarry] *@You*__ Look, I don't want to talk about the weather, all right?  
> __[You] *@50*__ I'm at one heart.  
> __[You] *@50*__ Thanks, guys.  
> __[You] *@50*__ Really, you're great.  
> You starved to death.  

## Configuration

A configuration file will be added eventually.

## Plugins

AtChat supports the creation of custom channels as separate plugins.  Clone or fork the [template][template] to create your own.  Use the in-line comments as a guide.

[template]: https://github.com/TapestryMinecraft/at-chat-plugin-template

### Available Plugins

Chat @ sms, to send a text message while in-game.
[AtChatSMS][sms]

[sms]: https://github.com/TapestryMinecraft/at-chat-sms
