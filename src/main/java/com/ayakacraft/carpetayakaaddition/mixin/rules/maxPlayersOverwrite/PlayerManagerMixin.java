/*
 * This file is part of the Carpet Ayaka Addition project, licensed under the
 * GNU General Public License v3.0
 *
 * Copyright (C) 2025  Calboot and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.ayakacraft.carpetayakaaddition.mixin.rules.maxPlayersOverwrite;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

    @Unique
    private int initialMaxPlayers;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(
            MinecraftServer server
            //#if MC>=11900
            , net.minecraft.registry.CombinedDynamicRegistries<net.minecraft.registry.ServerDynamicRegistryType> registryManager, net.minecraft.world.WorldSaveHandler saveHandler
            //#elseif MC>=11800
            //$$ , net.minecraft.util.registry.DynamicRegistryManager.Immutable registryManager, net.minecraft.world.WorldSaveHandler saveHandler
            //#elseif MC>=11600
            //$$ , net.minecraft.util.registry.DynamicRegistryManager.Impl registryManager, net.minecraft.world.WorldSaveHandler saveHandler
            //#endif
            , int maxPlayers, CallbackInfo ci) {
        initialMaxPlayers = maxPlayers;
    }

    @Redirect(method = {"checkCanJoin", "getMaxPlayerCount"}, at = @At(value = "FIELD", target = "Lnet/minecraft/server/PlayerManager;maxPlayers:I", opcode = Opcodes.GETFIELD))
    private int onGetMaxPlayers(PlayerManager instance) {
        if (CarpetAyakaSettings.maxPlayersOverwrite == 0) {
            return initialMaxPlayers;
        }
        return CarpetAyakaSettings.maxPlayersOverwrite;
    }

}
