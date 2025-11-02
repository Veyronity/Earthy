package au.lupine.earthy.fabric.mixin;

import au.lupine.earthy.fabric.EarthyFabric;
import au.lupine.earthy.fabric.module.ChatPreview;
import au.lupine.earthy.fabric.module.Session;
import au.lupine.earthy.fabric.object.config.Config;
import au.lupine.earthy.fabric.object.wrapper.ChatChannel;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EditBox.class)
public abstract class EditBoxMixin extends AbstractWidget {
    @Shadow @Final private Font font;
    @Shadow private @Nullable String suggestion;
    @Shadow private String value;
    @Shadow private int textY;
    @Unique
    private static final Logger logger = EarthyFabric.getLogger();

    public EditBoxMixin(int i, int j, int k, int l, Component component) {
        super(i, j, k, l, component);
    }

    @Inject(
            method = "renderWidget",
            at = @At("TAIL")
    )
    private void inject(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci, @Local(ordinal = 6) int o) {
        try {
            Session session = Session.getInstance();
            if (!session.isPlayerOnEarthMC()) return;
            if (!Config.previewCurrentChatChannel) return;
            if (!getMessage().equals(Component.translatable("chat.editBox"))) return;

            if (this.suggestion != null || !this.value.isEmpty()) return;

            ChatChannel current = ChatPreview.getInstance().getCurrentChatChannel();
            if (current == null) return;

            boolean inParty = ChatPreview.getInstance().isInPartyChat();
            String label = inParty ? current.getName() + " (party)" : current.getName();

            try {
                int x = this.getX();
                int y = this.getY();
                int color = current.getColour();
                guiGraphics.drawString(this.font, label, x, y, color, false);
            } catch (Exception e) {
                logger.warn("[EditBoxMixin] Exception drawing string: {}", e.getMessage());
            }
        } catch (Exception e) {
            logger.warn("[EditBoxMixin] Exception in inject: {}", e.getMessage());
        }
    }
}
