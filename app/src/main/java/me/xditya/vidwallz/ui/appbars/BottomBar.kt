package me.xditya.vidwallz.ui.appbars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

@Composable
fun BottomBar(
    navController: NavController,
) {
    BottomAppBar(
        actions = {
            IconButton(onClick = {
                navController.navigate("home")
            }) {
                Icon(
                    Icons.Sharp.Home,
                    contentDescription = "Home"
                )
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Wallpaper", fontWeight = FontWeight.Bold) },
                icon = {
                    Icon(
                        Icons.Sharp.Add, contentDescription = "add wall"
                    )
                }, onClick = {
                     // #TODO: Add wallpaper functions
                })
        }
    )
}

@Preview
@Composable
fun BottomBarPreview() {
    BottomBar(navController = NavController(LocalContext.current))
}