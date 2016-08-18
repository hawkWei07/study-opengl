package cn.hawk.lessonthree;


/**
 * Created by kehao.wei on 2016/8/18.
 */

public class LessonThreeRenderer extends LessonTwoRenderer {
    protected String getVertexShader() {
        // TODO: Explain why we normalize the vectors, explain some of the vector math behind it all. Explain what is eye space.
        final String vertexShader = "" +
                "uniform mat4 u_MVPMatrix;      \n"        // A constant representing the combined model/view/projection matrix.
                + "uniform mat4 u_MVMatrix;       \n"        // A constant representing the combined model/view matrix.

                + "attribute vec4 a_Position;     \n"        // Per-vertex position information we will pass in.
                + "attribute vec4 a_Color;        \n"        // Per-vertex color information we will pass in.
                + "attribute vec3 a_Normal;       \n"        // Per-vertex normal information we will pass in.

                + "varying vec3 v_Position;     \n"        // Per-vertex position information we will pass to fragment shader.
                + "varying vec4 v_Color;        \n"        // Per-vertex color information we will pass to fragment shader.
                + "varying vec3 v_Normal;       \n"        // Per-vertex normal information we will pass to fragment shader.
                + "void main(){\n"
                + "v_Position = vec3(u_MVMatrix * a_Position); \n" // Transform the vertext into eye space.
                + " v_Color = a_Color;\n"                                           // Pass through the color.
                + "v_Normal = vec3(u_MVMatrix * vec4(a_Normal,0.0));\n"// Transform the normal's orientation into eye space
                + "gl_Position = u_MVPMatrix * a_Position;\n"                       // gl_Position is a special variable used to store the final position.
                + "}";

        return vertexShader;
    }

    protected String getFragmentShader() {
        final String fragmentShader = "" +
                "precision mediump float;       \n"        // Set the default precision to medium. We don't need as high of a
                // precision in the fragment shader.
                + "uniform vec3 u_LightPos;       \n"        // The position of the light in eye space.
                + "varying vec3 v_Position; \n"
                + "varying vec4 v_Color; \n"
                + "varying vec3 v_Normal; \n"
                + "void main()\n"
                + "{\n"
                + "float distance = length(u_LightPos - v_Position);\n"
                + "vec3 lightVector = normalize(u_LightPos - v_Position);\n"
                + "float diffuse = max(dot(v_Normal,lightVector),0.1);\n"
                + "diffuse = diffuse * (1.0/(1.0 + 0.25 * distance * distance));\n"
                + "gl_FragColor = v_Color * diffuse;\n"
                + "}";
        return fragmentShader;
    }
}
