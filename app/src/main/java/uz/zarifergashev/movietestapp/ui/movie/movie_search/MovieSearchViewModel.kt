package uz.zarifergashev.movietestapp.ui.movie.movie_search

import android.os.Handler
import android.os.Looper
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
import uz.zarifergashev.movietestapp.models.MoviePagingListData
import uz.zarifergashev.movietestapp.models.PagingResult
import uz.zarifergashev.movietestapp.repositories.movie.MovieRepository

class MovieSearchViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _moviesPagingListData = MutableLiveData<Event<MoviePagingListData>>()
    val moviesPagingListData: LiveData<Event<MoviePagingListData>> get() = _moviesPagingListData

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> get() = _errorMessage

    private val _dataNotFound = MutableLiveData<Event<Boolean>>()
    val dataNotFound: LiveData<Event<Boolean>> get() = _dataNotFound

    private val _query = MutableLiveData<String>()
    val query: LiveData<String> get() = _query

    private val handler by lazy { Handler(Looper.getMainLooper()) }
    private var runnable: Runnable? = null

    private var page: Int = 0
    private var pageReset = false


    fun setQuery(query: String) {
        _query.value = query

        runnable?.let { handler.removeCallbacks(it) }
        runnable = Runnable {
            resetPage()
        }
        handler.postDelayed(runnable!!, 500)
    }

    fun submitQuery(query: String) {
        _query.value = query

        runnable?.let { handler.removeCallbacks(it) }
        resetPage()
    }


    private fun resetPage() {
        page = 0
        pageReset = true
        loadPage()
    }

    fun loadPage() {
        viewModelScope.launch {
            val query = _query.value ?: ""

            if (query.isEmpty()) {
                clearPagingListData()
                return@launch
            }

            val newPage = page + 1
            val currentList = loadCurrentList()

            if (currentList.isEmpty()) {
                _moviesPagingListData.value =
                    Event(MoviePagingListData(emptyList(), PaginationState.READY))
            }

            when (val result =
                movieRepository.searchMovie(page = newPage, query = query)) {
                is Result.Success -> {
                    //when result return successfully page number change
                    page = newPage

                    if (result.data.totalPages == 0 && result.data.results.isEmpty()) {
                        _dataNotFound.value = Event(true)
                    } else {
                        setPagingListData(result.data, currentList)
                    }
                }
                is Result.Error -> {
                    _moviesPagingListData.value =
                        Event(MoviePagingListData(currentList, PaginationState.ERROR))
                    _errorMessage.value = Event(ErrorTranslator.translate(result.exception))
                }
            }
        }
    }

    private fun loadCurrentList() =
        if (pageReset) {
            pageReset = false
            mutableListOf()
        } else {
            (_moviesPagingListData.value?.peekContent()?.list?.toMutableList()
                ?: mutableListOf())
        }


    private fun setPagingListData(data: PagingResult<Movie>, currentList: MutableList<Movie>) {

        val pageState = if (data.totalPages!! <= page) {
            PaginationState.COMPLETE
        } else {
            PaginationState.READY
        }

        currentList.addAll(data.results)
        _moviesPagingListData.value =
            Event(MoviePagingListData(currentList, pageState))
    }

    fun clearPagingListData() {
        _moviesPagingListData.value =
            Event(MoviePagingListData(emptyList(), PaginationState.COMPLETE))
    }
}