package com.example.umlandowallet.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.umlandowallet.*
import com.example.umlandowallet.Global.channelManager
import com.example.umlandowallet.Global.keysManager
import org.ldk.enums.Currency
import org.ldk.structs.*
import org.ldk.structs.Result_Bolt11InvoiceSignOrCreationErrorZ

@Composable
fun ReceivePaymentScreen() {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val logger: Logger = Logger.new_impl(LDKLogger)

    val description =  "coffee"
    val amtMsat: Long = 10000
    val invoice = UtilMethods.create_invoice_from_channelmanager(
        channelManager,
        keysManager!!.inner.as_NodeSigner(),
        logger,
        Currency.LDKCurrency_Regtest,
        Option_u64Z.some(amtMsat),
        description,
        300,
        Option_u16Z.some(144),
    )

    val invoiceResult = (invoice as Result_Bolt11InvoiceSignOrCreationErrorZ.Result_Bolt11InvoiceSignOrCreationErrorZ_OK).res
    val encodedInvoice = invoiceResult.to_str()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp)
    )
    {
        Text(
            text = "Lightning Invoice",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xff1f0208),
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
        )
        Text(
            text = encodedInvoice,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)        )
        Button(
            onClick = {
                clipboardManager.setText(AnnotatedString((encodedInvoice)))
            },
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
        ) {
            Text(text = "Copy invoice")
        }
    }
}
