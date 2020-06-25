package divyansh.tech.kotnewreader.ui.viewModels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import divyansh.tech.kotnewreader.network.models.NewsResponse
import divyansh.tech.kotnewreader.repositories.NewsRepository
import divyansh.tech.kotnewreader.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class newsViewModel @ViewModelInject constructor(
    val newRepository: NewsRepository
): ViewModel() {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val breakingPageNumber = 1

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchPageNumber = 1

    init {
        getBreakingNews("in")
    }

    fun handleNewsReponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                Log.i("ViewMOdel", it.articles.size.toString())
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun handleSearchReponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                Log.i("ViewMOdel", it.articles.size.toString())
                return Resource.Success(it)
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
}