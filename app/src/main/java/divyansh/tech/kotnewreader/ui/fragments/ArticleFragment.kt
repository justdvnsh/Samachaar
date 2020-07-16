package divyansh.tech.kotnewreader.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import divyansh.tech.kotnewreader.R
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.common_toolbar.view.*
import kotlinx.android.synthetic.main.common_toolbar.view.changeToArticle
import kotlinx.android.synthetic.main.fragment_article.*

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