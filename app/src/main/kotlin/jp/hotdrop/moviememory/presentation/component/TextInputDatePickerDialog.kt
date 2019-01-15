package jp.hotdrop.moviememory.presentation.component

import android.app.DatePickerDialog
import android.content.Context
import com.google.android.material.textfield.TextInputEditText
import jp.hotdrop.moviememory.model.Movie
import org.threeten.bp.LocalDate

object TextInputDatePickerDialog {

    fun show(context: Context, settingDateStr: String, onPositiveListener: (selectedDate: LocalDate) -> Unit) {
        val date = if (settingDateStr.isEmpty() || settingDateStr == Movie.DEFAULT_TEXT_VALUE) {
            LocalDate.now()
        } else {
            LocalDate.parse(settingDateStr)
        }

        context.run {
            DatePickerDialog(this,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                        onPositiveListener(selectedDate)
                    }, date.year, date.monthValue - 1, date.dayOfMonth)
                    .show()
        }
    }

    fun show(context: Context, textInputView: TextInputEditText) {
        val dateStr = textInputView.text.toString()
        val date = if (dateStr.isEmpty() || dateStr == Movie.DEFAULT_TEXT_VALUE) {
            LocalDate.now()
        } else {
            LocalDate.parse(dateStr)
        }

        context.run {
            DatePickerDialog(this,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                        textInputView.let {
                            it.setText(selectedDate.toString())
                            it.clearFocus()
                        }
                    }, date.year, date.monthValue - 1, date.dayOfMonth)
                    .let { dialog ->
                        dialog.setOnCancelListener {
                            textInputView.clearFocus()
                        }
                        dialog.show()
                    }
        }
    }
}