package com.ayakacraft.carpetAyakaAddition.commands;

import com.ayakacraft.carpetAyakaAddition.CarpetAyakaSettings;
import com.ayakacraft.carpetAyakaAddition.util.CommandUtils;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.TypeFilter;

import java.util.LinkedList;
import java.util.List;

import static net.minecraft.server.command.CommandManager.literal;


public class ClearItemCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("clearitem")
                        .requires(source -> CommandUtils.checkPermission(source, CarpetAyakaSettings.commandClearItem, true))
                        .executes(context -> {
                            ServerCommandSource source  = context.getSource();
                            List<ItemEntity>    targets = new LinkedList<>();
                            source.getServer().getWorlds().forEach(world -> targets.addAll(world.getEntitiesByType(TypeFilter.instanceOf(ItemEntity.class), itemEntity -> true)));
                            if (targets.isEmpty()) {
                                CommandUtils.sendFeedback(source, () -> Text.translatable("command.carpet-ayaka-addition.clearitem.none"), true);
                                return 0;
                            }
                            targets.forEach(Entity::kill);
                            if (targets.size() == 1) {
                                CommandUtils.sendFeedback(source, () -> Text.translatable("command.carpet-ayaka-addition.clearitem.single"), true);
                            } else {
                                CommandUtils.sendFeedback(source, () -> Text.translatable("command.carpet-ayaka-addition.clearitem.multiple", targets.size()), true);
                            }
                            return 1;
                        }));
    }

}
