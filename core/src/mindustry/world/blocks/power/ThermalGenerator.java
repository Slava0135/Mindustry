package mindustry.world.blocks.power;

import arc.*;
import arc.graphics.*;
import arc.math.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.graphics.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class ThermalGenerator extends PowerGenerator{
    public Effect generateEffect = Fx.none;

    public ThermalGenerator(String name){
        super(name);
    }

    @Override
    public void setStats(){
        super.setStats();
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid){
    }

    @Override
    public boolean canPlaceOn(Tile tile) {
        return true;
    }

    public class ThermalGeneratorEntity extends GeneratorEntity{
        @Override
        public void updateTile(){
            if(productionEfficiency > 0.1f && Mathf.chance(0.05 * delta())){
                generateEffect.at(x + Mathf.range(3f), y + Mathf.range(3f));
            }

            float delta =  heat().getFloorTemperature() - heat().getTemperature();
            if (delta > 0) {
                productionEfficiency = delta;
            } else {
                productionEfficiency = 0;
            }
        }

        @Override
        public void drawLight(){
            Drawf.light(x, y, (40f + Mathf.absin(10f, 5f)) * productionEfficiency * size, Color.scarlet, 0.4f);
        }

        @Override
        public void onProximityAdded(){
            super.onProximityAdded();
        }

        @Override
        public float getPowerProduction(){
            return powerProduction * productionEfficiency;
        }
    }
}
