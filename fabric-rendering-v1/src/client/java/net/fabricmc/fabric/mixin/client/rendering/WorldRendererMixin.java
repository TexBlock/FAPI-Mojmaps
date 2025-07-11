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

package net.fabricmc.fabric.mixin.client.rendering;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.InvalidateRenderStateCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.impl.client.rendering.WorldRenderContextImpl;
import net.fabricmc.fabric.impl.client.rendering.WorldRendererHooks;
import net.minecraft.client.Camera;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

@Mixin(LevelRenderer.class)
public abstract class WorldRendererMixin implements WorldRendererHooks {
	@Final
	@Shadow
	private RenderBuffers renderBuffers;
	@Shadow private ClientLevel level;
	@Final
	@Shadow
	private Minecraft minecraft;
	@Shadow
	@Final
	private LevelTargetBundle targets;
	@Unique private final WorldRenderContextImpl context = new WorldRenderContextImpl();
	@Unique private boolean isRendering = false;

	@Inject(method = "renderLevel", at = @At("HEAD"))
	private void beforeRender(GraphicsResourceAllocator objectAllocator, DeltaTracker tickCounter, boolean renderBlockOutline, Camera camera, Matrix4f positionMatrix, Matrix4f projectionMatrix, GpuBufferSlice slice, Vector4f skyColor, boolean thinFog, CallbackInfo ci) {
		context.prepare((LevelRenderer) (Object) this, tickCounter, renderBlockOutline, camera, this.minecraft.gameRenderer, positionMatrix, projectionMatrix, renderBuffers.bufferSource(), Minecraft.useShaderTransparency(), level);
		isRendering = true;
		WorldRenderEvents.START.invoker().onStart(context);
	}

	@Inject(method = "setupRender", at = @At("RETURN"))
	private void afterTerrainSetup(Camera camera, Frustum frustum, boolean hasForcedFrustum, boolean spectator, CallbackInfo ci) {
		context.setFrustum(frustum);
	}

	@Inject(
			method = "lambda$addMainPass$2",
			at = @At(
				value = "INVOKE_STRING",
				target = "Lnet/minecraft/util/profiling/ProfilerFiller;push(Ljava/lang/String;)V",
				args = "ldc=terrain",
				shift = Shift.AFTER
			) // Points to after profiler.push("terrain");
	)
	private void beforeTerrainSolid(CallbackInfo ci) {
		WorldRenderEvents.AFTER_SETUP.invoker().afterSetup(context);
	}

