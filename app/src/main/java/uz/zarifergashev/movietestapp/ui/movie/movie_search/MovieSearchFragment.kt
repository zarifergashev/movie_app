package uz.zarifergashev.movietestapp.ui.movie.movie_search

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.easyadapter.pagination.EasyPaginationAdapter
import uz.zarifergashev.movietestapp.R
import uz.zarifergashev.movietestapp.ui.MainViewModelFactory
import uz.zarifergashev.movietestapp.ui.movie.movie_search.easy.MovieItemController
import uz.zarifergashev.movietestapp.ui.movie.movie_search.easy.PaginationFooterItemController

class MovieSearchFragment : Fragment(R.layout.movide_search_fragment) {
    private val viewModel by viewModels<MovieSearchViewModel> { MainViewModelFactory() }
    private val adapter by lazy {
        EasyPaginationAdapter(PaginationFooterItemController()) {
            hideErrorMessage()
            viewModel.loadPage()
        }
    }

    private val itemController = MovieItemController {
        Toast.makeText(requireContext(), it.title, Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initObjects()
    }


    private fun initViews(view: View) {
        view.findViewById<RecyclerView>(R.id.rv_movies).adapter = adapter

        view.findViewById<AppCompatImageView>(R.id.iv_close_error_message).setOnClickListener {
            hideErrorMessage()
        }

        // listening to search query text change
        val searchView: SearchView = view.findViewById(R.id.search_view)
        searchView.isIconified = false
        searchView.setOnClickListener {
            searchView.isIconified = !searchView.isIconified
        }

        searchView.setOnCloseListener {
            viewModel.clearPagingListData()
            return@setOnCloseListener false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.submitQuery(it) }
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                query?.let { viewModel.setQuery(it) }
                return false
            }
        })
    }


    private fun initObjects() {
        viewModel.moviesPagingListData.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { movieListData ->
                showRecyclerContainer()
                adapter.setItems(
                    ItemList.create().addAll(movieListData.list, itemController),
                    movieListData.pageState,
                )
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, {
            val errorMessage = it.getContentIfNotHandled()

            if (errorMessage?.isNotEmpty() == true) {
                showErrorMessage(errorMessage)
            } else {
                hideErrorMessage()
            }
        })

        viewModel.dataNotFound.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                showDataNotFoundContainer()
            }
        })
    }

    private fun showDataNotFoundContainer() {
        view?.findViewById<LinearLayout>(R.id.ll_data_not_found_info_container)?.visibility =
            View.VISIBLE
        view?.findViewById<AppCompatTextView>(R.id.tv_data_not_found_info)?.text =
            getString(R.string.nothing_not_found, viewModel.query.value)

        view?.findViewById<RecyclerView>(R.id.rv_movies)?.visibility = View.GONE
    }

    private fun showRecyclerContainer() {
        val rv = view?.findViewById<RecyclerView>(R.id.rv_movies)
        if (rv?.visibility == View.GONE) {
            view?.findViewById<LinearLayout>(R.id.ll_data_not_found_info_container)?.visibility =
                View.GONE
            rv.visibility = View.VISIBLE
        }
    }


    private fun showErrorMessage(errorMessage: String) {
        view?.findViewById<AppCompatTextView>(R.id.tv_error_message)?.text = errorMessage
        view?.findViewById<LinearLayout>(R.id.ll_error_container)?.visibility = View.VISIBLE
    }

    private fun hideErrorMessage() {
        view?.findViewById<AppCompatTextView>(R.id.tv_error_message)?.text = ""
        view?.findViewById<LinearLayout>(R.id.ll_error_container)?.visibility = View.GONE
    }
}