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

package com.ayakacraft.carpetayakaaddition.logging;

import carpet.logging.HUDLogger;
import carpet.logging.LoggerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;

import java.lang.reflect.Field;

public abstract class AbstractAyakaHUDLogger extends HUDLogger implements AyakaExtensionLogger {

    //#if MC<11500
    private Field acceleratorField;
    //#endif

    public AbstractAyakaHUDLogger(String logName, String def, String[] options, boolean strictOptions) throws NoSuchFieldException {
        this(AyakaLoggerRegistry.class.getField("__"+logName), logName, def, options, strictOptions);
    }

    public AbstractAyakaHUDLogger(Field field, String logName, String def, String[] options, boolean strictOptions) {
        super(
                //#if MC>=11500
                field,
                //#endif
                logName, def, options
                //#if MC>=11700
                , strictOptions
                //#endif
        );

        //#if MC<11500
        //$$ this.acceleratorField = field;
        //#endif
    }

    //#if MC<11500
    //$$ public Field getField() {
    //$$     return acceleratorField;
    //$$ }
    //#endif

    public void doLogging() {
        LoggerRegistry.getLogger(getLogName()).log(this::onHUDUpdate);
    }

    public abstract MutableText[] onHUDUpdate(String playerOption, PlayerEntity player);

}
