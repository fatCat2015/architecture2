package com.eju.tools.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.MainThread
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eju.tools.R
import com.eju.tools.dp
import timber.log.Timber

class LetterNavigationView(context:Context, attrs:AttributeSet? = null ) :View(context,attrs){

    //自定义属性
    private var textColor:Int = 0

    private var textSize:Float = 0F

    private var textBold :Boolean = false

    private var selectedTextColor:Int = 0

    private var selectedTextSize:Float = 0F

    private var selectedTextBold :Boolean = true

    private var letterPadding :Int = 0

    private var backgroundOnTouch :Drawable? =null

    //绘制数据
    private val letters :MutableList<String> by lazy {
        MutableList(26){  //[65,90]: A-Z
            (it+65).toChar().toString()
        }
    }

    private var selectedPosition :Int = 0

    //绘制属性
    private val paint :Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG)
    }

    private val textRect :Rect by lazy {
        Rect()
    }

    private fun maxTextSize(textSize:Float,bold:Boolean,block:(String)->Float):Float{
        var maxSize = 0F
        paint.textSize = textSize
        if(bold){
            paint.typeface = Typeface.DEFAULT_BOLD
        }
        letters.forEach {
            maxSize = maxSize.coerceAtLeast(block(it))
        }
        return maxSize
    }

    private val textWidth :Float get() = maxTextSize(textSize,textBold) {
        paint.measureText(it)
    }

    private val textHeight :Float get() = maxTextSize(textSize,textBold) {
        paint.getTextBounds(it,0,it.length,textRect)
        textRect.height().toFloat()
    }

    private val selectedTextWidth :Float get() = maxTextSize(selectedTextSize,selectedTextBold) {
        paint.measureText(it)
    }

    private val selectedTexHeight :Float get() = maxTextSize(selectedTextSize,selectedTextBold) {
        paint.getTextBounds(it,0,it.length,textRect)
        textRect.height().toFloat()
    }

    private var letterWidth :Float = 0F

    private var letterHeight :Float = 0F

    private val letterRectF : RectF by lazy {
        RectF()
    }

    private val letterLocation:HashMap<Int,Pair<Float,Float>> by lazy {
        hashMapOf()
    }

    private var showTouchedBackground :Boolean = false

    //事件监听
    private var onLetterSelectedChangedListener :OnLetterSelectedChangedListener? = null

    init {
        obtainAttrs(context,attrs)
    }


    private fun obtainAttrs(context: Context,attrs: AttributeSet?){
        val attrValues = context.obtainStyledAttributes(attrs, R.styleable.LetterNavigationView)
        textColor = attrValues.getColor(R.styleable.LetterNavigationView_textColor, default_text_color.toInt())
        textSize = attrValues.getDimensionPixelSize(R.styleable.LetterNavigationView_textSize, default_text_size.dp.toInt()).toFloat()
        textBold = attrValues.getBoolean(R.styleable.LetterNavigationView_textBold, false)
        selectedTextColor = attrValues.getColor(R.styleable.LetterNavigationView_selectedTextColor, default_selected_text_color.toInt())
        selectedTextSize = attrValues.getDimensionPixelSize(R.styleable.LetterNavigationView_selectedTextSize, default_selected_text_size.dp.toInt()).toFloat()
        selectedTextBold = attrValues.getBoolean(R.styleable.LetterNavigationView_selectedTextBold, true)
        letterPadding = attrValues.getDimensionPixelSize(R.styleable.LetterNavigationView_letterPadding,0)
        backgroundOnTouch = attrValues.getDrawable(R.styleable.LetterNavigationView_backgroundOnTouch)
        attrValues.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        letterWidth = textWidth.coerceAtLeast(selectedTextWidth)
        letterHeight = textHeight.coerceAtLeast(selectedTexHeight)
        val width = letterWidth + paddingLeft + paddingRight
        val height = letterHeight*letters.size + letterPadding*(letters.size-1) + paddingTop + paddingBottom
        letterRectF.set(paddingLeft.toFloat() - touchPadding.dp, paddingTop.toFloat(), width-paddingRight + touchPadding.dp, height-paddingBottom)
        setMeasuredDimension(width.toInt(), height.toInt())
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if(changed){
            backgroundOnTouch?.let {
                it.setBounds(0,0,width,height)
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?:return
        if(showTouchedBackground){
            backgroundOnTouch?.let {
                it.draw(canvas)
            }
        }
        val anchorX = paddingLeft
        var anchorY = paddingTop + letterHeight
        letters.forEachIndexed { index, letter ->
            paint.color = if(index == selectedPosition) selectedTextColor else textColor
            paint.textSize = if(index == selectedPosition) selectedTextSize else textSize
            paint.typeface = if(index == selectedPosition){
                if(selectedTextBold) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
            }else{
                if(textBold) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
            }
            val textX = (letterWidth - paint.measureText(letter))/2 + anchorX
            paint.getTextBounds(letter,0,letter.length,textRect)
            val textY = anchorY - (letterHeight - textRect.height())/2
            canvas.drawText(letter,textX,textY,paint)
            recordLetterLocation(index,textY - letterHeight,textY)
            anchorY += letterHeight + letterPadding
        }
    }


    private fun recordLetterLocation(index:Int,startY:Float,endY:Float){
        letterLocation[index] = Pair(startY,endY)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let { event->
            when(event.action){
                MotionEvent.ACTION_DOWN ->{
                    showTouchedBackground = true
                    postInvalidate()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->{
                    showTouchedBackground = false
                    postInvalidate()
                }
            }
            val x = event.x
            val y = event.y
            if(letterRectF.contains(x,y)){
                letterLocation.filter {
                    y >= it.value.first && y <= it.value.second
                }.keys.firstOrNull()?.let {
                    if(setCurrentPosition(it)){
                        onLetterSelectedChangedListener?.invoke(currentLetter)
                    }
                }
            }
        }
        return true
    }

    //对外方法
    @MainThread
    fun setCurrentPosition(position : Int):Boolean{
        return if(position != selectedPosition && position in 0 until letters.size){
            selectedPosition = position
            invalidate()
            true
        }else{
            false
        }
    }

    @MainThread
    fun setCurrentLetter(letter : String){
        setCurrentPosition(letters.indexOf(letter))
    }

    val currentPosition :Int get() = selectedPosition

    val currentLetter :String get() = letters[selectedPosition]

    @MainThread
    fun setLetters(letters:List<String>,position: Int = 0){
        this.selectedPosition = position
        this.letters.clear()
        this.letters.addAll(letters)
        requestLayout()
        invalidate()
    }

    fun setOnLetterSelectedChangedListener(onLetterSelectedChangedListener: OnLetterSelectedChangedListener){
        this.onLetterSelectedChangedListener = onLetterSelectedChangedListener
    }


    companion object{
        private const val default_text_color = 0xFF333333
        private const val default_text_size = 10
        private const val default_selected_text_color = 0xFF333333
        private const val default_selected_text_size = 14
        private const val touchPadding = 10
    }
}

typealias OnLetterSelectedChangedListener = (letter:String) ->Unit

inline fun LetterNavigationView.setUpWithRecyclerView(recyclerView: RecyclerView,
                                                      crossinline letterFromPosition:(Int)->String,
                                                      crossinline positionFromLetter:(String)->Int
){
    recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager = (recyclerView.layoutManager as? LinearLayoutManager) ?: return
            val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
            if(firstVisiblePosition != RecyclerView.NO_POSITION){
                setCurrentLetter(letterFromPosition(firstVisiblePosition))
            }
        }
    })

    this.setOnLetterSelectedChangedListener { selectedLetter ->
        (recyclerView.layoutManager as? LinearLayoutManager)?.let { layoutManager->
            layoutManager.scrollToPositionWithOffset(positionFromLetter(selectedLetter),0)
        }
    }
}