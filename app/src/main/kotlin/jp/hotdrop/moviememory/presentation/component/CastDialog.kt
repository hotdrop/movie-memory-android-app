package jp.hotdrop.moviememory.presentation.component

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.rxkotlin.subscribeBy
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.DialogCastInputBinding
import jp.hotdrop.moviememory.model.Cast

object CastDialog {

    class Builder constructor(private val context: Context) {

        private lateinit var title: String

        private var positiveListener: ((Cast) -> Unit)? = null
        private var previousCast: Cast? = null

        fun setOnPositiveListener(listener: (Cast) -> Unit): Builder {
            positiveListener = listener
            return this
        }

        fun showAdd() {
            title = context.getString(R.string.dialog_title_cast_add)
            show()
        }

        fun showEdit(cast: Cast) {
            title = context.getString(R.string.dialog_title_cast_update)
            previousCast = cast
            show()
        }

        private fun show() {
            val binding = DataBindingUtil.inflate<DialogCastInputBinding>(LayoutInflater.from(context), R.layout.dialog_cast_input, null, false)

            binding.title.text = title
            previousCast?.let {
                if (it.actor != null) {
                    binding.textActor.setText(it.actor)
                }
                binding.textCast.setText(it.name)
            }

            setOnInputTextObservable(binding)

            val dialog = AlertDialog.Builder(context)
                    .setView(binding.root)
                    .setCancelable(true)
                    .show()

            binding.cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            binding.okButton.setOnClickListener {
                positiveListener?.let { listener ->
                    val cast: String = binding.textCast.text.toString()
                    val actor: String? = if (!binding.textActor.text.toString().isEmpty()) binding.textActor.text.toString() else null
                    listener(Cast(cast, actor))
                }
                dialog.dismiss()
            }

            dialog.show()
        }

        private fun setOnInputTextObservable(binding: DialogCastInputBinding) {
            binding.textCast
                    .textChanges()
                    .skipInitialValue()
                    .subscribeBy(
                            onNext = {
                                if (it.isNotEmpty()) {
                                    binding.textCastLayout.error = null
                                    binding.okButton.isEnabled = true
                                } else {
                                    binding.textCastLayout.error = context.getString(R.string.dialog_cast_not_input_error)
                                    binding.okButton.isEnabled = false
                                }
                            }
                    )
        }
    }
}