package com.eju.tools.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;


import com.eju.tools.R;

import java.util.ArrayList;
import java.util.List;

public class LetterNavigationView extends View {

    private List<String> letters;

    private int textColor;
    private int textSize;
    private int textSelectedColor;
    private int textSelectedSize;
    private int padding;
    private int letterPadding;
    private int touchBgRadius;
    private int touchBgColor;

    private Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);

    private Drawable touchBgDrawable;

    private int letterWidth;
    private int letterHeight;
    private int selectedLetterWidth;
    private int selectedLetterHeight;


    private int selectedPosition;

    private OnSelectedLetterChangedListener onSelectedLetterChangedListener;

    private RectF viewRect;



    public LetterNavigationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context,attrs);
        generateTouchBgDrawable();
        initDefaultLetters();
        calculateLetterSize();
    }


    private void initAttrs(Context context, @Nullable AttributeSet attrs){
        float density = context.getResources().getDisplayMetrics().density;
        TypedArray attrValues = context.obtainStyledAttributes(attrs, R.styleable.LetterNavigationView);
        textColor=attrValues.getColor(R.styleable.LetterNavigationView_textColor,0xff333333);
        textSelectedColor= attrValues.getColor(R.styleable.LetterNavigationView_textSelectedColor,0xff333333);
        textSize= attrValues.getDimensionPixelSize(R.styleable.LetterNavigationView_textSize, (int) (10*density));
        textSelectedSize=attrValues.getDimensionPixelSize(R.styleable.LetterNavigationView_textSelectedSize, (int) (16*density));
        padding=attrValues.getDimensionPixelSize(R.styleable.LetterNavigationView_padding, (int) (30*density));
        letterPadding=attrValues.getDimensionPixelSize(R.styleable.LetterNavigationView_letterPadding, (int) (1*density));
        touchBgRadius=attrValues.getDimensionPixelSize(R.styleable.LetterNavigationView_touchBgRadius, (int) (15*density));;
        touchBgColor=attrValues.getColor(R.styleable.LetterNavigationView_touchBgColor,Color.TRANSPARENT);;
        attrValues.recycle();

    }

    private void generateTouchBgDrawable(){
        GradientDrawable gradientDrawable=new GradientDrawable();
        gradientDrawable.setColor(touchBgColor);
        gradientDrawable.setCornerRadius(touchBgRadius);
        touchBgDrawable=gradientDrawable;
    }

    private void initDefaultLetters(){
        letters=new ArrayList<>();
        for (int i = 65; i <91 ; i++) {
            letters.add(String.valueOf((char)i));
        }
        letters.add("#");
    }

    private void calculateLetterSize(){
        Rect rect=new Rect();
        paint.setTextSize(textSize);
        paint.getTextBounds("A",0,1,rect);
        letterWidth=rect.width();
        letterHeight=rect.height();

        paint.setTextSize(textSelectedSize);
        paint.getTextBounds("A",0,1,rect);
        selectedLetterWidth=rect.width();
        selectedLetterHeight=rect.height();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int height=(selectedLetterHeight+letterPadding*2)*letters.size()+padding*2;
        setMeasuredDimension(widthSize,height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            viewRect=new RectF(0,0,getWidth(),getHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i <letters.size() ; i++) {
            int y=padding+(i+1)*(selectedLetterHeight+letterPadding*2);
            String letter=letters.get(i);
            if(i==selectedPosition){
                paint.setTextSize(textSelectedSize);
                paint.setColor(textSelectedColor);
                paint.setTypeface(Typeface.DEFAULT_BOLD);
                int x=(getMeasuredWidth()-selectedLetterWidth)/2;
                canvas.drawText(letter,x,y-letterPadding,paint);
            }else{
                paint.setTextSize(textSize);
                paint.setColor(textColor);
                paint.setTypeface(Typeface.DEFAULT_BOLD);
                y=y-letterPadding-(selectedLetterHeight-letterHeight)/2;
                int x=(getMeasuredWidth()-letterWidth)/2;
                canvas.drawText(letter,x,y,paint);
            }
        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            setBackground(touchBgDrawable);
        }else if(event.getAction()==MotionEvent.ACTION_UP||event.getAction()==MotionEvent.ACTION_CANCEL){
            setBackground(null);
        }
        if(viewRect.contains(event.getX(),event.getY())){
            float y=event.getY();
            int position= (int) ((y-padding)/(selectedLetterHeight+letterPadding*2));
            position=resetPosition(position);
            if(position!=selectedPosition){
                select(position);
                if(onSelectedLetterChangedListener!=null){
                    onSelectedLetterChangedListener.onSelectedLetterChanged(letters.get(position),position);
                }
            }
        }
        return true;
    }


    private int resetPosition(int position){
        if(position<0){
            position=0;
        }else if(position>=letters.size()){
            position=letters.size()-1;
        }
        return position;
    }




    public void setOnSelectedLetterChangedListener(OnSelectedLetterChangedListener onSelectedLetterChangedListener) {
        this.onSelectedLetterChangedListener = onSelectedLetterChangedListener;
    }

    public void select(String letter){
        select(letters.indexOf(letter));
    }

    public void select(int position){
        position=resetPosition(position);
        if(selectedPosition!=position){
            selectedPosition=position;
            postInvalidate();
        }
    }

    public void setLetters(List<String> letters){
        setLetters(letters,0);
    }

    public void setLetters(List<String> letters,int selectedPosition){
        this.letters=letters;
        this.selectedPosition=selectedPosition;
        postInvalidate();
    }


    public interface OnSelectedLetterChangedListener{
        void onSelectedLetterChanged(String newSelectedLetter, int letterPosition);
    }
}
