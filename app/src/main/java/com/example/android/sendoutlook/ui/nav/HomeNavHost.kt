package com.example.android.sendoutlook.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.android.sendoutlook.ui.inbox.Inbox
import com.example.android.sendoutlook.ui.inbox.contract.InboxVM
import com.example.android.sendoutlook.ui.inbox.contract.impl.InboxVMImpl
import com.example.android.sendoutlook.ui.outbox.Outbox
import com.example.android.sendoutlook.ui.outbox.contract.OutboxVM
import com.example.android.sendoutlook.ui.outbox.contract.impl.OutboxVMImpl
import com.example.android.sendoutlook.util.NavigationRoutes

@Composable
fun HomeNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavigationRoutes.INBOX_ROUTE,
    goToOutBox: () -> Unit = {},
    goToMailContent: (String) -> Unit = {}
) {
    NavHost(
        modifier = modifier, navController = navController, startDestination = startDestination
    ) {
        composable(NavigationRoutes.INBOX_ROUTE) {
            val vm: InboxVM = hiltViewModel<InboxVMImpl>()
            Inbox(
                vm = vm, goToOutBox = goToOutBox, goToMailContent = goToMailContent
            )
        }
        composable(NavigationRoutes.OUTBOX_ROUTE) {
            val vm: OutboxVM = hiltViewModel<OutboxVMImpl>()
            Outbox(vm = vm)
        }
    }
}