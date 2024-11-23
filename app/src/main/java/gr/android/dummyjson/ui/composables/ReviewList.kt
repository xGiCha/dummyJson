package gr.android.dummyjson.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gr.android.dummyjson.ui.productDetails.ProductDetailsContract.State.Data.Product.Review
import androidx.compose.foundation.lazy.items


@Composable
fun ReviewList(reviews: List<Review>) {

    val limitedReviews = reviews.take(3)

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Display each review using ReviewItem composable
        limitedReviews.forEach { review ->
            ReviewItem(review = review)
        }
    }
}

@Preview
@Composable
private fun ReviewListPreview() {
    val reviews = listOf(
        Review(rating = 5, comment = "Excellent product!", reviewerName = "John Doe", date = "2024-05-23", ratingText = ""),
        Review(rating = 4, comment = "Good, but could be improved.", reviewerName = "Jane Smith", date = "2024-06-12", ratingText = ""),
        Review(rating = 3, comment = "It's okay.", reviewerName = "Alice Johnson", date = "2024-07-01", ratingText = ""),
        Review(rating = 2, comment = "Not as expected.", reviewerName = "Bob Brown", date = "2024-08-15", ratingText = "")
    )
    ReviewList(
        reviews = reviews
    )
}