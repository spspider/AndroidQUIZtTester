package com.example.ttester_paukov.Utils;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import static android.util.TypedValue.COMPLEX_UNIT_PX;

/**
 * Created by tanja on 10.05.2017.
 */

public class Utils_onTouchSize {
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private Matrix matrixRb = new Matrix();
    private Matrix savedMatrixRb = new Matrix();
    // we can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private static final int H_EXIT=3;
    private int mode = NONE;
    // remember some things for zooming
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;
    private float[] lastEvent = null;


    float prevscale=10;
    float prevscaleRbtn=10;
    RadioGroup radioGroup;
    public float multiplysize=10;
    float scale = 1;
    float scaleRb = 1;
    int action=0;
    public View.OnTouchListener touchListener=(new View.OnTouchListener(){
        //float txtsize=1;
        //float txtsizeRb=1;

        @Override
        public boolean onTouch(View v, MotionEvent event) {


            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    v.performClick();
                    mode = NONE;
                    lastEvent = null;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    ///float scale=1;

                    if (v instanceof RadioButton) {
                        prevscaleRbtn = prevscaleRbtn * scaleRb;
                        //Log.d(LOG_TAG, "prevscaleRbtn:" + prevscaleRbtn);
                    }
                    else if(v instanceof TextView){
                        //Log.d(LOG_TAG,"prevscale:"+prevscale);
                        prevscale = prevscale * scale;

                    }
                    oldDist = spacing(event);


                    if (oldDist > 10f) {
                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {

                            matrix.set(savedMatrix);
                            if (v instanceof RadioButton)
                                scaleRb = (newDist / oldDist);
                            else if(v instanceof TextView)
                                scale = (newDist / oldDist);

                        }

                    }


                    break;
            }
            //float scale = 1;
            if (v instanceof RadioButton){
                float txtsize = prevscaleRbtn * scaleRb;
                float new_size = txtsize * multiplysize;
                if (new_size<30.0){new_size=40;prevscaleRbtn=(new_size/multiplysize)/1.0f+0.01f;}
                if (new_size>80.0){new_size=80;prevscaleRbtn=(new_size/multiplysize)/1.0f-0.01f;}
                ArrayList<RadioButton> radios = new ArrayList<>();
                RadioButton thisRadioTouch = (RadioButton) v;
                thisRadioTouch.setChecked(true);
                for (int i=0;i < radioGroup.getChildCount(); i++) {
                    View vradio = radioGroup.getChildAt(i);

                    if (vradio instanceof RadioButton) {
                        radios.add((RadioButton) vradio);
                        ((RadioButton) vradio).setTextSize(COMPLEX_UNIT_PX, new_size);


                    }
                }
            }
            else if (v instanceof TextView){
                float txtsize = prevscale * scale;
                TextView tview = (TextView) v;
                float new_size = txtsize * multiplysize;
                if (new_size<30.0){new_size=30;scale=1;prevscale=(new_size/multiplysize)/1.0f+0.01f;}
                if (new_size>80.0){new_size=80;scale=1;prevscale=(new_size/multiplysize)/1.0f-0.01f;}
                tview.setTextSize(COMPLEX_UNIT_PX, new_size);
            }
            return true;
        }
    });
    public View.OnTouchListener touchListener2=(new View.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {


            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    if (v instanceof RadioButton){
                        savedMatrixRb.set(matrixRb);}
                    else if (v instanceof TextView){
                        savedMatrix.set(matrix);}
                    start.set(event.getX(), event.getY());
                    mode = DRAG;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);

                    if (oldDist > 10f) {
                        if (v instanceof RadioButton){
                            savedMatrixRb.set(matrixRb);
                        }
                        else if (v instanceof TextView){
                            savedMatrix.set(matrix);}
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    d = rotation(event);
                    break;

                case MotionEvent.ACTION_UP:

                    //break;
                case MotionEvent.ACTION_POINTER_UP:
                    v.performClick();
                    //if (action!=H_EXIT) {
                        //if (mode!=DRAG) {
                        if (v instanceof RadioButton) {
                            RadioButton thisRadioTouch = (RadioButton) v;
                            thisRadioTouch.setChecked(true);
                            thisRadioTouch.setPressed(false);
                            //thisRadioTouch.setPressed(true);
                        }
                        //}
                   //}
                    /*
                    if (v instanceof RadioButton) {
                        RadioButton thisRadioTouch = (RadioButton) v;
                        thisRadioTouch.setPressed(false);
                        //thisRadioTouch.setPressed(true);
                    }
                    */
                    mode = NONE;
                    action = NONE;
                    break;


                case MotionEvent.ACTION_MOVE:
                    if (v instanceof RadioButton) {
                        RadioButton thisRadioTouch = (RadioButton) v;
                        thisRadioTouch.setPressed(true);
                        //thisRadioTouch.setPressed(true);
                    }

                    if (mode == DRAG) {


                        float dx = event.getX() - start.x;
                        float dy = event.getY() - start.y;
                        if (v instanceof RadioButton){
                            //Log.d(LOG_TAG,"DRAG:"+dx);
                            if (Math.abs(dx)>1.0){action = H_EXIT;
                                RadioButton thisRadioTouch = (RadioButton) v;
                                thisRadioTouch.setPressed(false);}
                            matrixRb.set(savedMatrixRb);
                            matrixRb.postTranslate(dx, dy);

                        }
                        else if (v instanceof TextView) {
                            matrix.set(savedMatrix);
                            matrix.postTranslate(dx, dy);
                        }

                    } else if (mode == ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {

                            float scale = (newDist / oldDist);
                            if (v instanceof RadioButton){
                                matrixRb.set(savedMatrixRb);
                                matrixRb.postScale(scale, scale, mid.x, mid.y);}
                            else if (v instanceof TextView){
                                matrix.set(savedMatrix);
                                matrix.postScale(scale, scale, mid.x, mid.y);}
                        }

                    }
                    break;
            }

            float[] fRb = new float[9];
            matrixRb.getValues(fRb);
            float[] f = new float[9];
            matrix.getValues(f);

            if (v instanceof RadioButton){


                float scaleX = fRb[Matrix.MSCALE_X];
                float scaleY = f[Matrix.MSCALE_Y];
                float new_size = scaleX * prevscaleRbtn;
                float scale=1;
                if (new_size<10.0){new_size=10;scale=new_size/prevscaleRbtn+0.001f;matrixRb.setScale(scale, scale, mid.x, mid.y);}
                if (new_size>80.0){new_size=80;scale=new_size/prevscaleRbtn-0.001f;matrixRb.setScale(scale, scale, mid.x, mid.y);}
                ArrayList<RadioButton> radios = new ArrayList<>();

                for (int i=0;i < radioGroup.getChildCount(); i++) {
                    View vradio = radioGroup.getChildAt(i);

                    if (vradio instanceof RadioButton) {
                        radios.add((RadioButton) vradio);
                        ((RadioButton) vradio).setTextSize(COMPLEX_UNIT_PX, new_size);
                    }
                }
            }
            else if (v instanceof TextView){


                float scaleX = f[Matrix.MSCALE_X];
                float scaleY = f[Matrix.MSCALE_Y];
                //float txtsize = prevscale * scale;
                TextView tview = (TextView) v;
                float new_size = scaleX * prevscale;
                float scale=1;
                if (new_size<10.0){new_size=10;scale=new_size/prevscale+0.001f;matrix.setScale(scale, scale, mid.x, mid.y);}
                if (new_size>80.0){new_size=80;scale=new_size/prevscale-0.001f;matrix.setScale(scale, scale, mid.x, mid.y);}
                tview.setTextSize(COMPLEX_UNIT_PX, new_size);
            }


            return true;
        }
    });
    public View.OnTouchListener touchListenerRadioBtn=(new View.OnTouchListener(){
        float txtsize=1;
        float scale=1;



        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //(RadioButton) v;


            //RadioButton rview = (RadioButton) v;
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    v.performClick();
                    mode = NONE;
                    lastEvent = null;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    oldDist = spacing(event);
                    prevscaleRbtn = prevscaleRbtn * scale;
                    if (oldDist > 10f) {
                        savedMatrix.set(matrix);
                        midPoint(mid, event);
                        mode = ZOOM;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == ZOOM) {
                        float newDist = spacing(event);
                        if (newDist > 10f) {

                            matrix.set(savedMatrix);
                            scale = (newDist / oldDist);

                        }

                    }

                    break;
            }

            float txtsize = prevscaleRbtn * scale;
            float new_size = txtsize * multiplysize;

            if (new_size<30.0){new_size=30;prevscaleRbtn=(new_size/multiplysize)/1.0f+0.01f;}
            if (new_size>80.0){new_size=80;prevscaleRbtn=(new_size/multiplysize)/1.0f-0.01f;}
            //rview.setTextSize(COMPLEX_UNIT_PX, new_size);
            //RadioGroup radioGroup = (RadioGroup) v;
            ArrayList<RadioButton> radios = new ArrayList<>();
            RadioButton thisRadioTouch = (RadioButton) v;
            thisRadioTouch.setChecked(true);
            for (int i=0;i < radioGroup.getChildCount(); i++) {
                View vradio = radioGroup.getChildAt(i);

                if (vradio instanceof RadioButton) {
                    radios.add((RadioButton) vradio);
                    ((RadioButton) vradio).setTextSize(COMPLEX_UNIT_PX, new_size);


                }
            }
            //setchecked(radios,thisRadioTouch);

            //rview.;
            return true;
        }

    });

    private void setchecked(final ArrayList<RadioButton> radios, View vradio) {
        vradio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                RadioButton r = (RadioButton) v;
                r.setChecked(true);
                for (RadioButton r2:radios) {
                    if (r2.getId() != r.getId()) {
                        r2.setChecked(false);
                    }
                }

            }
        });
    }

    private float spacing(MotionEvent event) {
        if (event.getPointerCount()>1) {
            float x = event.getX(0) - event.getX(1);
            float y = event.getY(0) - event.getY(1);
            return (float) Math.sqrt(x * x + y * y);
        }
        else{
            return (float) Math.sqrt(1 * 1 + 1 * 1);}
    }

    /**
     * Calculate the mid point of the first two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * Calculate the degree to be rotated by.
     *
     * @param event
     * @return Degrees
     */
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    public void setPrevscale(float prevscale) {
        this.prevscale = prevscale;


    }

    public void setPrevscaleRbtn(float prevscaleRbtn) {
        this.prevscaleRbtn = prevscaleRbtn;
    }

    public void setRadioGroup(RadioGroup radioGroup) {
        this.radioGroup = radioGroup;
    }
}
