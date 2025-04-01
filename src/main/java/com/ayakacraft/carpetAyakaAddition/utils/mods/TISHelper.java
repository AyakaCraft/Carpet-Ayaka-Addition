package com.ayakacraft.carpetayakaaddition.utils.mods;

import carpettisaddition.helpers.rule.opPlayerNoCheat.OpPlayerNoCheatHelper;
import net.minecraft.server.command.ServerCommandSource;

public final class TISHelper {

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean canCheat(ServerCommandSource source) {
        return OpPlayerNoCheatHelper.canCheat(source);
    }

}
