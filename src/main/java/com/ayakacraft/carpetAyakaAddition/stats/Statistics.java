package com.ayakacraft.carpetAyakaAddition.stats;

import com.ayakacraft.carpetAyakaAddition.CarpetAyakaAddition;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.util.Identifier;

import static net.minecraft.stat.Stats.CUSTOM;

public class Statistics {

    public static final Identifier MINED_ALL = CarpetAyakaAddition.identifier("mined_all");
    public static final Identifier USED_ALL = CarpetAyakaAddition.identifier("used_all");

    private static void register(Identifier id, StatFormatter formatter) {
        Registry.register(Registries.CUSTOM_STAT, id, id);
        CUSTOM.getOrCreateStat(id, formatter);
    }

    public static void registerAll() {
        register(MINED_ALL, StatFormatter.DEFAULT);
        register(USED_ALL, StatFormatter.DEFAULT);
    }

}
