package jp.hotdrop.moviememory.presentation.component

import android.app.DatePickerDialog
import android.content.Context
import com.google.android.material.textfield.TextInputEditText
import jp.hotdrop.moviememory.model.AppDate
import jp.hotdrop.moviememory.model.Movie

object TextInputDatePickerDialog {

    fun show(context: Context, settingDateStr: String, onPositiveListener: (selectedDate: AppDate) -> Unit) {
        val date = if (settingDateStr.isEmpty() || settingDateStr == Movie.DEFAULT_TEXT_VALUE) {
            AppDate()
        } else {
            AppDate(dateStr = settingDateStr)
        }

        context.run {
            DatePickerDialog(this,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        val selectedDate = AppDate.of(year, month + 1, dayOfMonth)
                        onPositiveListener(selectedDate)
                    }, date.year, date.month - 1, date.day)
                    .show()
        }
    }

    fun show(context: Context, textInputView: TextInputEditText) {
        val dateStr = textInputView.text.toString()
        val date = if (dateStr.isEmpty() || dateStr == Movie.DEFAULT_TEXT_VALUE) {
            AppDate()
        } else {
            AppDate(dateStr = dateStr)
        }

        context.run {
            DatePickerDialog(this,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        val selectedDate = AppDate.of(year, month + 1, dayOfMonth)
                        textInputView.let {
                            it.setText(selectedDate.toString())
                            it.clearFocus()
                        }
                    }, date.year, date.month - 1, date.day)
                    .let { dialog ->
                        dialog.setOnCancelListener {
                            textInputView.clearFocus()
                        }
                        dialog.show()
                    }
        }
    }
}