package com.ayakacraft.carpetayakaaddition.commands.waypoint;

import net.minecraft.registry.RegistryKey;
//#if MC>=11904
import net.minecraft.registry.RegistryKeys;
//#else
//$$ import net.minecraft.util.registry.Registry;
//#endif
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Waypoint {

    public Waypoint(String id, RegistryKey<World> dim, Vec3d pos) {
        this.id = id;
        this.dim = dim.getValue().toString();
        this.pos = pos;
    }

    public String id;

    public String dim;

    public Vec3d pos;

    public String getDim() {
        return dim;
    }

    public RegistryKey<World> getDimension() {
        //#if MC>=11904
        return RegistryKey.of(RegistryKeys.WORLD, new Identifier(dim));
        //#else
        //$$ return RegistryKey.of(Registry.WORLD_KEY, new Identifier(dim));
        //#endif
    }

    public double getX() {
        return pos.getX();
    }

    public double getY() {
        return pos.getY();
    }

    public double getZ() {
        return pos.getZ();
    }

    public String getId() {
        return id;
    }


    public Vec3d getPos() {
        return pos;
    }

}
