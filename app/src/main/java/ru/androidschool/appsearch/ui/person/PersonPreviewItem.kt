package ru.androidschool.appsearch.ui.person

import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import com.xwray.groupie.viewbinding.BindableItem
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import ru.androidschool.appsearch.R
import ru.androidschool.appsearch.data.Person
import ru.androidschool.appsearch.databinding.ItemPersonBinding

class PersonItem(
    private val content: Person,
    private val onClick: (p: Person) -> Unit
) : BindableItem<ItemPersonBinding>() {

    override fun getLayout() = R.layout.item_person

    override fun initializeViewBinding(view: View): ItemPersonBinding {
        return ItemPersonBinding.bind(view)
    }

    override fun bind(viewBinding: ItemPersonBinding, position: Int) {
        viewBinding.nameTv.text = content.name
        viewBinding.personAvatar.loadTransformationImage(
            content.imageUrl,
            CropCircleTransformation()
        )
    }
}

fun ImageView.loadTransformationImage(imgUrl: String?, transformation: Transformation) {
    if (!imgUrl.isNullOrEmpty()) {
        Picasso.get()
            .load(imgUrl)
            .transform(transformation)
            .into(this)
    }
}