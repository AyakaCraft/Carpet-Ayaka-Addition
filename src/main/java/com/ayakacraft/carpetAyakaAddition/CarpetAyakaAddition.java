package com.ayakacraft.carpetAyakaAddition;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.ayakacraft.carpetAyakaAddition.commands.*;
import com.ayakacraft.carpetAyakaAddition.data.WaypointManager;
import com.ayakacraft.carpetAyakaAddition.event.AfterBlockBreakHandler;
import com.ayakacraft.carpetAyakaAddition.stats.Statistics;
import com.ayakacraft.carpetAyakaAddition.util.TickTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
//#if MC>=11900
import net.minecraft.command.CommandRegistryAccess;
//#endif
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class CarpetAyakaAddition implements ModInitializer, CarpetExtension {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().create();

    public static final String MOD_ID = "carpet-ayaka-addition";

    public static final String MOD_NAME = "Carpet Ayaka Addition";

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static final String MOD_VERSION;

    public static final CarpetAyakaAddition INSTANCE = new CarpetAyakaAddition();

    static {
        Optional<ModContainer> o = FabricLoader.getInstance().getModContainer(MOD_ID);
        if (o.isPresent()) {
            MOD_VERSION = o.get().getMetadata().getVersion().toString();
        } else {
            MOD_VERSION = "dev";
        }
    }

    public static Identifier identifier(String path) {
        //#if MC>=11900
        return Identifier.of(MOD_ID, path);
        //#else
        //$$ return new Identifier(MOD_ID, path);
        //#endif
    }

    private final LinkedList<TickTask> tickTasks = new LinkedList<>();

    private final LinkedBlockingQueue<TickTask> preTickTasks = new LinkedBlockingQueue<>();

    public MinecraftServer mcServer;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing {} Version {}", MOD_NAME, MOD_VERSION);
        Statistics.registerAll();
        registerEvents();

        CarpetServer.manageExtension(INSTANCE);
    }

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
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, final CommandRegistryAccess commandBuildContext) {
        //#else
        //$$ public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        //#endif
        TptCommand.register(dispatcher);
        GoHomeCommand.register(dispatcher);
        WaypointCommand.register(dispatcher);
        CCommand.register(dispatcher);
        KillItemCommand.register(dispatcher);
    }

    @Override
    public void onServerClosed(MinecraftServer server) {
        WaypointManager.removeWaypointManager(mcServer);

        this.mcServer = null;
    }

    @Override
    public String version() {
        return MOD_ID;
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        final InputStream langStream = CarpetAyakaAddition.class.getClassLoader().getResourceAsStream(String.format("assets/carpet-ayaka-addition/lang/%s.json", lang));
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
        return GSON.fromJson(jsonData, new TypeToken<Map<String, String>>() {
        }.getType());
    }

    public void addTickTask(TickTask tickTask) {
        preTickTasks.add(tickTask);
    }

    public void registerEvents() {
        PlayerBlockBreakEvents.AFTER.register(new AfterBlockBreakHandler());
    }

}
