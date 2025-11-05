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

package com.ayakacraft.carpetayakaaddition.mixin.rules.fakePlayerDataFix;

import carpet.patches.EntityPlayerMPFake;
import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.helpers.rules.FakePlayerDataFixHelper;
import com.ayakacraft.carpetayakaaddition.utils.ModUtils;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(require = @Condition(value = ModUtils.CPT_ID, versionPredicates = ">=1.4.185 <=1.4.187"))
@Mixin(value = EntityPlayerMPFake.class, remap = false)
public class EntityPlayerMPFakeMixin {

    @WrapOperation(
            method = "lambda$createFake$2",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;placeNewPlayer(Lnet/minecraft/network/Connection;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/server/network/CommonListenerCookie;)V",
                    remap = true
            )
    )
    private static void loadPlayerData(PlayerList instance, Connection connection, ServerPlayer player, CommonListenerCookie clientData, Operation<Void> original) {
        if (CarpetAyakaSettings.fakePlayerDataFix) {
            FakePlayerDataFixHelper.loadPlayerDataAndJoin((EntityPlayerMPFake) player, instance, connection, clientData);
        } else {
            original.call(instance, connection, player, clientData);
        }
    }

    @WrapOperation(
            method = "createShadow",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;placeNewPlayer(Lnet/minecraft/network/Connection;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/server/network/CommonListenerCookie;)V",
                    remap = true
            )
    )
    private static void loadShadowData(PlayerList instance, Connection connection, ServerPlayer player, CommonListenerCookie clientData, Operation<Void> original) {
        if (CarpetAyakaSettings.fakePlayerDataFix) {
            FakePlayerDataFixHelper.loadPlayerDataAndJoin((EntityPlayerMPFake) player, instance, connection, clientData);
        } else {
            original.call(instance, connection, player, clientData);
        }
    }

}
