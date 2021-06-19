package uz.zarifergashev.movietestapp.ui.movie.movie_search.easy

import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ru.surfstudio.android.easyadapter.pagination.EasyPaginationAdapter
import ru.surfstudio.android.easyadapter.pagination.PaginationState
import uz.zarifergashev.movietestapp.R

class PaginationFooterItemController :
    EasyPaginationAdapter.BasePaginationFooterController<PaginationFooterItemController.Holder>() {

    override fun createViewHolder(
        parent: ViewGroup,
        listener: EasyPaginationAdapter.OnShowMoreListener
    ) = Holder(parent, listener)

    inner class Holder(
        parent: ViewGroup,
        listener: EasyPaginationAdapter.OnShowMoreListener
    ) : EasyPaginationAdapter.BasePaginationFooterHolder(
        parent,
        R.layout.layout_pagination_footer
    ) {

        private val loadingIndicator: ProgressBar =
            itemView.findViewById(R.id.pagination_footer_progress_bar)
        private val reloadBtn: AppCompatImageButton =
            itemView.findViewById(R.id.pagination_footer_reload_btn)

        init {
            reloadBtn.setOnClickListener { listener.onShowMore() }
            loadingIndicator.visibility = View.GONE
            reloadBtn.visibility = View.GONE
        }

        override fun bind(state: PaginationState) {
            // for StaggeredGrid pagination
            if (itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                itemView.updateLayoutParams<StaggeredGridLayoutManager.LayoutParams> {
                    isFullSpan = true
                }
            }

            when (state) {
                PaginationState.READY -> {
                    loadingIndicator.visibility = View.VISIBLE
                    reloadBtn.visibility = View.GONE
                }
                PaginationState.COMPLETE -> {
                    loadingIndicator.visibility = View.GONE
                    reloadBtn.visibility = View.GONE
                }
                PaginationState.ERROR -> {
                    loadingIndicator.visibility = View.GONE
                    reloadBtn.visibility = View.VISIBLE
                }
                else -> throw IllegalArgumentException("unsupported state: $state")
            }
        }
    }
}