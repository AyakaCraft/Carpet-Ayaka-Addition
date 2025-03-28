package com.ayakacraft.carpetayakaaddition;

import carpet.CarpetServer;
import com.ayakacraft.carpetayakaaddition.utils.mods.ModUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.ModContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public final class CarpetAyakaAddition implements ModInitializer {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().create();

    public static final String MOD_ID = "carpet-ayaka-addition";

    public static final String MOD_NAME = "Carpet Ayaka Addition";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static final String MOD_VERSION;

    static {
        final Optional<ModContainer> o = ModUtils.getModContainer(MOD_ID);
        MOD_VERSION = o.isPresent() ? o.get().getMetadata().getVersion().toString() : "dev";
    }

    @Override
    public void onInitialize() {
        CarpetAyakaAddition.LOGGER.info("Initializing {} Version {}", CarpetAyakaAddition.MOD_NAME, CarpetAyakaAddition.MOD_VERSION);
        CarpetServer.manageExtension(CarpetAyakaServer.INSTANCE);
    }

}
