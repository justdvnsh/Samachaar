package divyansh.tech.kotnewreader.ui.fragments

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
import divyansh.tech.kotnewreader.ui.viewModels.authViewModel
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment: Fragment() {
    lateinit var viewModel: authViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        viewModel = (activity as AuthActivity).viewModel
        val builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
        builder.setView(R.layout.alert_view_auth).setCancelable(false)
        val alert = builder.create()
        login_text_signup?.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        signup_btn?.setOnClickListener {
            if (email_register.text.isNotEmpty() && password_register.text.isNotEmpty()) {
                alert.show()
                viewModel.register(email_register.text.toString(), password_register.text.toString())
                viewModel.authenticatedUserLiveData?.observe(viewLifecycleOwner, Observer {
                    viewModel.createUser(it)
                    viewModel.createdUserLiveData?.observe(viewLifecycleOwner, Observer {
                        alert.dismiss()
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    })
                })
            } else {
                Toast.makeText(context, "Email and Password cannot be empty fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}