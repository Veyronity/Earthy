package au.lupine.earthy.fabric.module;

import au.lupine.earthy.fabric.object.base.Module;
import au.lupine.earthy.fabric.object.base.Tickable;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ServerData;

public final class Session extends Module {

    private static Session instance;

    private Session() {}

    public static Session getInstance() {
        if (instance == null) instance = new Session();
        return instance;
    }

    @Override
    public void enable() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> Tickable.tick());
    }

    public boolean isPlayerOnEarthMC() {
        ClientPacketListener cpl = Minecraft.getInstance().getConnection();
        if (cpl == null) return false;

        ServerData server = cpl.getServerData();
        if (server == null) return false;
        String ip = server.ip.toLowerCase();

        return ip.endsWith("earthmc.net");
    }
}
