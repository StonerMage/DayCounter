package net.daycounter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

public class DayCounterClient implements ClientModInitializer {
	private static boolean showDayHUD = true;
	private static boolean showFPSHUD = true;
	private static KeyBinding toggleDayKey;
	private static KeyBinding toggleFPSKey;
	private static String category = "category.daycounter";

	@Override
	public void onInitializeClient() {
		HudRenderCallback.EVENT.register(((drawContext, renderTickCounter) -> onHudRender(drawContext)));

		toggleDayKey = new KeyBinding("key.daycounter.toggleDay", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, category);
		toggleFPSKey = new KeyBinding("key.daycounter.toggleFPS", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, category);

		KeyBindingHelper.registerKeyBinding(toggleDayKey);
		KeyBindingHelper.registerKeyBinding(toggleFPSKey);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (toggleDayKey.wasPressed()) {
				showDayHUD = !showDayHUD;
			}
			if (toggleFPSKey.wasPressed()) {
				showFPSHUD = !showFPSHUD;
			}
		});
	}

	private void onHudRender(DrawContext drawContext) {
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.player != null && client.world != null) {
			long day = client.world.getTimeOfDay() / 24000L + 1;
			long fps = client.getCurrentFps();

			int DayX = 7;
			int DayY = 6;

			int FpsX = 7;
			int FpsY = 22;

			int red = 0xFFFF0000;
			int green = 0xFF00FF00;
			int blue = 0xFF0000FF;

			double currentTime = Util.getMeasuringTimeMs() / 1000.0;

			float lerpedAmount = MathHelper.abs(MathHelper.sin((float) currentTime));
			int lerpedRedToGreen = ColorHelper.lerp(lerpedAmount, red, green);
			int lerpedGreenToBlue = ColorHelper.lerp(lerpedAmount, green, blue);

			if (showDayHUD) {
				drawContext.drawTextWithShadow(client.textRenderer, "Day: " + day, DayX, DayY, lerpedGreenToBlue);
			}

			if (showFPSHUD) {
				if (!showDayHUD) {
					drawContext.drawTextWithShadow(client.textRenderer, fps + " FPS", DayX, DayY, lerpedRedToGreen);
				} else {
					drawContext.drawTextWithShadow(client.textRenderer, fps + " FPS", FpsX, FpsY, lerpedRedToGreen);
				}
			}
		}
	}
}