package divyansh.tech.kotnewreader.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.network.models.User
import divyansh.tech.kotnewreader.ui.NewsActivity
import divyansh.tech.kotnewreader.ui.viewModels.newsViewModel
import kotlinx.android.synthetic.main.common_toolbar.*

abstract class BaseFragment : Fragment() {

    lateinit var viewModel: newsViewModel
    lateinit var user: User
    val REQUEST_IMAGE_CAPTURE: Int = 1
    var imageBitmap: Bitmap? = null
    var detectedText: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return provideView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupListeners()
        user = (activity as NewsActivity).user
    }

    private fun setupListeners() {
        search?.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
        scanner?.setOnClickListener {
            openCamera()
        }
    }

    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.resolveActivity((activity as NewsActivity).packageManager)?.let {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val extras: Bundle? = data?.extras
            imageBitmap = extras?.get("data") as Bitmap
            imageBitmap?.let {
                viewModel.detectImage(it)?.observe(viewLifecycleOwner, Observer {
                    detectedText = it
                    if (detectedText != null) {
                        val args: Bundle? = Bundle().apply {
                            putString("query", detectedText)
                        }
                        findNavController().navigate(R.id.action_breakingNewsFragment_to_searchFragment, args)
                    }
                })
            }
        }
    }

    abstract fun provideView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
}