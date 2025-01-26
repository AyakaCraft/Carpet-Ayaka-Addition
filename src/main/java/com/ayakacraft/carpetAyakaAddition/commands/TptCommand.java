package com.ayakacraft.carpetAyakaAddition.commands;

import com.ayakacraft.carpetAyakaAddition.CarpetAyakaSettings;
import com.ayakacraft.carpetAyakaAddition.utils.CommandUtil;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;


import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class TptCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("tpt")
                        .requires(source -> CommandUtil.checkPermission(source, CarpetAyakaSettings.commandTpt, false))
                        .then(
                                argument("target", EntityArgumentType.player())
                                        .suggests(CommandUtil::playerSuggestionProvider)
                                        .executes(context -> {
                                            final @NotNull ServerCommandSource source = context.getSource();
                                            final @NotNull ServerPlayerEntity player = source.getPlayerOrThrow();
                                            final @NotNull ServerPlayerEntity target = EntityArgumentType.getPlayer(context, "target");
                                            final float f = MathHelper.wrapDegrees(target.getYaw());
                                            final float g = MathHelper.wrapDegrees(target.getPitch());
                                            player.teleport(target.getServerWorld(), target.getX(), target.getY(), target.getZ(), EnumSet.noneOf(PositionFlag.class), f, g);
                                            return Command.SINGLE_SUCCESS;
                                        })));
    }

}
