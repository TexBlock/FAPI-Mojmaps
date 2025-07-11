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
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import net.fabricmc.fabric.api.renderer.v1.render.FabricBlockModelRenderer;
import net.fabricmc.fabric.api.renderer.v1.render.RenderLayerHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.entity.layers.SnowGolemHeadLayer;
import net.minecraft.client.renderer.entity.state.SnowGolemRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.EmptyBlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(SnowGolemHeadLayer.class)
abstract class SnowGolemPumpkinFeatureRendererMixin {
	@Shadow
	@Final
	private BlockRenderDispatcher blockRenderer;

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/render/block/BlockModelRenderer.render(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/client/render/model/BlockStateModel;FFFII)V"))
	private void renderProxy(PoseStack.Pose entry, VertexConsumer vertexConsumer, BlockStateModel model, float red, float green, float blue, int light, int overlay, PoseStack matrices, MultiBufferSource vertexConsumers, int light1, SnowGolemRenderState renderState, float f, float g, @Local BlockState blockState) {
		// If true, the vertex consumer is for an outline render layer, and we want all geometry to go into this vertex
		// consumer.
		if (renderState.appearsGlowing && renderState.isInvisible) {
			// Fix tinted quads being rendered completely black and provide the BlockState as context.
			FabricBlockModelRenderer.render(entry, layer -> vertexConsumer, model, 1, 1, 1, light, overlay, EmptyBlockAndTintGetter.INSTANCE, BlockPos.ZERO, blockState);
		} else {
			// Support multi-render layer models, fix tinted quads being rendered completely black, and provide the BlockState as context.
			FabricBlockModelRenderer.render(entry, RenderLayerHelper.entityDelegate(vertexConsumers), model, 1, 1, 1, light, overlay, EmptyBlockAndTintGetter.INSTANCE, BlockPos.ZERO, blockState);
		}
	}
}
