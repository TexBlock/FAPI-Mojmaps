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

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.renderer.v1.render.RenderLayerHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.client.renderer.entity.state.FallingBlockRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(FallingBlockRenderer.class)
abstract class FallingBlockEntityRendererMixin extends EntityRenderer<FallingBlockEntity, FallingBlockRenderState> {
	@Shadow
	@Final
	private BlockRenderDispatcher dispatcher;

	private FallingBlockEntityRendererMixin(EntityRendererProvider.Context context) {
		super(context);
	}

	// Support multi-render layer models.
	@Overwrite
	public void render(FallingBlockRenderState renderState, PoseStack matrixStack, MultiBufferSource vertexConsumers, int light) {
		BlockState blockState = renderState.blockState;

		if (blockState.getRenderShape() == RenderShape.MODEL) {
			matrixStack.pushPose();
			matrixStack.translate(-0.5, 0.0, -0.5);

			BlockStateModel model = dispatcher.getBlockModel(blockState);
			long seed = blockState.getSeed(renderState.startBlockPos);
			dispatcher.getModelRenderer().tesselateBlock(renderState, model, blockState, renderState.blockPos, matrixStack, RenderLayerHelper.movingDelegate(vertexConsumers), false, seed, OverlayTexture.NO_OVERLAY);

			matrixStack.popPose();
			super.render(renderState, matrixStack, vertexConsumers, light);
		}
	}
}
