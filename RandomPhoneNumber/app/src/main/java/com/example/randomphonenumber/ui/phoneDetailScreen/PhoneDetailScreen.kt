package com.example.randomphonenumber.ui.phoneDetailScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneDetailsScreen(
    navController: NavHostController,
    phoneNumbers: List<String>,
    viewModel: PhoneDetailsViewModel = hiltViewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val maxOtherSelections = 2
    val selectedOptions = remember { mutableStateListOf(*Array(phoneNumbers.size) { "" }) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Phone Details Screen") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                phoneNumbers.forEachIndexed { index, phone ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = phone, modifier = Modifier.weight(1f))
                        DropdownMenuField(
                            options = getAvailableOptions(
                                selectedOptions,
                                index,
                                maxOtherSelections
                            ),
                            selectedOption = selectedOptions[index],
                            onOptionSelected = { selectedOptions[index] = it }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    enabled = selectedOptions.all { it.isNotBlank() },
                    onClick = {
                        viewModel.savePhoneDetails(phoneNumbers, selectedOptions) {
                            navController.popBackStack()
                            navController.navigate("displayNumbers")
                        }
                        Log.d("PhoneDetails", "Saved data: $selectedOptions")
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Save")
                }
            }
        }
    )
}

fun getAvailableOptions(
    selectedOptions: List<String>,
    currentIndex: Int,
    maxOtherSelections: Int
): List<String> {
    val allOptions = listOf("Home", "Work", "Other")
    val selectedCounts = selectedOptions.groupingBy { it }.eachCount()

    return allOptions.filter { option ->
        when (option) {
            "Home", "Work" -> !selectedOptions.contains(option) || selectedOptions[currentIndex] == option
            "Other" -> (selectedCounts["Other"]
                ?: 0) < maxOtherSelections || selectedOptions[currentIndex] == option

            else -> true
        }
    }
}

@Composable
fun DropdownMenuField(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopStart)
            .clickable { expanded = true }
    ) {
        Text(
            text = selectedOption.ifEmpty { "Select" },
            modifier = Modifier.padding(8.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
