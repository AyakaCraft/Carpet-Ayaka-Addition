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

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileUtils {

    public static String readResource(String path) throws IOException {
        InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            throw new IOException("Null input stream from path " + path);
        }
        return org.apache.commons.io.IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }

    public static String readString(Path path) throws IOException {
        return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(Files.readAllBytes(path))).toString();
    }

}
