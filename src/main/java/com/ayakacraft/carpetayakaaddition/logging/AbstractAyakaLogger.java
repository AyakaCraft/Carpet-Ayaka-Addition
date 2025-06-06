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

import carpet.logging.Logger;

import java.lang.reflect.Field;

//Do not remove the lines below
//TODO update in 1.14.4
public abstract class AbstractAyakaLogger extends Logger implements AyakaExtensionLogger {

    public AbstractAyakaLogger(String logName, String def, String[] options, boolean strictOptions) throws NoSuchFieldException {
        this(AyakaLoggerRegistry.class.getField("__" + logName), logName, def, options, strictOptions);
    }

    public AbstractAyakaLogger(Field field, String logName, String def, String[] options, boolean strictOptions) {
        super(
                field, logName, def, options
                //#if MC>=11700
                , strictOptions
                //#endif
        );
    }

}
