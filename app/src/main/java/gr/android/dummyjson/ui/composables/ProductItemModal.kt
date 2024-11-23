package gr.android.dummyjson.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import gr.android.dummyjson.R
import gr.android.dummyjson.ui.home.HomeContract
import gr.android.dummyjson.ui.theme.lineHeightStyleDefault

@Composable
fun ProductItemModal(
    product: HomeContract.State.Data.Product?,
    onProductClick: (HomeContract.State.Data.Product) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                product?.let {
                    onProductClick(product)
                }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .width(170.dp)
                .height(250.dp),
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = product?.image)
                        .apply(block = fun ImageRequest.Builder.() {
                            placeholder(R.drawable.ic_placeholder)
                            crossfade(true)
                        }).build()
                ),
                contentDescription = null,
                modifier = Modifier
                    .width(170.dp)
                    .height(124.dp)
                    .align(CenterHorizontally),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                text = product?.title.orEmpty(),
                maxLines = 2,
                minLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                style = TextStyle(lineHeightStyle = lineHeightStyleDefault)
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                text = "Category: ${product?.category.orEmpty()}",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                style = TextStyle(lineHeightStyle = lineHeightStyleDefault)
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                text = "Price: ${product?.price.orEmpty()}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                style = TextStyle(lineHeightStyle = lineHeightStyleDefault)
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

    }
}

@Preview
@Composable
private fun ProductItemModalPreview() {
    ProductItemModal(
        product = HomeContract.State.Data.Product(
            "All",
            id = 5,
            title = "Woman Printed Kurta",
            image = "",
            description = "Neque porro quisquam est qui dolorem ipsum quia",
            price = "1500"
        ),
        onProductClick = {},
    )
}