package cn.hawk.lessonone;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by kehaowei on 16/7/15.
 */
public class LessonOneRenderer implements GLSurfaceView.Renderer {


    /**
     * 三角形定点数据
     */
    private final FloatBuffer mTriangle1Vertices;
    private final FloatBuffer mTriangle2Vertices;
    private final FloatBuffer mTriangle3Vertices;

    /**
     * 每一个float的字节数
     */
    private final int mBytesPerFloat = 4;

    /**
     * 保存视角矩阵
     */
    private float[] mViewMatrix = new float[16];

    /**
     * 投射矩阵
     */
    private float[] mProjectionMatrix = new float[16];

    /**
     * 模型矩阵
     */
    private float[] mModelMatrix = new float[16];

    /**
     * 合成矩阵
     */
    private float[] mMVPMatrix = new float[16];

    /**
     * 每个顶点的参数字节数
     */
    private final int mStrideBytes = 7 * mBytesPerFloat;

    /**
     * 位置信息的偏移量
     */
    private final int mPositionOffset = 0;

    /**
     * 位置信息长度
     */
    private final int mPositionDataSize = 3;

    /**
     * 颜色信息偏移量
     */
    private final int mColorOffset = 3;

    /**
     * 颜色信息长度
     */
    private final int mColorDataSize = 4;

    /**
     * 参数操作代理
     */
    private int mMVPMatrixHandle;
    private int mPositionHandle;
    private int mColorHandle;

    public LessonOneRenderer() {

        // 三角形 红,绿,蓝
        final float[] triangle1VerticesData = {
                // X,Y,Z
                // R,G,B,A
                -0.5f, -0.25f, 0.0f,
                1.0f, 0.0f, 0.0f, 1.0f,

                0.5f, -0.25f, 0.0f,
                0.0f, 0.0f, 1.0f, 1.0f,

                0.0f, 0.559016994f, 0.0f,
                0.0f, 1.0f, 0.0f, 1.0f
        };

        // 三角形 黄,青,品红
        final float[] triangle2VerticesData = {
                // X, Y, Z,
                // R, G, B, A
                -0.5f, -0.25f, 0.0f,
                1.0f, 1.0f, 0.0f, 1.0f,

                0.5f, -0.25f, 0.0f,
                0.0f, 1.0f, 1.0f, 1.0f,

                0.0f, 0.559016994f, 0.0f,
                1.0f, 0.0f, 1.0f, 1.0f
        };

        // 三角形 白,灰,黑
        final float[] triangle3VerticesData = {
                // X, Y, Z,
                // R, G, B, A
                -0.5f, -0.25f, 0.0f,
                1.0f, 1.0f, 1.0f, 1.0f,

                0.5f, -0.25f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f,

                0.0f, 0.559016994f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        };

        // 设定顶点数据缓存格式
        mTriangle1Vertices = ByteBuffer.allocateDirect(triangle1VerticesData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangle2Vertices = ByteBuffer.allocateDirect(triangle2VerticesData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangle3Vertices = ByteBuffer.allocateDirect(triangle3VerticesData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();

        // 加载三角形顶点数据
        mTriangle1Vertices.put(triangle1VerticesData).position(0);
        mTriangle2Vertices.put(triangle2VerticesData).position(0);
        mTriangle3Vertices.put(triangle3VerticesData).position(0);
    }

    private void drawTriangle(final FloatBuffer aTriangleBuffer) {
        // 传入位置信息
        aTriangleBuffer.position(mPositionOffset);
        GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false, mStrideBytes, aTriangleBuffer);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // 传入颜色信息
        aTriangleBuffer.position(mColorOffset);
        GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false, mStrideBytes, aTriangleBuffer);
        GLES20.glEnableVertexAttribArray(mColorHandle);

        // 合成视角矩阵和模型矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        // 合成投射矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }


    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        // 清理画布颜色为灰色
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

        // 设置视角在原点后方
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 1.5f;

        // 设置视线方向为负z轴
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // 设置矢量方向为Y正方向,就好像人站立的方向是向上,也是我们头的方向
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // 设置视角矩阵,代表相机的位置属性
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);

        // 定义顶点着色器
        final String vertexShader =
                "uniform mat4 u_MVPMatrix;\n" + // 模型、视角、投射矩阵结合起来的常量
                        "attribute vec4 a_position;\n" + // 我们需要传入的每个顶点位置参数
                        "attribute vec4 a_color;\n" + // 我们需要传入的每个顶点颜色参数
                        "varying vec4 v_color;\n" + // 会被传入面着色器的颜色变量
                        "void main(){\n" + // 顶点着色器的入口
                        " v_color = a_color;\n" + // 将颜色传入面着色器
                        "gl_Position = u_MVPMatrix * a_position;\n" + // 将坐标与矩阵相乘获得最终位置
                        "}";

        // 定义面着色器
        final String fragmentShader =
                "precision mediump float;\n" + // 设置精确度为中等
                        "varying vec4 v_color;\n" + // 传递进来的变量
                        "void main(){\n" + // 面着色器的入口
                        "gl_FragColor = v_color;\n" + // 通过管道传递颜色值
                        "}";

        // 加载顶点着色器
        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        if (vertexShaderHandle != 0) {
            // 传入数据
            GLES20.glShaderSource(vertexShaderHandle, vertexShader);
            // 合成着色器
            GLES20.glCompileShader(vertexShaderHandle);
            // 返回合成结果
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
            // 如果合成失败,删除着色器
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(vertexShaderHandle);
                vertexShaderHandle = 0;
            }

        }

        if (vertexShaderHandle == 0) {
            throw new RuntimeException("Error creating vertex shader.");
        }

        // Load in the fragment shader shader.
        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        if (fragmentShaderHandle != 0) {
            // Pass in the shader source.
            GLES20.glShaderSource(fragmentShaderHandle, fragmentShader);

            // Compile the shader.
            GLES20.glCompileShader(fragmentShaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0) {
                GLES20.glDeleteShader(fragmentShaderHandle);
                fragmentShaderHandle = 0;
            }
        }

        if (fragmentShaderHandle == 0) {
            throw new RuntimeException("Error creating fragment shader.");
        }

        // 创建渲染程序主体
        int programHandle = GLES20.glCreateProgram();
        if (programHandle != 0) {
            // 绑定着色器
            GLES20.glAttachShader(programHandle, vertexShaderHandle);
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            // 绑定参数
            GLES20.glBindAttribLocation(programHandle, 0, "a_position");
            GLES20.glBindAttribLocation(programHandle, 1, "a_color");

            // 链接程序
            GLES20.glLinkProgram(programHandle);

            // 获得链接结果
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // 如果链接失败,删除程序主体
            if (linkStatus[0] == 0) {
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0) {
            throw new RuntimeException("Error creating program.");
        }

        // 获得参数的代理
        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_position");
        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_color");

        GLES20.glUseProgram(programHandle);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        // 重置视角矩阵
        GLES20.glViewport(0, 0, width, height);

        // 设置精确的投射矩阵
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;

        // 设置锥形的投射矩阵
        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);


    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        // 清除深度和颜色缓存
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        // 每10秒做一次完整的旋转
        long time = System.currentTimeMillis() % 10000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);

        // 面朝天绘制三角形
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);
        drawTriangle(mTriangle1Vertices);

        // Draw one translated a bit down and rotated to be flat on the ground.
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, -1.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, 90.0f, 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);
        drawTriangle(mTriangle2Vertices);

        // Draw one translated a bit to the right and rotated to be facing to the left.
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, 90.0f, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);
        drawTriangle(mTriangle3Vertices);
    }
}
