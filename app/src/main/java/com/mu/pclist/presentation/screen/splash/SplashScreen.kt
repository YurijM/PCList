package com.mu.pclist.presentation.screen.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mu.pclist.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    toMain: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val angle = infiniteTransition.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing)
        )
    )
    val scale = remember {
        Animatable(0f)
    }
    /*val scalePC = remember {
        Animatable(0f)
    }*/
    val scaleAuthor = remember {
        Animatable(0f)
    }

    val developmentText = stringResource(R.string.design) + "\n"
    val companyText = stringResource(R.string.company)
    val text = buildAnnotatedString {
        append("$developmentText ")
        withStyle(
            style = SpanStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        ) {
            pushStringAnnotation(tag = companyText, annotation = companyText)
            append(companyText)
        }
    }

    LaunchedEffect(key1 = true) {
        /*scalePC.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 3000,
                easing = {
                    OvershootInterpolator(5f).getInterpolation(it)
                }
            )
        )*/
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 3000,
                easing = {
                    OvershootInterpolator(6f).getInterpolation(it)
                }
            )
        )
        scaleAuthor.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 2000,
                easing = {
                    OvershootInterpolator(10f).getInterpolation(it)
                }
            )
        )
        delay(1000L)
        toMain()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 28.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.pc),
                contentDescription = "Title",
                modifier = Modifier
                    .weight(1f)
                    /*.graphicsLayer {
                        rotationZ = angle.value
                    }*/
                    .scale(scale.value)
            )
            Text(
                text = stringResource(R.string.app_name),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 40.dp)
            )
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1.5f),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.author),
                    contentDescription = "Logo",
                    modifier = Modifier.scale(scaleAuthor.value)
                        .rotate(angle.value),
                )
                AssistChip(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .scale(scale.value),
                    onClick = {},
                    label = {
                        Text(text = text)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "settings"
                        )
                    }
                )
            }
        }
    }
}