package com.latencyviewer.mixin;

import com.latencyviewer.util.LatencyColor;
import com.latencyviewer.util.ModLogger;
import com.latencyviewer.util.PingStatistics;
import com.latencyviewer.util.ProfileHelper;
import java.util.UUID;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerListHud.class)
public class PlayerListHudMixin {

    @Unique
    private static boolean logged = false;

    @Unique
    private static final int EXTRA_WIDTH = 140;

    @ModifyConstant(method = "render", constant = @Constant(intValue = 13))
    private int modifySlotWidth(int original) {
        return original + EXTRA_WIDTH;
    }

    @Inject(method = "renderLatencyIcon", at = @At("HEAD"), cancellable = true)
    private void renderLatencyText(DrawContext context, int width, int x, int y, PlayerListEntry entry, CallbackInfo ci) {
        if (!logged) {
            ModLogger.getInstance().info("Mixin applied successfully!");
            logged = true;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.textRenderer == null) {
            ci.cancel();
            return;
        }

        int latency = entry.getLatency();

        UUID playerId = ProfileHelper.getUUID(entry.getProfile());
        if (playerId == null) {
            ci.cancel();
            return;
        }

        PingStatistics.recordPing(playerId, latency);

        PingStatistics.Stats stats = PingStatistics.getStats(playerId);

        String currentPing = LatencyColor.formatLatency(latency);
        String avgPing = LatencyColor.formatLatency(stats.average);
        String p50Ping = LatencyColor.formatLatency(stats.p50);
        String p99Ping = LatencyColor.formatLatency(stats.p99);

        int rightEdge = x + width - 2;

        int spacing = 35;

        int p99Color = 0xFFFF6666;
        int p99Width = client.textRenderer.getWidth(p99Ping);
        int p99X = rightEdge - p99Width;
        context.drawTextWithShadow(client.textRenderer, p99Ping, p99X, y, p99Color);

        int p50Color = 0xFFFFAA00;
        int p50Width = client.textRenderer.getWidth(p50Ping);
        int p50X = rightEdge - spacing - p50Width;
        context.drawTextWithShadow(client.textRenderer, p50Ping, p50X, y, p50Color);

        int avgColor = 0xFF00AAFF;
        int avgWidth = client.textRenderer.getWidth(avgPing);
        int avgX = rightEdge - spacing * 2 - avgWidth;
        context.drawTextWithShadow(client.textRenderer, avgPing, avgX, y, avgColor);

        int currentColor = LatencyColor.getColor(latency);
        int currentWidth = client.textRenderer.getWidth(currentPing);
        int currentX = rightEdge - spacing * 3 - currentWidth;
        context.drawTextWithShadow(client.textRenderer, currentPing, currentX, y, currentColor);

        ci.cancel();
    }
}
