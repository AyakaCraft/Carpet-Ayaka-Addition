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
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
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
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public final class AddressCommand {

    public static final String NAME = "address";

    public static final String NAME_S = "ad";

    public static final Translator TR = AyakaCommandRegistry.COMMAND_TR.resolve(NAME);

    private static int root(CommandContext<CommandSourceStack> context) {
        final CommandSourceStack source = context.getSource();

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
                    TR.tr(source, "root.header").withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD),
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

    private static int list(CommandContext<CommandSourceStack> context) {
        final CommandSourceStack source = context.getSource();

        sendAddressList(source, AddressManager.getOrCreate(source.getServer()).getAddresses());

        return 1;
    }

    private static int detail(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source  = context.getSource();
        final String       id      = StringArgumentType.getString(context, "id");
        final Address      address = AddressManager.getOrCreate(source.getServer()).get(id);
        if (checkNull(id, address, source)) {
            return 0;
        }
        sendFeedback(source,
                TextUtils.joinTexts(new Component[]{
                        TR.tr(source, "detail.0", address.getId()).withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD),
                        TextUtils.enter(),
                        TR.tr(source, "detail.1").withStyle(ChatFormatting.GREEN),
                        Component.literal(address.getId()),
                        TextUtils.enter(),
                        TR.tr(source, "detail.2").withStyle(ChatFormatting.GREEN),
                        Component.literal(address.getDim()),
                        TextUtils.enter(),
                        TR.tr(source, "detail.3").withStyle(ChatFormatting.GREEN),
                        Component.literal(String.format("%.2f %.2f %.2f", address.getX(), address.getY(), address.getZ())),
                        TextUtils.enter(),
                        TR.tr(source, "detail.4").withStyle(ChatFormatting.GREEN),
                        Component.literal(address.getDesc())
                }),
                false);
        address.onDetailDisplayed();
        return 1;
    }

    private static int reload(CommandContext<CommandSourceStack> context) {
        final CommandSourceStack source = context.getSource();
        try {
            AddressManager.getOrCreate(source.getServer()).load();
        } catch (IOException e) {
            CarpetAyakaAddition.LOGGER.error("Failed to load addresses", e);
            source.sendFailure(TR.tr(source, "reload.failure"));
            return 0;
        }
        sendFeedback(source, TR.tr(source, "reload.success"), false);
        return 1;
    }

    private static int set(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final String id = StringArgumentType.getString(context, "id");
        //#if MC>=11600
        final net.minecraft.resources.ResourceKey<Level> dim = DimensionArgument.getDimension(context, "dim").dimension();
        //#else
        //$$ final net.minecraft.world.level.dimension.DimensionType dim = DimensionTypeArgument.getDimension(context, "dim");
        //#endif
        final Vec3   pos = Vec3Argument.getVec3(context, "pos");
        final String desc;
        String       s;
        try {
            s = StringArgumentType.getString(context, "desc");
        } catch (IllegalArgumentException e) {
            s = "";
        }
        desc = s;

        final CommandSourceStack source = context.getSource();

        try {
            AddressManager.getOrCreate(source.getServer()).set(new Address(id, dim, pos, desc, 0));
        } catch (IOException e) {
            CarpetAyakaAddition.LOGGER.error("Failed to save addresses", e);
            source.sendFailure(TR.tr(source, "save.failure"));
            return 0;
        }
        sendFeedback(source, TR.tr(source, "set.success", id), false);
        return 1;
    }

    private static int remove(CommandContext<CommandSourceStack> context) {
        final String             id     = StringArgumentType.getString(context, "id");
        final CommandSourceStack source = context.getSource();

        try {
            if (checkNull(id, AddressManager.getOrCreate(source.getServer()).remove(id), source)) {
                return 0;
            }
        } catch (IOException e) {
            CarpetAyakaAddition.LOGGER.error("Failed to save addresses", e);
            source.sendFailure(TR.tr(source, "save.failure"));
            return 0;
        }
        sendFeedback(source, TR.tr(source, "remove.success", id), false);
        return 1;
    }

    private static int tp(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final CommandSourceStack source  = context.getSource();
        final String             id      = StringArgumentType.getString(context, "id");
        final Address            address = AddressManager.getOrCreate(source.getServer()).get(id);
        final ServerPlayer       self    = source.getPlayerOrException();
        if (checkNull(id, address, source)) {
            return 0;
        }

        final ServerLevel dim = source.getServer().getLevel(address.getDimension());
        final Vec3        pos = address.getPos();
        if (dim == null) {
            source.sendFailure(TR.tr(source, "tp.dimension_unrecognized", address.getDim()));
            return 0;
        }
        if (!dim.getWorldBorder().isWithinBounds(new BlockPos(Mth.floor(pos.x()), Mth.floor(pos.y()), Mth.floor(pos.z())))) {
            source.sendFailure(TR.tr(source, "tp.out_of_world_border", id));
            return 0;
        }
        ServerPlayerUtils.teleport(self, dim, pos.x(), pos.y(), pos.z());
        address.onTeleportedTo();
        return 1;
    }

    private static int rename(CommandContext<CommandSourceStack> context) {
        final CommandSourceStack source  = context.getSource();
        final String             oldId   = StringArgumentType.getString(context, "oldId");
        final String             newId   = StringArgumentType.getString(context, "id");
        final AddressManager     manager = AddressManager.getOrCreate(source.getServer());
        try {
            if (checkNull(oldId, manager.rename(oldId, newId), source)) {
                return 0;
            }
        } catch (IOException e) {
            CarpetAyakaAddition.LOGGER.error("Failed to save addresses", e);
            source.sendFailure(TR.tr(source, "save.failure"));
            return 0;
        }
        sendFeedback(source, TR.tr(source, "rename.success", oldId, newId), false);
        return 1;
    }

    private static int desc(CommandContext<CommandSourceStack> context) {
        final CommandSourceStack source  = context.getSource();
        final AddressManager     manager = AddressManager.getOrCreate(source.getServer());
        final String             id      = StringArgumentType.getString(context, "id");
        final String             desc    = StringArgumentType.getString(context, "desc");
        final Address            w       = manager.get(id);
        if (checkNull(id, w, source)) {
            return 0;
        }
        w.setDesc(desc);
        return trySave(manager, source, TR.trS(source, "desc.success", id, desc));
    }

    private static int pin(CommandContext<CommandSourceStack> context) {
        final CommandSourceStack source  = context.getSource();
        final AddressManager     manager = AddressManager.getOrCreate(source.getServer());
        final String             id      = StringArgumentType.getString(context, "id");
        final Address            w       = manager.get(id);
        if (checkNull(id, w, source)) {
            return 0;
        }
        if (w.isPinned()) {
            return 1;
        }
        w.pin();
        return trySave(manager, source, TR.trS(source, "pin.success", id));

    }

    private static int unpin(CommandContext<CommandSourceStack> context) {
        final CommandSourceStack source  = context.getSource();
        final AddressManager     manager = AddressManager.getOrCreate(source.getServer());
        final String             id      = StringArgumentType.getString(context, "id");
        final Address            w       = manager.get(id);
        if (checkNull(id, w, source)) {
            return 0;
        }
        if (!w.isPinned()) {
            return 1;
        }
        w.unpin();
        return trySave(manager, source, TR.trS(source, "unpin.success", id));
    }

    private static int listInDimension(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final CommandSourceStack source = context.getSource();
        final Level dim =
                //#if MC>=11600
                DimensionArgument.getDimension(context, "dim")
                //#else
                //$$ source.getServer().getLevel(DimensionTypeArgument.getDimension(context, "dim"))
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

    private static int listRadiusChunk(CommandContext<CommandSourceStack> context) {
        final CommandSourceStack source             = context.getSource();
        final ChunkPos           chunkPos           = MathUtils.getChunkPos(source.getPosition());
        final Level              world              = source.getLevel();
        final int                squaredRadiusChunk = MathUtils.square(IntegerArgumentType.getInteger(context, "radius"));

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

    private static int listPinned(CommandContext<CommandSourceStack> context) {
        final CommandSourceStack source = context.getSource();

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

    private static int xaero(CommandContext<CommandSourceStack> context) {
        final CommandSourceStack source  = context.getSource();
        final MinecraftServer    server  = source.getServer();
        final String             id      = StringArgumentType.getString(context, "id");
        final Address            address = AddressManager.getOrCreate(server).get(id);
        if (checkNull(id, address, source)) {
            return 0;
        }
        TextUtils.broadcast(server, Component.literal(address.getXaeroWaypointString()), false);

        return 1;
    }

    private static int trySave(AddressManager manager, CommandSourceStack source, Supplier<MutableComponent> success) {
        try {
            manager.save();
        } catch (IOException e) {
            CarpetAyakaAddition.LOGGER.error("Failed to save addresses", e);
            source.sendFailure(TR.tr(source, "save.failure"));
            return 0;
        }
        sendFeedback(source, success.get(), false);
        return 1;
    }

    private static boolean checkNull(String id, @Nullable Address w, CommandSourceStack source) {
        if (w == null) {
            source.sendFailure(TR.tr(source, "not_exist", id));
            return true;
        }
        return false;
    }

    private static boolean canTp(CommandSourceStack source) {
        return CommandUtils.checkPermission(source, CarpetAyakaSettings.commandAddressTp, true);
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

    private static CompletableFuture<Suggestions> suggestWaypoints(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(AddressManager.getOrCreate(context.getSource().getServer()).getIDs().stream().map(id -> '"' + id + '"'), builder);
    }

    private static Component waypointIdText(String id, boolean pinned, CommandSourceStack source) {
        Component t = pinned
                ? TextUtils.withCommand(TR.tr(source, "list.unpin"), String.format("/ad unpin \"%s\"", id)).withStyle(ChatFormatting.LIGHT_PURPLE)
                : TextUtils.withCommand(TR.tr(source, "list.pin"), String.format("/ad pin \"%s\"", id)).withStyle(ChatFormatting.DARK_PURPLE);
        if (canTp(source)) {
            return TextUtils.format(
                    "[{}] [{}] [{}] [{}] [{}]",
                    Component.literal(id).withStyle(ChatFormatting.GREEN),
                    TextUtils.withCommand(TR.tr(source, "list.detail"), String.format("/ad detail \"%s\"", id)).withStyle(ChatFormatting.GOLD),
                    TextUtils.withCommand(TR.tr(source, "list.tp"), String.format("/ad tp \"%s\"", id)).withStyle(ChatFormatting.RED),
                    TextUtils.withCommand(TR.tr(source, "list.xaero"), String.format("/ad xaero \"%s\"", id)).withStyle(ChatFormatting.AQUA),
                    t
            );
        } else {
            return TextUtils.format(
                    "[{}] [{}] [{}] [{}]",
                    Component.literal(id).withStyle(ChatFormatting.GREEN),
                    TextUtils.withCommand(TR.tr(source, "list.detail"), String.format("/ad detail \"%s\"", id)).withStyle(ChatFormatting.GOLD),
                    TextUtils.withCommand(TR.tr(source, "list.xaero"), String.format("/ad xaero \"%s\"", id)).withStyle(ChatFormatting.AQUA),
                    t
            );
        }
    }

    private static Component listWaypointIdsText(Collection<Address> addresses, CommandSourceStack source) {
        return TextUtils.join(addresses, TextUtils.enter(), address -> waypointIdText(address.getId(), address.isPinned(), source));
    }

    private static void sendAddressList(CommandSourceStack source, Collection<Address> addresses) {
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

    private static void sendEmpty(CommandSourceStack source) {
        sendFeedback(
                source,
                TR.tr(source, "list.empty").withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD),
                false
        );
    }

    private static void sendUnpinned(CommandSourceStack source, List<Address> unpinned) {
        if (!unpinned.isEmpty()) {
            unpinned.sort(Comparator.naturalOrder());
            sendFeedback(
                    source,
                    TR.tr(source, "list.header").withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD),
                    false
            );
            sendFeedback(
                    source,
                    listWaypointIdsText(unpinned, source),
                    false
            );
        }
    }

    private static void sendPinned(CommandSourceStack source, List<Address> pinned) {
        if (!pinned.isEmpty()) {
            pinned.sort(Comparator.naturalOrder());
            sendFeedback(
                    source,
                    TR.tr(source, "list.header.pinned").withStyle(ChatFormatting.YELLOW, ChatFormatting.BOLD),
                    false
            );
            sendFeedback(
                    source,
                    listWaypointIdsText(pinned, source),
                    false
            );
        }
    }

    private static LiteralArgumentBuilder<CommandSourceStack> registerSubCommands(LiteralArgumentBuilder<CommandSourceStack> builder) {
        return builder
                .requires(source -> CommandUtils.checkPermission(source, CarpetAyakaSettings.commandAddress, false))
                .executes(AddressCommand::root)
                .then(literal("reload").executes(AddressCommand::reload))
                .then(literal("list").executes(AddressCommand::list)
                        .then(literal("dim").then(argument("dim", DimensionArgument.dimension())
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
                                .then(argument("dim", DimensionArgument.dimension())
                                        .then(argument("pos", Vec3Argument.vec3())
                                                .executes(AddressCommand::set)
                                                .then(argument("desc", StringArgumentType.greedyString())
                                                        .executes(AddressCommand::set))))))
                .then(literal("remove")
                        .then(argument("id", StringArgumentType.string()).suggests(AddressCommand::suggestWaypoints)
                                .executes(AddressCommand::remove)))
                .then(literal("tp")
                        .requires(AddressCommand::canTp)
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

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(registerSubCommands(literal(NAME)));
        dispatcher.register(registerSubCommands(literal(NAME_S)));
    }

}
