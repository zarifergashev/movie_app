package uz.zarifergashev.movietestapp.ui.movie.movie_search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.surfstudio.android.easyadapter.pagination.PaginationState
import uz.zarifergashev.movietestapp.commons.ErrorTranslator
import uz.zarifergashev.movietestapp.commons.Event
import uz.zarifergashev.movietestapp.commons.Result
import uz.zarifergashev.movietestapp.models.Movie
import uz.zarifergashev.movietestapp.repositories.movie.MovieRepository

class MovieSearchViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    private val _paginationState = MutableLiveData<Event<PaginationState>>()
    val paginationState: LiveData<Event<PaginationState>> get() = _paginationState

    private val _movies = MutableLiveData<Event<List<Movie>>>()
    val movies: LiveData<Event<List<Movie>>> get() = _movies

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> get() = _errorMessage


    fun loadData() {
        viewModelScope.launch {
            _paginationState.value = Event(PaginationState.READY)

            when (val result =
                movieRepository.searchMovie(page =, query =)) {
                is Result.Success -> {
                    _paginationState.value = Event(PaginationState.COMPLETE)
                    (_movies.value?.getContentIfNotHandled()?.toMutableList()
                        ?: mutableListOf()).also { _movies.value = Event(it) }
                }
                is Result.Error -> {
                    _paginationState.value = Event(PaginationState.ERROR)
                    _errorMessage.value = Event(ErrorTranslator.translate(result.exception))
                }
            }
        }
    }
}