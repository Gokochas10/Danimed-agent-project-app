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
import androidx.compose.foundation.Image
import agent_app.composeapp.generated.resources.Res
import agent_app.composeapp.generated.resources.doctor_icon
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.text.style.TextAlign
import org.jetbrains.compose.resources.painterResource
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import compose.icons.FeatherIcons
import compose.icons.feathericons.Activity
import compose.icons.feathericons.Calendar
import compose.icons.feathericons.Circle
import compose.icons.feathericons.Clock
import compose.icons.feathericons.Edit
import compose.icons.feathericons.Filter
import compose.icons.feathericons.Info
import compose.icons.feathericons.MapPin
import compose.icons.feathericons.User

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
    when (currentNavItem) {
        BottomNavItem.Calendar -> {
            CalendarScreen(
                currentNavItem = currentNavItem,
                onNavItemClick = onNavItemClick
            )
        }
        else -> {
            AgendaScreen(
                currentNavItem = currentNavItem,
                onNavItemClick = onNavItemClick
            )
        }
    }
}

@Composable
private fun AgendaScreen(
    currentNavItem: BottomNavItem = BottomNavItem.Agenda,
    onNavItemClick: (BottomNavItem) -> Unit = {}
) {
    SetStatusBarColor(PrimaryBlue)
    
    val agendaItems = listOf(
        AgendaItem(
            id = "1",
            title = "Juan Jose Fiallos",
            duration = "60 MIN",
            time = "9:30am - 10:30am",
            colorIndicator = Color(0xFF4FC3F7),
            startTime = "9:30am",
            endTime = "10:00am"
        ),
        AgendaItem(
            id = "2",
            title = "Silvana Diaz",
            duration = "60 MIN",
            time = "9:30am - 10:00am",
            colorIndicator = Color(0xFFFFEB3B),
            startTime = "9:30am",
            endTime = "10:00am"
        ),
         AgendaItem(
            id = "2",
            title = "Matias Gamboa",
            duration = "30 MIN",
            time = "9:30am - 10:00am",
            colorIndicator = Color(0xFFFFEB3B),
            startTime = "9:30am",
            endTime = "10:00am"
        ),
         AgendaItem(
            id = "2",
            title = "Lenin Herrera",
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
            text = "Lunes, 24 de Diciembre de 2025",
            fontSize = 15.sp,
            fontFamily = InterFontFamily(),
            fontWeight = FontWeight.Bold,
            color = White,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ProfileSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(Res.drawable.doctor_icon),
                contentDescription = null,
                modifier = Modifier.size(35.dp)
            )
            Text(
                text = "1805263782",
                fontSize = 8.sp,
                fontFamily = InterFontFamily(),
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                color = White
            )
        }
        
        Text(
            text = "Horario de Joshua",
            fontSize = 18.sp,
            fontFamily = InterFontFamily(),
            fontWeight = FontWeight.ExtraBold,
            color = White,
        )

        Button(
            onClick = { /* Acción del botón */ },
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .wrapContentSize()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = rememberVectorPainter(image = FeatherIcons.Filter),
                    contentDescription = "Filtrar",
                    tint = androidx.compose.ui.graphics.Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Filtrar",
                    fontSize = 10.sp,
                    fontFamily = InterFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = androidx.compose.ui.graphics.Color.White,

                )
            }
        }
    }
}

@Composable
private fun AgendaCard(item: AgendaItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEFF4FF)
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

                Icon(
                    painter = rememberVectorPainter(image = FeatherIcons.Activity),
                    contentDescription = "Estado",
                    tint = SplashBackground,
                    modifier = Modifier.size(20.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = rememberVectorPainter(image = FeatherIcons.Clock),
                            contentDescription = "Tiempo",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = item.duration,
                        fontSize = 12.sp,
                        fontFamily = InterFontFamily(),
                        color = Color(0xFF4CAF50),
                        textAlign = TextAlign.Center
                    )
                }

                Button(
                    onClick = { /* Acción */ },
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = FeatherIcons.Edit),
                        contentDescription = "Estado",
                        tint = SplashBackground,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = rememberVectorPainter(image = FeatherIcons.User),
                    contentDescription = "Paciente",
                    tint = SplashBackground,
                    modifier = Modifier.size(16.dp)
                )
                
                Spacer(modifier = Modifier.size(8.dp))
                
                Text(
                    text = item.title,
                    fontSize = 18.sp,
                    fontFamily = InterFontFamily(),
                    fontWeight = FontWeight.Bold,
                    color = SplashBackground
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = rememberVectorPainter(image = FeatherIcons.Calendar),
                    contentDescription = "Horario",
                    tint = SplashBackground,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = item.time,
                    fontSize = 14.sp,
                    fontFamily = InterFontFamily(),
                    color = SplashBackground
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = rememberVectorPainter(image = FeatherIcons.Info),
                    contentDescription = "Estado",
                    tint = Color(0xFFF44336),
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "Cancelada",
                    fontSize = 14.sp,
                    fontFamily = InterFontFamily(),
                    color = Color(0xFFF44336)
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

