package mindustry.world.modules;

import arc.math.geom.Point2;
import arc.struct.Array;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Tilec;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.Edges;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;

import static java.lang.Math.sqrt;
import static mindustry.Vars.world;

public class HeatModule extends BlockModule {

    private float heat;

    private float heatConduction;
    private float heatCapacity;
    //** in Kelvins */
    private float temperature;
    private float maxTemperature;

    private final Floor floor;
    private final Block block;
    private final Tile tile;

    HeatModule(Floor floor, Block block, Tile tile) {
        this.floor = floor;
        this.block = block;
        this.tile = tile;
        calculateConduction();
        calculateCapacity();
        calculateMaxTemperature();
        temperature = floor.temperature;
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
                * sqrt(heatConduction * other.heatConduction);
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
                block.heatIsolation
                * (floor.temperature - temperature) * (floor.temperature - temperature)
                * sqrt(floor.heatConduction * heatConduction);
        if (floor.temperature > temperature) {
            heat += delta;
        } else {
            heat -= delta;
        }
        calculateTemperature();
    }

    private void calculateConduction() {
        float sum = 0f;
        float count = 0;
        for (ItemStack itemStack: block.requirements) {
            sum += itemStack.item.heatConduction * itemStack.amount;
            count += itemStack.amount;
        }
        heatConduction = sum / count;
    }

    private void calculateCapacity() {
        float sum = 0f;
        float count = 0;
        for (ItemStack itemStack: block.requirements) {
            sum += itemStack.item.heatCapacity * itemStack.amount;
            count += itemStack.amount;
        }
        heatCapacity = block.size * block.size * sum / count;
    }

    private void calculateMaxTemperature() {
        float sum = 0f;
        float count = 0;
        for (ItemStack itemStack: block.requirements) {
            sum += itemStack.item.maxTemperature * itemStack.amount;
            count += itemStack.amount;
        }
       maxTemperature = sum / count;
    }

    private void calculateTemperature() {
        temperature = heat / heatCapacity;
    }

    private void calculateHeat() {
        heat = temperature * heatCapacity;
    }

    @Override
    public void write(Writes write) {
        write.f(heat);
    }

    @Override
    public void read(Reads read) {
        heat = read.f();
        calculateCapacity();
        calculateConduction();
        calculateMaxTemperature();
        calculateTemperature();
    }
}
