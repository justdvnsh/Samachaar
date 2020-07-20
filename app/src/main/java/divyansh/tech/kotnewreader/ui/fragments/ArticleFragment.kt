package divyansh.tech.kotnewreader.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
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
    lateinit var bundle: Bundle

    override fun provideView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.titleText.text = getString(R.string.ArticleTitle)
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
                    scrollArticle.visibility = VISIBLE
                    titleArticle.text = args.article.title
                    dateArticle.text = args.article.publishedAt
                    article.text = it.data?.article_text
                    Glide.with(context!!).load(args.article.urlToImage).into(imageArticle)
                    analyze.visibility = VISIBLE
                    bundle = Bundle().apply {
                        putString(getString(R.string.queryArgument), it.data?.article_text)
                    }
                    translate?.visibility = VISIBLE
                    translate?.setOnClickListener {v ->
                        translateArticle(it.data?.article_text)
                    }
                    alert.dismiss()
                }

                is Resource.Error -> {
                    alert.dismiss()
                    it.message?.let {
                        Toast.makeText(activity, "${getString(R.string.failed)} ${it}", Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    alert.show()
                }
            }
        })
    }

    private fun translateArticle(text: String?) {
        text?.let {
            viewModel.translate(it)
            viewModel.translatedText.observe(viewLifecycleOwner, Observer {it ->
                when (it) {
                    is Resource.Success -> {
                        article.text = it.data
                        alert.dismiss()
                    }

                    is Resource.Error -> {
                        alert.dismiss()
                        it.message?.let {
                            Toast.makeText(activity, "${getString(R.string.failed)} ${it}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    is Resource.Loading -> {
                        alert.show()
                    }
                }
            })
        }
    }

    private fun setupWebView() {
        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(args.article.url)
        }
    }

    private fun setupFab(view: View) {
        analyze.setOnClickListener {
            findNavController().navigate(
                R.id.action_articleFragment_to_analyzeFragment,
                bundle
            )
        }
        share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shareIntentString) +
                    args.article.url)
            intent.putExtra(Intent.EXTRA_SUBJECT, args.article.title)
            startActivity(Intent.createChooser(intent, getString(R.string.shareUsing)))

        }
        fab.setOnClickListener {
            viewModel.upsertArticle(args.article)
            Snackbar.make(view, getString(R.string.articleSaved), Snackbar.LENGTH_SHORT).show()
        }
    }
}