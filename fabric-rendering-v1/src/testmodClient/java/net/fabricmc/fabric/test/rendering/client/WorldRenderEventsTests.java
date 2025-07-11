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

package net.fabricmc.fabric.test.rendering.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class WorldRenderEventsTests implements ClientModInitializer {
	private static boolean onBlockOutline(WorldRenderContext wrc, WorldRenderContext.BlockOutlineContext blockOutlineContext) {
		if (blockOutlineContext.blockState().is(Blocks.DIAMOND_BLOCK)) {
			PoseStack matrixStack = new PoseStack();
			matrixStack.pushPose();
			Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
			BlockPos pos = blockOutlineContext.blockPos();
			double x = pos.getX() - cameraPos.x;
			double y = pos.getY() - cameraPos.y;
			double z = pos.getZ() - cameraPos.z;
			matrixStack.translate(x+0.25, y+0.25+1, z+0.25);
			matrixStack.scale(0.5f, 0.5f, 0.5f);

			Minecraft.getInstance().getBlockRenderer().renderSingleBlock(
					Blocks.DIAMOND_BLOCK.defaultBlockState(),
					matrixStack, wrc.consumers(), 15728880, OverlayTexture.NO_OVERLAY);

			matrixStack.popPose();
		}

		return true;
	}

	/**
	 * Renders a translucent filled box at (0, 100, 0).
	 */
	private static void renderAfterTranslucent(WorldRenderContext context) {
		PoseStack matrices = context.matrixStack();
		Vec3 camera = context.camera().getPosition();

		matrices.pushPose();
		matrices.translate(-camera.x, -camera.y, -camera.z);

		ShapeRenderer.addChainedFilledBoxVertices(matrices, context.consumers().getBuffer(RenderType.debugFilledBox()), 0, 100, 0, 1, 101, 1, 0, 1, 0, 0.5f);

		matrices.popPose();
	}

	@Override
	public void onInitializeClient() {
		// Renders a diamond block above diamond blocks when they are looked at.
		WorldRenderEvents.BLOCK_OUTLINE.register(WorldRenderEventsTests::onBlockOutline);
		// Renders a translucent filled box at (0, 100, 0)
		WorldRenderEvents.AFTER_TRANSLUCENT.register(WorldRenderEventsTests::renderAfterTranslucent);
	}
}
