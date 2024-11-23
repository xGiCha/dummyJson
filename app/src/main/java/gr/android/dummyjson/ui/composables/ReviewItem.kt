package gr.android.dummyjson.ui.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gr.android.dummyjson.ui.productDetails.ProductDetailsContract.State.Data.Product.Review

@Composable
fun ReviewItem(
    review: Review
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(
            text = review.ratingText.orEmpty(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        review.rating?.let { rating ->
            Row {
                repeat(5) { index ->
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star",
                        tint = if (index < review.rating) Color.Yellow else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = review.reviewerName.orEmpty(),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = review.comment.orEmpty(),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = review.date.orEmpty(),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview
@Composable
private fun ReviewItemPreview() {
    val review =  Review(rating = 5, comment = "Excellent product!", reviewerName = "John Doe", date = "2024-05-23", ratingText = "")
    ReviewItem(
        review
    )
}