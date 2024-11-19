package gr.android.dummyjson.data.local.database.typeConverts

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ImagesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun imagesListToString(images: List<String>): String {
        return gson.toJson(images)
    }

    @TypeConverter
    fun stringToImagesList(imagesString: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(imagesString, listType)
    }
}