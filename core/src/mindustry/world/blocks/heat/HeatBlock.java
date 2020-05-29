package mindustry.world.blocks.heat;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import mindustry.gen.TileEntity;
import mindustry.world.Block;
import mindustry.world.meta.BlockGroup;

public class HeatBlock extends Block {
    public int variants = 0;

    public HeatBlock(String name){
        super(name);
        solid = true;
        destructible = true;
        group = BlockGroup.none;
        update = true;
    }

    @Override
    public void load(){
        super.load();

        if(variants != 0){
            variantRegions = new TextureRegion[variants];

            for(int i = 0; i < variants; i++){
                variantRegions[i] = Core.atlas.find(name + (i + 1));
            }
            region = variantRegions[0];
        }
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
            if(variants == 0){
                Draw.rect(region, x, y);
            }else{
                Draw.rect(variantRegions[Mathf.randomSeed(tile.pos(), 0, Math.max(0, variantRegions.length - 1))], x, y);
            }
        }
    }
}
