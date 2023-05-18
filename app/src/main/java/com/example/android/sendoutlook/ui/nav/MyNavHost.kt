package com.example.android.sendoutlook.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
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
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavigationRoutes.INBOX_ROUTE
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationRoutes.INBOX_ROUTE) {
            val vm: InboxVM = viewModel<InboxVMImpl>()
            Inbox(vm = vm)
        }
        composable(NavigationRoutes.OUTBOX_ROUTE) {
            val vm: OutboxVM = viewModel<OutboxVMImpl>()
            Outbox(vm = vm)
        }
    }
}