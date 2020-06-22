package divyansh.tech.kotnewreader.ui.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import divyansh.tech.kotnewreader.repositories.NewsRepository

class newsViewModel @ViewModelInject constructor(
    val newRepository: NewsRepository
): ViewModel() {
}