package ru.androidschool.appsearch.ui.feed

import android.view.View
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.viewbinding.BindableItem
import ru.androidschool.appsearch.R
import com.xwray.groupie.GroupieViewHolder
import ru.androidschool.appsearch.databinding.ItemCardBinding

class MainCardContainer(
    private val title: String,
    private val items: List<BindableItem<*>>
) : BindableItem<ItemCardBinding>() {

    override fun getLayout() = R.layout.item_card

    override fun initializeViewBinding(view: View): ItemCardBinding {
        return ItemCardBinding.bind(view)
    }

    override fun bind(viewBinding: ItemCardBinding, position: Int) {
        viewBinding.titleTextView.text = title
        viewBinding.itemsContainer.adapter =
            GroupAdapter<GroupieViewHolder>().apply { addAll(items) }
    }
}
