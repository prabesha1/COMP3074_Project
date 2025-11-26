package com.example.dinesmart.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun GlassSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search restaurants..."
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.2f),
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier.background(
                Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.25f),
                        Color.White.copy(alpha = 0.15f)
                    )
                )
            )
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(placeholder, color = Color.White.copy(alpha = 0.7f)) },
                leadingIcon = {
                    Icon(
                        Icons.Rounded.Search,
                        contentDescription = "Search",
                        tint = Color.White.copy(alpha = 0.8f)
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(
                                Icons.Rounded.Clear,
                                contentDescription = "Clear",
                                tint = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White.copy(alpha = 0.9f),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@Composable
fun FilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(20.dp),
        color = if (selected)
            Color.White.copy(alpha = 0.35f)
        else
            Color.White.copy(alpha = 0.15f),
        shadowElevation = if (selected) 8.dp else 2.dp
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (selected) {
                        Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.4f),
                                Color.White.copy(alpha = 0.2f)
                            )
                        )
                    } else {
                        Brush.linearGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.2f),
                                Color.White.copy(alpha = 0.1f)
                            )
                        )
                    }
                )
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selected) {
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(
                    label,
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = Color.White,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                    )
                )
            }
        }
    }
}

@Composable
fun RatingFilterBar(
    selectedRating: Int,
    onRatingSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    GlassCard(
        modifier = modifier,
        cornerRadius = 20.dp,
        glassAlpha = 0.18f
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Minimum Rating",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
                if (selectedRating > 0) {
                    TextButton(onClick = { onRatingSelected(0) }) {
                        Text(
                            "Clear",
                            color = Color.White.copy(alpha = 0.9f),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                (1..5).forEach { rating ->
                    Surface(
                        onClick = {
                            onRatingSelected(if (selectedRating == rating) 0 else rating)
                        },
                        modifier = Modifier.size(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = if (rating <= selectedRating && selectedRating > 0)
                            Color.White.copy(alpha = 0.35f)
                        else
                            Color.White.copy(alpha = 0.12f),
                        shadowElevation = if (rating <= selectedRating && selectedRating > 0) 6.dp else 0.dp
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "$rating⭐",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

