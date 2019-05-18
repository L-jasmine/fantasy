package tcdk.fantasy.Util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Collectors;

public class MathHelp {
    public static boolean isEntityOnPath(Entity entity,Vec3d start,Vec3d end){
        return entity.getEntityBoundingBox().calculateIntercept(start,end)!=null;
    }

    public static List<Entity> findEntityOnPath(Class<? extends Entity> ClassEntity,World world, Vec3d start,Vec3d end){
        List<Entity> entitys=world.getEntitiesWithinAABB(ClassEntity, new AxisAlignedBB(
                                                                            start.x,start.y,start.z,
                                                                            end.x,end.y,end.z));
        return entitys.stream()
                .filter(entity -> isEntityOnPath(entity,start,end))
                .collect(Collectors.toList());
    }

    public static float[] getPY(Vec3d vec)
    {
        float[] res=new float[2];
        vec=vec.normalize();
        res[0]=-(float)(Math.asin(vec.y)*180.0F/Math.PI);
        res[1]=vec.z==0?(vec.x>0?-90.0F:90.0F):
                (float)(-Math.atan(vec.x/vec.z)*180.F/Math.PI);
        if(vec.z<0){
            res[1]+=180.0F;
        }
        return res;
    }
}
