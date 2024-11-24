package gr.android.dummyjson.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gr.android.dummyjson.R

@Composable
fun ConditionalText(text: String) {

    val minimumLineLength = 3

    val expandedState = remember { mutableStateOf(false) }
    val showReadMoreButtonState = remember { mutableStateOf(false) }
    val maxLines = if (expandedState.value) 200 else minimumLineLength

    Column(modifier = Modifier.padding(top = 10.dp)) {
        Text(
            text = text,
            fontSize = 12.sp,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            maxLines = maxLines,
            onTextLayout = { textLayoutResult: TextLayoutResult ->
                if (textLayoutResult.lineCount > minimumLineLength - 1) {
                    if (textLayoutResult.isLineEllipsized(minimumLineLength - 1)) showReadMoreButtonState.value =
                        true
                }
            }
        )
        if (showReadMoreButtonState.value) {
            Text(
                text = stringResource(R.string.read_more),
                color = Color.Blue,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clickable {
                        expandedState.value = !expandedState.value
                    },

                style = MaterialTheme.typography.bodySmall

            )
        }

    }
}