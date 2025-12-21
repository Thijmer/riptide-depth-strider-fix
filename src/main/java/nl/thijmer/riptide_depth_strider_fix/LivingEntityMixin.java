package nl.thijmer.riptide_depth_strider_fix;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Shadow protected int autoSpinAttackTicks;

    // Inject this function at the right point in the travelInFluid function.
    @ModifyVariable(
            method = "travelInWater(Lnet/minecraft/world/phys/Vec3;DZD)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getSpeed()F"),
            ordinal = 0
    )
    protected float undoRiptideDrag(float f, @Local(ordinal = 2) float h) {
        // Undo the drag increase from depth strider when we're using Riptide.
        if (this.autoSpinAttackTicks != 0 && (1 - h) != 0) {
            return (f - 0.54600006F * h) / (1 - h);
        }
        return f;
    }
}
