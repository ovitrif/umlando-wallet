package com.example.umlandowallet.ui.settings

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.umlandowallet.Global.channelManager
import com.example.umlandowallet.Global.temporaryChannelId
import com.example.umlandowallet.utils.toByteArray
import com.example.umlandowallet.utils.LDKTAG
import org.ldk.structs.ChannelHandshakeConfig
import org.ldk.structs.Result_ThirtyTwoBytesAPIErrorZ
import org.ldk.structs.UserConfig
import org.ldk.util.UInt128
import java.io.File
import java.io.FileWriter
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenChannelScreen() {
    var pubKey by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp)
    )
    {
        Text(
            text = "Open a channel",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xff1f0208),
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
        )
        TextField(
            value = pubKey,
            onValueChange = { pubKey = it },
            placeholder = {
                Text("Peer public node ID")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )
        Button(
            onClick = {
                createChannel(pubKey)
            },
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
        ) {
            Text(text = "Connect")
        }
    }
}

private fun createChannel(pubKey: String) {
    temporaryChannelId = null

    val amount: Long = 100000
    val pushMsat: Long = 0
    val userId = UInt128(42L)

    val userConfig = UserConfig.with_default()

    val channelHandshakeConfig = ChannelHandshakeConfig.with_default()
    // set the following to false to open a private channel
    channelHandshakeConfig._announced_channel = false
 
    val createChannelResult = channelManager!!.create_channel(
        pubKey.toByteArray(), amount, pushMsat, userId, userConfig
    )

    if (createChannelResult !is Result_ThirtyTwoBytesAPIErrorZ) {
        Log.i(LDKTAG, "ERROR: failed to open channel with: $pubKey")
    }

    if(createChannelResult.is_ok) {
        Log.i(LDKTAG, "EVENT: initiated channel with peer: $pubKey")
    }
}
