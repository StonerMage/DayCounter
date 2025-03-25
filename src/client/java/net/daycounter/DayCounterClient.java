package net.daycounter;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

public class DayCounterClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		HudRenderCallback.EVENT.register(((drawContext, renderTickCounter) -> onHudRender(drawContext)));
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

			drawContext.drawTextWithShadow(client.textRenderer, "Day: " + day, DayX, DayY, lerpedGreenToBlue);
			drawContext.drawTextWithShadow(client.textRenderer, fps + " FPS", FpsX, FpsY, lerpedRedToGreen);
		}
	}
}