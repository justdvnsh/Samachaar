package divyansh.tech.kotnewreader.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.adapters.KeyPhrasesAdapter
import divyansh.tech.kotnewreader.utils.Alert
import divyansh.tech.kotnewreader.utils.Resource
import kotlinx.android.synthetic.main.fragment_analyze.*
import javax.inject.Inject

@AndroidEntryPoint
class AnalyzeFragment : BaseFragment() {

    @Inject
    lateinit var keyPhrasesAdapter: KeyPhrasesAdapter
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
        setupKeyPhraseRecyclerView()
        setupKeyPhrasesObservers()
    }

    @SuppressLint("SetTextI18n")
    private fun setupSentimentText() {
        viewModel.getSentiments(args.query)
        viewModel.sentimentText.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    alert.dismiss()
                    positiveSentiment.text = "${it.data?.pos_percent}"
                    neutralSentiment.text = "${it.data?.mid_percent}"
                    negativeSentiment.text = "${it.data?.neg_percent}"
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

    @SuppressLint("SetTextI18n")
    private fun setupEmotionText() {
        viewModel.getCommunicationAnalysis(args.query)
        viewModel.getEmotionalAnalysis(args.query)
        viewModel.communicationText.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    alert.dismiss()
                    communicationAnalysis.text =
                        "${it.data?.get(0)?.predictions?.get(0)?.prediction} - ${it.data?.get(0)?.predictions?.get(
                            0
                        )?.probability.toString()}"
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

        viewModel.emotionText.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    alert.dismiss()
                    emotionalAnalysis.text =
                        "${it.data?.get(0)?.predictions?.get(0)?.prediction} ${it.data?.get(0)?.predictions?.get(
                            0
                        )?.probability.toString()}"
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

    private fun setupKeyPhraseRecyclerView() {
        rvKeyPhrases.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = keyPhrasesAdapter
        }
    }

    private fun setupKeyPhrasesObservers() {
        viewModel.getKeyPhrases(args.query)
        viewModel.keyPhrases.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    alert.dismiss()
                    it.data?.let {
                        keyPhrasesAdapter.differ.submitList(it.get(0).keyPhrases.toList())
                    }
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