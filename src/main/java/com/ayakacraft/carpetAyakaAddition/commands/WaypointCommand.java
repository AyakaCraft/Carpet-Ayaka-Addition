package com.ayakacraft.carpetAyakaAddition.commands;

import com.ayakacraft.carpetAyakaAddition.CarpetAyakaSettings;
import com.ayakacraft.carpetAyakaAddition.data.Waypoint;
import com.ayakacraft.carpetAyakaAddition.data.WaypointManager;
import com.ayakacraft.carpetAyakaAddition.util.CommandUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class WaypointCommand {

    private static int list(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> {
            Set<String> id = WaypointManager.waypoints.keySet();
            if (id.isEmpty()) {
                return Text.translatable("command.carpet-ayaka-addition.waypoint.list.empty").formatted(Formatting.YELLOW, Formatting.BOLD);
            }
            return Text
                    .translatable("command.carpet-ayaka-addition.waypoint.list")
                    .formatted(Formatting.YELLOW, Formatting.BOLD)
                    .append(Texts.join(id, Text.of(" "), WaypointCommand::waypointIdText));
        }, false);
        return 1;
    }

    private static int detail(CommandContext<ServerCommandSource> context) {
        @NotNull String   id       = StringArgumentType.getString(context, "id");
        @NotNull Waypoint waypoint = WaypointManager.waypoints.get(id);
        if (waypoint == null) {
            context.getSource().sendError(Text.translatable("command.carpet-ayaka-addition.waypoint.not_exist", id));
            return 0;
        }
        context.getSource().sendFeedback(() -> Text.translatable("command.carpet-ayaka-addition.waypoint.detail.1", waypoint.getId())
                        .formatted(Formatting.YELLOW, Formatting.BOLD)
                        .append(
                                Text.translatable(
                                                "command.carpet-ayaka-addition.waypoint.detail.2",
                                                waypoint.getId(), waypoint.getDim(),
                                                waypoint.getX(), waypoint.getY(), waypoint.getZ())
                                        .formatted(Formatting.GREEN)),
                false);
        return 1;
    }

    private static int reload(CommandContext<ServerCommandSource> context) {
        try {
            WaypointManager.reloadWaypoints(null);
        } catch (IOException e) {
            context.getSource().sendError(Text.translatable("command.carpet-ayaka-addition.waypoint.reload.failed"));
            return 0;
        }
        context.getSource().sendFeedback(() -> Text.translatable("command.carpet-ayaka-addition.waypoint.reload.success"), false);
        return 1;
    }

    private static int set(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String             id  = StringArgumentType.getString(context, "id");
        RegistryKey<World> dim = DimensionArgumentType.getDimensionArgument(context, "dim").getRegistryKey();
        Vec3d              pos = Vec3ArgumentType.getVec3(context, "pos");

        WaypointManager.waypoints.put(id, new Waypoint(id, dim, pos));
        try {
            WaypointManager.saveWaypoints();
        } catch (IOException e) {
            context.getSource().sendError(Text.translatable("command.carpet-ayaka-addition.waypoint.save.failed"));
            return 0;
        }
        context.getSource().sendFeedback(() -> Text.translatable("command.carpet-ayaka-addition.waypoint.set.success", id), false);
        return 1;
    }

    private static int delete(CommandContext<ServerCommandSource> context) {
        String id = StringArgumentType.getString(context, "id");

        WaypointManager.waypoints.remove(id);
        try {
            WaypointManager.saveWaypoints();
        } catch (IOException e) {
            context.getSource().sendError(Text.translatable("command.carpet-ayaka-addition.waypoint.save.failed"));
            return 0;
        }
        context.getSource().sendFeedback(() -> Text.translatable("command.carpet-ayaka-addition.waypoint.delete.success", id), false);
        return 1;
    }

    private static int tp(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source   = context.getSource();
        String              id       = StringArgumentType.getString(context, "id");
        Waypoint            waypoint = WaypointManager.waypoints.get(id);
        ServerPlayerEntity  self     = source.getPlayerOrThrow();
        if (waypoint == null) {
            source.sendError(Text.translatable("command.carpet-ayaka-addition.waypoint.not_exist", id));
            return 0;
        }

        ServerWorld dim = source.getServer().getWorld(waypoint.getDimension());
        Vec3d       pos = waypoint.getPos();
        if (dim == null) {
            source.sendError(Text.translatable("command.carpet-ayaka-addition.waypoint.dimension_unrecognized", waypoint.getDim()));
            return 0;
        }
        if (!dim.getWorldBorder().contains(pos.getX(), pos.getZ())) {
            source.sendError(Text.translatable("command.carpet-ayaka-addition.waypoint.out_of_world_border", id));
            return 0;
        }
        self.teleport(dim, pos.getX(), pos.getY(), pos.getZ(), EnumSet.noneOf(PositionFlag.class), MathHelper.wrapDegrees(self.getYaw()), MathHelper.wrapDegrees(self.getPitch()));
        return 1;
    }

    private static CompletableFuture<Suggestions> suggestWaypoints(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(WaypointManager.waypoints.keySet().stream().map(id -> "\"" + id + "\""), builder);
    }

    private static MutableText waypointIdText(String id) {
        MutableText txt = Text.literal("[%s]".formatted(id)).formatted(Formatting.GREEN);
        Waypoint    w   = WaypointManager.waypoints.get(id);
        txt.setStyle(txt.getStyle()
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/waypoint tp \"%s\"".formatted(id)))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("command.carpet-ayaka-addition.waypoint.list.hover", w.getDim(), w.getX(), w.getY(), w.getZ())))
        );
        return txt;
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("waypoint")
                        .requires(source -> CommandUtils.checkPermission(source, CarpetAyakaSettings.commandWaypoint, true))
                        .then(literal("reload").executes(WaypointCommand::reload))
                        .then(literal("list").executes(WaypointCommand::list))
                        .then(literal("detail")
                                .then(argument("id", StringArgumentType.string())
                                        .suggests(WaypointCommand::suggestWaypoints)
                                        .executes(WaypointCommand::detail)))
                        .then(literal("set")
                                .then(argument("id", StringArgumentType.string()).suggests(WaypointCommand::suggestWaypoints)
                                        .then(argument("dim", DimensionArgumentType.dimension())
                                                .then(argument("pos", Vec3ArgumentType.vec3()).suggests(CommandUtils::vec3dSuggestionProvider)
                                                        .executes(WaypointCommand::set)))))
                        .then(literal("delete")
                                .then(argument("id", StringArgumentType.string())
                                        .suggests(WaypointCommand::suggestWaypoints)
                                        .executes(WaypointCommand::delete)))
                        .then(literal("tp")
                                .requires(ServerCommandSource::isExecutedByPlayer)
                                .then(argument("id", StringArgumentType.string())
                                        .suggests(WaypointCommand::suggestWaypoints)
                                        .executes(WaypointCommand::tp)))
        );
    }

}
