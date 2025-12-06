package com.danimed.agent_app.core.scheduling.presentation.components

import agent_app.composeapp.generated.resources.Res
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danimed.agent_app.shared.theme.InterFontFamily
import com.danimed.agent_app.shared.theme.SplashBackground
import com.danimed.agent_app.shared.theme.White
import compose.icons.FeatherIcons
import compose.icons.feathericons.PlusSquare
import org.jetbrains.compose.resources.painterResource

@Composable
fun CalendarTopBar(
    onAddClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SplashBackground)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 16.dp)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Calendar",
            fontSize = 20.sp,
            fontFamily = InterFontFamily(),
            fontWeight = FontWeight.Bold,
            color = White
        )
        Box(
            modifier = Modifier.wrapContentSize(),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onAddClick,
                modifier = Modifier.align(Alignment.Center)
            ) {
                Icon(
                    painter = rememberVectorPainter(image = FeatherIcons.PlusSquare),
                    contentDescription = "Agendar",
                    tint = White,
                    modifier = Modifier.size(26.dp)
                )
            }

            Text(
                text = "Agendar",
                fontSize = 12.sp,
                fontFamily = InterFontFamily(),
                color = White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 8.dp)
            )
        }
    }
}

