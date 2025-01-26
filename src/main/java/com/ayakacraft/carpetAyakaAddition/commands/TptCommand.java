package com.ayakacraft.carpetAyakaAddition.commands;

import com.ayakacraft.carpetAyakaAddition.CarpetAyakaSettings;
import com.ayakacraft.carpetAyakaAddition.utils.CommandUtil;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class TptCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("tpt")
                        .requires(source -> CommandUtil.checkPermissioin(source, CarpetAyakaSettings.commandTpt, false))
                        .then(
                                argument("player", EntityArgumentType.player())
                                        .suggests(CommandUtil::playerSuggestionProvider)
                                        .executes(context -> {
                                            var source = context.getSource();
                                            var self   = source.getPlayer();
                                            if (self == null) {
                                                return 0;
                                            }
                                            var to = EntityArgumentType.getPlayer(context, "player");
                                            self.teleport(to.getServerWorld(), to.getX(), to.getY(), to.getZ(), to.getYaw(), to.getPitch());
                                            return 1;
                                        })));
    }

}
