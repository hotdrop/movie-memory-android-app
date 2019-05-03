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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.GoogleAuthProvider
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.FragmentAccountBinding
import jp.hotdrop.moviememory.di.component.component
import jp.hotdrop.moviememory.service.Firebase
import timber.log.Timber
import javax.inject.Inject

class AccountFragment: Fragment() {

    private lateinit var binding: FragmentAccountBinding

    @Inject
    lateinit var firebase: Firebase
    lateinit var googleSignInClient: GoogleSignInClient

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
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("TODO") // TODO
                .requestEmail()
                .build()
        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        binding.googleSignInButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Timber.w(e, "Googleアカウントのサインインに失敗しました。")
                Snackbar.make(binding.contentArea, R.string.account_login_failure, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebase.loginByGoogle(credential, {
            updateView()
        }, {
            Snackbar.make(binding.contentArea, R.string.account_login_failure, Snackbar.LENGTH_LONG).show()
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
