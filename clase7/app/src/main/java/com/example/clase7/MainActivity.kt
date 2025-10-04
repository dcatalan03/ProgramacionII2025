package com.example.clase7

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.clase7.screens.informes.DetalleInformeScreen
import com.example.clase7.screens.informes.ListaInformesScreen
import com.example.clase7.screens.informes.NuevoInformeScreen
import com.example.clase7.ui.theme.Clase7Theme
import com.example.clase7.viewmodels.InformeViewModel
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        if (FirebaseApp.getApps(this).isEmpty()){
            FirebaseApp.initializeApp(this)
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            Clase7Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        MainScreens()
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreens() {
    val context = LocalContext.current
    val navController = rememberNavController()

    var initialScreen: String = stringResource(R.string.screen1)

    val auth = Firebase.auth
    val currentUser = auth.currentUser

    if (currentUser != null) {
        initialScreen = "informes"
    }

    NavHost(navController = navController, startDestination = initialScreen) {
        // Pantallas de autenticación
        composable(context.getString(R.string.screen1)) { LoginScreen(navController) }
        composable(context.getString(R.string.screen2)) { RegisterScreen(navController) }
        composable(context.getString(R.string.screen3)) { SuccessScreen(navController) }
        composable(context.getString(R.string.screen4)) { UserScreen(navController) }
        composable(context.getString(R.string.screen5)) { UsersFormScreen(navController) }
        
        // Pantallas de informes
        composable("informes") {
            val viewModel: InformeViewModel = viewModel()
            ListaInformesScreen(navController = navController, viewModel = viewModel)
        }
        
        composable("nuevo_informe") {
            val viewModel: InformeViewModel = viewModel()
            NuevoInformeScreen(navController = navController, viewModel = viewModel)
        }
        
        composable(
            "informes/{informeId}",
            arguments = listOf(navArgument("informeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val viewModel: InformeViewModel = viewModel()
            val informeId = backStackEntry.arguments?.getString("informeId") ?: return@composable
            DetalleInformeScreen(
                navController = navController,
                viewModel = viewModel,
                informeId = informeId
            )
        }
    }
}

