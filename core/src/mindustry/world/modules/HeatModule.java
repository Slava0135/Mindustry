package mindustry.world.modules;

import arc.math.geom.Point2;
import arc.struct.Array;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Tilec;
import mindustry.world.Block;
import mindustry.world.Edges;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;

import static java.lang.Math.sqrt;
import static mindustry.Vars.world;

public class HeatModule extends BlockModule {

    private float heat;
    //** in Kelvins */
    private float temperature;
    private float floorConduction;
    private float floorTemperature;

    public void changeHeat(float delta) {
        heat += delta;
        calculateTemperature();
    }
    public float getTemperature() {
        return temperature;
    }

    private final Block block;
    private final Tile tile;

    public HeatModule(Block block, Tile tile) {
        this.block = block;
        this.tile = tile;
        Array<Tile> linked = tile.getLinkedTiles(new Array<>());
        floorConduction = 0;
        floorTemperature = 0;
        for (Tile lTile: linked) {
            floorConduction += lTile.floor().heatConduction;
            floorTemperature += lTile.floor().temperature;
        }
        floorConduction /= linked.size;
        floorTemperature /= linked.size;
        temperature = floorTemperature;
        calculateHeat();
    }

    public void update(Array<Tilec> proximity) {
        floorExchange();
        for(Tilec tilec: proximity) {
            shareHeat(tilec.heat(), getContactArea(tilec));
        }
    }

    private int getContactArea(Tilec tilec) {
        int count = 0;
        Point2[] nearby = Edges.getEdges(block.size);
        for (Point2 point: nearby) {
            if (world.ent(tile.x + point.x, tile.y + point.y) == tilec) {
                count++;
            }
        }
        return count;
    }

    private void shareHeat(HeatModule other, int contactArea) {
        double delta =
                contactArea
                * (temperature - other.temperature) * (temperature - other.temperature)
                * sqrt(block.heatConduction * other.block.heatConduction) / 1000;
        if (other.temperature > temperature) {
            other.heat -= delta;
            heat += delta;
        } else {
            other.heat += delta;
            heat -= delta;
        }
        other.calculateTemperature();
        calculateTemperature();
    }

    private void floorExchange() {
        double delta =
                (1 - block.heatIsolation)
                * (floorTemperature - temperature) * (floorTemperature - temperature)
                * sqrt(floorConduction * block.heatConduction) / 1000;
        if (floorTemperature > temperature) {
            heat += delta;
        } else {
            heat -= delta;
        }
        calculateTemperature();
    }

    private void calculateTemperature() {
        temperature = heat / block.heatCapacity;
    }

    private void calculateHeat() {
        heat = temperature * block.heatCapacity;
    }

    public float overheatRate() {
        float overheat = temperature / block.maxTemperature;
        return Math.min(overheat, 1f);
    }

    @Override
    public void write(Writes write) {
        write.f(heat);
    }

    @Override
    public void read(Reads read) {
        heat = read.f();
        calculateTemperature();
    }
}
