package com.example.android.sendoutlook.ui.outbox

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.android.sendoutlook.ui.outbox.contract.OutboxVM
import com.example.android.sendoutlook.util.SendingStatus
import com.microsoft.graph.models.BodyType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Outbox(
    vm: OutboxVM, modifier: Modifier = Modifier
) {
    val sendingStatus by vm.sendingStatus.collectAsState()
    var address by rememberSaveable { mutableStateOf("") }
    var cc by rememberSaveable { mutableStateOf("") }
    var subject by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }
    var contentType by rememberSaveable { mutableStateOf(BodyType.TEXT) }
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current
    LaunchedEffect(key1 = sendingStatus) {
        if (sendingStatus == SendingStatus.SENT) {
            address = ""
            cc = ""
            subject = ""
            content = ""
            focusManager.clearFocus()
            snackbarHostState.showSnackbar("The mail has been sent successfully!")
        }
        if (sendingStatus == SendingStatus.ERROR) {
            snackbarHostState.showSnackbar("Email sending failure!")
            focusManager.clearFocus()
        }
    }
    Scaffold(modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            BottomAppBar(actions = {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "Outlook Email Sending App",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = "Created by: Nguyễn Ngọc Vũ",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }, floatingActionButton = {
                if (sendingStatus == SendingStatus.SENDING) {
                    FloatingActionButton(
                        onClick = {},
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    FloatingActionButton(
                        onClick = { vm.sendMail(address, cc, subject, contentType, content) },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(Icons.Filled.Send, "Localized description")
                    }
                }
            })
        }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            MailTemplate(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 0.dp),
                address = address,
                cc = cc,
                subject = subject,
                content = content,
                contentType = contentType,
                onAddressChange = { s -> address = s },
                onCcChange = { s -> cc = s },
                onContentChange = { s -> content = s },
                onContentTypeChange = { ct -> contentType = ct },
                onSubjectChange = { s -> subject = s })
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MailTemplate(
    modifier: Modifier = Modifier,
    address: String,
    cc: String,
    subject: String,
    content: String,
    contentType: BodyType,
    onAddressChange: (String) -> Unit,
    onCcChange: (String) -> Unit,
    onSubjectChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onContentTypeChange: (BodyType) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = address,
                onValueChange = onAddressChange,
                label = { Text(text = "To") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                )
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = cc,
                onValueChange = onCcChange,
                label = { Text(text = "Cc") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                )
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = subject,
                onValueChange = onSubjectChange,
                label = { Text(text = "Subject") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Content type: ")
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                ExposedDropdownMenuBox(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.TopStart),
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }) {
                    TextField(modifier = Modifier.menuAnchor(),
                        readOnly = true,
                        value = contentType.name,
                        onValueChange = {},
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded
                            )
                        })
                    ExposedDropdownMenu(modifier = Modifier,
                        expanded = expanded,
                        onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(text = { Text(text = "Text") }, onClick = {
                            expanded = false
                            onContentTypeChange(BodyType.TEXT)
                        }, leadingIcon = {})
                        DropdownMenuItem(text = { Text(text = "HTML") }, onClick = {
                            expanded = false
                            onContentTypeChange(BodyType.HTML)
                        }, leadingIcon = {})
                    }
                }
            }
        }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            value = content,
            onValueChange = onContentChange,
            singleLine = false,
            label = { Text(text = "Content") },
        )
    }
}