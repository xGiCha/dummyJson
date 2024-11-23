package gr.android.dummyjson.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import gr.android.dummyjson.ui.home.HomeNavigation
import gr.android.dummyjson.ui.home.HomeScreen
import gr.android.dummyjson.ui.login.LoginNavigation
import gr.android.dummyjson.ui.login.LoginScreen
import gr.android.dummyjson.ui.navigation.Screen.HomeScreen.withArgsFormat
import gr.android.dummyjson.ui.productDetails.ProductDetailsScreen
import gr.android.dummyjson.ui.productDetails.ProductDetailsScreenNavigation
import gr.android.dummyjson.ui.splash.SplashNavigation
import gr.android.dummyjson.ui.splash.SplashScreen

@Composable
fun DummyJsonApp() {
    val navController = rememberNavController()
    DummyJsonNavHost(
        navController = navController
    )
}

@Composable
fun DummyJsonNavHost(
    navController: NavHostController,
) {
    Scaffold(
    content = { insets ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    PaddingValues(
                        top = insets.calculateTopPadding(),
                        bottom = insets.calculateBottomPadding(),
                        start = insets.calculateStartPadding(LocalLayoutDirection.current),
                        end = insets.calculateEndPadding(LocalLayoutDirection.current)
                    )
                ),
        ) {
            Box {
                NavHost(
                    navController = navController,
                    startDestination = Screen.SplashScreen.route.value,
                ) {
                    navigateToSplashScreen(navController = navController)
                    navigateToLoginScreen(navController = navController)
                    navigateToHomeScreen(navController = navController)
                    navigateToProductDetailsScreen(navController = navController)
                }
            }
        }
    }
    )

}

private fun NavGraphBuilder.navigateToHomeScreen(
    navController: NavHostController
) {
    composable(route = Screen.HomeScreen.route.value) {
        HomeScreen(
            navigate = {
                when(it) {
                    is HomeNavigation.NavigateToDetails -> {
                        navController.navigate(Screen.ProductDetailsScreen.createRoute(it.productId.toString()))
                    }

                    HomeNavigation.NavigateToLoginScreen -> {
                        navController.navigate(Screen.LoginScreen.route.value) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            }
        )
    }
}

private fun NavGraphBuilder.navigateToLoginScreen(
    navController: NavHostController
) {
    composable(route = Screen.LoginScreen.route.value) {
        LoginScreen(
            navigate = {
                when(it){
                    LoginNavigation.NavigateToHome -> {
                        goHomeAndClearBackStack(navController)
                    }
                }
            }
        )
    }
}

private fun NavGraphBuilder.navigateToSplashScreen(
    navController: NavHostController
) {
    composable(route = Screen.SplashScreen.route.value) {
        SplashScreen(
            navigate = {
                when(it){
                    SplashNavigation.LoginToHome -> {
                        navController.navigate(Screen.LoginScreen.route.value) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        }

                    }
                    SplashNavigation.NavigateToHome -> {
                        goHomeAndClearBackStack(navController)
                    }
                }
            }
        )
    }
}

private fun NavGraphBuilder.navigateToProductDetailsScreen(
    navController: NavHostController
) {
    composable(
        route = Screen.ProductDetailsScreen.route.withArgsFormat(
            Screen.ProductDetailsScreen.ARGUMENT_PRODUCT_ID,
        ),
        arguments = listOf(navArgument(Screen.ProductDetailsScreen.ARGUMENT_PRODUCT_ID) {
            type = NavType.StringType
            nullable = false
        })
    ) { backStackEntry ->
        val productId =
            backStackEntry.arguments?.getString(Screen.ProductDetailsScreen.ARGUMENT_PRODUCT_ID)
        ProductDetailsScreen(
            productId = productId?.toInt(),
            navigate = {
                when(it) {
                    ProductDetailsScreenNavigation.OnBack -> {
                        navController.popBackStack()
                    }
                }
            }
        )
    }
}

private fun goHomeAndClearBackStack(navController: NavHostController) {
    navController.navigate(Screen.HomeScreen.route.value) {
        popUpTo(0) {
            inclusive = true
        }
        launchSingleTop = true
    }
}