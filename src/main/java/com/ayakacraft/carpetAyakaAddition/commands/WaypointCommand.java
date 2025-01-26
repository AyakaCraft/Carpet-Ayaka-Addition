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
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class WaypointCommand {

    private static int list(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> {
            val id = WaypointManager.waypoints.keySet();
            if (id.isEmpty()) {
                return Text.translatable("command.carpet-ayaka-addition.waypoint.list.empty").formatted(Formatting.YELLOW,Formatting.BOLD);
            }
//            val str = new StringBuilder("The waypoints are as below:\n");
//            id.forEach(s -> str.append("[").append(s).append("], "));
//            str.delete(str.length() - 2, str.length());
            val str = Text.translatable("command.carpet-ayaka-addition.waypoint.list").formatted(Formatting.YELLOW, Formatting.BOLD);
            id.forEach(s -> {
                MutableText txt = Text.literal("[%s]".formatted(s)).formatted(Formatting.GREEN);
                txt.setStyle(txt.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/waypoint tpt "+s)));
                str.append(txt).append(" ");
            });
//            val str2 = new StringBuilder();
//            id.forEach(s -> str2.append("[").append(s).append("], "));
//            str2.delete(str2.length() - 2, str2.length());
//            return str.append(Text.literal(str2.toString()).formatted(Formatting.GREEN));
            return str;
        }, false);
        return 1;
    }

    private static int detail(CommandContext<ServerCommandSource> context) {
        val id       = StringArgumentType.getString(context, "id");
        val waypoint = WaypointManager.waypoints.get(id);
        if (waypoint == null) {
            context.getSource().sendError(Text.translatable("command.carpet-ayaka-addition.waypoint.not_exist", id));
            return 0;
        }
        context.getSource().sendFeedback(() -> Text.translatable("command.carpet-ayaka-addition.waypoint.detail.1")
                        .formatted(Formatting.YELLOW, Formatting.BOLD)
                        .append(
                                Text.translatable(
                                        "command.carpet-ayaka-addition.waypoint.detail.2",
                                        waypoint.getId(), waypoint.getDim(),
                                        waypoint.getX(), waypoint.getY(), waypoint.getZ())),
                false);
        return 1;
    }

    private static int reload(CommandContext<ServerCommandSource> context) {
        try {
            WaypointManager.reloadWaypoints(null);
        } catch (IOException e) {
            context.getSource().sendError(Text.literal("command.carpet-ayaka-addition.waypoint.reload.failed"));
            return 0;
        }
        context.getSource().sendFeedback(() -> Text.literal("command.carpet-ayaka-addition.waypoint.reload.success"), true);
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
            context.getSource().sendError(Text.translatable("command.carpet-ayaka-addition.waypoint.save.failed"));
            return 0;
        }
        context.getSource().sendFeedback(() -> Text.translatable("command.carpet-ayaka-addition.waypoint.set.success", id), true);
        return 1;
    }

    private static int delete(CommandContext<ServerCommandSource> context) {
        val id = StringArgumentType.getString(context, "id");

        WaypointManager.waypoints.remove(id);
        try {
            WaypointManager.saveWaypoints();
        } catch (IOException e) {
            context.getSource().sendError(Text.translatable("command.carpet-ayaka-addition.waypoint.save.failed"));
            return 0;
        }
        context.getSource().sendFeedback(() -> Text.translatable("command.carpet-ayaka-addition.waypoint.delete.success", id), true);
        return 1;
    }

    private static int tpt(CommandContext<ServerCommandSource> context) {
        val source   = context.getSource();
        val id       = StringArgumentType.getString(context, "id");
        val waypoint = WaypointManager.waypoints.get(id);
        if (waypoint == null) {
            source.sendError(Text.translatable("command.carpet-ayaka-addition.waypoint.not_exist", id));
            return 0;
        }

        val dim = source.getServer().getWorld(waypoint.getDimension());
        val pos = waypoint.getPos();
        if (dim == null) {
            source.sendError(Text.translatable("command.carpet-ayaka-addition.waypoint.dimension_unrecognized", waypoint.getDim()));
            return 0;
        }
        if (!dim.getWorldBorder().contains(pos.getX(), pos.getZ())) {
            source.sendError(Text.translatable("command.carpet-ayaka-addition.waypoint.out_of_world_border", id));
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
