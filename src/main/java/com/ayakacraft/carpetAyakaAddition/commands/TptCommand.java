package com.ayakacraft.carpetAyakaAddition.commands;

import com.ayakacraft.carpetAyakaAddition.CarpetAyakaSettings;
import com.ayakacraft.carpetAyakaAddition.util.CommandUtils;
import com.ayakacraft.carpetAyakaAddition.util.MethodWrapper;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class TptCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("tpt")
                        .requires(source -> CommandUtils.checkPermission(source, CarpetAyakaSettings.commandTpt, false))
                        .then(
                                argument("target", EntityArgumentType.player())
                                        .suggests(CommandUtils::playerSuggestionProvider)
                                        .executes(context -> {
                                            final @NotNull ServerCommandSource source = context.getSource();
                                            final @NotNull ServerPlayerEntity  player = source.getPlayerOrThrow();
                                            final @NotNull ServerPlayerEntity  target = EntityArgumentType.getPlayer(context, "target");
                                            player.teleport(MethodWrapper.getServerWorld(player), target.getX(), target.getY(), target.getZ(),
                                                    MethodWrapper.getYaw(target), MethodWrapper.getPitch(target));
                                            return Command.SINGLE_SUCCESS;
                                        })));
    }

}
