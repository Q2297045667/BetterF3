package me.treyruffy.betterf3.mixin;

import java.util.ArrayList;
import java.util.List;
import me.cominixo.betterf3.config.GeneralOptions;
import me.cominixo.betterf3.utils.DebugRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * The Debug Screen Overlay.
 */
@Mixin(DebugScreenOverlay.class)
public abstract class NeoForgeDebugMixin {

  @Shadow
  @Final
  private Minecraft minecraft;
  @Shadow
  @Final
  private Font font;

  /**
   * Gets the information on the left side of the screen.
   *
   * @return the game information
   */
  @SuppressWarnings("checkstyle:MethodName")
  @Shadow
  protected abstract List<String> getGameInformation();

  /**
   * Gets the information on the right side of the screen.
   *
   * @return the system information
   */
  @SuppressWarnings("checkstyle:MethodName")
  @Shadow
  protected abstract List<String> getSystemInformation();

  /**
   * Renders the text on the screen.
   *
   * @param guiGraphics the draw context
   * @param list        the list of strings
   * @param bl          the left side
   * @param ci          the callback info
   */
  @Inject(method = "renderLines", at = @At(value = "HEAD"), cancellable = true, order = 2000)
  public void drawText(final GuiGraphics guiGraphics, final List<String> list, final boolean bl, final CallbackInfo ci) {

    if (GeneralOptions.disableMod) {
      return;
    }

    if (bl) {
      final List<Component> leftList = DebugRenderer.newText(this.minecraft, true, this.getGameInformation(), this.getSystemInformation());
      DebugRenderer.drawLeftText(leftList, guiGraphics, this.minecraft, this.font, list);
    } else {
      final List<Component> rightList = DebugRenderer.newText(this.minecraft, false, this.getGameInformation(), this.getSystemInformation());
      DebugRenderer.drawRightText(rightList, guiGraphics, this.minecraft, this.font, list);
    }

    ci.cancel();
  }

  /**
   * Sets collect game information text to an empty list.
   *
   * @param cir the callback info returnable
   */
  @Inject(method = "collectGameInformationText", at = @At("HEAD"), cancellable = true)
  public void collectGameInformationText(final CallbackInfoReturnable<List<String>> cir) {
    if (GeneralOptions.disableMod) {
      return;
    }
    cir.setReturnValue(new ArrayList<>());
  }

  /**
   * Sets collect system information text to an empty list.
   *
   * @param cir the callback info returnable
   */
  @Inject(method = "collectSystemInformationText", at = @At("HEAD"), cancellable = true)
  public void collectSystemInformationText(final CallbackInfoReturnable<List<String>> cir) {
    if (GeneralOptions.disableMod) {
      return;
    }
    cir.setReturnValue(new ArrayList<>());
  }
}
