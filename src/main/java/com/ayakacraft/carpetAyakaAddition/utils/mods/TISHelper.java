package com.ayakacraft.carpetayakaaddition.utils.mods;

import carpettisaddition.helpers.rule.opPlayerNoCheat.OpPlayerNoCheatHelper;
import net.minecraft.server.command.ServerCommandSource;

public final class TISHelper {

    public static boolean cannotCheat(ServerCommandSource source) {
        return !OpPlayerNoCheatHelper.canCheat(source);
    }

}
