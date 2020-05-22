package com.bluroverly;

import android.app.Activity;
import android.renderscript.RenderScript;
import android.view.View;
import java.util.Arrays;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.views.view.ReactViewGroup;

public class SajjadBlurOverlayManager extends ViewGroupManager<ReactViewGroup> {
    private static final String REACT_CLASS = "RCTSajjadBlurOverlay";
    private final ReactApplicationContext reactContext;
    private int mRadius = 20;
    private float mFactor = 1;
    public float[] mColorMatrix = new float[] {
        1, 0, 0, 0, 0,
        0, 1, 0, 0, 0,
        0, 0, 1, 0, 0,
        0, 0, 0, 1, 0
    };
    private RenderScript rs;
    SajjadBlurOverlayManager(ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
        this.rs = RenderScript.create(reactContext);
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected ReactViewGroup createViewInstance(ThemedReactContext reactContext) {
        return new ReactViewGroup(reactContext);
    }


    @ReactProp(name = "radius")
    public void setRadius(ReactViewGroup view, int Radius) {
        mRadius = Radius;
    }

    @ReactProp(name = "downsampling")
    public void setRadius(ReactViewGroup view, float factor) {
        mFactor = factor;
    }

    @ReactProp(name = "colorMatrix")
    public void setColorMatrix(ReactViewGroup view, ReadableArray cmArray) {
        float[] matrixArray = new float[20];

        for (int i = 0; i < cmArray.size(); i++) {
            matrixArray[i] = (float)cmArray.getDouble(i);
        }

        mColorMatrix = matrixArray;

        setBlurred(view);
        view.requestFocus();
    }

    private void setBlurred(final View view){
        final Activity activity = reactContext.getCurrentActivity();
        if(activity==null) return;
        new BlurTask(view,reactContext,rs,activity,mRadius,mFactor,mColorMatrix).execute();
    }
}
