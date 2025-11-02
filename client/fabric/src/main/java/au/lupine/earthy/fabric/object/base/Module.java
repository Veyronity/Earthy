package au.lupine.earthy.fabric.object.base;

import au.lupine.earthy.fabric.EarthyFabric;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import org.slf4j.Logger;

public abstract class Module {
    private static final Logger logger = EarthyFabric.getLogger();

    public Module() {
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> disable());
    }

    public void launch() {
        try {
            enable();
        } catch (Exception e) {
            logger.warn("Exception while enabling {}", this.getClass().getSimpleName());
        }
        logger.info("Enabled Module {}", this.getClass().getSimpleName());
    }

    public void enable() {}

    public void disable() {}
}
