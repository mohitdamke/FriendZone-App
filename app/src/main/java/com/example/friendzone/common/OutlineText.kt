package com.example.friendzone.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Preview
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlineText(
    modifier: Modifier = Modifier,
    value: String,
    icons: ImageVector,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value.trim(),
        onValueChange = { onValueChange(it) },
        singleLine = true
        ,leadingIcon = {
            Icon(
                imageVector = icons,
                contentDescription = "",
                modifier = Modifier.padding(10.dp), tint = Gray
            )
        },
        label = {
            Text(
                text = "Type your $label", fontSize = 16.sp,
                fontWeight = FontWeight.W600, color = Gray,
                fontFamily = FontFamily.SansSerif, maxLines = 1
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Gray,
            unfocusedBorderColor = Gray,
            focusedTextColor = Gray,
            unfocusedTextColor = Gray
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        modifier = modifier.fillMaxWidth(), minLines = 1
    )
}


@Preview(showBackground = true)
@Composable
private fun prev() {
    var text by rememberSaveable { mutableStateOf("") }
    OutlineText(
        value = text,
        onValueChange = { text = it },
        icons = Icons.Rounded.Preview,
        label = "Text"
    )
}