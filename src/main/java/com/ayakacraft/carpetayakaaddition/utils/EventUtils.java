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

import com.ayakacraft.carpetayakaaddition.utils.preprocess.PreprocessPattern;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;

public final class EventUtils {

    @PreprocessPattern
    private static ClickEvent runCommand(String cmd) {
        //#if MC>=12105
        //$$ return new ClickEvent.RunCommand(cmd);
        //#else
        return new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd);
        //#endif
    }

    @PreprocessPattern
    private static HoverEvent showText(Text txt) {
        //#if MC>=12105
        //$$ return new HoverEvent.ShowText(txt);
        //#else
        return new HoverEvent(HoverEvent.Action.SHOW_TEXT, txt);
        //#endif
    }

}
