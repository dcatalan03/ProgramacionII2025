package com.example.clase7

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.clase7.utils.FirestoreUtils
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = Firebase.auth
    val coroutineScope = rememberCoroutineScope()

    var stateEmail by remember { mutableStateOf("") }
    var statePassword by remember { mutableStateOf("") }
    var stateConfirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val activity = LocalView.current.context as Activity

    Scaffold (
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}){
                         Icon(
                             imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                             contentDescription= stringResource(R.string.content_description_icon_exit)
                         )
                    }
                }
            )
        }
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 32.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                imageVector = Icons.Default.Person,
                contentDescription = stringResource(R.string.content_description_icon_person),
                modifier = Modifier.size(200.dp)
            )
            Text(
                text = stringResource(R.string.register_screen_text),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0066B3)
            )
            Spacer(
                modifier = Modifier.height(20.dp)
            )
            OutlinedTextField(
                value = stateEmail,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = stringResource(R.string.content_description_icon_email)
                    )
                },
                onValueChange = {stateEmail = it},
                label = {Text(stringResource(R.string.fields_email))},
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = statePassword,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = stringResource(R.string.content_description_icon_password)
                    )
                },
                onValueChange = {statePassword = it},
                label = {Text(stringResource(R.string.fields_password))}
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = stateConfirmPassword,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                    )
                },
                onValueChange = {stateConfirmPassword = it},
                label = {Text(stringResource(R.string.fields_confirm_password))}
            )
            // Mostrar mensaje de error si existe
            errorMessage?.let { message ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error",
                        tint = Color.Red
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = message,
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
            }

            Button(
                onClick = {
                    if (statePassword != stateConfirmPassword) {
                        errorMessage = context.getString(R.string.register_screen_password_error)
                        return@Button
                    }

                    if (statePassword.length < 6) {
                        errorMessage = "La contraseña debe tener al menos 6 caracteres"
                        return@Button
                    }

                    isLoading = true
                    errorMessage = null

                    coroutineScope.launch {
                        try {
                            // Verificar si el usuario está en la lista de permitidos
                            val isUserAllowed = FirestoreUtils.isUserAllowedToRegister(stateEmail)
                            
                            if (!isUserAllowed) {
                                errorMessage = "No tienes permiso para registrarte con este correo. Contacta al administrador."
                                isLoading = false
                                return@launch
                            }

                            // Si está permitido, proceder con el registro
                            auth.createUserWithEmailAndPassword(stateEmail, statePassword)
                                .addOnCompleteListener(activity) { task ->
                                    isLoading = false
                                    if (task.isSuccessful) {
                                        // Registro exitoso, navegar a la pantalla de éxito
                                        navController.navigate(context.getString(R.string.screen3)) {
                                            popUpTo(context.getString(R.string.screen1)) {
                                                inclusive = true
                                            }
                                        }
                                    } else {
                                        errorMessage = task.exception?.message ?: "Error al crear la cuenta"
                                    }
                                }
                        } catch (e: Exception) {
                            isLoading = false
                            errorMessage = "Error al verificar el correo: ${e.message}"
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0066B3)
                ),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(text = stringResource(R.string.register_screen_register_button))
                }
            }
        }
    }
}