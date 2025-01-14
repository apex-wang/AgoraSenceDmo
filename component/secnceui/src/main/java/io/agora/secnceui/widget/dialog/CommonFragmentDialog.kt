package io.agora.secnceui.widget.dialog

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import io.agora.baseui.dialog.BaseFragmentDialog
import io.agora.buddy.tool.dp
import io.agora.secnceui.databinding.DialogFragmentCommonBinding

class CommonFragmentDialog : BaseFragmentDialog<DialogFragmentCommonBinding>() {

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): DialogFragmentCommonBinding {
        return DialogFragmentCommonBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCanceledOnTouchOutside(false)
        mBinding?.apply {
            addMargin(view)
            if (!TextUtils.isEmpty(titleText)){
                mtTitle.text = titleText
            }
            if (!TextUtils.isEmpty(contentText)){
                mtContent.text = contentText
            }
            if (!TextUtils.isEmpty(leftText)){
                mbLeft.text = leftText
            }
            if (!TextUtils.isEmpty(rightText)){
                mbRight.text = rightText
            }
            mbLeft.setOnClickListener {
                clickListener?.onCancelClick()
                dismiss()
            }
            mbLeft.setOnClickListener {
                clickListener?.onConfirmClick()
                dismiss()
            }
        }
    }

    private fun addMargin(view: View) {
        val layoutParams: FrameLayout.LayoutParams = view.layoutParams as FrameLayout.LayoutParams
        val marginHorizontal = 38.dp.toInt()
        val marginVertical = 24.dp.toInt()
        layoutParams.setMargins(marginHorizontal, 0, marginHorizontal, marginVertical)
        view.layoutParams = layoutParams
    }

    private var titleText: String = ""
    private var contentText: String = ""
    private var leftText: String = ""
    private var rightText: String = ""
    private var clickListener: OnClickBottomListener? = null

    fun titleText(titleText: String) = apply {
        this.titleText = titleText
    }

    fun contentText(contentText: String) = apply {
        this.contentText = contentText
    }

    fun leftText(leftText: String) = apply {
        this.leftText = leftText
    }

    fun rightText(rightText: String) = apply {
        this.rightText = rightText
    }

    fun setOnClickListener(clickListener: OnClickBottomListener) = apply {
        this.clickListener = clickListener
    }

    interface OnClickBottomListener {
        /**
         * 点击确定按钮事件
         */
        fun onConfirmClick()

        /**
         * 点击取消按钮事件
         */
        fun onCancelClick()
    }
}