package co.yishun.lighting.ui.view.shoot.filter;//package me.relex.camerafilter.filter;
//
//import android.content.Context;
//import android.opengl.GLES10;
//import me.relex.camerafilter.R;
//import me.relex.camerafilter.gles.GlUtil;
//
//public class ImageFilterBlur extends CameraFilterGroup {
//    public ImageFilterBlur(Context applicationContext) {
//        super(applicationContext);
//    }
//
//    @Override public int getTextureTarget() {
//        return GLES10.GL_TEXTURE_2D;
//    }
//
//    @Override protected int createProgram(Context applicationContext) {
//        return GlUtil.createProgram(applicationContext, R.raw.vertex_shader,
//                R.raw.fragment_shader_2d_kernel);
//    }
//}
