package com.latencyviewer.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PingStatistics {
    private static final int HISTORY_DURATION_MS = 60_000;
    private static final Map<UUID, PingHistory> playerPingHistories = new ConcurrentHashMap<>();

    private PingStatistics() {
    }

    public static void recordPing(UUID playerId, int ping) {
        if (ping < 0) return;

        playerPingHistories
            .computeIfAbsent(playerId, k -> new PingHistory())
            .addPing(ping);
    }

    public static Stats getStats(UUID playerId) {
        PingHistory history = playerPingHistories.get(playerId);
        if (history == null) {
            return new Stats(-1, -1, -1);
        }
        return history.calculateStats();
    }

    public static void removePlayer(UUID playerId) {
        playerPingHistories.remove(playerId);
    }

    public static void clear() {
        playerPingHistories.clear();
    }

    public static class Stats {
        public final int average;
        public final int p50;
        public final int p99;

        public Stats(int average, int p50, int p99) {
            this.average = average;
            this.p50 = p50;
            this.p99 = p99;
        }
    }

    private static class PingHistory {
        private final List<PingEntry> entries = new ArrayList<>();

        public synchronized void addPing(int ping) {
            long now = System.currentTimeMillis();
            entries.add(new PingEntry(now, ping));

            entries.removeIf(e -> now - e.timestamp > HISTORY_DURATION_MS);
        }

        public synchronized Stats calculateStats() {
            long now = System.currentTimeMillis();

            List<Integer> recentPings = new ArrayList<>();
            for (PingEntry entry : entries) {
                if (now - entry.timestamp <= HISTORY_DURATION_MS) {
                    recentPings.add(entry.ping);
                }
            }

            if (recentPings.isEmpty()) {
                return new Stats(-1, -1, -1);
            }

            int sum = 0;
            for (int ping : recentPings) {
                sum += ping;
            }
            int average = sum / recentPings.size();

            Collections.sort(recentPings);
            int p50 = getPercentile(recentPings, 50);
            int p99 = getPercentile(recentPings, 99);

            return new Stats(average, p50, p99);
        }

        private int getPercentile(List<Integer> sortedList, int percentile) {
            if (sortedList.isEmpty()) return -1;
            int index = (int) Math.ceil(percentile / 100.0 * sortedList.size()) - 1;
            index = Math.max(0, Math.min(index, sortedList.size() - 1));
            return sortedList.get(index);
        }
    }

    private static class PingEntry {
        final long timestamp;
        final int ping;

        PingEntry(long timestamp, int ping) {
            this.timestamp = timestamp;
            this.ping = ping;
        }
    }
}
