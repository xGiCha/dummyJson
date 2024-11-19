package gr.android.dummyjson.data.local.database.products

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TagsConverter {
    @TypeConverter
    fun fromTagsList(tags: List<String>): String {
        return Gson().toJson(tags)
    }

    @TypeConverter
    fun toTagsList(tagsString: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(tagsString, listType)
    }
}

class ReviewsConverter {
    @TypeConverter
    fun fromReviewsList(reviews: List<ProductEntity.ReviewEntity>): String {
        return Gson().toJson(reviews)
    }

    @TypeConverter
    fun toReviewsList(reviewsString: String): List<ProductEntity.ReviewEntity> {
        val listType = object : TypeToken<List<ProductEntity.ReviewEntity>>() {}.type
        return Gson().fromJson(reviewsString, listType)
    }
}

class ImagesConverter {
    @TypeConverter
    fun fromImagesList(images: List<String>): String {
        return Gson().toJson(images)
    }

    @TypeConverter
    fun toImagesList(imagesString: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(imagesString, listType)
    }
}

class DimensionsConverter {
    @TypeConverter
    fun fromDimensions(dimensions: ProductEntity.DimensionsEntity): String {
        return "${dimensions.width},${dimensions.height},${dimensions.depth}"
    }

    @TypeConverter
    fun toDimensions(dimensionsString: String): ProductEntity.DimensionsEntity {
        val dimensionsArray = dimensionsString.split(",")
        return ProductEntity.DimensionsEntity(
            width = dimensionsArray[0].toDouble(),
            height = dimensionsArray[1].toDouble(),
            depth = dimensionsArray[2].toDouble()
        )
    }
}
