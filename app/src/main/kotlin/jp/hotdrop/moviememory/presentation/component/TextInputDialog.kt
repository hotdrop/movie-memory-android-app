package jp.hotdrop.moviememory.presentation.component

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.DialogTextInputBinding
import jp.hotdrop.moviememory.databinding.DialogTextMultilineInputBinding

object TextInputDialog {

    class Builder constructor(private val context: Context) {

        private var title: String? = null
        private var text: String? = null
        private var hint: String? = null
        private var positiveListener: ((String) -> Unit)? = null

        fun setTitle(@StringRes resId: Int): Builder {
            title = context.getString(resId)
            return this
        }

        fun setText(text: String): Builder {
            this.text = text
            return this
        }

        fun setTextHint(@StringRes resId: Int, vararg args: Any): Builder {
            hint = context.getString(resId, *args)
            return this
        }

        fun setOnPositiveListener(listener: (String) -> Unit): Builder {
            positiveListener = listener
            return this
        }

        fun show() {
            val binding = DataBindingUtil.inflate<DialogTextInputBinding>(LayoutInflater.from(context), R.layout.dialog_text_input, null, false)

            title?.let { binding.title.text = it }
            text?.let { binding.textInput.setText(it) }
            hint?.let { binding.textInput.hint = it }

            AlertDialog.Builder(context)
                    .setView(binding.root)
                    .setPositiveButton(android.R.string.ok) { dialogInterface: DialogInterface, _: Int ->
                        positiveListener?.let { listener ->
                            listener(binding.textInput.text.toString())
                        }
                        dialogInterface.dismiss()
                    }.setNegativeButton(android.R.string.cancel) { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                    }.setCancelable(true)
                    .show()
        }

        fun showWithInputMultiLine() {
            val binding = DataBindingUtil.inflate<DialogTextMultilineInputBinding>(LayoutInflater.from(context), R.layout.dialog_text_multiline_input, null, false)

            title?.let { binding.title.text = it }
            text?.let { binding.textInput.setText(it) }
            hint?.let { binding.textInput.hint = it }

            AlertDialog.Builder(context)
                    .setView(binding.root)
                    .setPositiveButton(android.R.string.ok) { dialogInterface: DialogInterface, _: Int ->
                        positiveListener?.let { listener ->
                            listener(binding.textInput.text.toString())
                        }
                        dialogInterface.dismiss()
                    }.setNegativeButton(android.R.string.cancel) { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                    }.setCancelable(true)
                    .show()
        }
    }
}