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

package com.ayakacraft.carpetayakaaddition.utils.text;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class TextFormatter {

    private static void divide(Object[] args, List<Text> textArgs, List<Object> objArgs) {
        for (Object o : args) {
            if (o instanceof Text) {
                textArgs.add((Text) o);
            } else {
                objArgs.add(o);
            }
        }
    }

    public static MutableText format(String format, Object... args) {
        if (args == null || args.length == 0) {
            return Text.literal(format);
        }

        List<Text>   textArgs = new LinkedList<>();
        List<Object> objArgs  = new LinkedList<>();
        divide(args, textArgs, objArgs);

        return format(String.format(format, objArgs.toArray()), textArgs);
    }

    public static MutableText format(String format, List<Text> args) {
        if (args == null || args.isEmpty()) {
            return Text.literal(format);
        }

        int            lenMinusOne   = format.length() - 1;
        int            j     = 0;
        Iterator<Text> it    = args.iterator();
        List<String>   split = new LinkedList<>();
        for (int i = 0; i < lenMinusOne; i++) {
            if ("{}".equals(format.substring(i, i + 2)) && it.hasNext()) {
                split.add(format.substring(j, i));
                j = i + 2;
                i++;
            }
        }
        split.add(format.substring(j));

        int         sizeMinusOne = split.size() - 1;
        MutableText txt          = TextUtils.empty();
        for (int i = 0; i < sizeMinusOne; i++) {
            txt.append(split.get(i)).append(args.get(i));
        }
        txt.append(split.get(sizeMinusOne));

        return txt;
    }

}
