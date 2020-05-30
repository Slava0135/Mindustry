package mindustry.world.blocks.heat;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import mindustry.annotations.Annotations;
import mindustry.gen.TileEntity;
import mindustry.world.meta.BlockGroup;

public class Fan extends HeatBlock {

    public Fan(String name) {
        super(name);
        solid = true;
        destructible = true;
        group = BlockGroup.none;
        update = true;
    }

    public float rotateSpeed = 16f;

    public @Annotations.Load("@-rim") TextureRegion rimRegion;
    public @Annotations.Load("@-rotator") TextureRegion rotatorRegion;
    public @Annotations.Load("@-top") TextureRegion topRegion;

    @Override
    public TextureRegion[] generateIcons(){
        return new TextureRegion[]{Core.atlas.find(name), Core.atlas.find(name + "-rotator"), Core.atlas.find(name + "-top")};
    }

    public class FanEntity extends TileEntity {
        private float progress = 0;
        private float speed = 0;

        @Override
        public void draw(){
            speed += (rotateSpeed * power().status - speed) / 64;
            progress += delta() * speed;
            Draw.rect(region, x, y);
            super.drawCracks();
            Draw.rect(rimRegion, x, y);
            Draw.rect(rotatorRegion, x, y, progress);
            Draw.rect(topRegion, x, y);
        }
    }
}
