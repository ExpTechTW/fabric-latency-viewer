package com.latencyviewer.util;

public class LatencyColor {
    // 預設顏色閾值
    private static final int THRESHOLD_EXCELLENT = 50;
    private static final int THRESHOLD_GOOD = 100;
    private static final int THRESHOLD_FAIR = 150;
    private static final int THRESHOLD_POOR = 300;

    // 顏色值 (ARGB)
    private static final int COLOR_UNKNOWN = 0xFF555555;  // Gray
    private static final int COLOR_EXCELLENT = 0xFF00FF00; // Green
    private static final int COLOR_GOOD = 0xFF99FF00;      // Yellow-Green
    private static final int COLOR_FAIR = 0xFFFFFF00;      // Yellow
    private static final int COLOR_POOR = 0xFFFF9900;      // Orange
    private static final int COLOR_BAD = 0xFFFF0000;       // Red

    private LatencyColor() {
        // Private constructor to prevent instantiation
    }

    public static int getColor(int latency) {
        if (latency < 0) {
            return COLOR_UNKNOWN;
        } else if (latency < THRESHOLD_EXCELLENT) {
            return COLOR_EXCELLENT;
        } else if (latency < THRESHOLD_GOOD) {
            return COLOR_GOOD;
        } else if (latency < THRESHOLD_FAIR) {
            return COLOR_FAIR;
        } else if (latency < THRESHOLD_POOR) {
            return COLOR_POOR;
        } else {
            return COLOR_BAD;
        }
    }

    public static String formatLatency(int latency) {
        return latency < 0 ? "?" : String.valueOf(latency);
    }
}
