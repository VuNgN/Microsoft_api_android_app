package com.example.android.sendoutlook.ui.mailcontent

import android.text.Html
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.android.sendoutlook.ui.mailcontent.contract.MailContentVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MailContent(modifier: Modifier = Modifier, vm: MailContentVM, goBack: () -> Unit) {
    val message by vm.message.collectAsState()
    val loading by vm.loading.collectAsState()
    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        TopAppBar(modifier = Modifier, title = {}, navigationIcon = {
            IconButton(onClick = { goBack() }) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.ArrowBack, contentDescription = "Back navigation"
                )
            }
        })
    }) { paddingValues ->
        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 20.dp),
                contentPadding = paddingValues,
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    Text(
                        text = message.subject.toString(),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = message.sender?.emailAddress?.name.toString(),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = Html.fromHtml(
                            message.body?.content.toString(), Html.FROM_HTML_MODE_COMPACT
                        ).toString(), style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}