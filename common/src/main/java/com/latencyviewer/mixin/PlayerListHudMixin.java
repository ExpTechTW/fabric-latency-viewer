package com.latencyviewer.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {

    @Inject(method = "renderLatencyIcon", at = @At("HEAD"), cancellable = true)
    private void renderLatencyText(DrawContext context, int width, int x, int y, PlayerListEntry entry, CallbackInfo ci) {
        int latency = entry.getLatency();

        int color = getLatencyColor(latency);
        String pingText = latency < 0 ? "?" : String.valueOf(latency);

        MinecraftClient client = MinecraftClient.getInstance();
        int textWidth = client.textRenderer.getWidth(pingText);
        int textX = x + width - textWidth - 1;

        context.drawTextWithShadow(client.textRenderer, pingText, textX, y, color);

        ci.cancel();
    }

    private static int getLatencyColor(int latency) {
        if (latency < 0) {
            return 0x555555; // Gray for unknown
        } else if (latency < 50) {
            return 0x00FF00; // Green
        } else if (latency < 100) {
            return 0x99FF00; // Yellow-Green
        } else if (latency < 150) {
            return 0xFFFF00; // Yellow
        } else if (latency < 300) {
            return 0xFF9900; // Orange
        } else {
            return 0xFF0000; // Red
        }
    }
}
