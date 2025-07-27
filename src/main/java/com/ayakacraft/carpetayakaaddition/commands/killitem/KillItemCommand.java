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

package com.ayakacraft.carpetayakaaddition.commands.killitem;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaServer;
import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.commands.AyakaCommandRegistry;
import com.ayakacraft.carpetayakaaddition.utils.CommandUtils;
import com.ayakacraft.carpetayakaaddition.utils.EntityUtils;
import com.ayakacraft.carpetayakaaddition.utils.TickTask;
import com.ayakacraft.carpetayakaaddition.utils.text.TextUtils;
import com.ayakacraft.carpetayakaaddition.utils.translation.Translator;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

import java.util.List;

import static net.minecraft.server.command.CommandManager.literal;


public final class KillItemCommand {

    public static final String NAME = "killitem";

    public static final Translator TR = AyakaCommandRegistry.COMMAND_TR.resolve(NAME);

    private static int killItem(CommandContext<ServerCommandSource> context) {
        if (CarpetAyakaSettings.killItemAwaitSeconds == 0) {
            return killItem0(context.getSource());
        } else {
            CarpetAyakaServer.INSTANCE.addTickTask(it -> new KillItemTickTask(it, CarpetAyakaSettings.killItemAwaitSeconds, context.getSource()));
            return 1;
        }
    }

    private static int killItem0(ServerCommandSource source) {
        final List<Entity>    targets = Lists.newLinkedList();
        final MinecraftServer server  = source.getServer();
        server.getWorlds().forEach(world -> targets.addAll(world.getEntitiesByType(
                EntityType.ITEM,
                itemEntity -> true)));
        if (targets.isEmpty()) {
            TextUtils.broadcastTranslatable(server, false, TR.resolve("none"));
            return 0;
        }
        targets.forEach(EntityUtils::kill);
        if (targets.size() == 1) {
            TextUtils.broadcastTranslatable(
                    server, false,
                    TR.resolve("single"), targets.get(0).getDisplayName()
            );
        } else {
            TextUtils.broadcastTranslatable(
                    server, false,
                    TR.resolve("multiple"), targets.size()
            );
        }
        return targets.size();
    }

    private static int cancel(CommandContext<ServerCommandSource> context) {
        final int                 i      = CarpetAyakaServer.INSTANCE.cancelTickTasksMatching(tickTask -> tickTask instanceof KillItemTickTask);
        final ServerCommandSource source = context.getSource();
        if (i == 0) {
            CommandUtils.sendFeedback(source, TR.tr(source, "cancel.none"), false);
        } else if (i == 1) {
            CommandUtils.sendFeedback(source, TR.tr(source, "cancel.single"), false);
        } else {
            CommandUtils.sendFeedback(source, TR.tr(source, "cancel.multiple", i), false);
        }
        return i;
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal(NAME)
                        .requires(source -> CommandUtils.checkPermission(source, CarpetAyakaSettings.commandKillItem, false))
                        .executes(KillItemCommand::killItem)
                        .then(literal("cancel").executes(KillItemCommand::cancel)));
    }

    private static final class KillItemTickTask extends TickTask {

        private final int awaitSeconds;

        private final ServerCommandSource source;

        private int ticks;

        public KillItemTickTask(CarpetAyakaServer modServer, int awaitSeconds, ServerCommandSource source) {
            super(modServer);
            this.awaitSeconds = awaitSeconds;
            this.ticks = awaitSeconds * 20;
            this.source = source;
        }

        @Override
        public void start() {
            TextUtils.broadcast(
                    mcServer,
                    TR.tr("task.start", awaitSeconds).formatted(Formatting.GOLD),
                    p -> TR.tr(p, "task.start", awaitSeconds).formatted(Formatting.GOLD),
                    false
            );
        }

        @Override
        public void tick() {
            if ((--ticks) <= 0) {
                killItem0(source);
                finish();
            }
        }

    }

}
