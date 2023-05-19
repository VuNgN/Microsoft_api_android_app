package com.example.android.sendoutlook.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.android.sendoutlook.ui.mailcontent.MailContent
import com.example.android.sendoutlook.ui.mailcontent.contract.MailContentVM
import com.example.android.sendoutlook.ui.mailcontent.contract.impl.MailContentVMImpl
import com.example.android.sendoutlook.ui.main.AppContainer
import com.example.android.sendoutlook.ui.main.constract.MainVM
import com.example.android.sendoutlook.ui.main.constract.impl.MainVMImpl
import com.example.android.sendoutlook.util.NavigationRoutes
import kotlinx.coroutines.launch

@Composable
fun RootNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavigationRoutes.MAIN_ROUTE,
) {
    val coroutineScope = rememberCoroutineScope()
    NavHost(
        modifier = modifier, navController = navController, startDestination = startDestination
    ) {
        composable(NavigationRoutes.MAIN_ROUTE) {
            val vm: MainVM = hiltViewModel<MainVMImpl>()
            AppContainer(vm = vm, goToMailContent = { id ->
                coroutineScope.launch {
                    navController.navigate("${NavigationRoutes.MAIL_CONTENT_ROUTE}/$id")
                }
            })
        }
        composable(
            "${NavigationRoutes.MAIL_CONTENT_ROUTE}/{id}", arguments = listOf(
                navArgument("id") { type = NavType.StringType })
        ) {
            val vm: MailContentVM = hiltViewModel<MailContentVMImpl>()
            val id = it.arguments?.getString("id").toString()
            LaunchedEffect(key1 = true) {
                vm.getMessage(id)
            }
            MailContent(
                vm = vm,
                goBack = { coroutineScope.launch { navController.popBackStack() } })
        }
    }
}