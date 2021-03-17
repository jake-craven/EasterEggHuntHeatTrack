package com.example.easterhunt;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class HuntHint extends androidx.appcompat.widget.AppCompatButton {
    final String TAG = "Hint Button";
    private String hint;
    boolean unlocked = false;
    int unlockDistanceRange;
    TextView HintTextBox;
    private int index;

    public HuntHint(Context context, String hint, int unlockDistanceRange,
                    TextView HintTextBox, int index){
        super(context);
        this.setVisibility(VISIBLE);
        this.index = index+1;
        this.hint = hint;
        this.unlockDistanceRange = unlockDistanceRange;
        this.unlocked = false;
        this.HintTextBox = HintTextBox;
        ButtonSetup();
    }

    void ButtonSetup(){
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(WRAP_CONTENT,
                WRAP_CONTENT);
        lp.setMargins(10,10,10,10);
        this.setLayoutParams(lp);
        setButtonText();
        this.setVisibility(VISIBLE);
        this.setBackgroundColor(Color.GRAY);
        GradientDrawable shape =  new GradientDrawable();
        shape.setColor(Color.GRAY);
        shape.setCornerRadius( 8 );
        this.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        this.setTextSize(18);
        this.setMinimumHeight(50);
        this.setPadding(20,20,20,20);
        this.setBackground(shape);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });
    }

    boolean isUnlocked(){
        return unlocked;
    }

    boolean UpdatedDistance(int distance){
        if( distance <= unlockDistanceRange){
            unlocked = true;
        }
        setButtonText();
        return unlocked;
    }

    private void setButtonText(){
        if(isUnlocked()){
            setUnlockedText();
        } else {
            setLockedText();
        }
    }

    public void click() {
        Log.d(TAG, "setOnClickListener: Click Listener");
        if(isUnlocked()){
            HintTextBox.setText(hint);
        } else {
            HintTextBox.setText("Hint locked get within "+unlockDistanceRange+" metres to view");
        }
    }

    private void setLockedText(){
        this.setText("\n??Locked??\n");
    }

    private void setUnlockedText(){
        this.setText("\n  HINT "+index+"  \n");
    }
}
