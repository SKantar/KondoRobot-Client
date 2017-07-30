package raf.edu.rs.kondorobot;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class SwipeListener implements View.OnTouchListener {

    protected static final String TAG = "TouchListener";

    private GestureDetector gestureDetector;
    private Context context;
    private Handler handler;

    public SwipeListener(Context context, Handler socketHandler) {
        this.context = context;
        this.gestureDetector = new GestureDetector(context, new GestureListener());
        this.handler = socketHandler;
    }

    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            onClick();
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            onDoubleClick();
            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            onLongClick();
            super.onLongPress(e);
        }

        // Determines the fling velocity and then fires the appropriate swipe event accordingly
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeDown();
                        } else {
                            onSwipeUp();
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    private void sendCommand(String command){
        Message message = handler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("message", command);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    private void onSwipeRight() {
        sendCommand("RIGHT");
    }

    private void onSwipeLeft() {
        sendCommand("LEFT");
    }

    private void onSwipeUp() {
        sendCommand("UP");
    }

    private void onSwipeDown() {
        sendCommand("DOWN");
    }

    public void onClick() {
        sendCommand("CLICK");
    }

    public void onDoubleClick() {
        sendCommand("DOUBLE_CLICK");
    }

    public void onLongClick() {
        sendCommand("LONG");
    }
}
