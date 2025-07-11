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

package net.fabricmc.fabric.test.rendering.client.gui;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

public class BannerGuiElementRenderer extends PictureInPictureRenderer<BannerGuiElementRenderState> {
	protected BannerGuiElementRenderer(MultiBufferSource.BufferSource vertexConsumers) {
		super(vertexConsumers);
	}

	@Override
	public Class<BannerGuiElementRenderState> getRenderStateClass() {
		return BannerGuiElementRenderState.class;
	}

	@Override
	protected void renderToTexture(BannerGuiElementRenderState state, PoseStack matrices) {
		Minecraft.getInstance().gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_FLAT);
		BannerRenderer.renderPatterns(matrices, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.STANDING_BANNER_FLAG).getChild("flag"), ModelBakery.BANNER_BASE, true, DyeColor.BLUE, BannerPatternLayers.EMPTY);
	}

	@Override
	protected String getTextureLabel() {
		return "fabric test banner";
	}
}
