package com.latencyviewer;

import com.latencyviewer.util.ModLogger;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class LatencyViewerMod implements ClientModInitializer {
    public static final String MOD_ID = "latency-viewer";
    public static final String MOD_NAME = "Fabric Latency Viewer";

    private static LatencyViewerMod instance;

    public LatencyViewerMod() {
        instance = this;
    }

    @Override
    public void onInitializeClient() {
        ModLogger.getInstance().info("Mod initialized! (v{})", getVersion());
    }

    public static LatencyViewerMod getInstance() {
        return instance;
    }

    public static String getVersion() {
        return FabricLoader.getInstance()
                .getModContainer(MOD_ID)
                .map(container -> container.getMetadata().getVersion().getFriendlyString())
                .orElse("unknown");
    }
}
