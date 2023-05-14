package com.chestnut.aides.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import com.chestnut.aides.window.QuickInputWindow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
object WindowIcon {
    @Composable
    fun WindowMinButton(state: WindowState) {
        val scope = rememberCoroutineScope()
        IconButton(onClick = {
            val originHeight = state.size.height
            state.placement = WindowPlacement.Floating
            scope.launch {
                for (i in 1..10) {
                    state.size= DpSize(state.size.width,height = originHeight * (10 - i) / 10)
                    delay(20)
                }
                state.isMinimized = true
                state.size= DpSize(state.size.width,height = originHeight)
            }
        }) {
            Icon(painterResource("icons/windowMin.svg"), modifier = Modifier.size(20.dp), contentDescription = null)
        }
    }

    @Composable
    fun WindowCloseButton(exit: () -> Unit) {
        IconButton(onClick = exit) {
            Icon(painterResource("icons/windowClose.svg"), modifier = Modifier.size(20.dp), contentDescription = null)
        }
    }

    @Composable
    fun WindowPlacementButton(state: WindowState) {
        // 当处于全屏状态时，图标为"windowFloat.svg",点击进入浮动状态,切换图标为"windowFullScreen.svg"
        // 当处于浮动状态时,图标为"windowFullScreen.svg"，点击进入全屏状态，切换图标为"windowFloat.svg"
        IconButton(onClick = {
            val actPlacement: WindowPlacement =
                if (state.placement == WindowPlacement.Floating) WindowPlacement.Maximized else WindowPlacement.Floating
            state.placement = actPlacement
        }) {
            Icon(
                painterResource(if (state.placement == WindowPlacement.Floating) "icons/windowFullScreen.svg" else "icons/windowFloat.svg"),
                modifier = Modifier.size(20.dp),
                contentDescription = null
            )
        }


    }

    @Composable
    @Preview
    fun TitleIcon() {

        Row {
            // 创建一个鼠标悬停提示区域
            IconButton(modifier = Modifier
                .size(25.dp), onClick = { /* handle click */ }) {
                Image(
                    painter = painterResource("wanhuatong-alpha.svg"),
                    contentDescription = "chestnut",
                    modifier = Modifier.clip(CircleShape)
                )
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    @Preview
    fun ChestnutTitleIcon() {
        TooltipArea(
            tooltip = {
                ChestnutDescTip()
            }
        ) {
            TitleIcon()
        }
    }

    @Composable
    @Preview
    fun ChestnutDescTip() {
        Surface(
            shape = RoundedCornerShape(4.dp),
            elevation = 4.dp,
            modifier = Modifier.padding(8.dp).shadow(1.dp, RoundedCornerShape(1.dp))
        ) {
            Column {
                Text("chestnut", modifier = Modifier.padding(8.dp))
                Text("版本号：1.0.0", modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.caption)
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    @Preview
    fun WindowMenuTip(){
        TooltipArea(
            tooltip = {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    elevation = 4.dp,
                    modifier = Modifier.padding(8.dp)
                        .shadow(1.dp, RoundedCornerShape(1.dp)).width(200.dp)
                ) {
                    Column {
                        Text(
                            "我还没想好这个按钮做什么",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
            }
        ) {
            IconButton(
                modifier = Modifier.size(20.dp),
                onClick = { /* doSomething() */ }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        }
    }
}
