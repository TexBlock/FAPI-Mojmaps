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

package net.fabricmc.fabric.api.client.rendering.v1.hud;

import net.minecraft.resources.ResourceLocation;

/**
 * A hud element that has an identifier attached for use in {@link HudElementRegistry}.
 *
 * <p>The identifiers in this interface are the vanilla hud layers in the order they are drawn in.
 * The first element is drawn first, which means it is at the bottom.
 * All vanilla layers except {@link #SLEEP} are in sub drawers and have a render condition attached ({@link net.minecraft.client.Options#hideGui}).
 * Operations relative to any element will generally inherit that element's render condition.
 * There is currently no mechanism to change the render condition of an element.
 *
 * <p>For common use cases and more details on how this API deals with render condition, see {@link HudElementRegistry}.
 */
public final class VanillaHudElements {
	/**
	 * The identifier for the vanilla miscellaneous overlays (such as vignette, spyglass, and powder snow) element.
	 */
	public static final ResourceLocation MISC_OVERLAYS = ResourceLocation.withDefaultNamespace("misc_overlays");
	/**
	 * The identifier for the vanilla crosshair element.
	 */
	public static final ResourceLocation CROSSHAIR = ResourceLocation.withDefaultNamespace("crosshair");
	/**
	 * The identifier for the vanilla spectator menu.
	 */
	public static final ResourceLocation SPECTATOR_MENU = ResourceLocation.withDefaultNamespace("spectator_menu");
	/**
	 * The identifier for the vanilla hotbar.
	 */
	public static final ResourceLocation HOTBAR = ResourceLocation.withDefaultNamespace("hotbar");
	/**
	 * The identifier for the player armor level bar.
	 */
	public static final ResourceLocation ARMOR_BAR = ResourceLocation.withDefaultNamespace("armor_bar");
	/**
	 * The identifier for the player health bar.
	 */
	public static final ResourceLocation HEALTH_BAR = ResourceLocation.withDefaultNamespace("health_bar");
	/**
	 * The identifier for the player hunger level bar.
	 */
	public static final ResourceLocation FOOD_BAR = ResourceLocation.withDefaultNamespace("food_bar");
	/**
	 * The identifier for the player air level bar.
	 */
	public static final ResourceLocation AIR_BAR = ResourceLocation.withDefaultNamespace("air_bar");
	/**
	 * The identifier for the vanilla mount health.
	 */
	public static final ResourceLocation MOUNT_HEALTH = ResourceLocation.withDefaultNamespace("mount_health");
	/**
	 * The identifier for the info bar, either empty, experience bar, locator, or jump bar.
	 */
	public static final ResourceLocation INFO_BAR = ResourceLocation.withDefaultNamespace("info_bar");
	/**
	 * The identifier for experience level tooltip.
	 */
	public static final ResourceLocation EXPERIENCE_LEVEL = ResourceLocation.withDefaultNamespace("experience_level");
	/**
	 * The identifier for held item tooltip.
	 */
	public static final ResourceLocation HELD_ITEM_TOOLTIP = ResourceLocation.withDefaultNamespace("held_item_tooltip");
	/**
	 * The identifier for the vanilla spectator tooltip.
	 */
	public static final ResourceLocation SPECTATOR_TOOLTIP = ResourceLocation.withDefaultNamespace("spectator_tooltip");
	/**
	 * The identifier for the vanilla status effects element.
	 */
	public static final ResourceLocation STATUS_EFFECTS = ResourceLocation.withDefaultNamespace("status_effects");
	/**
	 * The identifier for the vanilla boss bar element.
	 */
	public static final ResourceLocation BOSS_BAR = ResourceLocation.withDefaultNamespace("boss_bar");
	/**
	 * The identifier for the vanilla sleep overlay element.
	 */
	public static final ResourceLocation SLEEP = ResourceLocation.withDefaultNamespace("sleep");
	/**
	 * The identifier for the vanilla demo timer element.
	 */
	public static final ResourceLocation DEMO_TIMER = ResourceLocation.withDefaultNamespace("demo_timer");
	/**
	 * The identifier for the vanilla debug hud element.
	 */
	public static final ResourceLocation DEBUG = ResourceLocation.withDefaultNamespace("debug");
	/**
	 * The identifier for the vanilla scoreboard element.
	 */
	public static final ResourceLocation SCOREBOARD = ResourceLocation.withDefaultNamespace("scoreboard");
	/**
	 * The identifier for the vanilla overlay message element.
	 */
	public static final ResourceLocation OVERLAY_MESSAGE = ResourceLocation.withDefaultNamespace("overlay_message");
	/**
	 * The identifier for the vanilla title and subtitle element.
	 *
	 * <p>Note that this is not the sound subtitles.
	 */
	public static final ResourceLocation TITLE_AND_SUBTITLE = ResourceLocation.withDefaultNamespace("title_and_subtitle");
	/**
	 * The identifier for the vanilla chat element.
	 */
	public static final ResourceLocation CHAT = ResourceLocation.withDefaultNamespace("chat");
	/**
	 * The identifier for the vanilla player list element.
	 */
	public static final ResourceLocation PLAYER_LIST = ResourceLocation.withDefaultNamespace("player_list");
	/**
	 * The identifier for the vanilla sound subtitles element.
	 */
	public static final ResourceLocation SUBTITLES = ResourceLocation.withDefaultNamespace("subtitles");

	private VanillaHudElements() {
	}
}
