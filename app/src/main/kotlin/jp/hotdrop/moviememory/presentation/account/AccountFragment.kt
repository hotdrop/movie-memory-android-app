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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import jp.hotdrop.moviememory.R
import jp.hotdrop.moviememory.databinding.FragmentAccountBinding
import jp.hotdrop.moviememory.di.component.component
import jp.hotdrop.moviememory.model.User
import jp.hotdrop.moviememory.service.GoogleAuth
import timber.log.Timber
import javax.inject.Inject

class AccountFragment: Fragment() {

    private lateinit var binding: FragmentAccountBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: AccountViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(AccountViewModel::class.java)
    }

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
        observe()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            firebaseAuthWithGoogle(data)
        }
    }

    private fun initView() {
        initGoogleSignInButton()
    }

    private fun observe() {
        viewModel.user.observe(this, Observer {
            it?.run {
                updateView(this)
            }
        })
        viewModel.error.observe(this, Observer {
            it?.run {
                Snackbar.make(binding.snackbarArea, R.string.account_login_failure, Snackbar.LENGTH_LONG).show()
            }
        })
        lifecycle.addObserver(viewModel)
    }

    private fun initGoogleSignInButton() {
        googleAuth = GoogleAuth(getString(R.string.google_auth_token), requireActivity())

        binding.googleSignInButton.setOnClickListener {
            val intent = googleAuth.getSignInIntent()
            startActivityForResult(intent, RC_GOOGLE_SIGN_IN)
        }
    }

    private fun firebaseAuthWithGoogle(data: Intent?) {
        val idToken = googleAuth.takeTokenFromIntent(data)
        if (idToken == null) {
            Snackbar.make(binding.snackbarArea, R.string.account_login_failure, Snackbar.LENGTH_LONG).show()
            return
        }

        viewModel.loginByGoogle(idToken)
    }

    private fun updateView(user: User) {
        if (!user.isAnonymous) {
            binding.accountName.text = user.name ?: getString(R.string.account_name_no_setting)

            binding.snsContentArea.isGone = true
            binding.accountContentArea.isVisible = true
        }
    }

    companion object {
        private const val RC_GOOGLE_SIGN_IN = 1000
        fun newInstance() = AccountFragment()
    }
}
