package jp.hotdrop.moviememory.presentation.component

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.rxkotlin.subscribeBy
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.DialogCategoryBinding
import jp.hotdrop.moviememory.model.Category

object CategoryDialog {

    class Builder constructor(private val context: Context) {

        private lateinit var title: String

        private var category: Category? = null
        private var message: String? = null

        private var positiveListener: ((Category, Boolean) -> Unit)? = null
        private var categoriesForCheckDuplicate: List<Category>? = null
        private var isIntegrate: Boolean = false

        fun setMessage(@StringRes resId: Int, vararg args: String): Builder {
            message = context.getString(resId, *args)
            return this
        }

        fun setCategoriesForCheckDuplicate(categories: List<Category>): Builder {
            // カテゴリー入力時に重複チェックするため自分以外のカテゴリーを保持
            category?.run {
                categoriesForCheckDuplicate = categories.filter { it.id != this.id }
            } ?: kotlin.run {
                categoriesForCheckDuplicate = categories
            }
            return this
        }

        fun setOnPositiveListener(listener: (Category, Boolean) -> Unit): Builder {
            positiveListener = listener
            return this
        }

        fun showAdd() {
            title = context.getString(R.string.category_dialog_add_title)
            show(enableDuplicateName = false, showInputTextView = true)
        }

        fun showEdit(category: Category) {
            title = context.getString(R.string.category_dialog_edit_title)
            this.category = category
            show(enableDuplicateName = true, showInputTextView = true)
        }

        fun  showDelete() {
            title = context.getString(R.string.category_dialog_delete_title)
            show(enableDuplicateName = false, showInputTextView = false)
        }

        private fun show(enableDuplicateName: Boolean, showInputTextView: Boolean) {
            val binding = DataBindingUtil.inflate<DialogCategoryBinding>(LayoutInflater.from(context), R.layout.dialog_category, null, false)

            binding.title.text = title
            message?.let { binding.message.text = message }
            category?.let { binding.textInput.setText(it.name) }

            if (showInputTextView) {
                setOnInputTextObservable(binding, enableDuplicateName)
            } else {
                binding.textInputLayout.isGone = true
            }

            val dialog = AlertDialog.Builder(context)
                    .setView(binding.root)
                    .setCancelable(true)
                    .create()

            binding.cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            binding.okButton.setOnClickListener {
                positiveListener?.let { listener ->

                    val newCategoryName = binding.textInput.text.toString()

                    // 統合の場合は統合先をnewCategoryとして参照する
                    // isIntegrateがtrueの状態でcategoriesForCheckDuplicateもfindもnullになることはないので共に!!をつける。
                    val newCategory = if (isIntegrate) {
                        categoriesForCheckDuplicate?.find {
                            category -> category.name == newCategoryName
                        } ?: throw IllegalStateException("program bag!!")
                    } else {
                        category?.copy(name = newCategoryName) ?: Category(name = newCategoryName)
                    }
                    listener(newCategory, isIntegrate)
                }
                dialog.dismiss()
            }

            dialog.show()
        }

        private fun setOnInputTextObservable(binding: DialogCategoryBinding, enableDuplicateName: Boolean) {
            binding.textInput
                    .textChanges()
                    .skipInitialValue()
                    .map { inputCategoryName ->
                        categoriesForCheckDuplicate?.any { it.name == inputCategoryName.toString() } ?: false
                    }.subscribeBy(
                            onNext = { isDuplicateName ->
                                if (isDuplicateName && enableDuplicateName) {
                                    visibleError(R.string.category_edit_dialog_duplicate_message, binding)
                                    isIntegrate = true
                                } else if (isDuplicateName) {
                                    visibleError(R.string.category_add_dialog_duplicate_message, binding)
                                    binding.okButton.isEnabled = false
                                } else {
                                    hideError(binding)
                                    binding.okButton.isEnabled = true
                                    isIntegrate = false
                                }
                            }
                    )
        }

        private fun visibleError(@StringRes resId: Int, binding: DialogCategoryBinding) {
            binding.textInputLayout.error = context.getString(resId)
        }

        private fun hideError(binding: DialogCategoryBinding) {
            binding.textInputLayout.error = null
        }
    }
}