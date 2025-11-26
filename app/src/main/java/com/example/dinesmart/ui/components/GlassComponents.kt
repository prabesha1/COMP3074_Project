package com.example.dinesmart.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * iOS 18-inspired Glass Card with blur and translucent effects
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    cornerRadius: Dp = 24.dp,
    glassAlpha: Float = 0.15f,
    content: @Composable ColumnScope.() -> Unit
) {
    val cardModifier = if (onClick != null) {
        modifier.then(Modifier)
    } else {
        modifier
    }

    Surface(
        modifier = cardModifier
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(cornerRadius),
                spotColor = Color.Black.copy(alpha = 0.15f),
                ambientColor = Color.Black.copy(alpha = 0.1f)
            ),
        onClick = onClick ?: {},
        enabled = onClick != null,
        shape = RoundedCornerShape(cornerRadius),
        color = Color.White.copy(alpha = glassAlpha),
        tonalElevation = 0.dp
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = glassAlpha + 0.05f),
                            Color.White.copy(alpha = glassAlpha - 0.05f)
                        )
                    )
                )
        ) {
            Column(content = content)
        }
    }
}

/**
 * Glass Button with gradient and blur effects
 */
@Composable
fun GlassButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    isPrimary: Boolean = false,
    enabled: Boolean = true
) {
    val backgroundColor = if (isPrimary) {
        Color.White.copy(alpha = 0.3f)
    } else {
        Color.White.copy(alpha = 0.15f)
    }

    Surface(
        onClick = onClick,
        modifier = modifier
            .height(56.dp),
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        shadowElevation = if (enabled) 8.dp else 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = if (isPrimary) 0.35f else 0.2f),
                            Color.White.copy(alpha = if (isPrimary) 0.15f else 0.05f)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                icon?.let {
                    Icon(
                        it,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text,
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

/**
 * Glass Input Field with frosted effect
 */
@Composable
fun GlassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isError: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
    leadingIcon: ImageVector? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White.copy(alpha = 0.9f)) },
        placeholder = { Text(placeholder, color = Color.White.copy(alpha = 0.6f)) },
        modifier = modifier,
        singleLine = singleLine,
        minLines = minLines,
        isError = isError,
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    it,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.8f)
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White.copy(alpha = 0.9f),
            focusedContainerColor = Color.White.copy(alpha = 0.12f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.08f),
            disabledContainerColor = Color.White.copy(alpha = 0.05f),
            focusedBorderColor = Color.White.copy(alpha = 0.4f),
            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
            cursorColor = Color.White,
            errorBorderColor = Color(0xFFFF3B30).copy(alpha = 0.6f),
            errorContainerColor = Color(0xFFFF3B30).copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(16.dp)
    )
}

/**
 * Animated floating gradient orb for background
 */
@Composable
fun FloatingGradientOrb(
    color: Color,
    size: Dp,
    initialOffsetX: Dp = 0.dp,
    initialOffsetY: Dp = 0.dp,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "orb")
    val offsetX by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetX"
    )
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetY"
    )

    Box(
        modifier = modifier
            .offset(x = initialOffsetX + offsetX.dp, y = initialOffsetY + offsetY.dp)
            .size(size)
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.3f))
            .blur(100.dp)
    )
}

/**
 * Glass stat card for displaying metrics
 */
@Composable
fun GlassStatCard(
    value: String,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        cornerRadius = 20.dp,
        glassAlpha = 0.2f
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.size(28.dp)
            )
            Text(
                value,
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                label,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White.copy(alpha = 0.8f)
                )
            )
        }
    }
}

/**
 * Glass top bar with blur effect
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlassTopBar(
    title: String,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White.copy(alpha = 0.85f),
        shadowElevation = 4.dp
    ) {
        Box(
            modifier = Modifier.background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.95f),
                        Color.White.copy(alpha = 0.85f)
                    )
                )
            )
        ) {
            TopAppBar(
                title = {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    onBackClick?.let {
                        IconButton(onClick = it) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                },
                actions = actions,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    }
}

