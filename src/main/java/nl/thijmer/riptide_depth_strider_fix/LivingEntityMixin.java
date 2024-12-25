package nl.thijmer.riptide_depth_strider_fix;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Shadow protected int riptideTicks;

    // Inject this function at the point specified in the smart person's comment on the bug report: https://bugs.mojang.com/browse/MC-136249
    @ModifyVariable(
            method = "travelInFluid(Lnet/minecraft/util/math/Vec3d;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getMovementSpeed()F"),
            ordinal = 0
    )
    protected float undoRiptideDrag(float f, @Local(ordinal = 2) float h) {
        // Undo the drag increase from depth strider when we're using Riptide.
        if (this.riptideTicks != 0 && (1 - h) != 0) {
            return (f - 0.54600006F * h) / (1 - h);
        }
        return f;
    }
}