package com.ayakacraft.carpetAyakaAddition.commands;

import com.ayakacraft.carpetAyakaAddition.CarpetAyakaSettings;
import com.ayakacraft.carpetAyakaAddition.util.CommandUtils;
import com.ayakacraft.carpetAyakaAddition.util.MethodWrapper;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
//#if MC>=11700
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.TypeFilter;
//#endif

import java.util.LinkedList;
import java.util.List;

import static net.minecraft.server.command.CommandManager.literal;


public final class KillItemCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("killitem")
                        .requires(source -> CommandUtils.checkPermission(source, CarpetAyakaSettings.commandKillItem, true))
                        .executes(context -> {
                            ServerCommandSource source  = context.getSource();
                            List<Entity>        targets = new LinkedList<>();
                            source.getServer().getWorlds().forEach(world -> {
                                targets.addAll(world.getEntitiesByType(
                                        //#if MC>=11700
                                        TypeFilter.instanceOf(ItemEntity.class),
                                        //#else
                                        //$$ net.minecraft.entity.EntityType.ITEM,
                                        //#endif
                                        itemEntity -> true));
                            });
                            if (targets.isEmpty()) {
                                MethodWrapper.sendFeedback(source, () -> MethodWrapper.translatableText("command.carpet-ayaka-addition.killitem.none"), true);
                                return 0;
                            }
                            targets.forEach(Entity::kill);
                            if (targets.size() == 1) {
                                MethodWrapper.sendFeedback(source, () -> MethodWrapper.translatableText("command.carpet-ayaka-addition.killitem.single"), true);
                            } else {
                                MethodWrapper.sendFeedback(source, () -> MethodWrapper.translatableText("command.carpet-ayaka-addition.killitem.multiple", targets.size()), true);
                            }
                            return 1;
                        }));
    }

}
