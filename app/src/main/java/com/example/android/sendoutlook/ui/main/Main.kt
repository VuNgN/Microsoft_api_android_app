package com.example.android.sendoutlook.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.Inbox
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.android.sendoutlook.ui.main.constract.MainVM
import com.example.android.sendoutlook.ui.nav.HomeNavHost
import com.example.android.sendoutlook.util.MenuItem
import com.example.android.sendoutlook.util.NavigationRoutes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AppContainer(
    modifier: Modifier = Modifier, vm: MainVM, goToMailContent: (String) -> Unit
) {
    val user by vm.user.collectAsState()
    val interactionSource = remember { MutableInteractionSource() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val items = listOf(
        MenuItem(Icons.Rounded.Inbox, "Inbox", NavigationRoutes.INBOX_ROUTE),
        MenuItem(Icons.Rounded.Send, "Outbox", NavigationRoutes.OUTBOX_ROUTE)
    )
    val selectedItem = remember { mutableStateOf(items[0]) }
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet {
            Spacer(modifier = Modifier.height(12.dp))
            items.forEach { item ->
                NavigationDrawerItem(
                    icon = { Icon(item.icon, contentDescription = "") },
                    label = { Text(text = item.title) },
                    selected = item == selectedItem.value,
                    onClick = {
                        coroutineScope.launch {
                            navController.navigate(item.route)
                            drawerState.close()
                        }
                        selectedItem.value = item
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    }) {
        Scaffold(modifier = modifier
            .clickable(
                indication = null, interactionSource = interactionSource
            ) { keyboardController?.hide() }
            .fillMaxSize(), topBar = {
            TopAppBar(title = {
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = user.displayName ?: "",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(text = user.mail ?: "", style = MaterialTheme.typography.titleSmall)
                }
            }, navigationIcon = {
                IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Open drawer navigation menu"
                    )
                }
            }, actions = {
                IconButton(onClick = { coroutineScope.launch { vm.signOut() } }) {
                    Icon(
                        imageVector = Icons.Filled.Logout, contentDescription = "Logout button"
                    )
                }
            })
        }) { paddingValues ->
            HomeNavHost(
                modifier = modifier.padding(paddingValues),
                navController = navController,
                goToOutBox = {
                    coroutineScope.launch {
                        navController.navigate(NavigationRoutes.OUTBOX_ROUTE)
                        selectedItem.value = items[1]
                    }
                },
                goToMailContent = goToMailContent
            )
        }
    }
}