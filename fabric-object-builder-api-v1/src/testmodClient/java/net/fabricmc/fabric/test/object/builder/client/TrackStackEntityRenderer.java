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

package net.fabricmc.fabric.test.object.builder.client;

import org.jetbrains.annotations.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.test.object.builder.TrackStackEntity;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityAttachment;

public class TrackStackEntityRenderer extends MobRenderer<TrackStackEntity, TrackStackEntityRenderer.RenderState, ChickenModel> {
	public TrackStackEntityRenderer(EntityRendererProvider.Context context) {
		super(context, new ChickenModel(context.bakeLayer(ModelLayers.CHICKEN)), 0.3f);
	}

	@Override
	public void render(RenderState renderState, PoseStack matrices, MultiBufferSource vertexConsumers, int light) {
		super.render(renderState, matrices, vertexConsumers, light);
		Iterable<Component> labelLines = renderState.labelLines;

		if (labelLines == null) {
			return;
		}

		matrices.pushPose();
		matrices.translate(0, -2, 0);

		for (Component line : labelLines) {
			this.renderNameTag(renderState, line, matrices, vertexConsumers, light);
			matrices.translate(0, 0.25875f, 0);
		}

		matrices.popPose();
	}

	@Override
	public ResourceLocation getTextureLocation(RenderState renderState) {
		return MissingTextureAtlasSprite.getLocation();
	}

	@Override
	public RenderState createRenderState() {
		return new RenderState();
	}

	@Override
	public void extractRenderState(TrackStackEntity entity, RenderState renderState, float tickProgress) {
		super.extractRenderState(entity, renderState, tickProgress);
		renderState.labelLines = entity.getLabelLines();

		if (renderState.nameTagAttachment == null) {
			renderState.nameTagAttachment = entity.getAttachments().getNullable(EntityAttachment.NAME_TAG, 0, entity.getYRot(tickProgress));
		}
	}

	public static class RenderState extends ChickenRenderState {
		@Nullable
		public Iterable<Component> labelLines;
	}
}
