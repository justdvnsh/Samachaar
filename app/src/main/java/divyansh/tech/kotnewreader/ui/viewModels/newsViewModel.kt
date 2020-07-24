package divyansh.tech.kotnewreader.ui.viewModels

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import divyansh.tech.kotnewreader.network.models.Article
import divyansh.tech.kotnewreader.network.models.Corona
import divyansh.tech.kotnewreader.network.models.MLModels.*
import divyansh.tech.kotnewreader.network.models.NewsResponse
import divyansh.tech.kotnewreader.repositories.NewsRepository
import divyansh.tech.kotnewreader.ui.fragments.BaseFragment
import divyansh.tech.kotnewreader.utils.Resource
import divyansh.tech.kotnewreader.work.SyncWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketTimeoutException

class newsViewModel @ViewModelInject constructor(
    val newRepository: NewsRepository
): ViewModel() {

    var breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsResponse: NewsResponse? = null
    var breakingNewsPage = 1
    var pageChanged: Boolean = false

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsResponse: NewsResponse? = null

    var coronaDetails: MutableLiveData<Resource<Corona>> = MutableLiveData()

    var articleText: MutableLiveData<Resource<ArticleView>> = MutableLiveData()

    var translatedText: MutableLiveData<Resource<String>> = MutableLiveData()

    var sentimentText: MutableLiveData<Resource<sentimentModel>> = MutableLiveData()

    var communicationText: MutableLiveData<Resource<List<communicationAnalysis>>> = MutableLiveData()
    var emotionText: MutableLiveData<Resource<List<communicationAnalysis>>> = MutableLiveData()

    var keyPhrases: MutableLiveData<Resource<List<KeyPhrases>>> = MutableLiveData()

    var entities: MutableLiveData<Resource<List<EntitiesModels>>> = MutableLiveData()

    var relatedNews: MutableLiveData<Resource<List<RelatedNews>>> = MutableLiveData()

    fun handleNewsReponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                if (BaseFragment.shouldPaginate) breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = it
                } else {
                    if (pageChanged) {
                        breakingNewsResponse?.articles?.clear()
                        breakingNewsResponse?.articles?.addAll(it.articles)
                    } else {
                        breakingNewsResponse?.articles?.addAll(it.articles)
                    }
                }
                return Resource.Success(breakingNewsResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    fun handleSearchReponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                if (searchNewsResponse == null) {
                    searchNewsResponse = it
                } else {
                    searchNewsResponse?.articles?.addAll(it.articles)
                }
                return Resource.Success(searchNewsResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getBreakingNews(countryCode: String, category: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = newRepository.getBreakingNews(countryCode, category.toLowerCase(), breakingNewsPage)
        Log.i("vIEWMoDEL", response.raw().request.url.toString() + "\n" + response.body().toString())
        breakingNews.postValue(handleNewsReponse(response))
    }

    fun getSearchNews(searchQuery: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = newRepository.searchNews(searchQuery)
        Log.i("SEARCH", response.raw().request.url.toString())
        searchNews.postValue(handleSearchReponse(response))
    }

    private fun handleCoronaResponse(response: Response<Corona>): Resource<Corona> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getDailyReports() = viewModelScope.launch {
        coronaDetails.postValue(Resource.Loading())
        val response = newRepository.getDailyReport()
        Log.i("MainActivity", response.raw().request.url.toString())
        coronaDetails.postValue(handleCoronaResponse(response))
    }

    fun upsertArticle(article: Article) = viewModelScope.launch {
        newRepository.upsertArticle(article)
    }

    fun getAllArticles() = newRepository.getAllArticles()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newRepository.deleteArticle(article)
    }

    fun detectImage(image: Bitmap): LiveData<String>? {
        return newRepository.detectImage(image)
    }

    fun syncNews(context: Context): LiveData<Operation.State> {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()

        val repeatingRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints).build()

        return WorkManager.getInstance(context).enqueue(
            repeatingRequest
        ).state
    }

    fun changeToArticleView(url: String) = viewModelScope.launch {
        articleText.postValue(Resource.Loading())
        try {
            val response = newRepository.changeToArticleView(url)
            Log.i("Main", response.raw().request.url.toString())
            if (response.isSuccessful) response.body()?.let {
                articleText.postValue(Resource.Success(it))
                Log.i("Main", it.article_text)
            }
            else articleText.postValue(Resource.Error(response.message()))
        } catch (e: SocketTimeoutException) {
            articleText.postValue(Resource.Error("Timed Out"))
        }
    }

    fun translate(text: String) = CoroutineScope(Dispatchers.IO + Job()).launch {
        translatedText.postValue(Resource.Loading())
        val response = newRepository.translate(text)
        Log.i("Main", response.raw().request.url.toString())
        if (response.isSuccessful) response.body()?.let {
            translatedText.postValue(Resource.Success(it.translatedText))
            Log.i("Main", it.toString())
        }
        else translatedText.postValue(Resource.Error(response.message()))
    }

    fun getSentiments(text: String) =  CoroutineScope(Dispatchers.IO + Job()).launch {
        sentimentText.postValue(Resource.Loading())
        val response = newRepository.getSetimentAnalysis(text)
        Log.i("Main", response.raw().request.url.toString())
        if (response.isSuccessful) response.body()?.let {
            sentimentText.postValue(Resource.Success(it))
            Log.i("Main", it.toString())
        }
        else sentimentText.postValue(Resource.Error(response.message()))
    }

    fun getCommunicationAnalysis(text: String) =  CoroutineScope(Dispatchers.IO + Job()).launch {
        communicationText.postValue(Resource.Loading())
        val response = newRepository.getCommunicationAnalysis(text)
        Log.i("Main", response.raw().request.url.toString())
        if (response.isSuccessful) response.body()?.let {
            communicationText.postValue(Resource.Success(it))
            Log.i("Main", it.toString())
        }
        else communicationText.postValue(Resource.Error(response.message()))
    }

    fun getEmotionalAnalysis(text: String) =  CoroutineScope(Dispatchers.IO + Job()).launch {
        emotionText.postValue(Resource.Loading())
        val response = newRepository.getEmotionalAnalysis(text)
        Log.i("Main", response.raw().request.url.toString())
        if (response.isSuccessful) response.body()?.let {
            emotionText.postValue(Resource.Success(it))
            Log.i("Main", it.toString())
        }
        else emotionText.postValue(Resource.Error(response.message()))
    }

    fun getKeyPhrases(text: String) =  CoroutineScope(Dispatchers.IO + Job()).launch {
        keyPhrases.postValue(Resource.Loading())
        val response = newRepository.getKeyPhrases(text)
        Log.i("Main", response.raw().request.url.toString())
        if (response.isSuccessful) response.body()?.let {
            keyPhrases.postValue(Resource.Success(it.documents))
            Log.i("Main", it.toString())
        }
        else keyPhrases.postValue(Resource.Error(response.message()))
    }

    fun getEntities(text: String) =  CoroutineScope(Dispatchers.IO + Job()).launch {
        entities.postValue(Resource.Loading())
        val response = newRepository.getEntities(text)
        Log.i("Main", response.raw().request.url.toString())
        if (response.isSuccessful) response.body()?.let {
            entities.postValue(Resource.Success(it.documents))
            Log.i("Main", it.toString())
        }
        else entities.postValue(Resource.Error(response.message()))
    }

    fun getRelatedNews(text: String) = CoroutineScope(Dispatchers.IO + Job()).launch {
        try {
            relatedNews.postValue(Resource.Loading())
            val response = newRepository.getRelatedNews(text)
            Log.i("Main", response.raw().request.url.toString())
            if (response.isSuccessful) response.body()?.let {
                relatedNews.postValue(Resource.Success(it.entries))
                Log.i("Main", it.toString())
            }
            else relatedNews.postValue(Resource.Error(response.message()))
        } catch (e: SocketTimeoutException) {
            relatedNews.postValue(Resource.Error("Timeout occured"))
        }
    }
}