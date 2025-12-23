package com.latencyviewer;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LatencyViewerMod implements ClientModInitializer {
    public static final String MOD_ID = "latency-viewer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Latency Viewer mod initialized!");
    }
}
