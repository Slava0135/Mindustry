package mindustry.world.modules;

import arc.struct.Array;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Tilec;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.environment.Floor;

import static java.lang.Math.sqrt;

public class HeatModule extends BlockModule {

    private float heat;

    private float heatConduction;
    private float heatCapacity;
    //** in Kelvins */
    private float temperature;
    private float maxTemperature;

    private Floor floor;
    private Block block;

    HeatModule(Floor floor, Block block) {
        this.floor = floor;
        this.block = block;
        calculateConduction();
        calculateCapacity();
        calculateMaxTemperature();
        temperature = floor.temperature;
        calculateHeat();
    }

    public void update(Array<Tilec> proximity) {
        floorExchange();
        for(Tilec tilec: proximity) {
        }
    }

    private void shareHeat(HeatModule other) {

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
        heatCapacity = sum / count;
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
