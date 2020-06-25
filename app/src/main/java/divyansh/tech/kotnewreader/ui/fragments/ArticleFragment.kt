package divyansh.tech.kotnewreader.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import divyansh.tech.kotnewreader.R
import kotlinx.android.synthetic.main.common_toolbar.view.*
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
        view.titleText.text = args.article.title.substring(0, 15) + "...."
        setupWebView()
    }

    private fun setupWebView() {
        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(args.article.url)
        }
    }
}