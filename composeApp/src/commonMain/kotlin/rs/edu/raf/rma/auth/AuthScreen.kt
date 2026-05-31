package rs.edu.raf.rma.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onNavigateToCatalog: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AuthContract.SideEffect.NavigateToCatalog -> onNavigateToCatalog()
            }
        }
    }
    // Ovo stanje sme biti lokalno jer se odnosi samo na UI animaciju (ikonica oka)
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val isLoginMode = state.currentScreen == AuthContract.AuthScreen.LOGIN

    // Validacija na osnovu stanja iz ViewModel-a
    val usernameRegex = "^[a-zA-Z0-9_]*$".toRegex()
    val isUsernameValid = state.username.length >= 3 && state.username.matches(usernameRegex)
    val isPasswordValid = state.password.length >= 8
    val isNameValid = state.fullName.isNotBlank()

    val isFormValid = if (isLoginMode) {
        isUsernameValid && isPasswordValid
    } else {
        isUsernameValid && isPasswordValid && isNameValid
    }

    val pureBlack = Color(0xFF000000)
    val darkGray = Color(0xFF1C1C1E)
    val iosBlue = Color(0xFF0A84FF)
    val errorRed = Color(0xFFFF453A)
    val textLight = Color(0xFFEBEBF5)
    val textMuted = Color(0xFF8E8E93)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(pureBlack)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.widthIn(max = 400.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "SHOWTIME",
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isLoginMode) "Sign in to your account" else "Create a new account",
                fontSize = 16.sp,
                color = textMuted,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            AnimatedVisibility(
                visible = state.error != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Surface(
                    color = errorRed.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Text(
                        text = state.error ?: "",
                        color = errorRed,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            AnimatedVisibility(visible = !isLoginMode) {
                Column {
                    OutlinedTextField(
                        value = state.fullName,
                        onValueChange = { viewModel.onEvent(AuthContract.UiEvent.UpdateFullName(it)) },
                        label = { Text("Full Name", color = textMuted) },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = textMuted) },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = darkGray,
                            unfocusedContainerColor = darkGray,
                            focusedBorderColor = iosBlue,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = textLight
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            OutlinedTextField(
                value = state.username,
                onValueChange = { viewModel.onEvent(AuthContract.UiEvent.UpdateUsername(it)) },
                label = { Text("Username", color = textMuted) },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = textMuted) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                isError = state.username.isNotEmpty() && !isUsernameValid,
                supportingText = {
                    if (state.username.isNotEmpty() && !isUsernameValid) {
                        Text("Min 3 chars (a-z, 0-9, _)", color = errorRed)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = darkGray,
                    unfocusedContainerColor = darkGray,
                    focusedBorderColor = iosBlue,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = textLight,
                    errorContainerColor = darkGray,
                    errorBorderColor = errorRed,
                    errorTextColor = Color.White,
                    errorCursorColor = iosBlue
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = { viewModel.onEvent(AuthContract.UiEvent.UpdatePassword(it)) },
                label = { Text("Password", color = textMuted) },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = textMuted) },
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(image, contentDescription = null, tint = textMuted)
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                isError = state.password.isNotEmpty() && !isPasswordValid,
                supportingText = {
                    if (state.password.isNotEmpty() && !isPasswordValid) {
                        Text("Min 8 characters", color = errorRed)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = darkGray,
                    unfocusedContainerColor = darkGray,
                    focusedBorderColor = iosBlue,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = textLight,
                    errorContainerColor = darkGray,
                    errorBorderColor = errorRed,
                    errorTextColor = Color.White,
                    errorCursorColor = iosBlue
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (state.isLoading) {
                CircularProgressIndicator(color = iosBlue)
            } else {
                Button(
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(50),
                    enabled = isFormValid,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = iosBlue,
                        disabledContainerColor = darkGray,
                        contentColor = Color.White,
                        disabledContentColor = textMuted
                    ),
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.onEvent(if (isLoginMode) AuthContract.UiEvent.Login(state.username, state.password) else AuthContract.UiEvent.Signup(state.fullName, state.username, state.password))
                    }
                ) {
                    Text(
                        text = if (isLoginMode) "Log In" else "Sign Up",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = {
                    viewModel.onEvent(AuthContract.UiEvent.NavigateTo(
                        if (isLoginMode) AuthContract.AuthScreen.SIGNUP else AuthContract.AuthScreen.LOGIN
                    ))
                    viewModel.onEvent(AuthContract.UiEvent.ClearError)
                    // Resetujemo polja u ViewModel-u pri promeni ekrana
                    viewModel.onEvent(AuthContract.UiEvent.UpdateUsername(""))
                    viewModel.onEvent(AuthContract.UiEvent.UpdatePassword(""))
                    viewModel.onEvent(AuthContract.UiEvent.UpdateFullName(""))
                }
            ) {
                Text(
                    text = if (isLoginMode) "Don't have an account? Sign up" else "Already have an account? Log in",
                    color = textMuted,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}