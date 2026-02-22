package au.lupine.earthy.fabric.module;

import au.lupine.earthy.fabric.EarthyFabric;
import au.lupine.earthy.fabric.object.base.Module;
import au.lupine.earthy.fabric.object.base.Tickable;
import au.lupine.emcapiclient.object.apiobject.Player;
import au.lupine.emcapiclient.object.apiobject.ServerInfo;
import au.lupine.emcapiclient.object.apiobject.Town;
import au.lupine.emcapiclient.object.exception.FailedRequestException;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public final class Cache extends Module {
    private static Cache instance;
    private static final Map<String, Player> CACHED_PLAYERS = new ConcurrentHashMap<>();
    private static final Logger logger = EarthyFabric.getLogger();

    private Cache() {}

    public static Cache getInstance() {
        if (instance == null) instance = new Cache();
        return instance;
    }

    @Override
    public void enable() {
        Session session = Session.getInstance();

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

            scheduler.schedule(() -> {
                if (!session.isPlayerOnEarthMC()) {
                    logger.warn("[Cache] [Scheduler] Player not EMC!");
                    return;
                }
                updateCachedPlayers();
            }, 6L, TimeUnit.SECONDS);
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            CACHED_PLAYERS.clear();
        });

        Tickable.register(() -> {
            if (!session.isPlayerOnEarthMC()) {
                logger.warn("[Cache] [Tickable] Player not EMC!");
                return;
            }

            updateCachedPlayers();
        }, 3L, TimeUnit.MINUTES);
    }

    private void updateCachedPlayers() {
        ClientPacketListener cpl = Minecraft.getInstance().getConnection();
        if (cpl == null) return;

        CompletableFuture.runAsync(() -> {
            try {
                List<Player> online = EarthyFabric.getAPI().getPlayersByUUIDs(cpl.getOnlinePlayers()
                        .stream()
                        .map(player -> player.getProfile().id())
                        .toList()
                );

                CACHED_PLAYERS.clear();
                for (Player player : online) {
                    CACHED_PLAYERS.put(player.getName(), player);
                }
            } catch (FailedRequestException e) {
                logger.warn("[Cache] FailedRequestException: {}", e.getMessage());
            }
        });
    }

    public static Collection<Player> getPlayers() {
        return CACHED_PLAYERS.values();
    }

    public static Player getPlayer(String name) {
        return CACHED_PLAYERS.get(name);
    }

    public static void addPlayer(String name, Player player) {
        CACHED_PLAYERS.put(name, player);
    }
}
