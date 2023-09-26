package com.tcreatesllc.mderiv.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tcreatesllc.mderiv.R
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeScreen() {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val screenWidth = LocalConfiguration.current.screenWidthDp

    val listItems = arrayOf("VR4257389", "CR3572933", "CR1123412", "CR3518444")
    val listItemsMarkets = arrayOf("Vol. 10(1s) Index", "Vol. 10 Index", "Vol. 25(1s) Index", "Vol. 25 Index", "AUD/JPY", "EUR/AUD")
    val contextForToast = LocalContext.current.applicationContext

    // state of the menu
    var isExpanded by remember {
        mutableStateOf(false)
    }

    // remember the selected item
    var selectedItem by remember {
        mutableStateOf(listItems[0])
    }

    var isExpandedMarkets by remember {
        mutableStateOf(false)
    }

    // remember the selected item
    var selectedItemMarkets by remember {
        mutableStateOf(listItemsMarkets[0])
    }

    //Log.i("oooo", ((screenHeight * 0.5).toString()))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = (screenWidth * 0.03).dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 5.dp, top = 5.dp)
        ) {

            /*          val txtTitleModsViewAll = Modifier
                          .padding(horizontal = (screenWidth * 0.05).dp)
          */
            Spacer(Modifier.weight(3f))
            // box
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = {
                    isExpanded = !isExpanded
                }
            ) {
                // text field
                OutlinedTextField(

                    value = selectedItem,
                    onValueChange = {},
                    textStyle = TextStyle(
                        fontFamily = mDerivDigitFamily,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    ),
                    readOnly = true,
                    label = { Text(text = "USD", fontFamily = mDerivDigitFamily) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = isExpanded
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.coin_vertical_svgrepo_com
                            ), ""
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(containerColor = Color.Transparent),
                    modifier = Modifier.menuAnchor()
                )
                // menu
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    listItems.forEach { selectedOption ->
                        // menu item
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = selectedOption,
                                    fontFamily = mDerivDigitFamily,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            onClick = {
                                selectedItem = selectedOption
                                isExpanded = false
                            })
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = (screenHeight * 0.0).dp, bottom = (screenHeight * 0.0).dp)

            //.height((screenHeight * 0.2).dp)
        ) {
            LazyColumn(
                userScrollEnabled = true,
                modifier = Modifier.height((screenHeight * 0.60).dp)
            ) {
                items(50) { index ->
                    Text(text = "Item: ${index + 1}")
                }
            }

        }
        Row(
            modifier = Modifier.align(Alignment.Start)

                .padding(bottom = 5.dp, top = 5.dp, start = (screenWidth * 0.05).dp, end = (screenWidth * 0.05).dp )
        ) {

            ExposedDropdownMenuBox(
                expanded = isExpandedMarkets,
                onExpandedChange = {
                    isExpandedMarkets = !isExpandedMarkets
                }
            ) {
                // text field
                OutlinedTextField(

                    value = selectedItemMarkets,
                    onValueChange = {},
                    textStyle = TextStyle(
                        fontFamily = mDerivDigitFamily,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start
                    ),
                    readOnly = true,
                    label = { Text(text = "Markets", fontFamily = mDerivDigitFamily) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = isExpandedMarkets
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.market_basket_svgrepo_com
                            ), ""
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(containerColor = Color.Transparent),
                    modifier = Modifier.menuAnchor()
                )
                // menu
                ExposedDropdownMenu(
                    expanded = isExpandedMarkets,
                    onDismissRequest = { isExpandedMarkets = false }
                ) {
                    listItemsMarkets.forEach { selectedOption ->
                        // menu item
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = selectedOption,
                                    fontFamily = mDerivDigitFamily,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            onClick = {
                                selectedItemMarkets = selectedOption
                                isExpandedMarkets = false
                            })
                    }
                }
            }
            Spacer(Modifier.weight(3f))
            ExtendedFloatingActionButton(
                onClick = { },
                shape = RectangleShape,
                icon = { Icon(
                    painter = painterResource(
                        id = R.drawable.graph_svgrepo_com
                    ), ""
                ) },
                text = { Text(text = "Trade", fontFamily = mDerivTextFamily, fontSize = 20.sp,) },
            )
        }
    }

}



/* val txtTitleMods = Modifier
     .align(Alignment.CenterHorizontally)
     .padding(top = 10.dp, bottom = 25.dp)

 val txtSubTitleMods = Modifier
     .align(Alignment.CenterHorizontally)
     .padding(bottom = 20.dp)

 val txtTitleModsStart = Modifier
     .align(Alignment.Start)
     .padding(horizontal = 20.dp)

 val txtTitleModsViewAll = Modifier
     .align(Alignment.CenterHorizontally)
     .padding(horizontal = 20.dp)

 val txtTitleCaption = Modifier
     .align(Alignment.Start)
     .padding(horizontal = 5.dp, vertical = 0.dp)

 val txtSubTitleCaptionModStart = Modifier
     .align(Alignment.Start)
     .padding(horizontal = 5.dp)

 val txtTitleCaptionModsStart = Modifier
     .align(Alignment.Start)
     .padding(horizontal = 5.dp)

 val txtSubTitleModStart = Modifier
     .align(Alignment.Start)
     .padding(horizontal = 20.dp)

 val walletTitleMods = Modifier
     .align(Alignment.CenterHorizontally)
     .padding(top = 0.dp)

 val walletSubTitleMods = Modifier
     .align(Alignment.CenterHorizontally)
     .padding(horizontal = 20.dp)*/
