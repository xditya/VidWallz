package me.xditya.vidwallz.ui.appbars

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.navigation.NavController
import java.io.IOException

@Composable
fun BottomBar(
    navController: NavController,
) {
    // for image search
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    val bitmap =  remember {
        mutableStateOf<Bitmap?>(null)
    }
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    // for wall setting
    val ctxW = LocalContext.current as ComponentActivity
    val launcherW = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {}
    )

    val currentNavDest = navController.currentDestination?.route

    BottomAppBar(
        actions = {
            IconButton(onClick = {
                if (currentNavDest != "home") {
                    navController.navigate("home")
                }
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
                    if (hasSetWallpaperPermission(ctxW)) {
                        launcher.launch("image/*")
                    } else {
                        Toast.makeText(context, "Permission not granted", Toast.LENGTH_SHORT).show()
                        launcherW.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    }
                    }
            )
            imageUri?.let {
                if (Build.VERSION.SDK_INT < 28) {
                    @Suppress("DEPRECATION")
                    bitmap.value = MediaStore.Images
                        .Media.getBitmap(context.contentResolver,it)

                } else {
                    val source = ImageDecoder
                        .createSource(context.contentResolver,it)
                    bitmap.value = ImageDecoder.decodeBitmap(source)
                }

                bitmap.value?.let {  btm ->
                    Image(bitmap = btm.asImageBitmap(),
                        contentDescription =null,
                        modifier = Modifier.size(400.dp))
                    val wallpaperManager = WallpaperManager.getInstance(context)
                    try {
                        wallpaperManager.setBitmap(bitmap.value)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    )
}

private fun hasSetWallpaperPermission(context: ComponentActivity): Boolean {
    val permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    val result = ContextCompat.checkSelfPermission(context, permission)
    return result == PermissionChecker.PERMISSION_GRANTED
}

@Preview
@Composable
fun BottomBarPreview() {
    BottomBar(navController = NavController(LocalContext.current))
}