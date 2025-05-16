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

package com.ayakacraft.carpetayakaaddition;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.logging.HUDController;
import com.ayakacraft.carpetayakaaddition.commands.address.AddressCommand;
import com.ayakacraft.carpetayakaaddition.commands.address.AddressManager;
import com.ayakacraft.carpetayakaaddition.commands.c.CCommand;
import com.ayakacraft.carpetayakaaddition.commands.gohome.GoHomeCommand;
import com.ayakacraft.carpetayakaaddition.commands.killitem.KillItemCommand;
import com.ayakacraft.carpetayakaaddition.commands.tpt.TptCommand;
import com.ayakacraft.carpetayakaaddition.logging.AyakaLoggerRegistry;
import com.ayakacraft.carpetayakaaddition.utils.TickTask;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Predicate;

public final class CarpetAyakaServer implements CarpetExtension {

    public static final CarpetAyakaServer INSTANCE = new CarpetAyakaServer();

    private final LinkedList<TickTask> tickTasks = new LinkedList<>();

    private final LinkedBlockingQueue<TickTask> scheduledTickTasks = new LinkedBlockingQueue<>();

    public MinecraftServer mcServer;

    private CarpetAyakaServer() {
        super();
    }

    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(CarpetAyakaSettings.class);

        //#if MC>=11600
        HUDController.register(minecraftServer -> AyakaLoggerRegistry.updateHUD()); // We use mixin to deal with 1.14 and 1.15
        //#endif
    }

    @Override
    public void onServerLoaded(MinecraftServer server) {
        this.mcServer = server;
    }

    //#if MC>=11600
    @Override
    public void onServerLoadedWorlds(MinecraftServer server) {
        onServerLoadedWorlds$Ayaka();
    }
    //#endif

    @Override
    public void onTick(MinecraftServer server) {
        tickTasks.forEach(TickTask::tick);
        tickTasks.removeIf(TickTask::isFinished);
        scheduledTickTasks.forEach(TickTask::start);
        scheduledTickTasks.drainTo(tickTasks);
    }

    @Override
    public void registerCommands(
            CommandDispatcher<ServerCommandSource> dispatcher
            //#if MC>=11900
            , final net.minecraft.command.CommandRegistryAccess registryAccess
            //#endif
    ) {
        TptCommand.register(dispatcher);
        GoHomeCommand.register(dispatcher);
        AddressCommand.register(dispatcher);
        CCommand.register(dispatcher);
        KillItemCommand.register(dispatcher);
    }

    @Override
    public void onServerClosed(MinecraftServer server) {
        AddressManager.removeWaypointManager(mcServer);
        cancelTickTasksMatching(it -> true);

        this.mcServer = null;
    }

    @Override
    public String version() {
        return CarpetAyakaAddition.MOD_ID;
    }

    //#if MC>=11500
    @Override
    public void registerLoggers() {
        AyakaLoggerRegistry.registerToCarpet();
    }
    //#endif

    //#if MC>=11500
    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return CarpetAyakaAddition.canHasTranslations(lang);
    }
    //#endif

    public void addTickTask(TickTask tickTask) {
        scheduledTickTasks.add(tickTask);
    }

    public synchronized int cancelTickTasksMatching(Predicate<? super TickTask> filter) {
        int                      i    = 0;
        final Iterator<TickTask> each = tickTasks.iterator();
        while (each.hasNext()) {
            TickTask task = each.next();
            if (filter.test(task)) {
                task.cancel();
                each.remove();
                i++;
            }
        }
        return i;
    }

    public void onServerLoadedWorlds$Ayaka() {
        AddressManager.getOrCreate(mcServer);
    }

}
