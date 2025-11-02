package au.lupine.earthy.fabric.object.wrapper;

import java.util.Set;

public class ChatChannel {

    private final String name;
    private final int colour;

    public static final ChatChannel GLOBAL = new ChatChannel("global", 0xffaaaaaa);
    public static final ChatChannel TOWN = new ChatChannel("town", 0xFF55ffff);
    public static final ChatChannel NATION = new ChatChannel("nation", 0xFFffff55);
    public static final ChatChannel LOCAL = new ChatChannel("local", 0xFF5BEA72);
    public static final ChatChannel STAFF = new ChatChannel("staff", 0xFFA80000);
    public static final ChatChannel TRADE = new ChatChannel("trade", 0xFF55ffff);
    public static final ChatChannel PREMIUM = new ChatChannel("premium", 0xFFFC54FC);

    public static final ChatChannel PORTUGUESE = new ChatChannel("portuguese", 0xFF54FC54);
    public static final ChatChannel TURKISH = new ChatChannel("turkish", 0xFF54FC54);
    public static final ChatChannel SWEDISH = new ChatChannel("swedish", 0xFF54FC54);
    public static final ChatChannel GERMAN = new ChatChannel("german", 0xFF54FC54);
    public static final ChatChannel UKRAINIAN = new ChatChannel("ukrainian", 0xFF54FC54);
    public static final ChatChannel CHINESE = new ChatChannel("chinese", 0xFF54FC54);
    public static final ChatChannel FRENCH = new ChatChannel("french", 0xFF54FC54);
    public static final ChatChannel POLISH = new ChatChannel("polish", 0xFF54FC54);
    public static final ChatChannel RUSSIAN = new ChatChannel("russian", 0xFF54FC54);
    public static final ChatChannel SPANISH = new ChatChannel("spanish", 0xFF54FC54);
    public static final ChatChannel DUTCH = new ChatChannel("dutch", 0xFF54FC54);
    public static final ChatChannel JAPANESE = new ChatChannel("japanese", 0xFF54FC54);

    public static final Set<ChatChannel> CHANNELS = Set.of(
            GLOBAL, TOWN, NATION, LOCAL,
            STAFF, TRADE, PREMIUM, PORTUGUESE,
            TURKISH, SWEDISH, GERMAN, UKRAINIAN,
            CHINESE, FRENCH, POLISH, RUSSIAN,
            SPANISH, DUTCH, JAPANESE
    );

    public ChatChannel(String name, int colour) {
        this.name = name;
        this.colour = colour;
    }

    public static ChatChannel getOrDefault(String name) {
        for (ChatChannel channel : CHANNELS) {
            if (channel.getName().equals(name)) return channel;
        }

        return new ChatChannel(name, 0xaaaaaa);
    }

    public String getName() {
        return name;
    }

    public int getColour() {
        return colour;
    }
}
