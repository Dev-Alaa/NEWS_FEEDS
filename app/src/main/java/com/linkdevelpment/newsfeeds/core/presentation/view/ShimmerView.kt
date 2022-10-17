package com.linkdevelpment.newsfeeds.core.presentation.view

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import com.linkdevelpment.newsfeeds.R

class ShimmerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {
    private var maskOffsetX = 0
    private var maskRect: Rect? = null
    private var gradientTexturePaint: Paint? = null
    private var maskAnimator: ValueAnimator? = null
    private var localMaskBitmap: Bitmap? = null
    private var maskBitmap: Bitmap? = null
    private var canvasForShimmerMask: Canvas? = null
    private var isAnimationReversed = false
    private var isAnimationStarted = false
        private set
    private var autoStart = false
    private var shimmerAnimationDuration = 0
    private var shimmerColor = 0
    private var shimmerAngle = 0
    private var maskWidth = 0f
    private var gradientCenterColorWidth = 0f
    private var startAnimationPreDrawListener: ViewTreeObserver.OnPreDrawListener? = null
    override fun onDetachedFromWindow() {
        resetShimmering()
        super.onDetachedFromWindow()
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (!isAnimationStarted || width <= 0 || height <= 0) {
            super.dispatchDraw(canvas)
        } else {
            dispatchDrawShimmer(canvas)
        }
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        if (visibility == View.VISIBLE) {
            if (autoStart) {
                startShimmerAnimation()
            }
        } else {
            stopShimmerAnimation()
        }
    }

