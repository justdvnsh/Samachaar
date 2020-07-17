package divyansh.tech.kotnewreader.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.utils.Alert
import divyansh.tech.kotnewreader.utils.Resource
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_analyze.*
import kotlinx.android.synthetic.main.fragment_article.*

class AnalyzeFragment : BaseFragment() {

    val args: AnalyzeFragmentArgs by navArgs()

    override fun provideView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_analyze, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSentimentText()
        setupEmotionText()
    }

    private fun setupSentimentText() {
        sentiment?.text = args.query
        viewModel.getSentiments(args.query)
        viewModel.sentimentText.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    sentiment.text = it.data?.pos.toString() + " " + it.data?.neg.toString() + " " + it.data?.mid.toString()
                }

                is Resource.Error -> {
                    Alert.createAlertDialog(context!!).show()
                    it.message?.let {
                        Toast.makeText(activity, "Failed ${it}", Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    Alert.createAlertDialog(context!!).show()
                }
            }
        })
    }

    private fun setupEmotionText() {
        viewModel.getEmotion(args.query)
        viewModel.emotionText.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    emotion.text = it.data?.get(0)?.predictions?.get(0)?.prediction + " " + it.data?.get(0)?.predictions?.get(0)?.probability.toString()
                }

                is Resource.Error -> {
                    Alert.createAlertDialog(context!!).show()
                    it.message?.let {
                        Toast.makeText(activity, "Failed ${it}", Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    Alert.createAlertDialog(context!!).show()
                }
            }
        })
    }
}