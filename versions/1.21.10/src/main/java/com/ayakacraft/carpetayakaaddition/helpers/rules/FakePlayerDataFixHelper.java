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
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.storage.NbtReadView;
import net.minecraft.storage.ReadView;
import net.minecraft.util.ErrorReporter;

import java.util.Optional;

public final class FakePlayerDataFixHelper {

    public static void loadPlayerDataAndJoin(EntityPlayerMPFake player, PlayerManager playerManager, ClientConnection connection, ConnectedClientData clientData) {
        try (ErrorReporter.Logging logging = new ErrorReporter.Logging(player.getErrorReporterContext(), CarpetAyakaAddition.LOGGER)) {
            Optional<ReadView> readView = playerManager
                    .loadPlayerData(player.getPlayerConfigEntry())
                    .map((playerNbt) ->
                            NbtReadView.create(logging, player.getRegistryManager(), playerNbt)
                    );
            readView.ifPresent(player::readData);
            playerManager.onPlayerConnect(connection, player, clientData);
            readView.ifPresent(playerData -> {
                player.readEnderPearls(playerData);
                player.readRootVehicle(playerData);
            });
        }
    }

}
