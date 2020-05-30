package mindustry.world.blocks.heat;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import mindustry.gen.TileEntity;
import mindustry.world.Block;
import mindustry.world.meta.BlockGroup;

public class HeatBlock extends Block {
    public HeatBlock(String name){
        super(name);
        solid = true;
        destructible = true;
        group = BlockGroup.none;
        update = true;
    }

    @Override
    public TextureRegion[] generateIcons(){
        return new TextureRegion[]{Core.atlas.find(Core.atlas.has(name) ? name : name + "1")};
    }

    @Override
    public boolean canReplace(Block other){
        return false;
    }

    public class HeatEntity extends TileEntity {

        @Override
        public void draw(){
            Draw.rect(region, x, y);
        }
    }
}
