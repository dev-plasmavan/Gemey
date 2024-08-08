package com.plasmavan.gemey

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.plasmavan.gemey.ui.theme.GemeyTheme

class ResultActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val response = intent.getStringExtra("response").toString()

        setContent {
            GemeyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ResultActivityUI(response)
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if(keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            super.onKeyDown(keyCode, event)
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ResultActivityUI(response: String) {
        val context = LocalContext.current

        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(
                            text = getString(R.string.app_name)
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                finish()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowLeft,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = "送信されたデータはGoogleによって、Googleのエンタープライズ機能、プロダクト、サービスを含む、Googleのプロダクト、サービス、機械学習技術の提供、向上、および開発のためにを使用されます。",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Start
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = "出力された内容 ：",
                                modifier = Modifier
                                    .padding(16.dp),
                                textAlign = TextAlign.Start
                            )
                            Text(
                                text = response,
                                modifier = Modifier
                                    .padding(16.dp),
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                }
            }
        }
    }
}