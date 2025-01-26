package com.ayakacraft.carpetAyakaAddition.stats;

import com.ayakacraft.carpetAyakaAddition.CarpetAyakaAddition;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.util.Identifier;

import static net.minecraft.stat.Stats.CUSTOM;

public class CarpetAyakaAdditionStats {
    public static final Identifier MINED_ALL = register("mined_all", StatFormatter.DEFAULT);
//    public static final Identifier USED_ALL = register("used_all", StatFormatter.DEFAULT);

    public static void register() {

    }

    private static Identifier register(String id, StatFormatter formatter) {
        Identifier identifier = CarpetAyakaAddition.of(id);
        Registry.register(Registries.CUSTOM_STAT, identifier, identifier);
        CUSTOM.getOrCreateStat(identifier, formatter);
        return identifier;
    }
}
