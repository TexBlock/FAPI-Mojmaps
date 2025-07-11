/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.mixin.renderer.client.block.render;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.fabricmc.fabric.api.renderer.v1.render.FabricBlockModelRenderer;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.entity.ItemFrameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.EmptyBlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(ItemFrameRenderer.class)
abstract class ItemFrameEntityRendererMixin {
	// Provide the BlockState as context.
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/render/block/BlockModelRenderer.render(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/client/render/model/BlockStateModel;FFFII)V"))
	private void renderProxy(PoseStack.Pose matrices, VertexConsumer vertexConsumer, BlockStateModel model, float red, float green, float blue, int light, int overlay, @Local BlockState blockState) {
		// The vertex consumer is for a special layer that renders solid, but vanilla has no equivalent
		// cutout/translucent layers that we can use here without risking compatibility.
		FabricBlockModelRenderer.render(matrices, layer -> vertexConsumer, model, red, green, blue, light, overlay, EmptyBlockAndTintGetter.INSTANCE, BlockPos.ZERO, blockState);
	}
}
