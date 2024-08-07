package com.plasmavan.gemey

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content
import com.plasmavan.gemey.ui.theme.GemeyTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {

    private val chatHistory: MutableList<Content> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fieldList: ArrayList<String> = arrayListOf()
        try {
            val csvFile = resources.assets.open("fields.csv")
            val fileReader = BufferedReader(InputStreamReader(csvFile))
            fileReader.forEachLine {
                fieldList.add(it)
            }
        } catch (e: Exception) {
            Log.e("Error", "$e")
        }

        val levelList: ArrayList<String> = arrayListOf()
        try {
            val csvFile = resources.assets.open("levels.csv")
            val fileReader = BufferedReader(InputStreamReader(csvFile))
            fileReader.forEachLine {
                levelList.add(it)
            }
        } catch (e: Exception) {
            Log.e("Error", "$e")
        }

        val difficultyList: ArrayList<String> = arrayListOf("なし", "簡単", "普通", "難しい")

        val certificationList: ArrayList<String> = arrayListOf()
        try {
            val csvFile = resources.assets.open("certifications.csv")
            val fileReader = BufferedReader(InputStreamReader(csvFile))
            fileReader.forEachLine {
                certificationList.add(it)
            }
        } catch (e: Exception) {
            Log.e("Error", "$e")
        }

        setContent {
            GemeyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainActivityUI(fieldList, levelList, difficultyList, certificationList)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun MainActivityUI(fieldOptions: ArrayList<String>, levelOptions: ArrayList<String>, difficultyOptions: ArrayList<String>, certificationOptions: ArrayList<String>) {
        var fieldText by remember { mutableStateOf(fieldOptions[0]) }
        var levelText by remember { mutableStateOf(levelOptions[0]) }
        var difficultyText by remember { mutableStateOf(difficultyOptions[0]) }
        var certificationsText by remember { mutableStateOf(certificationOptions[0]) }
        var expandedField by remember { mutableStateOf(false) }
        var expandedLevel by remember { mutableStateOf(false) }
        var expandedDifficulty by remember { mutableStateOf(false) }
        var expandedCertifications by remember { mutableStateOf(false) }
        var response by remember { mutableStateOf("") }

        val filteredField by remember { derivedStateOf { levelOptions.filter { it.contains(fieldText, ignoreCase = true) } } }

        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = "Googleのエンタープライズ機能、プロダクト、サービスを含む、Googleのプロダクト、サービス、機械学習技術の提供、向上、および開発のためにこのデータを使用します",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Start
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        ExposedDropdownMenuBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            expanded = expandedField,
                            onExpandedChange = { expandedField = it }
                        ) {
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                value = fieldText,
                                onValueChange = {},
                                readOnly = true,
                                singleLine = true,
                                label = { Text("分野") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedField) },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            )
                            ExposedDropdownMenu(
                                expanded = expandedField,
                                onDismissRequest = { expandedField = false },
                            ) {
                                fieldOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                option,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                        },
                                        onClick = {
                                            fieldText = option
                                            expandedField = false
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                    )
                                }
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        ExposedDropdownMenuBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            expanded = expandedLevel,
                            onExpandedChange = { expandedLevel = it }
                        ) {
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                value = levelText,
                                onValueChange = {},
                                readOnly = true,
                                singleLine = true,
                                label = { Text("レベル") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLevel) },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            )
                            ExposedDropdownMenu(
                                expanded = expandedLevel,
                                onDismissRequest = { expandedLevel = false },
                            ) {
                                levelOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                option,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                        },
                                        onClick = {
                                            levelText = option
                                            expandedLevel = false
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                    )
                                }
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        ExposedDropdownMenuBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            expanded = expandedDifficulty,
                            onExpandedChange = { expandedDifficulty = it }
                        ) {
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                value = difficultyText,
                                onValueChange = {},
                                readOnly = true,
                                singleLine = true,
                                label = { Text("難易度") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDifficulty) },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            )
                            ExposedDropdownMenu(
                                expanded = expandedDifficulty,
                                onDismissRequest = { expandedDifficulty = false },
                            ) {
                                difficultyOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                option,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                        },
                                        onClick = {
                                            difficultyText = option
                                            expandedDifficulty = false
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                    )
                                }
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        ExposedDropdownMenuBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            expanded = expandedCertifications,
                            onExpandedChange = { expandedCertifications = it }
                        ) {
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                value = certificationsText,
                                onValueChange = {},
                                readOnly = true,
                                singleLine = true,
                                label = { Text("検定・資格") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCertifications) },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                            )
                            ExposedDropdownMenu(
                                expanded = expandedCertifications,
                                onDismissRequest = { expandedCertifications = false },
                            ) {
                                certificationOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                option,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                        },
                                        onClick = {
                                            certificationsText = option
                                            expandedCertifications = false
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                    )
                                }
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        IconButton(
                            onClick = {
                                if(fieldText == "なし" && levelText == "なし" && difficultyText == "なし" && certificationsText == "なし") {
                                    return@IconButton
                                } else if(fieldText == "なし") {
                                    fieldText = ""
                                } else if(levelText == "なし") {
                                    levelText = ""
                                } else if(difficultyText == "なし") {
                                    difficultyText = ""
                                } else if(certificationsText == "なし") {
                                    certificationsText = ""
                                }

                                val prompt: String = "四択を選択する形式の問題を以下の内容で作成してください。また、内容が空欄のものについては無視し、最低でも１０問は作成してください。もし、問題がこの内容で問題が作成できない場合には、おすすめの問題を提案してください。\n# 内容\n- 分野：${fieldText}\n- レベル：${levelText}\n- 難易度：${difficultyText}\n- 関連する検定・資格：${certificationsText}"

                                generateResponse(prompt) {
                                    response = it
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }

    private fun generateResponse(prompt: String, onResponseReceived: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val generativeModel = GenerativeModel(
                modelName = "gemini-1.5-flash-latest",
                apiKey = BuildConfig.apiKey
            )
            val chat = generativeModel.startChat(
                history = chatHistory
            )

            val userMessage = content(role = "user") {
                text(prompt)
            }
            chatHistory.add(userMessage)

            val response = chat.sendMessage(prompt)

            val intent = Intent(applicationContext, ResultActivity::class.java)
            intent.putExtra("response", response.text.toString())
            startActivity(intent)

            withContext(Dispatchers.Main) {
                onResponseReceived(response.text.toString())
            }
        }
    }
}