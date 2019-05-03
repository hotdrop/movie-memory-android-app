package jp.hotdrop.moviememory.presentation.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.FragmentAccountBinding
import jp.hotdrop.moviememory.di.component.component
import jp.hotdrop.moviememory.service.Firebase
import jp.hotdrop.moviememory.service.GoogleAuth
import timber.log.Timber
import javax.inject.Inject

class AccountFragment: Fragment() {

    private lateinit var binding: FragmentAccountBinding

    @Inject
    lateinit var firebase: Firebase
    lateinit var googleAuth: GoogleAuth

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.component?.fragment()?.inject(this) ?: run {
            Timber.d("onAttachが呼ばれましたがgetActivityがnullだったので終了します")
            onDestroy()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
    }

    private fun initView() {

        initGoogleSignInButton()

        updateView()
    }

    private fun initGoogleSignInButton() {
        googleAuth = GoogleAuth(getString(R.string.google_auth_token), requireActivity())

        binding.googleSignInButton.setOnClickListener {
            val intent = googleAuth.getSignInIntent()
            startActivityForResult(intent, RC_GOOGLE_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            firebaseAuthWithGoogle(data)
        }
    }

    private fun firebaseAuthWithGoogle(data: Intent?) {
        val idToken = googleAuth.takeTokenFromIntent(data)
        if (idToken == null) {
            Snackbar.make(binding.snackbarArea, R.string.account_login_failure, Snackbar.LENGTH_LONG).show()
            return
        }

        firebase.loginByGoogle(idToken, {
            updateView()
        }, {
            Snackbar.make(binding.snackbarArea, R.string.account_login_failure, Snackbar.LENGTH_LONG).show()
        })
    }

    private fun updateView() {
        if (firebase.isLoginNotAnonymous()) {
            binding.accountName.text = firebase.getUserName() ?: ""

            binding.snsContentArea.isGone = true
            binding.accountContentArea.isVisible = true
        }
    }

    companion object {
        private const val RC_GOOGLE_SIGN_IN = 1000
        fun newInstance() = AccountFragment()
    }

}
