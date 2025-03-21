package com.ayakacraft.carpetayakaaddition.commands.waypoint;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.utils.CommandUtils;
import com.ayakacraft.carpetayakaaddition.utils.ServerPlayerUtils;
import com.ayakacraft.carpetayakaaddition.utils.TextUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class WaypointCommand {

    private static int list(CommandContext<ServerCommandSource> context) {
        final ServerCommandSource source  = context.getSource();
        final WaypointManager     manager = WaypointManager.getWaypointManager(source.getServer());

        sendWaypointList(source, manager.waypoints.keySet(), manager);

        return 1;
    }

    private static int detail(CommandContext<ServerCommandSource> context) {
        @NotNull String   id       = StringArgumentType.getString(context, "id");
        @NotNull Waypoint waypoint = WaypointManager.getWaypointManager(context.getSource().getServer()).waypoints.get(id);
        if (waypoint == null) {
            context.getSource().sendError(TextUtils.translatableText("command.carpet-ayaka-addition.waypoint.not_exist", id));
            return 0;
        }
        ServerCommandSource source = context.getSource();
        CommandUtils.sendFeedback(source, TextUtils.translatableText("command.carpet-ayaka-addition.waypoint.detail.1", waypoint.getId())
                .formatted(Formatting.YELLOW, Formatting.BOLD)
                .append(
                        TextUtils.translatableText("command.carpet-ayaka-addition.waypoint.detail.2", waypoint.getId(), waypoint.getDim(), waypoint.getX(), waypoint.getY(), waypoint.getZ())
                                .formatted(Formatting.GREEN)), false);
        return 1;
    }

    private static int reload(CommandContext<ServerCommandSource> context) {
        try {
            WaypointManager.getWaypointManager(context.getSource().getServer()).loadWaypoints();
        } catch (IOException e) {
            context.getSource().sendError(TextUtils.translatableText("command.carpet-ayaka-addition.waypoint.reload.failed"));
            return 0;
        }
        ServerCommandSource source = context.getSource();
        CommandUtils.sendFeedback(source, TextUtils.translatableText("command.carpet-ayaka-addition.waypoint.reload.success"), false);
        return 1;
    }

    private static int set(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        String              id     = StringArgumentType.getString(context, "id");
        RegistryKey<World>  dim    = DimensionArgumentType.getDimensionArgument(context, "dim").getRegistryKey();
        Vec3d               pos    = Vec3ArgumentType.getVec3(context, "pos");
        ServerCommandSource source = context.getSource();

        final WaypointManager manager = WaypointManager.getWaypointManager(source.getServer());
        manager.waypoints.put(id, new Waypoint(id, dim, pos));
        try {
            manager.saveWaypoints();
        } catch (IOException e) {
            source.sendError(TextUtils.translatableText("command.carpet-ayaka-addition.waypoint.save.failed"));
            return 0;
        }
        CommandUtils.sendFeedback(source, TextUtils.translatableText("command.carpet-ayaka-addition.waypoint.set.success", id), false);
        return 1;
    }

    private static int remove(CommandContext<ServerCommandSource> context) {
        String              id     = StringArgumentType.getString(context, "id");
        ServerCommandSource source = context.getSource();

        final WaypointManager manager = WaypointManager.getWaypointManager(source.getServer());
        manager.waypoints.remove(id);
        try {
            manager.saveWaypoints();
        } catch (IOException e) {
            source.sendError(TextUtils.translatableText("command.carpet-ayaka-addition.waypoint.save.failed"));
            return 0;
        }
        CommandUtils.sendFeedback(source, TextUtils.translatableText("command.carpet-ayaka-addition.waypoint.remove.success", id), false);
        return 1;
    }

    private static int tp(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source   = context.getSource();
        String              id       = StringArgumentType.getString(context, "id");
        Waypoint            waypoint = WaypointManager.getWaypointManager(source.getServer()).waypoints.get(id);
        ServerPlayerEntity  self     = source.getPlayerOrThrow();
        if (waypoint == null) {
            source.sendError(TextUtils.translatableText("command.carpet-ayaka-addition.waypoint.not_exist", id));
            return 0;
        }

        ServerWorld dim = source.getServer().getWorld(waypoint.getDimension());
        Vec3d       pos = waypoint.getPos();
        if (dim == null) {
            source.sendError(TextUtils.translatableText("command.carpet-ayaka-addition.waypoint.dimension_unrecognized", waypoint.getDim()));
            return 0;
        }
        if (!dim.getWorldBorder().contains(new BlockPos(MathHelper.floor(pos.getX()), MathHelper.floor(pos.getY()), MathHelper.floor(pos.getZ())))) {
            source.sendError(TextUtils.translatableText("command.carpet-ayaka-addition.waypoint.out_of_world_border", id));
            return 0;
        }
        self.teleport(dim, pos.getX(), pos.getY(), pos.getZ(), ServerPlayerUtils.getYaw(self), ServerPlayerUtils.getPitch(self));
        return 1;
    }

    private static CompletableFuture<Suggestions> suggestWaypoints(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(WaypointManager.getWaypointManager(context.getSource().getServer()).waypoints.keySet().stream().map(id -> "\"" + id + "\""), builder);
    }

    private static int listDimension(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        final ServerCommandSource  source    = context.getSource();
        final String               dim       = DimensionArgumentType.getDimensionArgument(context, "dim").getRegistryKey().getValue().toString();
        final WaypointManager      manager   = WaypointManager.getWaypointManager(source.getServer());
        final Collection<Waypoint> waypoints = manager.waypoints.values();
        final List<String>         idList    = waypoints.stream().filter(w -> Objects.equals(w.getDim(), dim)).map(Waypoint::getId).collect(Collectors.toList());

        sendWaypointList(source, idList, manager);

        return 1;
    }

    private static MutableText waypointIdText(String id, WaypointManager manager) {
        MutableText txt = TextUtils.literalText("[").append(TextUtils.literalText(id).formatted(Formatting.GREEN)).append("]");
        Waypoint    w   = manager.waypoints.get(id);
        txt.setStyle(txt.getStyle()
                .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/waypoint tp \"%s\"", id)))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextUtils.translatableText("command.carpet-ayaka-addition.waypoint.list.hover", w.getDim(), w.getX(), w.getY(), w.getZ())))
        );
        return txt;
    }

    private static MutableText listWaypointIdsText(Collection<String> ids, WaypointManager manager) {
        return TextUtils.join(ids, Text.of(" "), id1 -> waypointIdText(id1, manager));
    }

    private static void sendWaypointList(ServerCommandSource source, Collection<String> ids, WaypointManager manager) {
        if (ids.isEmpty()) {
            CommandUtils.sendFeedback(
                    source,
                    TextUtils.translatableText("command.carpet-ayaka-addition.waypoint.list.empty")
                            .formatted(Formatting.YELLOW, Formatting.BOLD),
                    false
            );
        } else {
            CommandUtils.sendFeedback(
                    source,
                    TextUtils.translatableText("command.carpet-ayaka-addition.waypoint.list")
                            .formatted(Formatting.YELLOW, Formatting.BOLD),
                    false
            );
            CommandUtils.sendFeedback(source, listWaypointIdsText(ids, manager), false);
        }
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("waypoint")
                        .requires(source -> CommandUtils.checkPermission(source, CarpetAyakaSettings.commandWaypoint, false))
                        .then(literal("reload").executes(WaypointCommand::reload))
                        .then(literal("list").executes(WaypointCommand::list)
                                .then(argument("dim", DimensionArgumentType.dimension())
                                        .executes(WaypointCommand::listDimension)))
                        .then(literal("detail")
                                .then(argument("id", StringArgumentType.string())
                                        .suggests(WaypointCommand::suggestWaypoints)
                                        .executes(WaypointCommand::detail)))
                        .then(literal("set")
                                .then(argument("id", StringArgumentType.string()).suggests(WaypointCommand::suggestWaypoints)
                                        .then(argument("dim", DimensionArgumentType.dimension())
                                                .then(argument("pos", Vec3ArgumentType.vec3()).suggests(CommandUtils::vec3dSuggestionProvider)
                                                        .executes(WaypointCommand::set)))))
                        .then(literal("remove")
                                .then(argument("id", StringArgumentType.string())
                                        .suggests(WaypointCommand::suggestWaypoints)
                                        .executes(WaypointCommand::remove)))
                        .then(literal("tp")
                                .requires(CommandUtils::isExecutedByPlayer)
                                .then(argument("id", StringArgumentType.string())
                                        .suggests(WaypointCommand::suggestWaypoints)
                                        .executes(WaypointCommand::tp)))
        );
    }

}
