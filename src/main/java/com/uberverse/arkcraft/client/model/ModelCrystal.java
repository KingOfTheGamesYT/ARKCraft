package com.uberverse.arkcraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCrystal extends ModelBase
{
  //fields
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
    ModelRenderer Shape6;
    ModelRenderer Shape7;
    ModelRenderer Shape8;
    ModelRenderer Shape9;
  
  public ModelCrystal()
  {
    textureWidth = 64;
    textureHeight = 64;
    
      Shape1 = new ModelRenderer(this, 0, 0);
      Shape1.addBox(0F, 0F, 0F, 9, 3, 10);
      Shape1.setRotationPoint(-4F, 21F, -5F);
      Shape1.setTextureSize(64, 64);
      Shape1.mirror = true;
      setRotation(Shape1, 0F, 0F, 0F);
      Shape2 = new ModelRenderer(this, 23, 14);
      Shape2.addBox(0F, -9F, 0F, 2, 10, 2);
      Shape2.setRotationPoint(0F, 21F, -1F);
      Shape2.setTextureSize(64, 64);
      Shape2.mirror = true;
      setRotation(Shape2, 0.0523599F, 0F, 0F);
      Shape3 = new ModelRenderer(this, 50, 14);
      Shape3.addBox(0F, -6F, 0F, 2, 6, 2);
      Shape3.setRotationPoint(-1F, 22F, -3F);
      Shape3.setTextureSize(64, 64);
      Shape3.mirror = true;
      setRotation(Shape3, 0.4712389F, 0F, 0F);
      Shape4 = new ModelRenderer(this, 48, 6);
      Shape4.addBox(0F, -6F, 0F, 2, 6, 2);
      Shape4.setRotationPoint(-3F, 22F, -1.5F);
      Shape4.setTextureSize(64, 64);
      Shape4.mirror = true;
      setRotation(Shape4, 0F, 0F, -0.3316126F);
      Shape5 = new ModelRenderer(this, 41, 14);
      Shape5.addBox(0F, -6F, 0F, 2, 5, 2);
      Shape5.setRotationPoint(2F, 22F, -0.5F);
      Shape5.setTextureSize(64, 64);
      Shape5.mirror = true;
      setRotation(Shape5, 0F, 0F, 0.1745329F);
      Shape6 = new ModelRenderer(this, 32, 14);
      Shape6.addBox(0F, -7F, 0F, 2, 7, 2);
      Shape6.setRotationPoint(0F, 21F, 2F);
      Shape6.setTextureSize(64, 64);
      Shape6.mirror = true;
      setRotation(Shape6, -0.2094395F, 0F, 0F);
      Shape7 = new ModelRenderer(this, 0, 14);
      Shape7.addBox(0F, 0F, 0F, 2, 3, 7);
      Shape7.setRotationPoint(-6F, 21F, -4F);
      Shape7.setTextureSize(64, 64);
      Shape7.mirror = true;
      setRotation(Shape7, 0F, 0F, 0F);
      Shape8 = new ModelRenderer(this, 29, 0);
      Shape8.addBox(0F, 0F, 0F, 2, 2, 7);
      Shape8.setRotationPoint(5F, 22F, -3F);
      Shape8.setTextureSize(64, 64);
      Shape8.mirror = true;
      setRotation(Shape8, 0F, 0F, 0F);
      Shape9 = new ModelRenderer(this, 12, 14);
      Shape9.addBox(0F, 0F, 0F, 4, 2, 1);
      Shape9.setRotationPoint(-3F, 22F, 5F);
      Shape9.setTextureSize(64, 64);
      Shape9.mirror = true;
      setRotation(Shape9, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
	setRotationAngles(entity, f, f1, f2, f3, f4, f5);
    Shape1.render(f5);
    Shape2.render(f5);
    Shape3.render(f5);
    Shape4.render(f5);
    Shape5.render(f5);
    Shape6.render(f5);
    Shape7.render(f5);
    Shape8.render(f5);
    Shape9.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(Entity e, float f, float f1, float f2, float f3, float f4, float f5)
  {
	super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
  }
}
