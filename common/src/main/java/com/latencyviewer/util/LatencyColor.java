package com.latencyviewer.util;

public class LatencyColor {
    private static final int THRESHOLD_EXCELLENT = 50;
    private static final int THRESHOLD_GOOD = 100;
    private static final int THRESHOLD_FAIR = 150;
    private static final int THRESHOLD_POOR = 300;

    private static final int COLOR_UNKNOWN = 0xFF555555;
    private static final int COLOR_EXCELLENT = 0xFF00FF00;
    private static final int COLOR_GOOD = 0xFF99FF00;
    private static final int COLOR_FAIR = 0xFFFFFF00;
    private static final int COLOR_POOR = 0xFFFF9900;
    private static final int COLOR_BAD = 0xFFFF0000;

    private LatencyColor() {
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
