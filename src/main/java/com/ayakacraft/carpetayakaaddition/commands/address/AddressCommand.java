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

package com.ayakacraft.carpetayakaaddition.commands.address;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaAddition;
import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.commands.AyakaCommandRegistry;
import com.ayakacraft.carpetayakaaddition.utils.CommandUtils;
import com.ayakacraft.carpetayakaaddition.utils.MathUtils;
import com.ayakacraft.carpetayakaaddition.utils.ServerPlayerUtils;
import com.ayakacraft.carpetayakaaddition.utils.text.TextUtils;
import com.ayakacraft.carpetayakaaddition.utils.translation.Translator;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.ayakacraft.carpetayakaaddition.utils.CommandUtils.sendFeedback;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public final class AddressCommand {

    public static final String NAME = "address";

    public static final String NAME_S = "ad";

    public static final Translator TR = AyakaCommandRegistry.COMMAND_TR.resolve(NAME);

    private static int root(CommandContext<ServerCommandSource> context) {
        final ServerCommandSource source = context.getSource();

        Collection<Address> addresses = AddressManager.getOrCreate(source.getServer()).getAddresses();
        List<Address>       pinned    = Lists.newLinkedList();
        List<Address>       unpinned  = Lists.newLinkedList();
        if (addresses.isEmpty()) {
            sendEmpty(source);
        }

        divide(addresses, pinned, unpinned);

        if (!unpinned.isEmpty()) {
            unpinned.sort(Comparator.naturalOrder());
            unpinned = unpinned.subList(Math.max(unpinned.size() - 5, 0), unpinned.size());
            sendFeedback(
                    source,
                    TR.tr(source, "root.header").formatted(Formatting.YELLOW, Formatting.BOLD),
                    false
            );
            sendFeedback(
                    source,
                    listWaypointIdsText(unpinned, source),
                    false
            );
        }

        sendPinned(source, pinned);

        return 1;
    }

    private static int list(CommandContext<ServerCommandSource> context) {
        final ServerCommandSource source = context.getSource();

        sendAddressList(source, AddressManager.getOrCreate(source.getServer()).getAddresses());

        return 1;
    }

    private static int detail(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source  = context.getSource();
        final String        id      = StringArgumentType.getString(context, "id");
        final Address       address = AddressManager.getOrCreate(source.getServer()).get(id);
        if (checkNull(id, address, source)) {
            return 0;
        }
        sendFeedback(source,
                TextUtils.joinTexts(new Text[]{
                        TR.tr(source, "detail.0", address.getId()).formatted(Formatting.YELLOW, Formatting.BOLD),
                        TextUtils.enter(),
                        TR.tr(source, "detail.1").formatted(Formatting.GREEN),
                        Text.literal(address.getId()),
                        TextUtils.enter(),
                        TR.tr(source, "detail.2").formatted(Formatting.GREEN),
                        Text.literal(address.getDim()),
                        TextUtils.enter(),
                        TR.tr(source, "detail.3").formatted(Formatting.GREEN),
                        Text.literal(String.format("%.2f %.2f %.2f", address.getX(), address.getY(), address.getZ())),
                        TextUtils.enter(),
                        TR.tr(source, "detail.4").formatted(Formatting.GREEN),
                        Text.literal(address.getDesc())
                }),
                false);
        address.onDetailDisplayed();
        return 1;
    }

    private static int reload(CommandContext<ServerCommandSource> context) {
        final ServerCommandSource source = context.getSource();
        try {
            AddressManager.getOrCreate(source.getServer()).load();
        } catch (IOException e) {
            CarpetAyakaAddition.LOGGER.error("Failed to load addresses", e);
            source.sendError(TR.tr(source, "reload.failure"));
            return 0;
        }
        sendFeedback(source, TR.tr(source, "reload.success"), false);
        return 1;
    }

    private static int set(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        final String id = StringArgumentType.getString(context, "id");
        //#if MC>=11600
        final net.minecraft.registry.RegistryKey<World> dim = DimensionArgumentType.getDimensionArgument(context, "dim").getRegistryKey();
        //#else
        //$$ final net.minecraft.world.dimension.DimensionType dim = DimensionArgumentType.getDimensionArgument(context, "dim");
        //#endif
        final Vec3d  pos = Vec3ArgumentType.getVec3(context, "pos");
        final String desc;
        String       s;
        try {
            s = StringArgumentType.getString(context, "desc");
        } catch (IllegalArgumentException e) {
            s = "";
        }
        desc = s;

        final ServerCommandSource source = context.getSource();

        try {
            AddressManager.getOrCreate(source.getServer()).set(new Address(id, dim, pos, desc, 0));
        } catch (IOException e) {
            CarpetAyakaAddition.LOGGER.error("Failed to save addresses", e);
            source.sendError(TR.tr(source, "save.failure"));
            return 0;
        }
        sendFeedback(source, TR.tr(source, "set.success", id), false);
        return 1;
    }

    private static int remove(CommandContext<ServerCommandSource> context) {
        final String              id     = StringArgumentType.getString(context, "id");
        final ServerCommandSource source = context.getSource();

        try {
            if (checkNull(id, AddressManager.getOrCreate(source.getServer()).remove(id), source)) {
                return 0;
            }
        } catch (IOException e) {
            CarpetAyakaAddition.LOGGER.error("Failed to save addresses", e);
            source.sendError(TR.tr(source, "save.failure"));
            return 0;
        }
        sendFeedback(source, TR.tr(source, "remove.success", id), false);
        return 1;
    }

    private static int tp(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        final ServerCommandSource source  = context.getSource();
        final String              id      = StringArgumentType.getString(context, "id");
        final Address             address = AddressManager.getOrCreate(source.getServer()).get(id);
        final ServerPlayerEntity  self    = source.getPlayerOrThrow();
        if (checkNull(id, address, source)) {
            return 0;
        }

        final ServerWorld dim = source.getServer().getWorld(address.getDimension());
        final Vec3d       pos = address.getPos();
        if (dim == null) {
            source.sendError(TR.tr(source, "tp.dimension_unrecognized", address.getDim()));
            return 0;
        }
        if (!dim.getWorldBorder().contains(new BlockPos(MathHelper.floor(pos.getX()), MathHelper.floor(pos.getY()), MathHelper.floor(pos.getZ())))) {
            source.sendError(TR.tr(source, "tp.out_of_world_border", id));
            return 0;
        }
        ServerPlayerUtils.teleport(self, dim, pos.getX(), pos.getY(), pos.getZ());
        address.onTeleportedTo();
        return 1;
    }

    private static int rename(CommandContext<ServerCommandSource> context) {
        final ServerCommandSource source  = context.getSource();
        final String              oldId   = StringArgumentType.getString(context, "oldId");
        final String              newId   = StringArgumentType.getString(context, "id");
        final AddressManager      manager = AddressManager.getOrCreate(source.getServer());
        try {
            if (checkNull(oldId, manager.rename(oldId, newId), source)) {
                return 0;
            }
        } catch (IOException e) {
            CarpetAyakaAddition.LOGGER.error("Failed to save addresses", e);
            source.sendError(TR.tr(source, "save.failure"));
            return 0;
        }
        sendFeedback(source, TR.tr(source, "rename.success", oldId, newId), false);
        return 1;
    }

    private static int desc(CommandContext<ServerCommandSource> context) {
        final ServerCommandSource source  = context.getSource();
        final AddressManager      manager = AddressManager.getOrCreate(source.getServer());
        final String              id      = StringArgumentType.getString(context, "id");
        final String              desc    = StringArgumentType.getString(context, "desc");
        final Address             w       = manager.get(id);
        if (checkNull(id, w, source)) {
            return 0;
        }
        w.setDesc(desc);
        return trySave(manager, source, TR.trS(source, "desc.success", id, desc));
    }

    private static int pin(CommandContext<ServerCommandSource> context) {
        final ServerCommandSource source  = context.getSource();
        final AddressManager      manager = AddressManager.getOrCreate(source.getServer());
        final String              id      = StringArgumentType.getString(context, "id");
        final Address             w       = manager.get(id);
        if (checkNull(id, w, source)) {
            return 0;
        }
        if (w.isPinned()) {
            return 1;
        }
        w.pin();
        return trySave(manager, source, TR.trS(source, "pin.success", id));

    }

    private static int unpin(CommandContext<ServerCommandSource> context) {
        final ServerCommandSource source  = context.getSource();
        final AddressManager      manager = AddressManager.getOrCreate(source.getServer());
        final String              id      = StringArgumentType.getString(context, "id");
        final Address             w       = manager.get(id);
        if (checkNull(id, w, source)) {
            return 0;
        }
        if (!w.isPinned()) {
            return 1;
        }
        w.unpin();
        return trySave(manager, source, TR.trS(source, "unpin.success", id));
    }

    private static int trySave(AddressManager manager, ServerCommandSource source, Supplier<MutableText> success) {
        try {
            manager.save();
        } catch (IOException e) {
            CarpetAyakaAddition.LOGGER.error("Failed to save addresses", e);
            source.sendError(TR.tr(source, "save.failure"));
            return 0;
        }
        sendFeedback(source, success.get(), false);
        return 1;
    }

    private static boolean checkNull(String id, @Nullable Address w, ServerCommandSource source) {
        if (w == null) {
            source.sendError(TR.tr(source, "not_exist", id));
            return true;
        }
        return false;
    }

    @Contract(mutates = "param2, param3")
    private static void divide(Collection<Address> addresses, List<Address> pinned, List<Address> unpinned) {
        for (Address w : addresses) {
            if (w.isPinned()) {
                pinned.add(w);
            } else {
                unpinned.add(w);
            }
        }
    }

    private static CompletableFuture<Suggestions> suggestWaypoints(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(AddressManager.getOrCreate(context.getSource().getServer()).getIDs().stream().map(id -> '"' + id + '"'), builder);
    }

    private static int listInDimension(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        final ServerCommandSource source = context.getSource();
        final World dim =
                //#if MC>=11600
                DimensionArgumentType.getDimensionArgument(context, "dim")
                //#else
                //$$ source.getMinecraftServer().getWorld(DimensionArgumentType.getDimensionArgument(context, "dim"))
                //#endif
                ;

        sendAddressList(
                source,
                AddressManager.getOrCreate(source.getServer())
                        .getAddresses()
                        .stream()
                        .filter(w -> w.isInWorld(dim))
                        .collect(Collectors.toCollection(Lists::newLinkedList))
        );

        return 1;
    }

    private static int listRadiusChunk(CommandContext<ServerCommandSource> context) {
        final ServerCommandSource source             = context.getSource();
        final ChunkPos            chunkPos           = MathUtils.getChunkPos(source.getPosition());
        final World               world              = source.getWorld();
        final int                 squaredRadiusChunk = MathUtils.square(IntegerArgumentType.getInteger(context, "radius"));

        sendAddressList(
                source,
                AddressManager.getOrCreate(source.getServer())
                        .getAddresses()
                        .stream()
                        .filter(w -> w.isInWorld(world)
                                && MathUtils.getSquaredDistance(chunkPos, w.getChunkPos()) <= squaredRadiusChunk)
                        .collect(Collectors.toCollection(Lists::newLinkedList))
        );

        return 1;
    }

    private static int listPinned(CommandContext<ServerCommandSource> context) {
        final ServerCommandSource source = context.getSource();

        List<Address> pinned = AddressManager.getOrCreate(source.getServer()).getAddresses()
                .stream()
                .filter(Address::isPinned)
                .collect(Collectors.toCollection(Lists::newLinkedList));
        if (pinned.isEmpty()) {
            sendEmpty(source);
        } else {
            sendPinned(source, pinned);
        }

        return 1;
    }

    private static int xaero(CommandContext<ServerCommandSource> context) {
        final ServerCommandSource source  = context.getSource();
        final MinecraftServer     server  = source.getServer();
        final String              id      = StringArgumentType.getString(context, "id");
        final Address             address = AddressManager.getOrCreate(server).get(id);
        if (checkNull(id, address, source)) {
            return 0;
        }
        TextUtils.broadcast(server, Text.literal(address.getXaeroWaypointString()), false);

        return 0;
    }

    private static Text waypointIdText(String id, boolean pinned, ServerCommandSource source) {
        Text t = pinned
                ? TextUtils.withCommand(TR.tr(source, "list.unpin"), String.format("/ad unpin \"%s\"", id)).formatted(Formatting.LIGHT_PURPLE)
                : TextUtils.withCommand(TR.tr(source, "list.pin"), String.format("/ad pin \"%s\"", id)).formatted(Formatting.DARK_PURPLE);
        return TextUtils.format(
                "[{}] [{}] [{}] [{}] [{}]",
                Text.literal(id).formatted(Formatting.GREEN),
                TextUtils.withCommand(TR.tr(source, "list.detail"), String.format("/ad detail \"%s\"", id)).formatted(Formatting.GOLD),
                TextUtils.withCommand(TR.tr(source, "list.tp"), String.format("/ad tp \"%s\"", id)).formatted(Formatting.RED),
                TextUtils.withCommand(TR.tr(source, "list.xaero"), String.format("/ad xaero \"%s\"", id)).formatted(Formatting.AQUA),
                t
        );
    }

    private static Text listWaypointIdsText(Collection<Address> addresses, ServerCommandSource source) {
        return TextUtils.join(addresses, TextUtils.enter(), address -> waypointIdText(address.getId(), address.isPinned(), source));
    }

    private static void sendAddressList(ServerCommandSource source, Collection<Address> addresses) {
        List<Address> pinned   = Lists.newLinkedList();
        List<Address> unpinned = Lists.newLinkedList();
        if (addresses.isEmpty()) {
            sendEmpty(source);
            return;
        }

        divide(addresses, pinned, unpinned);
        sendUnpinned(source, unpinned);
        sendPinned(source, pinned);
    }

    private static void sendEmpty(ServerCommandSource source) {
        sendFeedback(
                source,
                TR.tr(source, "list.empty").formatted(Formatting.YELLOW, Formatting.BOLD),
                false
        );
    }

    private static void sendUnpinned(ServerCommandSource source, List<Address> unpinned) {
        if (!unpinned.isEmpty()) {
            unpinned.sort(Comparator.naturalOrder());
            sendFeedback(
                    source,
                    TR.tr(source, "list.header").formatted(Formatting.YELLOW, Formatting.BOLD),
                    false
            );
            sendFeedback(
                    source,
                    listWaypointIdsText(unpinned, source),
                    false
            );
        }
    }

    private static void sendPinned(ServerCommandSource source, List<Address> pinned) {
        if (!pinned.isEmpty()) {
            pinned.sort(Comparator.naturalOrder());
            sendFeedback(
                    source,
                    TR.tr(source, "list.header.pinned").formatted(Formatting.YELLOW, Formatting.BOLD),
                    false
            );
            sendFeedback(
                    source,
                    listWaypointIdsText(pinned, source),
                    false
            );
        }
    }

    private static LiteralArgumentBuilder<ServerCommandSource> registerSubCommands(LiteralArgumentBuilder<ServerCommandSource> builder) {
        return builder
                .requires(source -> CommandUtils.checkPermission(source, CarpetAyakaSettings.commandAddress, false))
                .executes(AddressCommand::root)
                .then(literal("reload").executes(AddressCommand::reload))
                .then(literal("list").executes(AddressCommand::list)
                        .then(literal("dim").then(argument("dim", DimensionArgumentType.dimension())
                                .executes(AddressCommand::listInDimension)))
                        .then(literal("radius").then(argument("radius", IntegerArgumentType.integer(0))
                                .executes(AddressCommand::listRadiusChunk)))
                        .then(literal("pinned").executes(AddressCommand::listPinned)))
                .then(literal("detail")
                        .then(argument("id", StringArgumentType.string())
                                .suggests(AddressCommand::suggestWaypoints)
                                .executes(AddressCommand::detail)))
                .then(literal("set")
                        .then(argument("id", StringArgumentType.string()).suggests(AddressCommand::suggestWaypoints)
                                .then(argument("dim", DimensionArgumentType.dimension())
                                        .then(argument("pos", Vec3ArgumentType.vec3())
                                                .executes(AddressCommand::set)
                                                .then(argument("desc", StringArgumentType.greedyString())
                                                        .executes(AddressCommand::set))))))
                .then(literal("remove")
                        .then(argument("id", StringArgumentType.string()).suggests(AddressCommand::suggestWaypoints)
                                .executes(AddressCommand::remove)))
                .then(literal("tp")
                        .requires(source -> source.isExecutedByPlayer())
                        .then(argument("id", StringArgumentType.string()).suggests(AddressCommand::suggestWaypoints)
                                .executes(AddressCommand::tp)))
                .then(literal("rename")
                        .then(argument("oldId", StringArgumentType.string()).suggests(AddressCommand::suggestWaypoints)
                                .then(argument("id", StringArgumentType.string())
                                        .executes(AddressCommand::rename))))
                .then(literal("desc")
                        .then(argument("id", StringArgumentType.string()).suggests(AddressCommand::suggestWaypoints)
                                .then(argument("desc", StringArgumentType.greedyString())
                                        .executes(AddressCommand::desc))))
                .then(literal("xaero")
                        .then(argument("id", StringArgumentType.string()).suggests(AddressCommand::suggestWaypoints)
                                .executes(AddressCommand::xaero)))
                .then(literal("pin")
                        .then(argument("id", StringArgumentType.string()).suggests(AddressCommand::suggestWaypoints)
                                .executes(AddressCommand::pin)))
                .then(literal("unpin")
                        .then(argument("id", StringArgumentType.string()).suggests(AddressCommand::suggestWaypoints)
                                .executes(AddressCommand::unpin)));
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(registerSubCommands(literal(NAME)));
        dispatcher.register(registerSubCommands(literal(NAME_S)));
    }

}
