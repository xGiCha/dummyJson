package gr.android.dummyjson.data.local.database.typeConverts

import androidx.room.TypeConverter
import gr.android.dummyjson.data.local.database.products.ProductEntity

class DimensionsTypeConverter {
    @TypeConverter
    fun dimensionsToString(dimensions: ProductEntity.DimensionsEntity): String {
        return "${dimensions.width},${dimensions.height},${dimensions.depth}"
    }

    @TypeConverter
    fun stringToDimensions(dimensionsString: String): ProductEntity.DimensionsEntity {
        val dimensionsArray = dimensionsString.split(",")
        return ProductEntity.DimensionsEntity(
            width = dimensionsArray[0].toDouble(),
            height = dimensionsArray[1].toDouble(),
            depth = dimensionsArray[2].toDouble()
        )
    }
}