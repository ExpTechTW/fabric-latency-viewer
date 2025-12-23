package com.latencyviewer.util;

import com.mojang.authlib.GameProfile;

import java.lang.reflect.Method;
import java.util.UUID;

public class ProfileHelper {
    private static Method uuidMethod;
    private static boolean initialized = false;

    private ProfileHelper() {
    }

    public static UUID getUUID(GameProfile profile) {
        if (profile == null) {
            return null;
        }

        if (!initialized) {
            initialize(profile);
        }

        if (uuidMethod != null) {
            try {
                return (UUID) uuidMethod.invoke(profile);
            } catch (Exception e) {
            }
        }

        return tryGetUUIDViaReflection(profile);
    }

    private static UUID tryGetUUIDViaReflection(GameProfile profile) {
        try {
            Method m = profile.getClass().getMethod("id");
            return (UUID) m.invoke(profile);
        } catch (Exception ignored) {
        }

        try {
            Method m = profile.getClass().getMethod("getId");
            return (UUID) m.invoke(profile);
        } catch (Exception ignored) {
        }

        return null;
    }

    private static synchronized void initialize(GameProfile profile) {
        if (initialized) return;

        try {
            uuidMethod = profile.getClass().getMethod("id");
            initialized = true;
            return;
        } catch (NoSuchMethodException ignored) {
        }

        try {
            uuidMethod = profile.getClass().getMethod("getId");
            initialized = true;
        } catch (NoSuchMethodException ignored) {
            initialized = true;
        }
    }
}
