package jp.hotdrop.moviememory.presentation.common.dialog

import android.annotation.SuppressLint
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
                binding.textActor.setText(it.actor)
                binding.textCharName.setText(it.charName ?: "")
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
                    val actor: String = binding.textActor.text.toString()
                    val charName: String = if (binding.textCharName.text.toString().isNotEmpty()) binding.textCharName.text.toString() else ""
                    listener(Cast(actor, charName))
                }
                dialog.dismiss()
            }

            dialog.show()
        }

        @SuppressLint("CheckResult")
        private fun setOnInputTextObservable(binding: DialogCastInputBinding) {
            binding.textActor
                    .textChanges()
                    .skipInitialValue()
                    .subscribeBy(
                            onNext = {
                                if (it.isNotEmpty()) {
                                    binding.textActorLayout.error = null
                                    binding.okButton.isEnabled = true
                                } else {
                                    binding.textActorLayout.error = context.getString(R.string.dialog_actor_not_input_error)
                                    binding.okButton.isEnabled = false
                                }
                            }
                    )
        }
    }
}
