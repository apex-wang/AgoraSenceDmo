package io.agora.chatroom.ui.dialog

import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import io.agora.baseui.dialog.BaseSheetDialog
import io.agora.buddy.tool.ToastTools
import io.agora.buddy.tool.logE
import io.agora.chatroom.R
import io.agora.chatroom.bean.RoomKitBean
import io.agora.chatroom.databinding.DialogChatroomNoticeBinding
import java.util.regex.Pattern

/**
 * @author create by zhangwei03
 *
 * 公告
 */
class RoomNoticeSheetDialog constructor(
    private val fragmentActivity: FragmentActivity,
    private val roomKitBean: RoomKitBean,
    private val confirmCallback: (String) -> Unit
) :
    BaseSheetDialog<DialogChatroomNoticeBinding>() {

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): DialogChatroomNoticeBinding {
        return DialogChatroomNoticeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            setOnApplyWindowInsets(root)
            mbEdit.isInvisible = !roomKitBean.isOwner
            mtContent.text = contentText
            etInput.setText(contentText)
            val filters = arrayOf<InputFilter>(NameLengthFilter())
            mbEdit.filters = filters
            mbEdit.setOnClickListener {
                mbEdit.isInvisible = true
                mtCancel.isVisible = true
                mbConfirm.isVisible = true
                textInputLayout.isVisible = true
                mtContent.isVisible = false
                showKeyboard(etInput)
            }
            mtCancel.setOnClickListener {
                mbEdit.isInvisible = false
                mtCancel.isVisible = false
                mbConfirm.isVisible = false
                textInputLayout.isVisible = false
                mtContent.isVisible = true
                hideKeyboard(etInput)
            }
            mbConfirm.setOnClickListener {
                confirmCallback.invoke(etInput.text.toString().trim())
                dismiss()
                hideKeyboard(etInput)
            }
        }
    }

    private fun showKeyboard(editText: EditText) {
        editText.isFocusable = true;
        editText.isFocusableInTouchMode = true;
        editText.requestFocus()
        editText.setSelection(editText.text.length)

        val imm = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, 0)
    }

    private fun hideKeyboard(editText: EditText) {
        val imm = fragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        if (imm != null && fragmentActivity.window.attributes.softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
//            imm.hideSoftInputFromWindow(fragmentActivity.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            imm.hideSoftInputFromWindow(editText.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    private var contentText: String = ""

    fun contentText(contentText: String) = apply {
        this.contentText = contentText
    }

    inner class NameLengthFilter constructor(private val maxEn: Int = 280) : InputFilter {
        private val regEx = "[\\u4e00-\\u9fa5]"
        override fun filter(
            source: CharSequence, start: Int, end: Int,
            dest: Spanned, dstart: Int, dend: Int
        ): CharSequence {
            val destCount = (dest.toString().length + getChineseCount(dest.toString()))
            val sourceCount = (source.toString().length + getChineseCount(source.toString()))
            "NameLengthFilter $destCount $sourceCount".logE("NameLengthFilter")
            return if (destCount + sourceCount > maxEn) {
                ToastTools.show(fragmentActivity, fragmentActivity.getString(R.string.chatroom_notice_edit_limit))
                ""
            } else {
                source
            }
        }

        private fun getChineseCount(str: String): Int {
            var count = 0
            val p = Pattern.compile(regEx)
            val m = p.matcher(str)
            while (m.find()) {
                for (i in 0..m.groupCount()) {
                    count += 1
                }
            }
            return count
        }
    }
}