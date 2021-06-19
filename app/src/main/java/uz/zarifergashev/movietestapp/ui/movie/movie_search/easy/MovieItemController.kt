package uz.zarifergashev.movietestapp.ui.movie.movie_search.easy

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
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
        private val avatarIv: AppCompatImageView = itemView.findViewById(R.id.iv_avatar)
        private val titleTv: TextView = itemView.findViewById(R.id.tv_title)
        private val overviewTv: TextView = itemView.findViewById(R.id.tv_overview)
        private val releaseDateTv: TextView = itemView.findViewById(R.id.tv_release_date)

        init {
            itemView.findViewById<View>(R.id.container).apply {
                setOnClickListener { onClickListener(data) }
            }
        }


        override fun bind(data: Movie) {
            this.data = data
            data.title?.let {
                titleTv.text = it
                titleTv.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
            }
            data.releaseDate?.let {
                releaseDateTv.text = it
                releaseDateTv.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
            }
            data.overview?.let {
                overviewTv.text = it
                overviewTv.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
            }

            data.avatarUrl()
                .takeIf { it.isNotEmpty() }
                ?.apply { Glide.with(itemView).load(this).into(avatarIv) }

        }
    }


}