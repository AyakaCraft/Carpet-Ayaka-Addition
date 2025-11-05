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

package com.ayakacraft.carpetayakaaddition.utils;

import com.ayakacraft.carpetayakaaddition.mixin.commands.gohome.LivingEntityAccessor;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Contract;

import java.util.Map;

public final class EntityUtils {

    @Contract(pure = true)
    public static Map<
            //#if MC>=12006
            //$$ net.minecraft.core.Holder<MobEffect>
            //#else
            MobEffect
            //#endif
            , MobEffectInstance> getActiveEffects(LivingEntity entity) {
        return ((LivingEntityAccessor) entity).getActiveEffects$Ayaka();
    }

    public static void kill(Entity entity) {
        //#if MC>=12102
        //$$ if (!entity.level().isClientSide()) {
        //$$     entity.kill((net.minecraft.server.level.ServerLevel) entity.level());;
        //$$ }
        //#else
        entity.kill();
        //#endif
    }

}
