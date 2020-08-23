package divyansh.tech.kotnewreader.ui.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.models.User
import divyansh.tech.kotnewreader.ui.activities.AudioPlayerActivity
import divyansh.tech.kotnewreader.ui.activities.NewsActivity
import divyansh.tech.kotnewreader.ui.viewModels.newsViewModel
import divyansh.tech.kotnewreader.utils.Alert
import divyansh.tech.kotnewreader.utils.Constants
import kotlinx.android.synthetic.main.common_toolbar.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

abstract class BaseFragment : Fragment(), EasyPermissions.PermissionCallbacks{

    lateinit var viewModel: newsViewModel
    lateinit var user: User
    lateinit var alert: AlertDialog
    val REQUEST_IMAGE_CAPTURE: Int = 1
    var imageBitmap: Bitmap? = null
    var detectedText: String? = null
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    companion object {
        var shouldPaginate = false
    }
    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItem + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItem >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.pageChanged = false
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            // check if the list is currently scrolling
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    fun showProgress(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
        isLoading = true
    }

    fun hideProgress(progressBar: ProgressBar) {
        progressBar.visibility = View.GONE
        isLoading = false
    }

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
        alert = Alert.createAlertDialog(activity as NewsActivity)
    }

    private fun setupListeners() {
        search?.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
        scanner?.setOnClickListener {
            openCamera(it)
        }
        speak?.setOnClickListener {
            openMediaPlayerActivity()
        }
    }

    fun openMediaPlayerActivity() {
        startActivity(Intent(activity, AudioPlayerActivity::class.java))
    }

    private fun hasCameraPermissions() :Boolean = EasyPermissions.hasPermissions(context!!, Manifest.permission.CAMERA)

    private fun openCamera(view: View) {
        if (hasCameraPermissions()) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureIntent.resolveActivity((activity as NewsActivity).packageManager)?.let {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_camera),
                Constants.RC_CAMERA_PERM,
                Manifest.permission.CAMERA)
        }
    }

    override fun onRequestPermissionsResult(requestCode:Int,
                                            permissions:Array<String>,
                                            grantResults:IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms))
        {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
//        TODO("Not yet implemented")
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