/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.labymod.addons.itemphysics.v1_12_2.client;

import javax.inject.Singleton;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.labymod.addons.itemphysics.bridge.BakedModel;
import net.labymod.addons.itemphysics.bridge.ItemEntity;
import net.labymod.addons.itemphysics.bridge.VersionBridge;
import net.labymod.addons.itemphysics.util.FloatOptional;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.client.world.item.Item;
import net.labymod.api.client.world.item.ItemStack;
import net.labymod.api.models.Implements;
import net.labymod.api.util.function.Functional;
import net.labymod.api.util.math.vector.FloatVector3;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lwjgl.util.vector.Vector3f;

@Singleton
@Implements(VersionBridge.class)
public class VersionedVersionBridge implements VersionBridge {

  private static final int WATER_ID = 9;
  private static final int LAVA_ID = 11;

  private final Int2ObjectMap<Fluid> fluids = Functional.of(new Int2ObjectOpenHashMap<>(), map -> {
    map.put(WATER_ID, new Fluid(5.0F));
    map.put(LAVA_ID, new Fluid(30.0F));
    map.defaultReturnValue(new Fluid(0.0F));
  });

  private final FloatVector3 groundScale = new FloatVector3(1.0F, 1.0F, 1.0F);

  @Override
  public int getItemId(ItemStack itemStack) {
    Item item = itemStack.getAsItem();
    return net.minecraft.item.Item.getIdFromItem((net.minecraft.item.Item) item);
  }

  @Override
  public boolean isGamePaused() {
    return Minecraft.getMinecraft().isGamePaused();
  }

  @Override
  public void preRenderItem(ItemEntity entity, boolean renderOutlines) {
    this.bindTexture(false);

    GlStateManager.enableRescaleNormal();
    GlStateManager.alphaFunc(516, 0.1F);
    GlStateManager.enableBlend();
    RenderHelper.enableStandardItemLighting();
    GlStateManager.tryBlendFuncSeparate(
        GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
        GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
    );

    if (renderOutlines) {
      GlStateManager.enableColorMaterial();
      GlStateManager.enableOutlineMode(this.getTeamColor((Entity) entity));
    }
  }

  @Override
  public void renderItem(
      ItemStack itemStack,
      Stack stack,
      Object bufferSource,
      int packedLightCoords,
      BakedModel bakedModel
  ) {
    RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
    ((IBakedModel) bakedModel).getItemCameraTransforms().applyTransform(TransformType.GROUND);
    itemRenderer.renderItem(
        (net.minecraft.item.ItemStack) (Object) itemStack,
        (IBakedModel) bakedModel
    );
  }

  @Override
  public void postRenderItem(ItemEntity entity, boolean renderOutlines) {
    if (renderOutlines) {
      GlStateManager.disableOutlineMode();
      GlStateManager.disableColorMaterial();
    }

    GlStateManager.disableRescaleNormal();
    GlStateManager.disableBlend();
    this.bindTexture(true);
  }

  @Override
  public boolean isSpecialBlock(ItemEntity entity) {
    EntityItem itemEntity = (EntityItem) entity;
    World level = itemEntity.world;

    BlockPos blockPos = itemEntity.getPosition();
    return level.getBlockState(blockPos).getBlock() == Blocks.SNOW ||
        level.getBlockState(blockPos.down()).getBlock() == Blocks.SOUL_SAND;
  }

  @Override
  public boolean hasOptions() {
    return Minecraft.getMinecraft().gameSettings != null;
  }

  @Override
  public FloatOptional getFluidViscosity(ItemEntity entity, boolean withBelow) {
    EntityItem itemEntity = (EntityItem) entity;

    Fluid fluid = this.getFluid(itemEntity);
    if (withBelow) {
      fluid = this.getFluid(itemEntity, true);
    }

    return fluid == null ? FloatOptional.empty() : FloatOptional.of(fluid.getTickDelay());
  }

  @Override
  public FloatVector3 getGroundScale(BakedModel model) {
    ItemCameraTransforms transforms = ((IBakedModel) model).getItemCameraTransforms();
    Vector3f scale = transforms.ground.scale;

    this.groundScale.set(scale.x, scale.y, scale.z);

    return this.groundScale;
  }

  private Fluid getFluid(EntityItem entity) {
    return this.getFluid(entity, false);
  }

  private Fluid getFluid(EntityItem entity, boolean below) {
    double y = entity.posY;
    BlockPos pos = entity.getPosition();
    if (below) {
      pos = pos.down();
    }

    World level = entity.world;
    IBlockState blockState = level.getBlockState(pos);
    Block block = blockState.getBlock();

    int blockId = Block.REGISTRY.getIDForObject(block);

    Fluid fluid = this.fluids.get(blockId);
    if (fluid.getTickDelay() == 0) {
      return null;
    }

    if (below) {
      return fluid;
    }

    AxisAlignedBB boundingBox = blockState.getBoundingBox(level, pos);
    float height = (float) (boundingBox.maxY - boundingBox.minY);
    if (y - pos.getY() - 0.2 <= height) {
      return fluid;
    }

    return null;
  }

  private int getTeamColor(Entity entity) {
    int color = 0xFFFFFF;
    ScorePlayerTeam team = (ScorePlayerTeam) entity.getTeam();

    if (team != null) {
      String text = FontRenderer.getFormatFromString(team.getPrefix());
      if (text.length() >= 2) {
        color = Minecraft.getMinecraft().fontRenderer.getColorCode(text.charAt(1));
      }
    }

    return color;
  }

  private void bindTexture(boolean restore) {
    TextureManager renderEngine = Minecraft.getMinecraft().getRenderManager().renderEngine;

    renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    ITextureObject texture = renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

    if (texture != null) {
      if (restore) {
        texture.restoreLastBlurMipmap();
      } else {
        texture.setBlurMipmap(false, false);
      }
    }
  }

  private static class Fluid {

    private final float tickDelay;

    public Fluid(float tickDelay) {
      this.tickDelay = tickDelay;
    }

    public float getTickDelay() {
      return this.tickDelay;
    }

  }

}
