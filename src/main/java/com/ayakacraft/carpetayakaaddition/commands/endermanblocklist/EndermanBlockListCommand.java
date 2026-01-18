/*
 * This file is part of the null project, licensed under the
 * GNU General Public License v3.0
 *
 * Copyright (C) 2026  Calboot and contributors
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

package com.ayakacraft.carpetayakaaddition.commands.endermanblocklist;

import com.ayakacraft.carpetayakaaddition.CarpetAyakaSettings;
import com.ayakacraft.carpetayakaaddition.commands.AyakaCommandRegistry;
import com.ayakacraft.carpetayakaaddition.utils.CommandUtils;
import com.ayakacraft.carpetayakaaddition.utils.IdentifierUtils;
import com.ayakacraft.carpetayakaaddition.utils.translation.Translator;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Locale;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.ayakacraft.carpetayakaaddition.CarpetAyakaAddition.LOGGER;
import static com.ayakacraft.carpetayakaaddition.utils.CommandUtils.sendFeedback;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public final class EndermanBlockListCommand {

    public static final String NAME = "endermanBlockList";

    public static final Translator TR = AyakaCommandRegistry.COMMAND_TR.resolve(NAME);

    private static EndermanBlockListConfig getConfig(MinecraftServer server) {
        try {
            return EndermanBlockListConfig.get(server);
        } catch (IOException e) {
            LOGGER.warn("Failed to get enderman_block_list", e);
            return null;
        }
    }

    private static int saveConfig(MinecraftServer server, Consumer<@NotNull EndermanBlockListConfig> task) {
        EndermanBlockListConfig config = getConfig(server);
        if (config == null) {
            return 0;
        }
        task.accept(config);
        try {
            EndermanBlockListConfig.save(server);
        } catch (IOException e) {
            LOGGER.warn("Failed to save enderman_block_list", e);
            return 0;
        }
        return 1;
    }

    private static int saveConfigWithCondition(MinecraftServer server, Function<@NotNull EndermanBlockListConfig, @NotNull Boolean> task) {
        EndermanBlockListConfig config = getConfig(server);
        if (config == null) {
            return 0;
        }
        if (task.apply(config)) {
            try {
                EndermanBlockListConfig.save(server);
            } catch (IOException e) {
                LOGGER.warn("Failed to save enderman_block_list", e);
                return 0;
            }
        }
        return 1;
    }

    private static int clearBlacklist(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        return saveConfig(source.getServer(), config -> {
            config.getBlacklist().clear();
            sendFeedback(source, TR.tr("blacklist.clear"), false);
        });
    }

    private static int addToBlacklist(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String             block  = IdentifierUtils.ofBlock(BlockStateArgument.getBlock(context, "block").getState().getBlock()).toString();
        return saveConfigWithCondition(
                source.getServer(),
                config -> {
                    boolean b = config.getBlacklist().add(block);
                    sendFeedback(source, TR.tr("blacklist.add", block), false);
                    return b;
                }
        );
    }

    private static int removeFromBlacklist(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String             block  = IdentifierUtils.ofBlock(BlockStateArgument.getBlock(context, "block").getState().getBlock()).toString();
        return saveConfigWithCondition(
                source.getServer(),
                config -> {
                    boolean b = config.getBlacklist().remove(block);
                    sendFeedback(source, TR.tr("blacklist.remove", block), false);
                    return b;
                }
        );
    }

    private static int clearWhitelist(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        return saveConfig(source.getServer(), config -> {
            config.getBlacklist().clear();
            sendFeedback(source, TR.tr("whitelist.clear"), false);
        });
    }

    private static int addToWhitelist(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String             block  = IdentifierUtils.ofBlock(BlockStateArgument.getBlock(context, "block").getState().getBlock()).toString();
        return saveConfigWithCondition(
                source.getServer(),
                config -> {
                    boolean b = config.getWhitelist().add(block);
                    sendFeedback(source, TR.tr("whitelist.add", block), false);
                    return b;
                }
        );
    }

    private static int removeFromWhitelist(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        String             block  = IdentifierUtils.ofBlock(BlockStateArgument.getBlock(context, "block").getState().getBlock()).toString();
        return saveConfigWithCondition(
                source.getServer(),
                config -> {
                    boolean b = config.getWhitelist().remove(block);
                    sendFeedback(source, TR.tr("whitelist.remove", block), false);
                    return b;
                }
        );
    }

    private static int get(CommandContext<CommandSourceStack> context) {
        CommandSourceStack           source = context.getSource();
        EndermanBlockListConfig      config = getConfig(source.getServer());
        EndermanBlockListConfig.Type type   = config == null ? EndermanBlockListConfig.Type.VANILLA : config.getType();
        sendFeedback(
                source,
                TR.tr("type", type.name().toLowerCase(Locale.ROOT)),
                false
        );
        StringJoiner sj = new StringJoiner("\", \"", "[\"", "\"]").setEmptyValue("[]");
        switch (type) {
            case BLACKLIST:
            case BLACKLIST_LOOSE:
                sendFeedback(
                        source,
                        TR.tr("blacklist"),
                        false
                );
                for (String s : config.getBlacklist()) {
                    sj.add(s);
                }
                break;
            case WHITELIST:
                sendFeedback(
                        source,
                        TR.tr("whitelist"),
                        false
                );
                for (String s : config.getWhitelist()) {
                    sj.add(s);
                }
                break;
            default:
                return 1;
        }
        sendFeedback(
                source,
                Component.literal(sj.toString()),
                false
        );
        return 1;
    }

    private static int setType(CommandContext<CommandSourceStack> context, @NotNull EndermanBlockListConfig.Type type) {
        return saveConfig(context.getSource().getServer(), config -> config.setType(type));
    }

    private static CompletableFuture<Suggestions> suggestBlacklist(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        try {
            EndermanBlockListConfig config = EndermanBlockListConfig.get(context.getSource().getServer());
            return SharedSuggestionProvider.suggest(config.getBlacklist(), builder);
        } catch (IOException e) {
            LOGGER.warn("Failed to get enderman_block_list", e);
        }
        return SharedSuggestionProvider.suggest(new String[0], builder);
    }

    private static CompletableFuture<Suggestions> suggestWhitelist(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        try {
            EndermanBlockListConfig config = EndermanBlockListConfig.get(context.getSource().getServer());
            return SharedSuggestionProvider.suggest(config.getWhitelist(), builder);
        } catch (IOException e) {
            LOGGER.warn("Failed to get enderman_block_list", e);
        }
        return SharedSuggestionProvider.suggest(new String[0], builder);
    }

    private static LiteralArgumentBuilder<CommandSourceStack> buildTypes() {
        LiteralArgumentBuilder<CommandSourceStack> res = literal("type");
        for (EndermanBlockListConfig.Type type : EndermanBlockListConfig.Type.values()) {
            res.then(literal(type.name().toLowerCase(Locale.ROOT))
                    .executes(context -> setType(context, type))
            );
        }
        return res;
    }

    public static void register(
            CommandDispatcher<CommandSourceStack> dispatcher
            //#if MC>=11900
            , net.minecraft.commands.CommandBuildContext buildContext
            //#endif
    ) {
        dispatcher.register(
                literal(NAME)
                        .requires(source -> CommandUtils.checkPermission(source, CarpetAyakaSettings.commandEndermanBlockList, false))
                        .executes(EndermanBlockListCommand::get)
                        .then(buildTypes())
                        .then(literal("blacklist")
                                .then(literal("clear")
                                        .executes(EndermanBlockListCommand::clearBlacklist))
                                .then(literal("add")
                                        .then(argument("block", BlockStateArgument.block(
                                                //#if MC>=11900
                                                buildContext
                                                //#endif
                                        ))
                                                .executes(EndermanBlockListCommand::addToBlacklist)))
                                .then(literal("remove")
                                        .then(argument("block", BlockStateArgument.block(
                                                //#if MC>=11900
                                                buildContext
                                                //#endif
                                        )).suggests(EndermanBlockListCommand::suggestBlacklist)
                                                .executes(EndermanBlockListCommand::removeFromBlacklist))))
                        .then(literal("whitelist")
                                .then(literal("clear")
                                        .executes(EndermanBlockListCommand::clearWhitelist))
                                .then(literal("add")
                                        .then(argument("block", BlockStateArgument.block(
                                                //#if MC>=11900
                                                buildContext
                                                //#endif
                                        ))
                                                .executes(EndermanBlockListCommand::addToWhitelist)))
                                .then(literal("remove")
                                        .then(argument("block", BlockStateArgument.block(
                                                //#if MC>=11900
                                                buildContext
                                                //#endif
                                        )).suggests(EndermanBlockListCommand::suggestWhitelist)
                                                .executes(EndermanBlockListCommand::removeFromWhitelist))))
        );
    }

}
