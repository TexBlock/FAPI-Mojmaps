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
import net.minecraft.client.renderer.blockentity.PistonHeadRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(PistonHeadRenderer.class)
abstract class PistonBlockEntityRendererMixin {
	@Shadow
	@Final
	private BlockRenderDispatcher blockRenderer;

	// Support multi-render layer models.
	@Overwrite
	private void renderBlock(BlockPos pos, BlockState state, PoseStack matrices, MultiBufferSource vertexConsumers, Level world, boolean cull, int overlay) {
		blockRenderer.getModelRenderer().tesselateBlock(world, blockRenderer.getBlockModel(state), state, pos, matrices, RenderLayerHelper.movingDelegate(vertexConsumers), cull, state.getSeed(pos), overlay);
	}
}