    fun startShimmerAnimation() {
        if (isAnimationStarted) {
            return
        }
        if (width == 0) {
            startAnimationPreDrawListener = object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    viewTreeObserver.removeOnPreDrawListener(this)
                    startShimmerAnimation()
                    return true
                }
            }
            viewTreeObserver.addOnPreDrawListener(startAnimationPreDrawListener)
            return
        }
        val animator = shimmerAnimation
        animator!!.start()
        isAnimationStarted = true
    }

    fun stopShimmerAnimation() {
        if (startAnimationPreDrawListener != null) {
            viewTreeObserver.removeOnPreDrawListener(startAnimationPreDrawListener)
        }
        resetShimmering()
    }

    fun setShimmerColor(shimmerColor: Int) {
        this.shimmerColor = shimmerColor
        resetIfStarted()
    }

    fun setShimmerAnimationDuration(durationMillis: Int) {
        shimmerAnimationDuration = durationMillis
        resetIfStarted()
    }

    fun setAnimationReversed(animationReversed: Boolean) {
        isAnimationReversed = animationReversed
        resetIfStarted()
    }


    fun setShimmerAngle(angle: Int) {
        require(!(angle < MIN_ANGLE_VALUE || MAX_ANGLE_VALUE < angle)) {
            String.format(
                "shimmerAngle value must be between %d and %d",
                MIN_ANGLE_VALUE,
                MAX_ANGLE_VALUE
            )
        }
        shimmerAngle = angle
        resetIfStarted()
    }

    fun setMaskWidth(maskWidth: Float) {
        require(!(maskWidth <= MIN_MASK_WIDTH_VALUE || MAX_MASK_WIDTH_VALUE < maskWidth)) {
            String.format(
                "maskWidth value must be higher than %d and less or equal to %d",
                MIN_MASK_WIDTH_VALUE,
                MAX_MASK_WIDTH_VALUE
            )
        }
        this.maskWidth = maskWidth
        resetIfStarted()
    }


    fun setGradientCenterColorWidth(gradientCenterColorWidth: Float) {
        require(
            !(gradientCenterColorWidth <= MIN_GRADIENT_CENTER_COLOR_WIDTH_VALUE
                    || MAX_GRADIENT_CENTER_COLOR_WIDTH_VALUE <= gradientCenterColorWidth)
        ) {
            String.format(
                "gradientCenterColorWidth value must be higher than %d and less than %d",
                MIN_GRADIENT_CENTER_COLOR_WIDTH_VALUE,
                MAX_GRADIENT_CENTER_COLOR_WIDTH_VALUE
            )
        }
        this.gradientCenterColorWidth = gradientCenterColorWidth
        resetIfStarted()
    }

    private fun resetIfStarted() {
        if (isAnimationStarted) {
            resetShimmering()
            startShimmerAnimation()
        }
    }

    private fun dispatchDrawShimmer(canvas: Canvas) {
        super.dispatchDraw(canvas)
        localMaskBitmap = getMaskBitmap()
        if (localMaskBitmap == null) {
            return
        }
        if (canvasForShimmerMask == null) {
            canvasForShimmerMask = Canvas(localMaskBitmap!!)
        }
        canvasForShimmerMask!!.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        canvasForShimmerMask!!.save()
        canvasForShimmerMask!!.translate(-maskOffsetX.toFloat(), 0f)
        super.dispatchDraw(canvasForShimmerMask)
        canvasForShimmerMask!!.restore()
        drawShimmer(canvas)
        localMaskBitmap = null
    }

    private fun drawShimmer(destinationCanvas: Canvas) {
        createShimmerPaint()
        destinationCanvas.save()
        destinationCanvas.translate(maskOffsetX.toFloat(), 0f)
        destinationCanvas.drawRect(
            maskRect!!.left.toFloat(),
            0f,
            maskRect!!.width().toFloat(),
            maskRect!!.height().toFloat(),
            gradientTexturePaint!!
        )
        destinationCanvas.restore()
    }

    private fun resetShimmering() {
        if (maskAnimator != null) {
            maskAnimator!!.end()
            maskAnimator!!.removeAllUpdateListeners()
        }
        maskAnimator = null
        gradientTexturePaint = null
        isAnimationStarted = false
        releaseBitMaps()
    }

    private fun releaseBitMaps() {
        canvasForShimmerMask = null
        if (maskBitmap != null) {
            maskBitmap!!.recycle()
            maskBitmap = null
        }
    }

    private fun getMaskBitmap(): Bitmap? {
        if (maskBitmap == null) {
            maskBitmap = createBitmap(maskRect!!.width(), height)
        }
        return maskBitmap
    }

    private fun createShimmerPaint() {
        if (gradientTexturePaint != null) {
            return
        }
        val edgeColor = reduceColorAlphaValueToZero(shimmerColor)
        val shimmerLineWidth = width / 2 * maskWidth
        val yPosition = if (0 <= shimmerAngle) height.toFloat() else 0.toFloat()

        val gradient = LinearGradient(
            0f,
            yPosition,
            Math.cos(Math.toRadians(shimmerAngle.toDouble())).toFloat() * shimmerLineWidth,
            yPosition + Math.sin(Math.toRadians(shimmerAngle.toDouble()))
                .toFloat() * shimmerLineWidth,
            intArrayOf(edgeColor, shimmerColor, shimmerColor, edgeColor),
            gradientColorDistribution,
            Shader.TileMode.CLAMP
        )

        val maskBitmapShader =
            BitmapShader(localMaskBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val composeShader =
            ComposeShader(gradient, maskBitmapShader, PorterDuff.Mode.DST_IN)
        gradientTexturePaint = Paint()
        gradientTexturePaint!!.isAntiAlias = true
        gradientTexturePaint!!.isDither = true
        gradientTexturePaint!!.isFilterBitmap = true

        gradientTexturePaint!!.shader = composeShader
    }

    private val shimmerAnimation: Animator?
        get() {
            if (maskAnimator != null) {
                return maskAnimator
            }
            if (maskRect == null) {
                maskRect = calculateBitmapMaskRect()
            }
            val animationToX = width
            val animationFromX: Int
            animationFromX = if (width > maskRect!!.width()) {
                -animationToX
            } else {
                -maskRect!!.width()
            }

            val shimmerBitmapWidth = maskRect!!.width()
            val shimmerAnimationFullLength = animationToX - animationFromX

            maskAnimator = if (isAnimationReversed) ValueAnimator.ofInt(
                shimmerAnimationFullLength,
                0
            ) else ValueAnimator.ofInt(0, shimmerAnimationFullLength)
            maskAnimator!!.duration = shimmerAnimationDuration.toLong()
            maskAnimator!!.repeatCount = ObjectAnimator.INFINITE
            maskAnimator!!.addUpdateListener { animation ->
                maskOffsetX = animationFromX + animation.animatedValue as Int
                if (maskOffsetX + shimmerBitmapWidth >= 0) {
                    invalidate()
                }
            }
            return maskAnimator
        }

    private fun createBitmap(width: Int, height: Int): Bitmap? {
        return try {
            Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8)
        } catch (e: OutOfMemoryError) {
            System.gc()
            null
        }
    }

    private fun getColor(id: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getColor(id)
        } else {
            resources.getColor(id)
        }
    }

    private fun reduceColorAlphaValueToZero(actualColor: Int): Int {
        return Color.argb(
            0,
            Color.red(actualColor),
            Color.green(actualColor),
            Color.blue(actualColor)
        )
    }

    private fun calculateBitmapMaskRect(): Rect {
        return Rect(0, 0, calculateMaskWidth(), height)
    }

    private fun calculateMaskWidth(): Int {
        val shimmerLineBottomWidth =
            width / 2 * maskWidth / Math.cos(
                Math.toRadians(
                    Math.abs(shimmerAngle).toDouble()
                )
            )
        val shimmerLineRemainingTopWidth = height * Math.tan(
            Math.toRadians(
                Math.abs(shimmerAngle).toDouble()
            )
        )
        return (shimmerLineBottomWidth + shimmerLineRemainingTopWidth).toInt()
    }

    private val gradientColorDistribution: FloatArray
        get() {
            val colorDistribution = FloatArray(4)
            colorDistribution[0] = 0F
            colorDistribution[3] = 1F
            colorDistribution[1] = 0.5f - gradientCenterColorWidth / 2f
            colorDistribution[2] = 0.5f + gradientCenterColorWidth / 2f
            return colorDistribution
        }


    @SuppressLint("ObsoleteSdkInt")
    private fun enableForcedSoftwareLayerIfNeeded() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        }
    }

    companion object {
        private const val DEFAULT_ANIMATION_DURATION = 1500
        private const val DEFAULT_ANGLE: Byte = 20
        private const val MIN_ANGLE_VALUE: Byte = -45
        private const val MAX_ANGLE_VALUE: Byte = 45
        private const val MIN_MASK_WIDTH_VALUE: Byte = 0
        private const val MAX_MASK_WIDTH_VALUE: Byte = 1
        private const val MIN_GRADIENT_CENTER_COLOR_WIDTH_VALUE: Byte = 0
        private const val MAX_GRADIENT_CENTER_COLOR_WIDTH_VALUE: Byte = 1
    }

    init {
        setWillNotDraw(false)
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ShimmerView,
            0, 0
        )
        try {
            shimmerAngle = a.getInteger(
                R.styleable.ShimmerView_shimmer_angle,
                DEFAULT_ANGLE.toInt()
            )
            shimmerAnimationDuration = a.getInteger(
                R.styleable.ShimmerView_shimmer_animation_duration,
                DEFAULT_ANIMATION_DURATION
            )
            shimmerColor =
                a.getColor(R.styleable.ShimmerView_shimmer_color, getColor(R.color.shimmer_color))
            autoStart = a.getBoolean(R.styleable.ShimmerView_shimmer_auto_start, false)
            maskWidth = a.getFloat(R.styleable.ShimmerView_shimmer_mask_width, 0.5f)
            gradientCenterColorWidth =
                a.getFloat(R.styleable.ShimmerView_shimmer_gradient_center_color_width, 0.1f)
            isAnimationReversed =
                a.getBoolean(R.styleable.ShimmerView_shimmer_reverse_animation, false)
        } finally {
            a.recycle()
        }
        setMaskWidth(maskWidth)
        setGradientCenterColorWidth(gradientCenterColorWidth)
        setShimmerAngle(shimmerAngle)
        enableForcedSoftwareLayerIfNeeded()
        if (autoStart && visibility == View.VISIBLE) {
            startShimmerAnimation()
        }
    }
}