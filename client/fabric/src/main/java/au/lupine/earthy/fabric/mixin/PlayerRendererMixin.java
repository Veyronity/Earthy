package au.lupine.earthy.fabric.mixin;

import au.lupine.earthy.fabric.module.Cache;
import au.lupine.earthy.fabric.module.Session;
import au.lupine.earthy.fabric.object.config.Config;
import au.lupine.emcapiclient.object.apiobject.Player;
import com.mojang.blaze3d.vertex.PoseStack;
import net.kyori.adventure.platform.modcommon.MinecraftClientAudiences;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, AvatarRenderState, PlayerModel> {

    public PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(
            method = "submitNameTag(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V",
            at = @At("HEAD")
    )
    private void inject(AvatarRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera, CallbackInfo ci) {
        Session session = Session.getInstance();
        if (!session.isPlayerOnEarthMC()) return;
        if (!Config.showAffiliationAboveHead) return;
        if (state.nameTag == null) return; // This Mixin is fired in inventory screen too

        String[] split = state.nameTag.getString().split(" ");
        if (split.length == 0) return;
        String name = split[split.length - 1];

        Player player = Cache.getPlayer(name);
        if (player == null) return;

        Component towny = MinecraftClientAudiences.of().asNative(createTownyComponent(player));

        poseStack.pushPose();

        poseStack.scale(0.75F, 0.75F, 0.75F);
        poseStack.translate(0.0D, 0.6D, 0.0D);

        collector.submitNameTag(
                poseStack,
                state.nameTagAttachment,
                0,
                towny,
                !state.isDiscrete,
                state.lightCoords,
                state.distanceToCameraSq,
                camera
        );

        poseStack.popPose();
        poseStack.translate(0D, 0.1225D, 0D);
    }

    @Unique
    private net.kyori.adventure.text.Component createTownyComponent(Player player) {
        if (!player.hasTown()) return net.kyori.adventure.text.Component.translatable("msg.earthy.nomad").color(NamedTextColor.DARK_AQUA);

        TextComponent.Builder builder = net.kyori.adventure.text.Component.text();

        if (player.isMayor()) {
            NamedTextColor colour = player.isKing() ? NamedTextColor.GOLD : NamedTextColor.DARK_AQUA;
            builder.append(net.kyori.adventure.text.Component.text("\uD83D\uDC51", colour));
            builder.append(net.kyori.adventure.text.Component.space());
        }

        builder.append(net.kyori.adventure.text.Component.text("[", NamedTextColor.GRAY));

        if (player.hasNation()) {
            builder.append(net.kyori.adventure.text.Component.text(player.getNation().getName(), NamedTextColor.GOLD));
            builder.append(net.kyori.adventure.text.Component.text("|", NamedTextColor.GRAY));
        }

        builder.append(net.kyori.adventure.text.Component.text(player.getTown().getName(), NamedTextColor.DARK_AQUA));
        builder.append(net.kyori.adventure.text.Component.text("]", NamedTextColor.GRAY));

        return builder.build();
    }
}