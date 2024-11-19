package gr.android.dummyjson.utils

import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    const val BASE_URL = "https://dummyjson.com/"
    val ACCESS_TOKEN = stringPreferencesKey(name = "access_token")
    val REFRESH_TOKEN = stringPreferencesKey(name = "refresh_token")
    const val PRODUCTS_TABLE = "products_table"
    const val PRODUCT_CATEGORY_TABLE = "products_category_table"
}