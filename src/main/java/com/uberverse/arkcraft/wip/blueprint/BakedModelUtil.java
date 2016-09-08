package com.uberverse.arkcraft.wip.blueprint;

import java.awt.Color;

import com.google.common.primitives.Ints;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class BakedModelUtil
{
	/**
	 * #region MBE15
	 * The following methods are taken from MBE15, all credit to TheGreyGhost
	 */
	/**
	 * // Creates a baked quad for the given face.
	 * // When you are directly looking at the face, the quad is centred at [centreLR, centreUD]
	 * // The left<->right "width" of the face is width, the bottom<-->top "height" is height.
	 * // The amount that the quad is displaced towards the viewer i.e. (perpendicular to the flat face you can see) is forwardDisplacement
	 * // - for example, for an EAST face, a value of 0.00 lies directly on the EAST face of the cube. a value of 0.01 lies
	 * // slightly to the east of the EAST face (at x=1.01). a value of -0.01 lies slightly to the west of the EAST face (at x=0.99).
	 * // The orientation of the faces is as per the diagram on this page
	 * // http://greyminecraftcoder.blogspot.com.au/2014/12/block-models-texturing-quads-faces.html
	 * // Read this page to learn more about how to draw a textured quad
	 * // http://greyminecraftcoder.blogspot.co.at/2014/12/the-tessellator-and-worldrenderer-18.html
	 * 
	 * @param centreLR
	 *            the centre point of the face left-right
	 * @param width
	 *            width of the face
	 * @param centreUD
	 *            centre point of the face top-bottom
	 * @param height
	 *            height of the face from top to bottom
	 * @param forwardDisplacement
	 *            the displacement of the face (towards the front)
	 * @param itemRenderLayer
	 *            which item layer the quad is on
	 * @param texture
	 *            the texture to use for the quad
	 * @param face
	 *            the face to draw this quad on
	 * @return
	 */
	public static BakedQuad createBakedQuadForFace(float centreLR, float width, float centreUD, float height, float forwardDisplacement,
			int itemRenderLayer, TextureAtlasSprite texture, EnumFacing face)
	{
		float x1, x2, x3, x4;
		float y1, y2, y3, y4;
		float z1, z2, z3, z4;
		final float CUBE_MIN = 0.0F;
		final float CUBE_MAX = 1.0F;

		switch (face)
		{
			case UP:
			{
				x1 = x2 = centreLR + width / 2.0F;
				x3 = x4 = centreLR - width / 2.0F;
				z1 = z4 = centreUD + height / 2.0F;
				z2 = z3 = centreUD - height / 2.0F;
				y1 = y2 = y3 = y4 = CUBE_MAX + forwardDisplacement;
				break;
			}
			case DOWN:
			{
				x1 = x2 = centreLR + width / 2.0F;
				x3 = x4 = centreLR - width / 2.0F;
				z1 = z4 = centreUD - height / 2.0F;
				z2 = z3 = centreUD + height / 2.0F;
				y1 = y2 = y3 = y4 = CUBE_MIN - forwardDisplacement;
				break;
			}
			case WEST:
			{
				z1 = z2 = centreLR + width / 2.0F;
				z3 = z4 = centreLR - width / 2.0F;
				y1 = y4 = centreUD - height / 2.0F;
				y2 = y3 = centreUD + height / 2.0F;
				x1 = x2 = x3 = x4 = CUBE_MIN - forwardDisplacement;
				break;
			}
			case EAST:
			{
				z1 = z2 = centreLR - width / 2.0F;
				z3 = z4 = centreLR + width / 2.0F;
				y1 = y4 = centreUD - height / 2.0F;
				y2 = y3 = centreUD + height / 2.0F;
				x1 = x2 = x3 = x4 = CUBE_MAX + forwardDisplacement;
				break;
			}
			case NORTH:
			{
				x1 = x2 = centreLR - width / 2.0F;
				x3 = x4 = centreLR + width / 2.0F;
				y1 = y4 = centreUD - height / 2.0F;
				y2 = y3 = centreUD + height / 2.0F;
				z1 = z2 = z3 = z4 = CUBE_MIN - forwardDisplacement;
				break;
			}
			case SOUTH:
			{
				x1 = x2 = centreLR + width / 2.0F;
				x3 = x4 = centreLR - width / 2.0F;
				y1 = y4 = centreUD - height / 2.0F;
				y2 = y3 = centreUD + height / 2.0F;
				z1 = z2 = z3 = z4 = CUBE_MAX + forwardDisplacement;
				break;
			}
			default:
			{
				assert false : "Unexpected facing in createBakedQuadForFace:" + face;
				return null;
			}
		}

		return new BakedQuad(Ints.concat(vertexToInts(x1, y1, z1, Color.WHITE.getRGB(), texture, 16, 16),
				vertexToInts(x2, y2, z2, Color.WHITE.getRGB(), texture, 16, 0), vertexToInts(x3, y3, z3, Color.WHITE.getRGB(), texture, 0, 0),
				vertexToInts(x4, y4, z4, Color.WHITE.getRGB(), texture, 0, 16)), itemRenderLayer, face);
	}

	/**
	 * Converts the vertex information to the int array format expected by BakedQuads.
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param z
	 *            z coordinate
	 * @param color
	 *            RGBA colour format - white for no effect, non-white to tint the face with the specified colour
	 * @param texture
	 *            the texture to use for the face
	 * @param u
	 *            u-coordinate of the texture (0 - 16) corresponding to [x,y,z]
	 * @param v
	 *            v-coordinate of the texture (0 - 16) corresponding to [x,y,z]
	 * @return
	 */
	private static int[] vertexToInts(float x, float y, float z, int color, TextureAtlasSprite texture, float u, float v)
	{
		return new int[] { Float.floatToRawIntBits(x), Float.floatToRawIntBits(y), Float.floatToRawIntBits(z), color,
				Float.floatToRawIntBits(texture.getInterpolatedU(u)), Float.floatToRawIntBits(texture.getInterpolatedV(v)), 0 };
	}

	/**
	 * #endregion
	 */
}
