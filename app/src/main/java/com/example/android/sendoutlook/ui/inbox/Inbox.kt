package com.example.android.sendoutlook.ui.inbox

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.android.sendoutlook.ui.inbox.contract.InboxVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Inbox(modifier: Modifier = Modifier, vm: InboxVM, goToOutBox: () -> Unit) {
    val messages by vm.messages.collectAsState()
    val loading by vm.loading.collectAsState()
    LaunchedEffect(key1 = true) {
        vm.getMail()
    }
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { goToOutBox() }) {
            Icon(imageVector = Icons.Rounded.Edit, contentDescription = "New email")
        }
    }) { padding ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(items = messages) { message ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable { },
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        if (message.isRead == true) MaterialTheme.colorScheme.background
                                        else MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = message.sender?.emailAddress?.name.toString(),
                                        style = MaterialTheme.typography.titleMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = message.receivedDateTime?.toLocalTime().toString(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = message.receivedDateTime?.toLocalDate().toString(),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        maxLines = 1
                                    )
                                }
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = message.subject.toString(),
                                    style = MaterialTheme.typography.titleSmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = message.bodyPreview.toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                        }
                    }
                }
            }
        }

    }
}