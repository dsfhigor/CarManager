package com.higordsf.carmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.higordsf.carmanager.data.ThemePreferences
import com.higordsf.carmanager.ui.theme.CarManagerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val themePreferences = remember { ThemePreferences(context) }
            val themeSelection by themePreferences.themeMode.collectAsState(initial = "system")
            val scope = rememberCoroutineScope()

            val useDarkTheme = when (themeSelection) {
                "light" -> false
                "dark" -> true
                else -> isSystemInDarkTheme()
            }

            CarManagerTheme(darkTheme = useDarkTheme) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Meu Veiculo") },
                            actions = {
                                ThemeActionMenu(onThemeSelected = { mode ->
                                    scope.launch {
                                        themePreferences.saveThemeMode(mode)
                                    }
                                })
                            }
                        )
                    }
                ) { innerPadding ->
                    Greeting(
                        name = "Sandero",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ThemeActionMenu(onThemeSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Selecionar Tema")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Padrão do Sistema") },
                onClick = {
                    onThemeSelected("system")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Escuro") },
                onClick = {
                    onThemeSelected("dark")
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Claro") },
                onClick = {
                    onThemeSelected("light")
                    expanded = false
                }
            )
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CarManagerTheme {
        Greeting("Android",)
    }
}