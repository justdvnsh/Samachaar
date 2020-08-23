package divyansh.tech.kotnewreader.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.ui.activities.AuthActivity
import divyansh.tech.kotnewreader.ui.activities.NewsActivity
import divyansh.tech.kotnewreader.ui.viewModels.authViewModel
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment: Fragment() {
    lateinit var viewModel: authViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        viewModel = (activity as AuthActivity).viewModel
        val builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
        builder.setView(R.layout.alert_view_auth).setCancelable(false)
        val alert = builder.create()
        signup_text_login?.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        login_btn?.setOnClickListener {
            if (email_login.text.isNotEmpty() && password_login.text.isNotEmpty()) {
                alert.show()
                viewModel.login(email_login.text.toString(), password_login.text.toString())
                viewModel.authenticatedUserLiveData?.observe(viewLifecycleOwner, Observer {
                    alert.dismiss()
                    val intent = Intent(activity, NewsActivity::class.java)
                    intent.putExtra(getString(R.string.userArgument), it)
                    startActivity(intent)
                    activity!!.finish()
                })
            } else {
                Toast.makeText(context, "Email and Password cannot be empty fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}