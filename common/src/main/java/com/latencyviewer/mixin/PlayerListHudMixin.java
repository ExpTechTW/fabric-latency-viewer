package com.latencyviewer.mixin;

import com.latencyviewer.util.LatencyColor;
import com.latencyviewer.util.ModLogger;
import com.latencyviewer.util.PingStatistics;
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

    // 擴展額外空間給 4 個 ping 數字 (每個約 35-40 像素)
    @Unique
    private static final int EXTRA_WIDTH = 140;

    // 擴展玩家列表槽位寬度
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

        // 記錄 ping 到統計系統
        PingStatistics.recordPing(entry.getProfile().id(), latency);

        // 取得統計數據
        PingStatistics.Stats stats = PingStatistics.getStats(entry.getProfile().id());

        // 格式: {最後 ping} {平均} {P50} {P99}
        String currentPing = LatencyColor.formatLatency(latency);
        String avgPing = LatencyColor.formatLatency(stats.average);
        String p50Ping = LatencyColor.formatLatency(stats.p50);
        String p99Ping = LatencyColor.formatLatency(stats.p99);

        int rightEdge = x + width - 2;

        // 從右到左繪製：P99 -> P50 -> Avg -> Current
        // 使用固定寬度間距，讓數字對齊
        int spacing = 35;

        // P99 (最右邊) - 紅色調
        int p99Color = 0xFFFF6666;
        int p99Width = client.textRenderer.getWidth(p99Ping);
        int p99X = rightEdge - p99Width;
        context.drawTextWithShadow(client.textRenderer, p99Ping, p99X, y, p99Color);

        // P50
        int p50Color = 0xFFFFAA00;
        int p50Width = client.textRenderer.getWidth(p50Ping);
        int p50X = rightEdge - spacing - p50Width;
        context.drawTextWithShadow(client.textRenderer, p50Ping, p50X, y, p50Color);

        // 平均
        int avgColor = 0xFF00AAFF;
        int avgWidth = client.textRenderer.getWidth(avgPing);
        int avgX = rightEdge - spacing * 2 - avgWidth;
        context.drawTextWithShadow(client.textRenderer, avgPing, avgX, y, avgColor);

        // 當前 ping (使用顏色編碼)
        int currentColor = LatencyColor.getColor(latency);
        int currentWidth = client.textRenderer.getWidth(currentPing);
        int currentX = rightEdge - spacing * 3 - currentWidth;
        context.drawTextWithShadow(client.textRenderer, currentPing, currentX, y, currentColor);

        ci.cancel();
    }
}
