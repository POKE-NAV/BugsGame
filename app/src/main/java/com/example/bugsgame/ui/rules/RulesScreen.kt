package com.example.bugsgame.ui.rules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bugsgame.R
import com.example.bugsgame.ui.theme.BugsGameTheme

@Composable
fun RulesScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val html = remember {
        context.resources.openRawResource(R.raw.rules)
            .bufferedReader()
            .use { it.readText() }
    }
    val rules = remember(html) { AnnotatedString.fromHtml(html) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(30.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = rules,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RulesScreenPreview() {
    BugsGameTheme {
        RulesScreen()
    }
}
