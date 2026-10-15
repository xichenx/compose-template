package com.xichen.dev.ui.navigation

sealed class AppRoute(val route: String) {
    data object Splash : AppRoute("splash")
    data object Home : AppRoute("home")
    data object AuthEntry : AppRoute("auth-entry")
    data object Login : AppRoute("login")
    data object Register : AppRoute("register")
}
