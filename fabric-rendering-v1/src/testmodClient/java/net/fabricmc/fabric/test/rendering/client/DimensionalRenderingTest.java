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

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class DimensionalRenderingTest implements ClientModInitializer {
	private static final ResourceLocation SKY_TEXTURE = ResourceLocation.withDefaultNamespace("textures/block/dirt.png");

	private static void render(WorldRenderContext context) {
		VertexConsumer vertexConsumer = context.consumers().getBuffer(RenderType.celestial(SKY_TEXTURE));
		vertexConsumer.addVertex(-100.0f, -100.0f, -100.0f).setUv(0.0F, 0.0F).setColor(255, 255, 255, 255);
		vertexConsumer.addVertex(-100.0f, -100.0f, 100.0f).setUv(0.0F, 1.0F).setColor(255, 255, 255, 255);
		vertexConsumer.addVertex(100.0f, -100.0f, 100.0f).setUv(1.0F, 1.0F).setColor(255, 255, 255, 255);
		vertexConsumer.addVertex(100.0f, -100.0f, -100.0f).setUv(1.0F, 0.0F).setColor(255, 255, 255, 255);

		vertexConsumer.addVertex(-100.0f, 100.0f, -100.0f).setUv(0.0F, 0.0F).setColor(255, 255, 255, 255);
		vertexConsumer.addVertex(-100.0f, -100.0f, -99.0f).setUv(0.0F, 1.0F).setColor(255, 255, 255, 255);
		vertexConsumer.addVertex(100.0f, -100.0f, -99.0f).setUv(1.0F, 1.0F).setColor(255, 255, 255, 255);
		vertexConsumer.addVertex(100.0f, 100.0f, -100.0f).setUv(1.0F, 0.0F).setColor(255, 255, 255, 255);

		vertexConsumer.addVertex(-100.0f, -100.0f, 100.0f).setUv(0.0F, 0.0F).setColor(255, 255, 255, 255);
		vertexConsumer.addVertex(-100.0f, 100.0f, 100.0f).setUv(0.0F, 1.0F).setColor(255, 255, 255, 255);
		vertexConsumer.addVertex(100.0f, 100.0f, 100.0f).setUv(1.0F, 1.0F).setColor(255, 255, 255, 255);
		vertexConsumer.addVertex(100.0f, -100.0f, 100.0f).setUv(1.0F, 0.0F).setColor(255, 255, 255, 255);

		vertexConsumer.addVertex(-100.0f, 100.0f, 101.0f).setUv(0.0F, 0.0F).setColor(255, 255, 255, 255);
		vertexConsumer.addVertex(-100.0f, 100.0f, -100.0f).setUv(0.0F, 1.0F).setColor(255, 255, 255, 255);
		vertexConsumer.addVertex(100.0f, 100.0f, -100.0f).setUv(1.0F, 1.0F).setColor(255, 255, 255, 255);
		vertexConsumer.addVertex(100.0f, 100.0f, 100.0f).setUv(1.0F, 0.0F).setColor(255, 255, 255, 255);

		vertexConsumer.addVertex(100.0f, -100.0f, -100.0f).setUv(0.0F, 0.0F).setColor(255, 255, 255, 255);
		vertexConsumer.addVertex(100.0f, -100.0f, 100.0f).setUv(0.0F, 1.0F).setColor(255, 255, 255, 255);
		vertexConsumer.addVertex(100.0f, 100.0f, 100.0f).setUv(1.0F, 1.0F).setColor(255, 255, 255, 255);
		vertexConsumer.addVertex(100.0f, 100.0f, -100.0f).setUv(1.0F, 0.0F).setColor(255, 255, 255, 255);

		vertexConsumer.addVertex(-100.0f, 100.0f, -100.0f).setUv(0.0F, 0.0F).setColor(255, 255, 255, 255);
		vertexConsumer.addVertex(-100.0f, 100.0f, 100.0f).setUv(0.0F, 1.0F).setColor(255, 255, 255, 255);
		vertexConsumer.addVertex(-100.0f, -100.0f, 100.0f).setUv(1.0F, 1.0F).setColor(255, 255, 255, 255);
		vertexConsumer.addVertex(-100.0f, -100.0f, -100.0f).setUv(1.0F, 0.0F).setColor(255, 255, 255, 255);
	}

	@Override
	public void onInitializeClient() {
		DimensionRenderingRegistry.registerSkyRenderer(ResourceKey.create(Registries.DIMENSION, ResourceLocation.fromNamespaceAndPath("fabric_dimension", "void")), DimensionalRenderingTest::render);
		DimensionRenderingRegistry.registerDimensionEffects(ResourceLocation.fromNamespaceAndPath("fabric_dimension", "void"), new DimensionSpecialEffects.EndEffects());
	}
}
