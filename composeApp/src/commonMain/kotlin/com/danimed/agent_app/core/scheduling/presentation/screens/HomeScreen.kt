package com.danimed.agent_app.core.scheduling.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import com.danimed.agent_app.shared.components.BottomNavBar
import com.danimed.agent_app.shared.components.BottomNavItem
import com.danimed.agent_app.shared.theme.InterFontFamily
import com.danimed.agent_app.shared.theme.PrimaryBlue
import com.danimed.agent_app.shared.theme.SplashBackground
import com.danimed.agent_app.shared.theme.White
import com.danimed.agent_app.shared.utils.SetStatusBarColor

data class AgendaItem(
    val id: String,
    val title: String,
    val duration: String,
    val time: String,
    val colorIndicator: Color,
    val startTime: String,
    val endTime: String
)

@Composable
fun HomeScreen(
    currentNavItem: BottomNavItem = BottomNavItem.Agenda,
    onNavItemClick: (BottomNavItem) -> Unit = {}
) {
    SetStatusBarColor(PrimaryBlue)
    
    val agendaItems = listOf(
        AgendaItem(
            id = "1",
            title = "Product Demo 1",
            duration = "30 MIN",
            time = "9:30am - 10:00am",
            colorIndicator = Color(0xFF4FC3F7),
            startTime = "9:30am",
            endTime = "10:00am"
        ),
        AgendaItem(
            id = "2",
            title = "Product Demo 2",
            duration = "30 MIN",
            time = "9:30am - 10:00am",
            colorIndicator = Color(0xFFFFEB3B),
            startTime = "9:30am",
            endTime = "10:00am"
        ),
         AgendaItem(
            id = "2",
            title = "Product Demo 2",
            duration = "30 MIN",
            time = "9:30am - 10:00am",
            colorIndicator = Color(0xFFFFEB3B),
            startTime = "9:30am",
            endTime = "10:00am"
        ),
         AgendaItem(
            id = "2",
            title = "Product Demo 2",
            duration = "30 MIN",
            time = "9:30am - 10:00am",
            colorIndicator = Color(0xFFFFEB3B),
            startTime = "9:30am",
            endTime = "10:00am"
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SplashBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                ) {
                    TopBar()
                    
                    ProfileSection()
                }
            }
            
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    top = 8.dp,
                    bottom = 100.dp
                )
            ) {
                items(agendaItems) { item ->
                    AgendaCard(item = item)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
        
        BottomNavBar(
            currentRoute = currentNavItem,
            onItemClick = onNavItemClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp, bottom = 8.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Today's agenda",
            fontSize = 20.sp,
            fontFamily = InterFontFamily(),
            fontWeight = FontWeight.Bold,
            color = White
        )
        
        IconButton(onClick = {}) {
            Text(
                text = "+",
                fontSize = 24.sp,
                color = White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ProfileSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF2196F3))
        )
        
        Spacer(modifier = Modifier.size(12.dp))
        
        Text(
            text = "Greta's schedule",
            fontSize = 16.sp,
            fontFamily = InterFontFamily(),
            fontWeight = FontWeight.Medium,
            color = White
        )
    }
}

@Composable
private fun AgendaCard(item: AgendaItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2C2C2C)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = item.duration,
                    fontSize = 12.sp,
                    fontFamily = InterFontFamily(),
                    color = Color(0xFFB0B0B0)
                )
                
                IconButton(
                    onClick = {},
                    modifier = Modifier.size(24.dp)
                ) {
                    Text(
                        text = "⚙",
                        fontSize = 18.sp,
                        color = Color(0xFFB0B0B0)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(item.colorIndicator)
                )
                
                Spacer(modifier = Modifier.size(8.dp))
                
                Text(
                    text = item.title,
                    fontSize = 18.sp,
                    fontFamily = InterFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = White
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = item.time,
                fontSize = 14.sp,
                fontFamily = InterFontFamily(),
                color = Color(0xFFB0B0B0)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceEvenly
            ) {
                ActionButton(
                    text = "View booking",
                    icon = "▼",
                    onClick = {},
                    modifier = Modifier.weight(1f)
                )
                
                ActionButton(
                    text = "Share booking",
                    icon = "📤",
                    onClick = {},
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ActionButton(
    text: String,
    icon: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            Text(
                text = icon,
                fontSize = 16.sp,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = text,
                fontSize = 13.sp,
                fontFamily = InterFontFamily(),
                color = PrimaryBlue
            )
        }
    }
}

