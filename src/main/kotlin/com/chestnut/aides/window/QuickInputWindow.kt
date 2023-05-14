package com.chestnut.aides.window

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import androidx.compose.ui.window.Window
import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import com.beust.klaxon.Parser.Companion.default
import com.chestnut.aides.ui.WindowIcon
import com.chestnut.aides.util.AwtUtils
import java.io.File

data class InputConfig(var desc:String,var values: Array<String>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InputConfig

        if (desc != other.desc) return false
        return values.contentEquals(other.values)
    }

    override fun hashCode(): Int {
        var result = desc.hashCode()
        result = 31 * result + values.contentHashCode()
        return result
    }
}

@Composable
fun QuickInputButton(desc:String,values:Array<String>){
    Button(onClick = {AwtUtils.writeToFocus(values)}){
        Text(text = desc)
    }
}
object InputList{
    var data = ClassLoader.getSystemResourceAsStream("config/quick_input.json")?.let { Klaxon().parseArray<InputConfig>(it) }
}
@Composable
fun QuickInputContent(){
    Row  {
        Surface(
            elevation = 4.dp,
            modifier = Modifier.fillMaxHeight()
                .shadow(1.dp, RoundedCornerShape(1.dp)).width(40.dp)
        ) {
            var arr = InputList.data
            LazyColumn {
                itemsIndexed(arr!!) {
                    _, item ->  QuickInputButton(item.desc,item.values)
                }
            }
        }
    }
}
@Composable
@Preview
fun QuickInputWindow(onCloseRequest: () -> Unit) {
    var state by remember {
        mutableStateOf(
            WindowState(
                placement = WindowPlacement.Floating,
                position = WindowPosition(Alignment.Center)
            )
        )
    }
    var scaffoldState = rememberScaffoldState()
    var quickInputOnTop by remember {
        mutableStateOf(true)
    }
    Window(
        focusable = false,
        alwaysOnTop = quickInputOnTop,
        state = state,
        icon = painterResource("wanhuatong-alpha.svg"),
        undecorated = true,
        title = "chestnut",
        onCloseRequest = onCloseRequest
    ) {
        MaterialTheme(colors = darkColors()) {
            Scaffold(
                scaffoldState = scaffoldState,
                topBar = {
                    TopAppBar(
                        modifier = Modifier.height(40.dp).pointerInput(Unit) {

                            var offset = Offset.Zero
                            detectDragGestures(
                                onDragStart = {
                                    offset = it
                                },
                                onDrag = { change, _ ->
                                    if (change.positionChanged()) change.consume()
                                    state.position = WindowPosition(
                                        state.position.x + (change.position.x - offset.x).dp,
                                        state.position.y + (change.position.y - offset.y).dp
                                    )
                                }
                            )
                        },
                        title = { Text("QuickInput") },
                        navigationIcon = {
                            Icon(Icons.Default.Star,modifier= Modifier.clickable(onClick={quickInputOnTop = !quickInputOnTop})
                                .background(color = if(quickInputOnTop) Color.Cyan else MaterialTheme.colors.primary,shape = RoundedCornerShape(1.dp)), contentDescription = "top")
                        },
                        actions = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                WindowIcon.WindowMinButton(state)
                                WindowIcon.WindowPlacementButton(state)
                                WindowIcon.WindowCloseButton(onCloseRequest)
                            }
                        }
                    )
                },
                bottomBar = {
                    BottomAppBar(modifier = Modifier.height(30.dp)) { }
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
                    Row(modifier = Modifier.padding(innerPadding)) {
                        Surface(
                            elevation = 4.dp,
                            modifier = Modifier.fillMaxHeight().align(Alignment.CenterVertically)
                                .shadow(1.dp, RoundedCornerShape(1.dp))
                        ) {
                            QuickInputContent()
                        }
                    }
                }
            )
        }
    }
}