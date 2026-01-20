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

import carpet.patches.EntityPlayerMPFake;
import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//#if MC<12109
//$$ import com.mojang.authlib.GameProfile;
//$$ import net.minecraft.server.players.GameProfileCache;
//$$ import java.util.Optional;
//#endif

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
        if (CarpetAyakaSettings.fakePlayerForceOffline) {
            return null;
        } else {
            return original.call(minecraftServer, s);
        }
    }
    //#elseif MC>=11700
    //$$ @WrapOperation(
    //$$         method = "createFake",
    //$$         at = @At(
    //$$                 value = "INVOKE",
    //$$                 target = "Lnet/minecraft/server/players/GameProfileCache;get(Ljava/lang/String;)Ljava/util/Optional;",
    //$$                 remap = true
    //$$         )
    //$$ )
    //$$ private static Optional<GameProfile> createFake(GameProfileCache instance, String name, Operation<Optional<GameProfile>> original) {
    //$$     if (CarpetAyakaSettings.fakePlayerForceOffline) {
    //$$         return Optional.empty();
    //$$     } else {
    //$$         return original.call(instance, name);
    //$$     }
    //$$ }
    //#else
    //$$ @WrapOperation(
    //$$         method = "createFake",
    //$$         at = @At(
    //$$                 value = "INVOKE",
    //$$                 target = "Lnet/minecraft/server/players/GameProfileCache;get(Ljava/lang/String;)Lcom/mojang/authlib/GameProfile;",
    //$$                 remap = true
    //$$         )
    //$$ )
    //$$ private static GameProfile createFake(GameProfileCache instance, String name, Operation<GameProfile> original) {
    //$$     if (CarpetAyakaSettings.fakePlayerForceOffline) {
    //#if MC>=11600
    //$$ return null;
    //#else
    //$$ return new GameProfile(net.minecraft.world.entity.player.Player.createPlayerUUID(name), name);
    //#endif
    //$$     } else {
    //$$         return original.call(instance, name);
    //$$     }
    //$$ }
    //#endif

}
