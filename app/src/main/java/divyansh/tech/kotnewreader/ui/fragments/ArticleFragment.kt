package divyansh.tech.kotnewreader.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.utils.Alert.Companion.createAlertDialog
import divyansh.tech.kotnewreader.utils.Resource
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.common_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_article.*
import kotlinx.android.synthetic.main.fragment_search.*

class ArticleFragment: BaseFragment() {

    val args: ArticleFragmentArgs by navArgs()

    override fun provideView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.titleText.text = "Article"
        setupListeners()
        setupWebView()
        setupFab(view)
    }

    private fun setupListeners() {
        changeToArticle?.visibility = VISIBLE
        changeToArticle?.setOnClickListener {
            changeToArticleView()
        }
    }

    private fun changeToArticleView() {
        viewModel.changeToArticleView(args.article.url!!)
        viewModel.articleText.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    webView.visibility = GONE
                    article.visibility = VISIBLE
                    article.text = it.data?.article_text
                }

                is Resource.Error -> {
                    createAlertDialog(context!!)
                    it.message?.let {
                        Toast.makeText(activity, "Failed ${it}", Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    createAlertDialog(context!!)
                }
            }
        })
    }

    private fun setupWebView() {
        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(args.article.url)
        }
    }

    private fun setupFab(view: View) {
        fab.setOnClickListener {
            viewModel.upsertArticle(args.article)
            Snackbar.make(view, "Article Saved Successfully.", Snackbar.LENGTH_SHORT).show()
        }
    }
}