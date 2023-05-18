package com.example.android.sendoutlook.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android.sendoutlook.R
import com.example.android.sendoutlook.ui.theme.SendOutlookTheme

@Composable
fun Login(modifier: Modifier = Modifier, doInteractiveSignIn: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), contentAlignment = Alignment.Center
        ) {
            Button(onClick = { doInteractiveSignIn() }) {
                Icon(
                    painter = painterResource(id = R.drawable.windows),
                    contentDescription = "Sign in button",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "Sign in with Microsoft")
            }
        }
        Text(
            text = "Outlook Email Sending App", style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = "Created by: Nguyễn Ngọc Vũ", style = MaterialTheme.typography.labelSmall
        )
    }
}

@Preview
@Composable
fun LoginPreview() {
    SendOutlookTheme { Login {} }
}