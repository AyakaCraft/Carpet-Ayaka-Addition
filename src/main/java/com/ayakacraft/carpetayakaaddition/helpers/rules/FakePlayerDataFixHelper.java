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

package com.ayakacraft.carpetayakaaddition.helpers.rules;

import carpet.patches.EntityPlayerMPFake;
import com.ayakacraft.carpetayakaaddition.CarpetAyakaAddition;
import net.minecraft.network.Connection;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import org.jetbrains.annotations.Contract;

import java.util.Optional;

public final class FakePlayerDataFixHelper {

    @Contract(pure = true)
    public static void loadPlayerDataAndJoin(EntityPlayerMPFake player, PlayerList playerManager, Connection connection, CommonListenerCookie clientData) {
        try (ProblemReporter.ScopedCollector logging = new ProblemReporter.ScopedCollector(player.problemPath(), CarpetAyakaAddition.LOGGER)) {
            Optional<ValueInput> readView = playerManager
                    .loadPlayerData(player.nameAndId())
                    .map((playerNbt) ->
                            TagValueInput.create(logging, player.registryAccess(), playerNbt)
                    );
            playerManager.placeNewPlayer(connection, player, clientData);
            readView.ifPresent(playerData -> {
                player.load(playerData);
                player.loadAndSpawnEnderPearls(playerData);
                player.loadAndSpawnParentVehicle(playerData);
                player.fixStartingPosition.run();
            });
        }
    }

}
