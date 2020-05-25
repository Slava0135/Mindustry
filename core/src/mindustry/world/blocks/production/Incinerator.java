package mindustry.world.blocks.production;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;

public class Incinerator extends Block{
    public Effect effect = Fx.fuelburn;
    public Color flameColor = Color.valueOf("ffad9d");

    public Incinerator(String name){
        super(name);
        hasPower = true;
        hasLiquids = true;
        update = true;
        solid = true;
    }

    public class IncineratorEntity extends TileEntity{
        @Override
        public void updateTile(){
            if(consValid() && efficiency() > 0.9f){
                heat().changeHeat(block.consumes.power(1).usage * 1000 * power.status);
            }
        }

        @Override
        public void draw(){
            super.draw();

            float g = 0.3f;
            float r = 0.06f;

            Draw.alpha(((1f - g) + Mathf.absin(Time.time(), 8f, g) + Mathf.random(r) - r) * heat().overheatRate());

            Draw.tint(flameColor);
            Fill.circle(x, y, 2f);
            Draw.color(1f, 1f, 1f, heat.overheatRate());
            Fill.circle(x, y, 1f);

            Draw.color();
        }

        @Override
        public void handleItem(Tilec source, Item item){
            if(Mathf.chance(0.3)){
                effect.at(x, y);
            }
        }

        @Override
        public boolean acceptItem(Tilec source, Item item){
            return heat.getTemperature() > 373;
        }

        @Override
        public void handleLiquid(Tilec source, Liquid liquid, float amount){
            if(Mathf.chance(0.02)){
                effect.at(x, y);
            }
        }

        @Override
        public boolean acceptLiquid(Tilec source, Liquid liquid, float amount){
            return heat.getTemperature() > 373;
        }
    }
}
