package cn.hawk.lessonfive;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * Created by kehaowei on 16/9/28.
 */

public class LessonFiveGLSurfaceView extends GLSurfaceView {
    private LessonFiveRenderer mRenderer;

    public LessonFiveGLSurfaceView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event != null) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (null != mRenderer) {
                    queueEvent(new Runnable() {
                        @Override
                        public void run() {
                            mRenderer.switchMode();
                        }
                    });
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public void setRenderer(LessonFiveRenderer renderer) {
        mRenderer = renderer;
        super.setRenderer(renderer);
    }
}
