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

package com.ayakacraft.carpetayakaaddition.mixin.rules.fakePlayerForceOffline;

import carpet.CarpetSettings;
import carpet.patches.EntityPlayerMPFake;
import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.players.CachedUserNameToIdResolver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

//Do not remove the lines below
//TODO update in 1.16.5
@Mixin(value = EntityPlayerMPFake.class, remap = false)
public class EntityPlayerMPFakeMixin {

    //#if MC>=12109
    @WrapOperation(
            method = "createFake",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/OldUsersConverter;convertMobOwnerIfNecessary(Lnet/minecraft/server/MinecraftServer;Ljava/lang/String;)Ljava/util/UUID;",
                    remap = true
            )
    )
    private static java.util.UUID createFake(net.minecraft.server.MinecraftServer minecraftServer, String s, Operation<java.util.UUID> original) {
        if (CarpetAyakaSettings.fakePlayerForceOffline && CarpetSettings.allowSpawningOfflinePlayers) {
            return net.minecraft.core.UUIDUtil.createOfflinePlayerUUID(s);
        } else {
            return original.call(minecraftServer, s);
        }
    }
    //#else
    //$$ @WrapOperation(
    //$$         method = "createFake",
    //$$         at = @At(
    //$$                 value = "INVOKE",
    //$$                 target = "Lnet/minecraft/server/players/GameProfileCache;get(Ljava/lang/String;)Ljava/util/Optional;",
    //$$                 remap = true
    //$$         )
    //$$ )
    //$$ private static Optional<GameProfile> createFake(GameProfileCache instance, String name, Operation<Optional<GameProfile>> original) {
    //$$     if (CarpetAyakaSettings.fakePlayerForceOffline && CarpetSettings.allowSpawningOfflinePlayers) {
    //$$         return Optional.of(
                    //#if MC>=11900
                    //$$ new GameProfile(net.minecraft.core.UUIDUtil.createOfflinePlayerUUID(name), name)
                    //#else
                    //$$ new GameProfile(net.minecraft.world.entity.player.Player.createPlayerUUID(name), name)
                    //#endif
    //$$         );
    //$$     } else {
    //$$         return original.call(instance, name);
    //$$     }
    //$$ }
    //#endif

}
