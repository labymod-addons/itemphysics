package net.labymod.addons.itemphysics.v1_8.mixins;

import net.labymod.addons.itemphysics.ItemPhysics;
import net.labymod.addons.itemphysics.ItemPhysicsConfiguration;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.inject.LabyGuice;
import net.labymod.core.main.LabyMod;
import net.labymod.v1_8.client.render.matrix.MatrixStack;
import net.labymod.v1_8.client.render.matrix.VersionedStackProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(RenderEntityItem.class)
public abstract class MixinItemEntityRenderer extends Render<EntityItem> {

  private ItemPhysicsConfiguration configuration;

  @Shadow
  @Final
  private Random field_177079_e;

  @Shadow
  private RenderItem itemRenderer;

  protected MixinItemEntityRenderer(RenderManager p_i1487_1_) {
    super(p_i1487_1_);
  }

  @Shadow
  public abstract int func_177078_a(ItemStack param0);

  @Inject(at = @At("HEAD"), method = "doRender", cancellable = true)
  public void render(@NotNull EntityItem itemEntity, double x, double y, double z, float yaw,
      float partialTicks, CallbackInfo callbackInfo) {

    if (this.configuration == null) {
      this.configuration = LabyGuice.getInstance(ItemPhysics.class).configuration();
      this.shadowSize = 0;
    }

    ItemStack itemStack = itemEntity.getEntityItem();

    if (!this.configuration.isEnabled() || itemStack.getItem() == null) {
      return;
    }

    int renderCount = this.func_177078_a(itemStack);

    Item item = itemStack.getItem();
    int seed = Item.getIdFromItem(item) + itemStack.getItemDamage();
    this.field_177079_e.setSeed(seed);

    float rotation =
        (((itemEntity.getAge() + partialTicks) / 20.0F + itemEntity.getEyeHeight()) / 10)
            * this.configuration.getRotationSpeed();

    this.bindTexture(TextureMap.locationBlocksTexture);
    this.getRenderManager().renderEngine.getTexture(TextureMap.locationBlocksTexture)
        .setBlurMipmap(false, false);

    GlStateManager.enableRescaleNormal();
    GlStateManager.alphaFunc(516, 0.1F);
    GlStateManager.enableBlend();
    RenderHelper.enableStandardItemLighting();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

    Stack stack = VersionedStackProvider.createStack(new MatrixStack());
    stack.push();

    stack.translate((float) x, (float) y, (float) z);
    stack.scale(0.5f, 0.5f, 0.5f);

    stack.rotateRadians((float) Math.PI / 2, 1, 0, 0);
    stack.rotateRadians(itemEntity.rotationYaw, 0, 0, 1);

    Minecraft mc = Minecraft.getMinecraft();
    IBakedModel iBakedModel = mc.getRenderItem().getItemModelMesher().getItemModel(itemStack);

    this.rotateX(itemEntity, rotation);
    if (iBakedModel.isGui3d()) {
      stack.translate(0, -0.2f, -0.08f);
    } else if (itemEntity.getEntityWorld().getBlockState(itemEntity.getPosition()).getBlock()
        == Blocks.snow
        || itemEntity.getEntityWorld().getBlockState(itemEntity.getPosition().down()).getBlock()
        == Blocks.soul_sand) {
      stack.translate(0, 0, -0.14f);
    } else {
      stack.translate(0, 0, -0.04f);
    }

    float height = 0.2f;
    if (iBakedModel.isGui3d()) {
      stack.translate(0, height, 0);
    }
    stack.rotateRadians(itemEntity.rotationPitch, 0, 1, 0);
    if (iBakedModel.isGui3d()) {
      stack.translate(0, -height, 0);
    }

    if (!iBakedModel.isGui3d()) {
      float xO = -0.0F * (renderCount - 1) * 0.5F;
      float yO = -0.0F * (renderCount - 1) * 0.5F;
      float zO = -0.09375F * (renderCount - 1) * 0.5F;
      stack.translate(xO, yO, zO);
    }

    for (int k = 0; k < renderCount; ++k) {
      stack.push();
      if (k > 0) {
        if (iBakedModel.isGui3d()) {
          float f11 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
          float f13 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
          float f10 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
          stack.translate(f11, f13, f10);
        }
      }

      iBakedModel.getItemCameraTransforms().applyTransform(TransformType.GROUND);
      this.itemRenderer.renderItem(itemStack, iBakedModel);

      stack.pop();
      if (!iBakedModel.isGui3d()) {
        stack.translate(0, 0, 0.09375F);
      }
    }

    stack.pop();

    GlStateManager.disableRescaleNormal();
    GlStateManager.disableBlend();
    this.bindTexture(TextureMap.locationBlocksTexture);
    this.getRenderManager().renderEngine.getTexture(TextureMap.locationBlocksTexture)
        .restoreLastBlurMipmap();

    callbackInfo.cancel();
  }

  private void rotateX(EntityItem itemEntity, float rotation) {
    if (itemEntity.onGround) {
      return;
    }
    itemEntity.rotationPitch = itemEntity.rotationPitch + rotation * 2;
  }
}
