package ca.gbc.comp3074.howlader.assignment1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme{
                MainScreen()

                }
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(){
    val context = LocalContext.current

    // Input
    var hoursInput by remember { mutableStateOf("") }
    var rateInput by remember { mutableStateOf("") }
    var taxRateInput by remember { mutableStateOf("") }

    // Output
    var pay by remember { mutableDoubleStateOf(0.0) }
    var overtimePay by remember { mutableDoubleStateOf(0.0) }
    var totalPay by remember { mutableDoubleStateOf(0.0) }
    var tax by remember { mutableDoubleStateOf(0.0) }
    var isCalculated by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Payroll Calculator")},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = {
                        val intent = Intent(context, AboutActivity::class.java)
                        context.startActivity(intent)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "About"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = hoursInput,
                onValueChange = { hoursInput = it },
                label = { Text("Number of Hours Worked") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = rateInput,
                onValueChange = { rateInput = it },
                label = { Text("Hourly Rate ($)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = taxRateInput,
                onValueChange = { taxRateInput = it },
                label = { Text("Tax Rate (eg. 20%)") },
                suffix = { Text("%") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Button(
                onClick = {
                    val hours = hoursInput.toDoubleOrNull() ?: 0.0
                    val rate = rateInput.toDoubleOrNull() ?: 0.0
                    val taxRate = (taxRateInput.toDoubleOrNull() ?: 0.0) / 100.0

                    // Pay and Overtime Pay Formula
                    if (hours <= 40.0) {
                        pay = hours * rate
                        overtimePay = 0.0
                    } else {
                        pay = 40.0 * rate
                        overtimePay = (hours - 40.0) * rate * 1.5
                    }

                    // Total Pay Formula
                    totalPay = pay + overtimePay

                    // Tax Formula
                    tax = pay * taxRate

                    isCalculated = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calculate Pay")
            }

            if (isCalculated) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Calculation Results",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text("Pay: $%.2f".format(pay))
                        Text("Overtime Pay: $%.2f".format(overtimePay))
                        Text("Total Pay: $%.2f".format(totalPay))
                        Text("Tax: $%.2f".format(tax))
                    }
                }
            }
        }
    }
}




