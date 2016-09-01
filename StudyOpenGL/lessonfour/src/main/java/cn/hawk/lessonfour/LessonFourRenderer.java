package cn.hawk.lessonfour;

import android.content.Context;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by kehao.wei on 2016/8/19.
 */

public class LessonFourRenderer implements GLSurfaceView.Renderer {
    /**
     * Used for debug logs.
     */
    private static final String TAG = "LessonFourRenderer";

    private final Context mActivityContext;

    /**
     * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
     * of being located at the center of the universe) to world space
     */
    private float[] mModelMatrix = new float[16];

    /**
     * Store the view matrix. this can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    private float[] mViewMatrix = new float[16];

    /**
     * Store the projection matrix. This is used to project the scene onto a 2D viewport
     */
    private float[] mProjectionMatix = new float[16];

    /**
     * Allocate storage for the final combined matrix. This will be passed into the shader program
     */
    private float[] mMVPMatrix = new float[16];


    public LessonFourRenderer(Context mActivityContext) {
        this.mActivityContext = mActivityContext;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {

    }

    @Override
    public void onDrawFrame(GL10 gl10) {

    }
}
