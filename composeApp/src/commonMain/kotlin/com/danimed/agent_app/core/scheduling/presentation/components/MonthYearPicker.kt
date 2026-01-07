package com.danimed.agent_app.core.scheduling.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.danimed.agent_app.core.scheduling.presentation.utils.DateFormatter
import com.danimed.agent_app.shared.theme.InterFontFamily
import com.danimed.agent_app.shared.theme.SplashBackground
import com.danimed.agent_app.shared.theme.White
import kotlinx.datetime.Month
import kotlinx.datetime.LocalDate

@Composable
fun MonthYearPicker(
    currentDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedMonth by remember { mutableStateOf(currentDate.month) }
    var selectedYear by remember { mutableStateOf(currentDate.year) }
    
    val years = remember {
        (2020..2030).toList()
    }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Seleccionar Mes y Año",
                        fontSize = 18.sp,
                        fontFamily = InterFontFamily(),
                        fontWeight = FontWeight.Bold,
                        color = SplashBackground
                    )
                    
                    IconButton(onClick = onDismiss) {
                        Text(
                            text = "✕",
                            fontSize = 20.sp,
                            color = SplashBackground,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Mes",
                            fontSize = 14.sp,
                            fontFamily = InterFontFamily(),
                            fontWeight = FontWeight.Bold,
                            color = SplashBackground,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        LazyColumn(
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                        ) {
                            items(kotlinx.datetime.Month.values().toList()) { month ->
                                MonthItem(
                                    month = month,
                                    isSelected = month == selectedMonth,
                                    onClick = { selectedMonth = month }
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Año",
                            fontSize = 14.sp,
                            fontFamily = InterFontFamily(),
                            fontWeight = FontWeight.Bold,
                            color = SplashBackground,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        LazyColumn(
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                        ) {
                            items(years) { year ->
                                YearItem(
                                    year = year,
                                    isSelected = year == selectedYear,
                                    onClick = { selectedYear = year }
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    androidx.compose.material3.TextButton(
                        onClick = {
                            val newDate = LocalDate(selectedYear, selectedMonth, 1)
                            onDateSelected(newDate)
                            onDismiss()
                        }
                    ) {
                        Text(
                            text = "Aceptar",
                            fontSize = 16.sp,
                            fontFamily = InterFontFamily(),
                            fontWeight = FontWeight.Bold,
                            color = SplashBackground
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthItem(
    month: Month,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(
                if (isSelected) Color(0xFFEFF4FF) else Color.Transparent
            )
            .padding(vertical = 5.dp, horizontal = 12.dp)
    ) {
        Text(
            text = DateFormatter.formatMonthSpanish(month),
            fontSize = 14.sp,
            fontFamily = InterFontFamily(),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) SplashBackground else Color(0xFF666666)
        )
    }
}

@Composable
private fun YearItem(
    year: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(
                if (isSelected) Color(0xFFEFF4FF) else Color.Transparent
            )
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Text(
            text = year.toString(),
            fontSize = 14.sp,
            fontFamily = InterFontFamily(),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) SplashBackground else Color(0xFF666666)
        )
    }
}











