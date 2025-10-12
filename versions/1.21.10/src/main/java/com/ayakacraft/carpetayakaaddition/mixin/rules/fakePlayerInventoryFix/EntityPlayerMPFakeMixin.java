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

package com.ayakacraft.carpetayakaaddition.mixin.rules.fakePlayerInventoryFix;

import carpet.patches.EntityPlayerMPFake;
import com.ayakacraft.carpetayakaaddition.utils.ModUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.NbtReadView;
import net.minecraft.util.ErrorReporter;
import net.minecraft.util.NameToIdCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Restriction(require = @Condition(value = ModUtils.MC_ID, versionPredicates = ">=1.21.9"))  // Hopefully this will be fixed by carpet in mc1.21.11
@Mixin(value = EntityPlayerMPFake.class, remap = false)
public class EntityPlayerMPFakeMixin {

    @WrapOperation(
            method = "createFake",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/NameToIdCache;setOfflineMode(Z)V",
                    ordinal = 1,
                    remap = true
            )
    )
    private static void reverseOffline(NameToIdCache instance, boolean b, Operation<Void> original) {
        original.call(instance, !b);
    }

    @WrapOperation(
            method = "lambda$createFake$2",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/PlayerManager;onPlayerConnect(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/server/network/ConnectedClientData;)V",
                    remap = true
            )
    )
    private static void loadPlayerData(PlayerManager instance, ClientConnection clientConnection, ServerPlayerEntity serverPlayerEntity, ConnectedClientData connectedClientData, Operation<Void> original) {
        Optional<NbtCompound> o = instance.loadPlayerData(serverPlayerEntity.getPlayerConfigEntry());
        o.ifPresent(nbtCompound -> serverPlayerEntity.readData(NbtReadView.create(ErrorReporter.EMPTY, serverPlayerEntity.getEntityWorld().getRegistryManager(), nbtCompound)));
        original.call(instance, clientConnection, serverPlayerEntity, connectedClientData);
    }

}
