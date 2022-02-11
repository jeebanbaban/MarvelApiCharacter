package marvel.api.marvelcharacters.data.model

import android.graphics.drawable.Drawable
import android.os.Parcelable
import android.widget.ImageView
import androidx.room.*
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import com.bumptech.glide.request.RequestOptions

import com.bumptech.glide.Glide

import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target


/**
 * Created by Jeeban Bagdi on 1/30/2022.
 */
@Entity
@Parcelize
data class Results(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("name") var name: String = "",
    @SerializedName("id") var id: Int? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("modified") var modified: String? = null,
    @SerializedName("thumbnail") @Embedded var thumbnail: Thumbnail? = Thumbnail(),//
    @SerializedName("resourceURI") var resourceURI: String? = null,
    @SerializedName("comics") @Ignore var comics: Comics? = Comics(),//
    @SerializedName("series") @Ignore var series: Series? = Series(),//
    @SerializedName("stories") @Ignore var stories: Stories? = Stories(),//
    @SerializedName("events") @Ignore var events: Events? = Events(),//
    @SerializedName("urls") @Ignore var urls: ArrayList<Urls> = arrayListOf()//
): Parcelable{

    companion object{
        @JvmStatic
        @BindingAdapter("characterImage")
        fun loadCharacterImage(view: ImageView, imageUrl: String?) {
            Glide.with(view.getContext())
                .load(imageUrl)
                .apply(RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL))
                .centerCrop()
                .into(view)
        }
    }

}
