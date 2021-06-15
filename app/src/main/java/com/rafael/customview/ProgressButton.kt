package com.rafael.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.rafael.customview.databinding.ProgressButtonBinding

class ProgressButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var title: String? = null
    private var loadingTitle: String? = null

    private val bindind = ProgressButtonBinding
        .inflate(LayoutInflater.from(context), this, true)

    private var state: ProgressButtonState = ProgressButtonState.Normal
        set(value) {
            field = value
            refreshState()
        }

    private fun refreshState() {
        isEnabled = state.isEnabled
        isClickable = state.isEnabled
        refreshDrawableState()

        bindind.textTitle.run {
            isEnabled = state.isEnabled
            isClickable = state.isEnabled
        }

        bindind.progressButton.visibility = state.progressVisibility

        when (state){
            ProgressButtonState.Normal -> bindind.textTitle.text = title
            ProgressButtonState.Loading -> bindind.textTitle.text = loadingTitle

        }
    }

    fun setLoading(){
        state = ProgressButtonState.Loading
    }

    fun setNormal(){
        state = ProgressButtonState.Normal
    }

    init {
        setLayout(attrs)
        refreshState()
    }

    private fun setLayout(attrs: AttributeSet?){
        //verifica se é null
        attrs?.let { attributeSet ->
            val attributes = context.obtainStyledAttributes(
                attributeSet,
                R.styleable.ProgressButton
            )

            setBackgroundResource(R.drawable.progress_button_background)

            val titleResId = attributes.getResourceId(R.styleable.ProgressButton_progress_button_title,0)
            if(titleResId != 0){
                title = context.getString(titleResId)
            }

            val loadingTitleResId = attributes.getResourceId(R.styleable.ProgressButton_progress_button_loading_title,0)
            if(loadingTitleResId != 0){
                loadingTitle = context.getString(loadingTitleResId)
            }

            attributes.recycle()
        }
    }

    sealed class ProgressButtonState(val isEnabled: Boolean, val progressVisibility: Int){
        object Normal : ProgressButtonState(true, View.GONE)
        object Loading : ProgressButtonState(false, View.VISIBLE)
    }
}