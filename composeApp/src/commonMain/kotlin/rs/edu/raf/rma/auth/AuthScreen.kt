package rs.edu.raf.rma.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AuthScreen(viewModel: AuthViewModel) {
    val state by viewModel.uiState.collectAsState()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
        Text(text = state.currentScreen.name, style = MaterialTheme.typography.headlineMedium)

        if (state.currentScreen == AuthContract.AuthScreen.SIGNUP) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") })
        }

        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") })
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") })

        Button(onClick = {
            if (state.currentScreen == AuthContract.AuthScreen.LOGIN) {
                viewModel.onEvent(AuthContract.UiEvent.Login(username, password))
            } else {
                viewModel.onEvent(AuthContract.UiEvent.Signup(name, username, password))
            }
        }) {
            Text(if (state.currentScreen == AuthContract.AuthScreen.LOGIN) "Login" else "Sign Up")
        }
    }
}