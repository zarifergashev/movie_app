package uz.zarifergashev.movietestapp.ui.movie.movie_search

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import ru.surfstudio.android.easyadapter.ItemList
import ru.surfstudio.android.easyadapter.pagination.EasyPaginationAdapter
import ru.surfstudio.android.easyadapter.pagination.PaginationState
import uz.zarifergashev.movietestapp.R
import uz.zarifergashev.movietestapp.ui.MainViewModelFactory
import uz.zarifergashev.movietestapp.ui.movie.movie_search.easy.MovieItemController
import uz.zarifergashev.movietestapp.ui.movie.movie_search.easy.PaginationFooterItemController

class MovieSearchFragment : Fragment(R.layout.movide_search_fragment) {
    private val viewModel by viewModels<MovieSearchViewModel> { MainViewModelFactory() }
    private val adapter by lazy {
        EasyPaginationAdapter(PaginationFooterItemController()) {
            viewModel.loadData()
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
    }


    private fun initObjects() {
        viewModel.paginationState.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { state -> adapter.setState(state) }

        })

        viewModel.movies.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { movies ->
                adapter.setItems(
                    ItemList.create()
                        .addAll(movies, itemController),
                    (viewModel.paginationState.value?.getContentIfNotHandled()
                        ?: PaginationState.READY),
                )
            }
        })

    }

}