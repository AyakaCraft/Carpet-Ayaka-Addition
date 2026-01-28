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

package com.ayakacraft.carpetayakaaddition.mixin.rules.betterOpPlayerNoCheat;

import com.ayakacraft.carpetayakaaddition.helpers.mods.TISHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.commands.CloneCommands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

//Do not remove the lines below
//TODO update in 1.21.5
@Mixin(CloneCommands.class)
public class CloneCommandMixin {

    @ModifyArg(
            method = "register",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;",
                    remap = false
            )
    )
    private static Predicate<CommandSourceStack> checkIfAllowCheating(Predicate<CommandSourceStack> original) {
        return original.and(TISHelper::canCheat);
    }

}