	@Inject(
			method = "lambda$addMainPass$2",
			at = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/client/renderer/LevelRenderer;renderEntities(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/Camera;Lnet/minecraft/client/DeltaTracker;Ljava/util/List;)V"
			)
	)
	private void afterTerrainSolid(CallbackInfo ci) {
		WorldRenderEvents.BEFORE_ENTITIES.invoker().beforeEntities(context);
	}

	@ModifyExpressionValue(method = "lambda$addMainPass$2", at = @At(value = "NEW", target = "com/mojang/blaze3d/vertex/PoseStack"))
	private PoseStack setMatrixStack(PoseStack matrixStack) {
		context.setMatrixStack(matrixStack);
		return matrixStack;
	}

	@Inject(method = "lambda$addMainPass$2", at = @At(value = "CONSTANT", args = "stringValue=blockentities", ordinal = 0))
	private void afterEntities(CallbackInfo ci) {
		WorldRenderEvents.AFTER_ENTITIES.invoker().afterEntities(context);
	}

	@Inject(method = "renderBlockOutline", at = @At("HEAD"))
	private void beforeRenderOutline(Camera camera, MultiBufferSource.BufferSource vertexConsumers, PoseStack matrices, boolean translucent, CallbackInfo ci) {
		context.setTranslucentBlockOutline(translucent);
		context.renderBlockOutline = WorldRenderEvents.BEFORE_BLOCK_OUTLINE.invoker().beforeBlockOutline(context, minecraft.hitResult);
	}

	@SuppressWarnings("ConstantConditions")
	@Inject(method = "renderBlockOutline", at = @At(value = "INVOKE", target = "net/minecraft/client/option/GameOptions.getHighContrastBlockOutline()Lnet/minecraft/client/option/SimpleOption;"), cancellable = true)
	private void onDrawBlockOutline(Camera camera, MultiBufferSource.BufferSource vertexConsumers, PoseStack matrices, boolean translucent, CallbackInfo ci, @Local BlockPos blockPos, @Local BlockState blockState, @Local Vec3 cameraPos) {
		if (!context.renderBlockOutline) {
			// Was cancelled before we got here, so do not
			// fire the BLOCK_OUTLINE event per contract of the API.
			ci.cancel();
			return;
		}

		context.prepareBlockOutline(camera.getEntity(), cameraPos.x, cameraPos.y, cameraPos.z, blockPos, blockState);

		if (!WorldRenderEvents.BLOCK_OUTLINE.invoker().onBlockOutline(context, context)) {
			vertexConsumers.endLastBatch();
			ci.cancel();
		}
	}

	@Inject(
			method = "lambda$addMainPass$2",
			at = @At(
				value = "INVOKE",
				target = "Lnet/minecraft/client/renderer/debug/DebugRenderer;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/culling/Frustum;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;DDD)V",
				ordinal = 0
			)
	)
	private void beforeDebugRender(CallbackInfo ci) {
		WorldRenderEvents.BEFORE_DEBUG_RENDER.invoker().beforeDebugRender(context);
	}

	@Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;getCloudsType()Lnet/minecraft/client/CloudStatus;"))
	private void beforeClouds(CallbackInfo ci, @Local FrameGraphBuilder frameGraphBuilder) {
		FramePass afterTranslucentPass = frameGraphBuilder.addPass("afterTranslucent");
		targets.main = afterTranslucentPass.readsAndWrites(targets.main);

		afterTranslucentPass.executes(() -> WorldRenderEvents.AFTER_TRANSLUCENT.invoker().afterTranslucent(context));
	}

	@Inject(method = "lambda$addMainPass$2", at = @At("RETURN"))
	private void onFinishWritingFramebuffer(CallbackInfo ci) {
		WorldRenderEvents.LAST.invoker().onLast(context);
	}

	@Inject(method = "renderLevel", at = @At("RETURN"))
	private void afterRender(CallbackInfo ci) {
		WorldRenderEvents.END.invoker().onEnd(context);
		isRendering = false;
	}

	@Inject(method = "allChanged()V", at = @At("HEAD"))
	private void onReload(CallbackInfo ci) {
		InvalidateRenderStateCallback.EVENT.invoker().onInvalidate();
	}

	@Inject(at = @At("HEAD"), method = "addWeatherPass", cancellable = true)
	private void renderWeather(FrameGraphBuilder frameGraphBuilder, Vec3 vec3d, float f, GpuBufferSlice fog, CallbackInfo info) {
		if (this.minecraft.level != null) {
			DimensionRenderingRegistry.WeatherRenderer renderer = DimensionRenderingRegistry.getWeatherRenderer(level.dimension());

			if (renderer != null) {
				renderer.render(context);
				info.cancel();
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "addCloudsPass", cancellable = true)
	private void renderCloud(FrameGraphBuilder frameGraphBuilder, CloudStatus cloudRenderMode, Vec3 vec3d, float f, int i, float g, CallbackInfo info) {
		if (this.minecraft.level != null) {
			DimensionRenderingRegistry.CloudRenderer renderer = DimensionRenderingRegistry.getCloudRenderer(level.dimension());

			if (renderer != null) {
				renderer.render(context);
				info.cancel();
			}
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "addSkyPass", cancellable = true)
	private void renderSky(FrameGraphBuilder frameGraphBuilder, Camera camera, float tickDelta, GpuBufferSlice fog, CallbackInfo info) {
		if (this.minecraft.level != null) {
			DimensionRenderingRegistry.SkyRenderer renderer = DimensionRenderingRegistry.getSkyRenderer(level.dimension());

			if (renderer != null) {
				renderer.render(context);
				info.cancel();
			}
		}
	}

	@Override
	public WorldRenderContext fabric$getWorldRenderContext() {
		if (!isRendering) {
			throw new IllegalStateException("WorldRenderer is not rendering");
		}

		return context;
	}
}
