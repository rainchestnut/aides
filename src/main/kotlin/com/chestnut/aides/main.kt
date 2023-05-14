import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.chestnut.aides.ui.WindowIcon
import com.chestnut.aides.window.QuickInputContent
import com.chestnut.aides.window.QuickInputWindow
import kotlinx.coroutines.launch
import java.awt.Toolkit


fun main() = application {
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    var quickInputWindowIsOpen by remember { mutableStateOf(false) }
    var state by remember {
        mutableStateOf(
            WindowState(
                placement = WindowPlacement.Floating,
                width = screenSize.width.dp,
                height = (screenSize.height - 50).dp,
                position = WindowPosition(Alignment.Center)
            )
        )
    }
    var scaffoldState = rememberScaffoldState()
    Window(
        focusable = true,
        state = state,
        icon = painterResource("wanhuatong-alpha.svg"),
        undecorated = true,
        title = "chestnut",
        onCloseRequest = ::exitApplication
    ) {
        MaterialTheme(colors = darkColors()) {
            Scaffold(
                modifier = Modifier.shadow(10.dp).border(1.dp,MaterialTheme.colors.background),
                scaffoldState = scaffoldState,
                topBar = {
                    TopAppBar(
                        modifier = Modifier.height(40.dp).pointerInput(Unit) {

                            var offset = Offset.Zero
                            detectDragGestures(
                                onDragStart= {
                                    offset = it
                                },
                                onDrag = {
                                        change, _ ->
                                    if ( change.positionChanged()) change.consume()
                                    state.position = WindowPosition(
                                        state.position.x + (change.position.x - offset.x).dp,
                                        state.position.y +  (change.position.y - offset.y ).dp
                                    )
                                }
                            )
                        },
                        title = { Text("") },
                        navigationIcon = {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 4.dp)) {
                                WindowIcon.ChestnutTitleIcon()
                                Spacer(modifier = Modifier.width(20.dp))
                                WindowIcon.WindowMenuTip()
                            }
                        },
                        actions = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                WindowIcon.WindowMinButton(state)
                                WindowIcon.WindowPlacementButton(state)
                                WindowIcon.WindowCloseButton(::exitApplication)
                            }
                        }
                    )
                },
                bottomBar = {
                   BottomAppBar(modifier = Modifier.height(30.dp)) {  }
                },
                drawerContent = {
                    Text("Drawer content")
                },
                floatingActionButton = {
                    FloatingActionButton(onClick = { /* handle click */ }) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                },
                content = { innerPadding ->
                    print(innerPadding)
                    Row (modifier = Modifier.padding(innerPadding)) {
                        Surface(
                            elevation = 4.dp,
                            modifier = Modifier.fillMaxHeight().align(Alignment.CenterVertically)
                                .shadow(1.dp, RoundedCornerShape(1.dp)).width(40.dp).border(0.dp, MaterialTheme.colors.background)
                        ) {
                            Column {
                                IconButton(modifier = Modifier.focusable(false), onClick = {
                                    quickInputWindowIsOpen  = true
                                }) {
                                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                                }
                            }
                        }

                        val scope = rememberCoroutineScope()
                        Button(onClick = {
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Hello, Snackbar!",
                                    actionLabel = "Undo"
                                )
                            }
                        }) {
                            //Text(modifier = Modifier.fillMaxHeight(),text="Show Snackbar")
                            Text(
                                "Hello, World",
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            )
        }
    }
    if (quickInputWindowIsOpen){
        QuickInputWindow { quickInputWindowIsOpen = false }
    }
}
