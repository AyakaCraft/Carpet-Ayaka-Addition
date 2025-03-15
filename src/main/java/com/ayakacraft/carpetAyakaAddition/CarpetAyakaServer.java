package com.ayakacraft.carpetayakaaddition;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.ayakacraft.carpetayakaaddition.commands.*;
import com.ayakacraft.carpetayakaaddition.commands.waypoint.WaypointCommand;
import com.ayakacraft.carpetayakaaddition.commands.waypoint.WaypointManager;
import com.ayakacraft.carpetayakaaddition.utils.TickTask;
import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.CommandDispatcher;
//#if MC>=11900
import net.minecraft.command.CommandRegistryAccess;
//#endif
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class CarpetAyakaServer implements CarpetExtension {

    public static final CarpetAyakaServer INSTANCE = new CarpetAyakaServer();

    private final LinkedList<TickTask> tickTasks = new LinkedList<>();

    private final LinkedBlockingQueue<TickTask> preTickTasks = new LinkedBlockingQueue<>();

    public MinecraftServer mcServer;

    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(CarpetAyakaSettings.class);
    }

    @Override
    public void onServerLoaded(MinecraftServer server) {
        this.mcServer = server;
    }

    @Override
    public void onServerLoadedWorlds(MinecraftServer server) {
        WaypointManager.getWaypointManager(mcServer);
    }

    @Override
    public void onTick(MinecraftServer server) {
        preTickTasks.forEach(TickTask::start);
        preTickTasks.drainTo(tickTasks);
        tickTasks.forEach(TickTask::tick);
        new LinkedList<>(tickTasks).stream().filter(TickTask::isFinished).forEach(tickTasks::remove);
    }

    @Override
    //#if MC>=11900
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, final CommandRegistryAccess registryAccess) {
        TptCommand.register(dispatcher);
        GoHomeCommand.register(dispatcher);
        WaypointCommand.register(dispatcher);
        CCommand.register(dispatcher);
        KillItemCommand.register(dispatcher);
    }
    //#else
    //$$ public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
    //$$     TptCommand.register(dispatcher);
    //$$     GoHomeCommand.register(dispatcher);
    //$$     WaypointCommand.register(dispatcher);
    //$$     CCommand.register(dispatcher);
    //$$     KillItemCommand.register(dispatcher);
    //$$ }
    //#endif

    @Override
    public void onServerClosed(MinecraftServer server) {
        WaypointManager.removeWaypointManager(mcServer);

        this.mcServer = null;
    }

    @Override
    public String version() {
        return CarpetAyakaAddition.MOD_ID;
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        final InputStream langStream = CarpetAyakaServer.class.getClassLoader().getResourceAsStream(String.format("assets/carpet-ayaka-addition/lang/%s.json", lang));
        if (langStream == null) {
            // we don't have that language
            return Collections.emptyMap();
        }
        final String jsonData;
        try {
            byte[] data = new byte[langStream.available()];
            int    i    = langStream.read(data);
            if (i != data.length) {
                data = Arrays.copyOf(data, i);
            }
            jsonData = new String(data, StandardCharsets.UTF_8);
            langStream.close();
        } catch (final IOException e) {
            return Collections.emptyMap();
        }
        return CarpetAyakaAddition.GSON.fromJson(jsonData, new TypeToken<Map<String, String>>() {
        }.getType());
    }

    public void addTickTask(TickTask tickTask) {
        preTickTasks.add(tickTask);
    }

}
