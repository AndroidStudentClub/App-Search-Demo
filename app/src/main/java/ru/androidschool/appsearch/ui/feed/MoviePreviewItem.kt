package ru.androidschool.appsearch.ui.feed

import android.view.View
import com.squareup.picasso.Picasso
import com.xwray.groupie.viewbinding.BindableItem
import ru.androidschool.appsearch.R
import ru.androidschool.appsearch.data.Movie
import ru.androidschool.appsearch.databinding.ItemMoviePreviewBinding

class MoviePreviewItem(
    private val movie: Movie,
    private val onClick: (Movie) -> Unit
) : BindableItem<ItemMoviePreviewBinding>() {
    override fun getLayout() = R.layout.item_movie_preview

    override fun initializeViewBinding(view: View): ItemMoviePreviewBinding {
        return ItemMoviePreviewBinding.bind(view)
    }

    override fun bind(viewBinding: ItemMoviePreviewBinding, position: Int) {
        viewBinding.content.setOnClickListener {
            onClick.invoke(movie)
        }

        viewBinding.movieTitle.text = movie.title
        viewBinding.movieDesc.text = movie.overview

        if (!movie.posterPath.isNullOrEmpty()) {
            Picasso.get().load(movie.posterPath).into(viewBinding.imagePreview2)
        }
    }
}
