package divyansh.tech.kotnewreader.ui.viewModels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.okhttp.Dispatcher
import divyansh.tech.kotnewreader.network.models.Article
import divyansh.tech.kotnewreader.network.models.NewsResponse
import divyansh.tech.kotnewreader.repositories.NewsRepository
import divyansh.tech.kotnewreader.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class newsViewModel @ViewModelInject constructor(
    val newRepository: NewsRepository
): ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingPageNumber = 1
    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchPageNumber = 1
    var searchNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("in")
    }

    fun handleNewsReponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                breakingPageNumber++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = it
                } else {
                    breakingNewsResponse?.articles?.addAll(it.articles)
                }
                return Resource.Success(breakingNewsResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    fun handleSearchReponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                searchPageNumber++
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

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = newRepository.getBreakingNews(countryCode, breakingPageNumber)
        Log.i("vIEWMoDEL", response.raw().request.url.toString() + response.body().toString())
        breakingNews.postValue(handleNewsReponse(response))
    }

    fun getSearchNews(searchQuery: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = newRepository.searchNews(searchQuery, searchPageNumber)
        searchNews.postValue(handleSearchReponse(response))
    }

    fun upsertArticle(article: Article) = viewModelScope.launch {
        newRepository.upsertArticle(article)
    }

    fun getAllArticles() = newRepository.getAllArticles()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newRepository.deleteArticle(article)
    }
}