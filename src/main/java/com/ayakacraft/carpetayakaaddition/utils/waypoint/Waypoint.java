package com.ayakacraft.carpetayakaaddition.utils.waypoint;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Waypoint {

    public static final String DESC_NONE = "#none";

    private final String id;

    private final String dim;

    private final Vec3d pos;

    private String desc;

    public Waypoint(String id, RegistryKey<World> dim, Vec3d pos, String desc) {
        this.id = id;
        this.dim = dim.getValue().toString();
        this.pos = pos;
        setDesc(desc);
    }

    public String getDim() {
        return dim;
    }

    public RegistryKey<World> getDimension() {
        //#if MC>=11904
        return RegistryKey.of(net.minecraft.registry.RegistryKeys.WORLD, new Identifier(dim));
        //#else
        //$$ return RegistryKey.of(net.minecraft.util.registry.Registry.WORLD_KEY, new Identifier(dim));
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

    public String getDesc() {
        return (desc == null || desc.isEmpty()) ? DESC_NONE : desc;
    }

    public void setDesc(String desc) {
        this.desc = (desc == null || desc.isEmpty()) ? DESC_NONE : desc;
    }

}
