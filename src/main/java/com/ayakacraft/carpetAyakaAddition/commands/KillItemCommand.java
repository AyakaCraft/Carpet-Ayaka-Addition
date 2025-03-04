package com.ayakacraft.carpetayakaaddition.commands;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaAddition;
import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.util.CommandUtils;
import com.ayakacraft.carpetayakaaddition.util.TextUtils;
import com.ayakacraft.carpetayakaaddition.util.TickTask;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

import java.util.LinkedList;
import java.util.List;

import static net.minecraft.server.command.CommandManager.literal;


public final class KillItemCommand {

    private static int killItem(CommandContext<ServerCommandSource> context) {
        if (CarpetAyakaSettings.killItemAwaitSeconds == 0) {
            return killItem0(context.getSource());
        } else {
            CarpetAyakaAddition.INSTANCE.addTickTask(new KillItemTickTask(CarpetAyakaAddition.INSTANCE, CarpetAyakaSettings.killItemAwaitSeconds, context.getSource()));
            return 1;
        }
    }

    private static int killItem0(ServerCommandSource source) {
        final List<Entity>    targets = new LinkedList<>();
        final MinecraftServer server  = source.getServer();
        server.getWorlds().forEach(world -> {
            targets.addAll(world.getEntitiesByType(
                    EntityType.ITEM,
                    itemEntity -> true));
        });
        if (targets.isEmpty()) {
            TextUtils.broadcastToPlayers(server, TextUtils.translatableText("command.carpet-ayaka-addition.killitem.none"), false);
            return 0;
        }
        targets.forEach(Entity::kill);
        if (targets.size() == 1) {
            TextUtils.broadcastToPlayers(server, TextUtils.translatableText("command.carpet-ayaka-addition.killitem.single"), false);
        } else {
            TextUtils.broadcastToPlayers(server, TextUtils.translatableText("command.carpet-ayaka-addition.killitem.multiple", targets.size()), false);
        }
        return 1;
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("killitem")
                        .requires(source -> CommandUtils.checkPermission(source, CarpetAyakaSettings.commandKillItem, false))
                        .executes(KillItemCommand::killItem));
    }

    private static class KillItemTickTask extends TickTask {

        private final int awaitSeconds;

        private final ServerCommandSource source;

        private int ticks;

        public KillItemTickTask(CarpetAyakaAddition modServer, int awaitSeconds, ServerCommandSource source) {
            super(modServer);
            this.awaitSeconds = awaitSeconds;
            this.ticks = awaitSeconds * 20;
            this.source = source;
        }

        @Override
        public void start() {
            TextUtils.broadcastToPlayers(mcServer, TextUtils.translatableText("command.carpet-ayaka-addition.killitem.warning", awaitSeconds).formatted(Formatting.GOLD), false);
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
