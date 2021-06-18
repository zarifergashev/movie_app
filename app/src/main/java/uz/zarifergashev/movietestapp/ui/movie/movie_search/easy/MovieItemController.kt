package uz.zarifergashev.movietestapp.ui.movie.movie_search.easy

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.surfstudio.android.easyadapter.controller.BindableItemController
import ru.surfstudio.android.easyadapter.holder.BindableViewHolder
import uz.zarifergashev.movietestapp.R
import uz.zarifergashev.movietestapp.models.Movie

class MovieItemController(private val onClickListener: (Movie) -> Unit) :
    BindableItemController<Movie, MovieItemController.Holder>() {

    override fun createViewHolder(parent: ViewGroup): Holder = Holder(parent)

    override fun getItemId(data: Movie): Int = data.id!!

    inner class Holder(
        parent: ViewGroup
    ) : BindableViewHolder<Movie>(parent, R.layout.item_movie) {
        private lateinit var data: Movie
        private val titleTv: TextView = itemView.findViewById(R.id.tv_title)

        init {
            itemView.findViewById<View>(R.id.container).apply {
                setOnClickListener { onClickListener(data) }
            }
        }


        override fun bind(data: Movie) {
            this.data = data
            titleTv.text = data.title
        }
    }


}