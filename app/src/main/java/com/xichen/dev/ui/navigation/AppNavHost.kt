package com.xichen.dev.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.xichen.dev.ui.feature.home.HomeRoute
import com.xichen.dev.ui.feature.login.LoginRoute
import com.xichen.dev.ui.feature.register.RegisterRoute
import com.xichen.dev.ui.feature.splash.SplashRoute

@Composable
fun AppNavHost(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoute.Splash.route,
        modifier = modifier,
        enterTransition = { appEnterTransition() },
        exitTransition = { appExitTransition() },
        popEnterTransition = { appPopEnterTransition() },
        popExitTransition = { appPopExitTransition() }
    ) {
        composable(AppRoute.Splash.route) {
            SplashRoute(
                onNavigateToLogin = {
                    navController.navigate(AppRoute.Login.route) {
                        popUpTo(AppRoute.Splash.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToHome = {
                    navController.navigate(AppRoute.Home.route) {
                        popUpTo(AppRoute.Splash.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(AppRoute.Home.route) {
            HomeRoute(
                windowSizeClass = windowSizeClass,
                onNavigateToLogin = {
                    navController.navigate(AppRoute.Login.route)
                }
            )
        }
        composable(AppRoute.Login.route) {
            LoginRoute(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToRegister = {
                    navController.navigate(AppRoute.Register.route)
                },
                onNavigateToHome = {
                    navController.navigate(AppRoute.Home.route) {
                        popUpTo(AppRoute.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(AppRoute.Register.route) {
            RegisterRoute(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToHome = {
                    navController.navigate(AppRoute.Home.route) {
                        popUpTo(AppRoute.Register.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.appEnterTransition(): EnterTransition {
    return if (initialState.destination.route == AppRoute.Splash.route) {
        fadeIn(animationSpec = tween(durationMillis = 220))
    } else {
        slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth / 5 },
            animationSpec = tween(durationMillis = 320)
        ) + fadeIn(animationSpec = tween(durationMillis = 220))
    }
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.appExitTransition(): ExitTransition {
    return if (targetState.destination.route == AppRoute.Splash.route) {
        fadeOut(animationSpec = tween(durationMillis = 180))
    } else {
        slideOutHorizontally(
            targetOffsetX = { fullWidth -> -(fullWidth / 6) },
            animationSpec = tween(durationMillis = 320)
        ) + fadeOut(animationSpec = tween(durationMillis = 200))
    }
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.appPopEnterTransition(): EnterTransition {
    return slideInHorizontally(
        initialOffsetX = { fullWidth -> -(fullWidth / 6) },
        animationSpec = tween(durationMillis = 300)
    ) + fadeIn(animationSpec = tween(durationMillis = 220))
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.appPopExitTransition(): ExitTransition {
    return slideOutHorizontally(
        targetOffsetX = { fullWidth -> fullWidth / 5 },
        animationSpec = tween(durationMillis = 300)
    ) + fadeOut(animationSpec = tween(durationMillis = 180))
}
