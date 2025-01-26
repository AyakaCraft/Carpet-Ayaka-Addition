package com.ayakacraft.carpetAyakaAddition.commands;

import com.ayakacraft.carpetAyakaAddition.CarpetAyakaSettings;
import com.ayakacraft.carpetAyakaAddition.data.Waypoint;
import com.ayakacraft.carpetAyakaAddition.data.WaypointManager;
import com.ayakacraft.carpetAyakaAddition.utils.CommandUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.val;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class WaypointCommand {

    private static int list(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> {
            val ids=WaypointManager.waypoints.keySet();
            if(ids.isEmpty()){
                return Text.literal("No waypoints available");
            }
            val str = new StringBuilder("The waypoints are as below:\n");
            ids.forEach(s -> str.append("[").append(s).append("], "));
            str.delete(str.length() - 2, str.length());
            return Text.literal(str.toString());
        }, false);
        return 1;
    }

    private static int detail(CommandContext<ServerCommandSource> context) {
        val id       = StringArgumentType.getString(context, "id");
        val waypoint = WaypointManager.waypoints.get(id);
        if (waypoint == null) {
            context.getSource().sendError(Text.literal("Waypoint [%s] does not exist".formatted(id)));
            return 0;
        }
        context.getSource().sendFeedback(() -> Text.literal(
                """
                        ID:        %s
                        Dimension: %s
                        x:         %f
                        y:         %f
                        z:         %f""".formatted(waypoint.getId(), waypoint.getDim(), waypoint.getX(), waypoint.getY(), waypoint.getZ())), false);
        return 1;
    }

    private static int reload(CommandContext<ServerCommandSource> context) {
        try {
            WaypointManager.reloadWaypoints(null);
        } catch (IOException e) {
            context.getSource().sendError(Text.literal("Failed to reload waypoints"));
            return 0;
        }
        context.getSource().sendFeedback(() -> Text.literal("Succeeded reloading waypoints"), true);
        return 1;
    }

    private static int set(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        val id  = StringArgumentType.getString(context, "id");
        val dim = DimensionArgumentType.getDimensionArgument(context, "dim").getRegistryKey();
        val pos = Vec3ArgumentType.getVec3(context, "pos");

        WaypointManager.waypoints.put(id, new Waypoint(id, dim, pos));
        try {
            WaypointManager.saveWaypoints();
        } catch (IOException e) {
            context.getSource().sendError(Text.literal("Failed to save waypoints"));
            return 0;
        }
        context.getSource().sendFeedback(() -> Text.literal("Saved waypoint [%s]".formatted(id)), true);
        return 1;
    }

    private static int delete(CommandContext<ServerCommandSource> context) {
        val id = StringArgumentType.getString(context, "id");

        WaypointManager.waypoints.remove(id);
        try {
            WaypointManager.saveWaypoints();
        } catch (IOException e) {
            context.getSource().sendError(Text.literal("Failed to delete waypoints"));
            return 0;
        }
        context.getSource().sendFeedback(() -> Text.literal("Deleted waypoint [%s]".formatted(id)), true);
        return 1;
    }

    private static int tpt(CommandContext<ServerCommandSource> context) {
        val source   = context.getSource();
        val id       = StringArgumentType.getString(context, "id");
        val waypoint = WaypointManager.waypoints.get(id);
        if (waypoint == null) {
            source.sendError(Text.literal("Waypoint [%s] does not exist".formatted(id)));
            return 0;
        }

        val dim = source.getServer().getWorld(waypoint.getDimension());
        val pos = waypoint.getPos();
        if (dim == null) {
            source.sendError(Text.literal("Unrecognized dimension"));
            return 0;
        }
        if (!dim.getWorldBorder().contains(pos.getX(), pos.getZ())) {
            source.sendError(Text.literal("Waypoint [%s] out of world border".formatted(id)));
            return 0;
        }
        source.getPlayer().teleport(dim, pos.getX(), pos.getY(), pos.getZ(), 0f, 0f);
        return 1;
    }

    private static CompletableFuture<Suggestions> suggestWaypoints(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(WaypointManager.waypoints.keySet(), builder);
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("waypoint")
                        .requires(source -> CommandUtil.checkPermissioin(source, CarpetAyakaSettings.commandWaypoint, true))
                        .then(literal("reload").executes(WaypointCommand::reload))
                        .then(literal("list").executes(WaypointCommand::list))
                        .then(literal("detail")
                                .then(argument("id", StringArgumentType.string())
                                        .suggests(WaypointCommand::suggestWaypoints)
                                        .executes(WaypointCommand::detail)))
                        .then(literal("set")
                                .then(argument("id", StringArgumentType.string()).suggests(WaypointCommand::suggestWaypoints)
                                        .then(argument("dim", DimensionArgumentType.dimension())
                                                .then(argument("pos", Vec3ArgumentType.vec3()).suggests(CommandUtil::vec3dSuggestionProvider)
                                                        .executes(WaypointCommand::set)))))
                        .then(literal("delete")
                                .then(argument("id", StringArgumentType.string())
                                        .suggests(WaypointCommand::suggestWaypoints)
                                        .executes(WaypointCommand::delete)))
                        .then(literal("tpt")
                                .then(argument("id", StringArgumentType.string())
                                        .suggests(WaypointCommand::suggestWaypoints)
                                        .executes(WaypointCommand::tpt)))
        );
    }

}
