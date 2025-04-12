package com.ayakacraft.carpetayakaaddition.commands;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.utils.CommandUtils;
import com.ayakacraft.carpetayakaaddition.utils.ServerPlayerUtils;
import com.ayakacraft.carpetayakaaddition.utils.TextUtils;
import com.ayakacraft.carpetayakaaddition.utils.waypoint.Waypoint;
import com.ayakacraft.carpetayakaaddition.utils.waypoint.WaypointManager;
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
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.ayakacraft.carpetayakaaddition.utils.CommandUtils.sendFeedback;
import static com.ayakacraft.carpetayakaaddition.utils.TextUtils.*;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class WaypointCommand {

    private static int list(CommandContext<ServerCommandSource> context) {
        final ServerCommandSource source  = context.getSource();
        final WaypointManager     manager = WaypointManager.getOrCreateWaypointManager(source.getServer());

        sendWaypointList(source, manager.getIds(), manager);

        return 1;
    }

    private static int detail(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source   = context.getSource();
        final String        id       = StringArgumentType.getString(context, "id");
        final Waypoint      waypoint = WaypointManager.getOrCreateWaypointManager(source.getServer()).get(id);
        if (waypoint == null) {
            source.sendError(tr("command.carpet-ayaka-addition.waypoint.not_exist", id));
            return 0;
        }
        sendFeedback(source,
                TextUtils.joinTexts(
                        tr("command.carpet-ayaka-addition.waypoint.detail.1", waypoint.getId())
                                .formatted(Formatting.YELLOW, Formatting.BOLD),
                        enter(),
                        tr("command.carpet-ayaka-addition.waypoint.detail.2").formatted(Formatting.GREEN),
                        li(waypoint.getId()),
                        enter(),
                        tr("command.carpet-ayaka-addition.waypoint.detail.3").formatted(Formatting.GREEN),
                        li(waypoint.getDim()),
                        enter(),
                        tr("command.carpet-ayaka-addition.waypoint.detail.4").formatted(Formatting.GREEN),
                        li(String.format("%f %f %f", waypoint.getX(), waypoint.getY(), waypoint.getZ())),
                        enter(),
                        tr("command.carpet-ayaka-addition.waypoint.detail.5").formatted(Formatting.GREEN),
                        li(waypoint.getDesc())
                ),
                false);
        return 1;
    }

    private static int reload(CommandContext<ServerCommandSource> context) {
        final ServerCommandSource source = context.getSource();
        try {
            WaypointManager.getOrCreateWaypointManager(source.getServer()).loadWaypoints();
        } catch (IOException e) {
            source.sendError(tr("command.carpet-ayaka-addition.waypoint.reload.failed"));
            return 0;
        }
        sendFeedback(source, tr("command.carpet-ayaka-addition.waypoint.reload.success"), false);
        return 1;
    }

    private static int set(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        final String             id  = StringArgumentType.getString(context, "id");
        final RegistryKey<World> dim = DimensionArgumentType.getDimensionArgument(context, "dim").getRegistryKey();
        final Vec3d              pos = Vec3ArgumentType.getVec3(context, "pos");
        final String             desc;
        String                   s;
        try {
            s = StringArgumentType.getString(context, "desc");
        } catch (IllegalArgumentException e) {
            s = "";
        }
        desc = s;

        final ServerCommandSource source = context.getSource();

        try {
            WaypointManager.getOrCreateWaypointManager(source.getServer()).set(new Waypoint(id, dim, pos, desc));
        } catch (IOException e) {
            source.sendError(tr("command.carpet-ayaka-addition.waypoint.save.failed"));
            return 0;
        }
        sendFeedback(source, tr("command.carpet-ayaka-addition.waypoint.set.success", id), false);
        return 1;
    }

    private static int remove(CommandContext<ServerCommandSource> context) {
        final String              id     = StringArgumentType.getString(context, "id");
        final ServerCommandSource source = context.getSource();

        try {
            if (WaypointManager.getOrCreateWaypointManager(source.getServer()).remove(id) == null) {
                source.sendError(tr("command.carpet-ayaka-addition.waypoint.not_exist", id));
                return 0;
            }
        } catch (IOException e) {
            source.sendError(tr("command.carpet-ayaka-addition.waypoint.save.failed"));
            return 0;
        }
        sendFeedback(source, tr("command.carpet-ayaka-addition.waypoint.remove.success", id), false);
        return 1;
    }

    private static int tp(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        final ServerCommandSource source   = context.getSource();
        final String              id       = StringArgumentType.getString(context, "id");
        final Waypoint            waypoint = WaypointManager.getOrCreateWaypointManager(source.getServer()).get(id);
        final ServerPlayerEntity  self     = source.getPlayerOrThrow();
        if (waypoint == null) {
            source.sendError(tr("command.carpet-ayaka-addition.waypoint.not_exist", id));
            return 0;
        }

        final ServerWorld dim = source.getServer().getWorld(waypoint.getDimension());
        final Vec3d       pos = waypoint.getPos();
        if (dim == null) {
            source.sendError(tr("command.carpet-ayaka-addition.waypoint.dimension_unrecognized", waypoint.getDim()));
            return 0;
        }
        if (!dim.getWorldBorder().contains(new BlockPos(MathHelper.floor(pos.getX()), MathHelper.floor(pos.getY()), MathHelper.floor(pos.getZ())))) {
            source.sendError(tr("command.carpet-ayaka-addition.waypoint.out_of_world_border", id));
            return 0;
        }
        ServerPlayerUtils.teleport(self, dim, pos.getX(), pos.getY(), pos.getZ());
        return 1;
    }

    private static int rename(CommandContext<ServerCommandSource> context) {
        final ServerCommandSource source  = context.getSource();
        final String              oldId   = StringArgumentType.getString(context, "oldId");
        final String              newId   = StringArgumentType.getString(context, "id");
        final WaypointManager     manager = WaypointManager.getOrCreateWaypointManager(source.getServer());
        if (manager.get(newId) != null) {
            remove(context);
        }
        try {
            if (manager.rename(oldId, newId) == null) {
                source.sendError(tr("command.carpet-ayaka-addition.waypoint.not_exist", oldId));
                return 0;
            }
        } catch (IOException e) {
            source.sendError(tr("command.carpet-ayaka-addition.waypoint.save.failed"));
            return 0;
        }
        sendFeedback(source, tr("command.carpet-ayaka-addition.waypoint.rename.success", oldId, newId), false);
        return 1;
    }

    private static int desc(CommandContext<ServerCommandSource> context) {
        final ServerCommandSource source  = context.getSource();
        final WaypointManager     manager = WaypointManager.getOrCreateWaypointManager(source.getServer());
        final String              id      = StringArgumentType.getString(context, "id");
        final String              desc    = StringArgumentType.getString(context, "desc");
        final Waypoint            w       = manager.get(id);
        if (w == null) {
            source.sendError(tr("command.carpet-ayaka-addition.waypoint.not_exist", id));
            return 0;
        }
        w.setDesc(desc);
        try {
            manager.saveWaypoints();
        } catch (IOException e) {
            source.sendError(tr("command.carpet-ayaka-addition.waypoint.save.failed"));
            return 0;
        }
        sendFeedback(source, tr("command.carpet-ayaka-addition.waypoint.desc.success", id, desc), false);
        return 1;
    }

    private static CompletableFuture<Suggestions> suggestWaypoints(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(WaypointManager.getOrCreateWaypointManager(context.getSource().getServer()).getIds().stream().map(id -> "\"" + id + "\""), builder);
    }

    private static int listDimension(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        final ServerCommandSource  source    = context.getSource();
        final String               dim       = DimensionArgumentType.getDimensionArgument(context, "dim").getRegistryKey().getValue().toString();
        final WaypointManager      manager   = WaypointManager.getOrCreateWaypointManager(source.getServer());
        final Collection<Waypoint> waypoints = manager.getWaypoints();
        final List<String>         idList    = waypoints.stream().filter(w -> Objects.equals(w.getDim(), dim)).map(Waypoint::getId).collect(Collectors.toList());

        sendWaypointList(source, idList, manager);

        return 1;
    }

    private static MutableText waypointIdText(String id, WaypointManager manager) {
        final Waypoint w = manager.get(id);
        return TextUtils.joinTexts(
                        li("["),
                        li(id).formatted(Formatting.GREEN),
                        li("]")
                )
                .styled(style -> style
                        .withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/waypoint tp \"%s\"", id)))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, tr(
                                "command.carpet-ayaka-addition.waypoint.list.hover",
                                w.getDim(),
                                w.getX(),
                                w.getY(),
                                w.getZ(),
                                w.getDesc())))
                );
    }

    private static MutableText listWaypointIdsText(Collection<String> ids, WaypointManager manager) {
        return TextUtils.join(ids, Text.of(" "), id1 -> waypointIdText(id1, manager));
    }

    private static void sendWaypointList(ServerCommandSource source, Collection<String> ids, WaypointManager manager) {
        if (ids.isEmpty()) {
            sendFeedback(
                    source,
                    tr("command.carpet-ayaka-addition.waypoint.list.empty")
                            .formatted(Formatting.YELLOW, Formatting.BOLD),
                    false
            );
        } else {
            sendFeedback(
                    source,
                    TextUtils.joinTexts(
                            tr("command.carpet-ayaka-addition.waypoint.list").formatted(Formatting.YELLOW, Formatting.BOLD),
                            enter(),
                            listWaypointIdsText(ids, manager)
                    ),
                    false
            );
        }
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("waypoint")
                        .requires(source -> CommandUtils.checkPermission(source, !CarpetAyakaSettings.commandWaypoint, false))
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
                                                .then(argument("pos", Vec3ArgumentType.vec3())
                                                        .executes(WaypointCommand::set)
                                                        .then(argument("desc", StringArgumentType.string())
                                                                .executes(WaypointCommand::set))))))
                        .then(literal("remove")
                                .then(argument("id", StringArgumentType.string())
                                        .suggests(WaypointCommand::suggestWaypoints)
                                        .executes(WaypointCommand::remove)))
                        .then(literal("tp")
                                .requires(CommandUtils::isExecutedByPlayer)
                                .then(argument("id", StringArgumentType.string()).suggests(WaypointCommand::suggestWaypoints)
                                        .executes(WaypointCommand::tp)))
                        .then(literal("rename")
                                .then(argument("oldId", StringArgumentType.string()).suggests(WaypointCommand::suggestWaypoints)
                                        .then(argument("id", StringArgumentType.string())
                                                .executes(WaypointCommand::rename))))
                        .then(literal("desc")
                                .then(argument("id", StringArgumentType.string()).suggests(WaypointCommand::suggestWaypoints)
                                        .then(argument("desc", StringArgumentType.string())
                                                .executes(WaypointCommand::desc))))
        );
    }

}
