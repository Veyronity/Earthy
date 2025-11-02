package au.lupine.earthy.fabric.module;

import au.lupine.earthy.fabric.EarthyFabric;
import au.lupine.earthy.fabric.object.base.Module;
import au.lupine.earthy.fabric.object.config.Config;
import au.lupine.earthy.fabric.object.wrapper.HUDType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import org.slf4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class AutoHUD extends Module {
    private static AutoHUD instance;
    private static final Logger logger = EarthyFabric.getLogger();

    private AutoHUD() {}

    public static AutoHUD getInstance() {
        if (instance == null) instance = new AutoHUD();
        return instance;
    }

    @Override
    public void enable() {
        Session session = Session.getInstance();

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

            scheduler.schedule(() -> {
                if (!session.isPlayerOnEarthMC()) {
                    logger.warn("[AutoHUD] Player not on EMC!");
                    return;
                }

                HUDType hud = Config.autoHUD;
                if (hud.equals(HUDType.NONE)) {
                    logger.warn("[AutoHUD] HUD is Nonee");
                    return;
                }

                handler.sendCommand(hud.getCommand());
                logger.info("[AutoHUD] Sent the command");
            }, 6L, TimeUnit.SECONDS);
        });
    }
}
