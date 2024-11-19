package gr.android.dummyjson.data.local.database.typeConverts

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import gr.android.dummyjson.data.local.database.products.ProductEntity

class ReviewsTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun reviewsListToString(reviews: List<ProductEntity.ReviewEntity>): String {
        return gson.toJson(reviews)
    }

    @TypeConverter
    fun stringToReviewsList(reviewsString: String): List<ProductEntity.ReviewEntity> {
        val listType = object : TypeToken<List<ProductEntity.ReviewEntity>>() {}.type
        return gson.fromJson(reviewsString, listType)
    }
}