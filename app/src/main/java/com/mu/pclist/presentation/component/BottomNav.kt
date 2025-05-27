package com.mu.pclist.presentation.component

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.mu.pclist.R
import com.mu.pclist.presentation.navigation.Destinations
import com.mu.pclist.presentation.util.BottomNavItem
import com.mu.pclist.presentation.util.YEAR_START

@SuppressLint("RestrictedApi")
@Composable
fun BottomNav(
    currentDestination: NavDestination?,
    currentYear: Int,
    onNavigate: (Destinations) -> Unit,
) {
    val years = YEAR_START.toString() +
            if (currentYear != YEAR_START) {
                "-$currentYear"
            } else {
                ""
            } + " ©"
    val navItems = listOf(
        BottomNavItem.OfficeListItem,
        BottomNavItem.UserListItem,
        BottomNavItem.PCListItem,
    )
    Column {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ) {
            navItems.forEach { item ->
                val title = stringResource(id = item.titleId)
                val isSelected =
                    currentDestination?.hierarchy?.any { it.hasRoute((item.destination::class).qualifiedName.toString(), null) } == true

                NavigationBarItem(
                    colors = NavigationBarItemDefaults.colors(
                        unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                        unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                        selectedTextColor = MaterialTheme.colorScheme.onSurface,
                        selectedIconColor = MaterialTheme.colorScheme.onSurface
                    ),
                    selected = isSelected,
                    onClick = { onNavigate(item.destination) },
                    icon = {
                        Icon(
                            painter = painterResource(id = item.iconId),
                            contentDescription = title
                        )
                    },
                    label = {
                        Text(text = title)
                    },
                    alwaysShowLabel = true
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(id = R.drawable.author),
                contentDescription = "author",
                modifier = Modifier.size(28.dp),
            )
            Text(
                text = years,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}