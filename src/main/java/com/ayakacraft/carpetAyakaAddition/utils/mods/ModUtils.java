package com.ayakacraft.carpetayakaaddition.utils.mods;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.Optional;

public final class ModUtils {

    public static final FabricLoader LOADER = FabricLoader.getInstance();

    public static final String TIS_ID = "carpet-tis-addition";

    public static boolean isModLoaded(String modId) {
        return LOADER.isModLoaded(modId);
    }

    public static Optional<ModContainer> getModContainer(String modId) {
        return LOADER.getModContainer(modId);
    }

    public static boolean isTISLoaded() {
        return isModLoaded(TIS_ID);
    }

}
