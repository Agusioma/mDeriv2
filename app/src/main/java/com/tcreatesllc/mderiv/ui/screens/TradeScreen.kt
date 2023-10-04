package com.tcreatesllc.mderiv.ui.screens

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tcreatesllc.mderiv.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tcreatesllc.mderiv.ui.charts.ComposeChart1
import com.tcreatesllc.mderiv.viewmodels.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeScreen(viewModel: MainViewModel = viewModel()) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val screenWidth = LocalConfiguration.current.screenWidthDp

    val listItems = arrayOf("VR4257389", "CR3572933", "CR1123412", "CR3518444")
    val listItemsMarkets = arrayOf(
        "Vol. 10(1s) Index",
        "Vol. 10 Index",
        "Vol. 25(1s) Index",
        "Vol. 25 Index",
        "AUD/JPY",
        "EUR/AUD"
    )

    val listItemsMultipliers = arrayOf("×10", "×20", "×30", "×50", "×100")
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

    var isExpandedMultipliers by remember {
        mutableStateOf(false)
    }

    // remember the selected item
    var selectedItemMultipliers by remember {
        mutableStateOf(listItemsMultipliers[0])
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }


    var (spCheckedState, spOnStateChange) = remember { mutableStateOf(false) }
    var (slCheckedState, slOnStateChange) = remember { mutableStateOf(false) }

    //var stoplosscheckstate = remember { mutableStateOf(true) }
    //var

    //Log.i("oooo", ((screenHeight * 0.5).toString()))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = (screenWidth * 0.03).dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val txtTitleMods = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = (screenHeight * 0.00).dp, end = 20.dp)

        val txtSubTitleMods = Modifier
            .align(Alignment.Start)
            .padding(top = (screenHeight * 0.12).dp)

        val txtTitleModsStart = Modifier
            .align(Alignment.Start)
            .padding(horizontal = 5.dp)

        val txtTitleModsCenter = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(horizontal = 5.dp)

        val txtSubTitleModStart = Modifier
            .align(Alignment.Start)
            .padding(horizontal = 5.dp)
        Row(
            modifier = Modifier
                .padding(bottom = 5.dp, top = 5.dp)
        ) {

            TextSubTitleBold("Open Positions", txtSubTitleMods)
            /*          val txtTitleModsViewAll = Modifier
                          .padding(horizontal = (screenWidth * 0.05).dp)
          */
            Spacer(Modifier.weight(3f))

            TextTitleCaptionSmall("1,884.98 USD", txtTitleMods)
            // box
            ExposedDropdownMenuBox(
                modifier = Modifier.wrapContentSize(),
                expanded = isExpanded,
                onExpandedChange = {
                    isExpanded = !isExpanded
                }
            ) {
                // text field
                CompositionLocalProvider(
                    LocalTextInputService provides null
                ) {
                    OutlinedTextField(

                        value = selectedItem,
                        onValueChange = {},
                        textStyle = TextStyle(
                            fontFamily = mDerivDigitFamily,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        ),
                        readOnly = true,
                        label = { Text(text = "Account", fontFamily = mDerivDigitFamily) },
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
                        colors = ExposedDropdownMenuDefaults.textFieldColors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent
                        ),
                        modifier = Modifier.menuAnchor()
                    )
                }
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


            @Composable
            fun AccountsCard() {
                ElevatedCard(
                    shape = RectangleShape,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier
                        .padding(start = 0.dp, end = 0.dp, bottom = 5.dp, top = 5.dp)
                        .wrapContentWidth()
                ) {
                    @Composable
                    fun Balance(
                        caption1: String, amt1: String,
                        caption2: String, amt2: String
                    ) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(start = 0.dp, end = 0.dp, top = 2.dp, bottom = 2.dp)
                        ) {

                            Column() {
                                TextSubTitle(caption1, txtSubTitleModStart)
                                TextSubTitleBold(amt1, txtTitleModsStart)
                            }
                            Column() {
                                TextSubTitle(caption2, txtSubTitleModStart)
                                TextSubTitleBold(amt2, txtTitleModsStart)
                            }
                        }


                    }

                    Balance("Market", "Vol. 100(1s) index", "Option", "Rise(×100)")
                    Divider(
                        thickness = 1.dp,
                        color = Color.LightGray,
                        modifier = Modifier
                            .padding(
                                start = (screenWidth * 0.025).dp,
                                end = (screenWidth * 0.025).dp
                            )
                            .width((screenWidth * 0.2).dp)
                    )
                    Balance("Buy. Price", "2,911", "Indicative amt.", "23.5")


                    Balance("Profit/Loss", "+25.98", "Max. payout", "5,000")
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(start = 20.dp, end = 20.dp, top = 0.dp, bottom = 0.dp)
                    ) {
                        AssistChip(
                            onClick = { /* Do something! */ },
                            label = { Text("Close") }
                        )
                    }
                }
            }
            LazyColumn(
                userScrollEnabled = true,
                modifier = Modifier
                    .height((screenHeight * 0.60).dp)
                    .background(Color.Transparent)
                    .padding(start = 0.dp, end = 10.dp)
            ) {
                items(5) { index ->
                    AccountsCard()
                }
            }



            ComposeChart1(
                chartEntryModelProducer = viewModel.customStepChartEntryModelProducer,
                //mods = Modifier.align(Alignment.CenterHorizontally)
            )

        }
        Row(
            modifier = Modifier
                .align(Alignment.Start)

                .padding(
                    bottom = 5.dp,
                    top = 0.dp,
                    start = (screenWidth * 0.05).dp,
                    end = (screenWidth * 0.05).dp
                )
        ) {

            ExposedDropdownMenuBox(
                expanded = isExpandedMarkets,
                onExpandedChange = {
                    isExpandedMarkets = !isExpandedMarkets
                }
            ) {
                // text field
                CompositionLocalProvider(
                    LocalTextInputService provides null
                ) {
                    OutlinedTextField(

                        value = selectedItemMarkets,
                        onValueChange = {},
                        textStyle = TextStyle(
                            fontFamily = mDerivDigitFamily,
                            fontSize = 15.sp,
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
                        colors = ExposedDropdownMenuDefaults.textFieldColors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent
                        ),
                        modifier = Modifier.menuAnchor()
                    )
                }
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
                                    fontSize = 15.sp,
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
                onClick = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = true
                        }
                    }
                },
                shape = RectangleShape,
                icon = {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.graph_svgrepo_com
                        ), ""
                    )
                },
                text = { Text(text = "Trade", fontFamily = mDerivTextFamily, fontSize = 20.sp) },
            )
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((screenHeight*0.9).dp)
                    .padding(top = 0.dp),
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                // Sheet content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = (screenWidth * 0.03).dp),
                    //verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextTitleCaptionSmallBold(
                        "Multipliers(Up/Down)",
                        txtTitleMods.padding(top = 0.dp, bottom = 10.dp)
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.Start)

                            .padding(
                                bottom = 5.dp,
                                top = 0.dp,
                                start = 0.dp,
                                end = 0.dp
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .width((screenWidth * 0.3).dp)
                                .padding(horizontal = 10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            OutlinedTextField(
                                value = "",
                                placeholder = {
                                    Text(
                                        "0.0",
                                        fontFamily = mDerivDigitFamily,
                                        color = Color.LightGray
                                    )
                                },
                                onValueChange = { },
                                label = { Text("Stake", fontFamily = mDerivDigitFamily) }
                            )
                            ExposedDropdownMenuBox(
                                expanded = isExpandedMultipliers,
                                onExpandedChange = {
                                    isExpandedMultipliers = !isExpandedMultipliers
                                }
                            ) {
                                // text field
                                CompositionLocalProvider(
                                    LocalTextInputService provides null
                                ) {
                                    OutlinedTextField(

                                        value = selectedItemMultipliers,
                                        onValueChange = {},
                                        textStyle = TextStyle(
                                            fontFamily = mDerivDigitFamily,
                                            fontSize = 20.sp,
                                            textAlign = TextAlign.Center
                                        ),
                                        readOnly = true,
                                        label = {
                                            Text(
                                                text = "Multiplier",
                                                fontFamily = mDerivDigitFamily
                                            )
                                        },
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(
                                                expanded = isExpandedMultipliers
                                            )
                                        },
                                        colors = ExposedDropdownMenuDefaults.textFieldColors(
                                            unfocusedContainerColor = Color.Transparent,
                                            focusedContainerColor = Color.Transparent
                                        ),
                                        modifier = Modifier.menuAnchor()
                                    )
                                }
                                // menu
                                ExposedDropdownMenu(
                                    expanded = isExpandedMultipliers,
                                    onDismissRequest = { isExpandedMultipliers = false }
                                ) {
                                    listItemsMultipliers.forEach { selectedOption ->
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
                                                selectedItemMultipliers = selectedOption
                                                isExpandedMultipliers = false
                                            })
                                    }
                                }
                            }
                        }

                        Column(
                            modifier = Modifier
                                .width((screenWidth * 0.3).dp)
                                .padding(horizontal = 10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            TextSubTitle(
                                "Win 10% of your stake for every 1% rise in Volatility 100 (1s) Index.",
                                txtTitleModsCenter
                            )
                            Row(modifier = Modifier.width((screenWidth * 0.2).dp)) {
                                TextSubTitle(
                                    "Stop out:",
                                    txtTitleModsStart.padding(top = 0.dp, bottom = 0.dp)
                                )
                                TextSubTitleBold(
                                    "200 USD",
                                    txtTitleMods.padding(top = 0.dp, bottom = 0.dp)
                                )
                            }
                            Row(modifier = Modifier.width((screenWidth * 0.2).dp)) {
                                TextSubTitle(
                                    "Stop profit:",
                                    txtTitleModsStart.padding(top = 0.dp, bottom = 0.dp)
                                )
                                TextSubTitleBold(
                                    "2 USD",
                                    txtTitleMods.padding(top = 0.dp, bottom = 0.dp)
                                )
                            }
                            Row(modifier = Modifier.width((screenWidth * 0.2).dp)) {
                                TextSubTitle(
                                    "Stop loss:",
                                    txtTitleModsStart.padding(top = 0.dp, bottom = 0.dp)
                                )
                                TextSubTitleBold(
                                    "2 USD",
                                    txtTitleMods.padding(top = 0.dp, bottom = 0.dp)
                                )
                            }

                        }
                        Column(
                            modifier = Modifier

                                .width((screenWidth * 0.3).dp)
                                .padding(horizontal = 10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.End
                        ) {
                            Row(
                                Modifier
                                    .width((screenWidth * 0.2).dp)
                                    .toggleable(
                                        value = spCheckedState,
                                        onValueChange = { spOnStateChange(!spCheckedState) },
                                        role = Role.Checkbox
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = spCheckedState,
                                    onCheckedChange = null // null recommended for accessibility with screenreaders
                                )
                                Text(
                                    text = "Stop Profit",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontFamily = mDerivDigitFamily,
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .align(Alignment.CenterVertically)
                                )
                            }
                            if (spCheckedState) {
                                if (slCheckedState) {
                                    slCheckedState = false
                                }
                                TextField(
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedContainerColor = Color.Transparent
                                    ),
                                    value = "",
                                    onValueChange = { },
                                    placeholder = {
                                        Text(
                                            "Enter the S.P. amount",
                                            fontFamily = mDerivDigitFamily,
                                            color = Color.Gray
                                        )
                                    },
                                    modifier = Modifier.padding(bottom = 10.dp),
                                    singleLine = true
                                )
                            }

                            Row(
                                Modifier
                                    .width((screenWidth * 0.2).dp)
                                    .toggleable(
                                        value = slCheckedState,
                                        onValueChange = { slOnStateChange(!slCheckedState) },
                                        role = Role.Checkbox
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = slCheckedState,
                                    onCheckedChange = null // null recommended for accessibility with screenreaders
                                )
                                Text(
                                    text = "Stop Loss",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontFamily = mDerivDigitFamily,
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .align(Alignment.CenterVertically)
                                )
                            }
                            if (slCheckedState) {
                                if (spCheckedState) {
                                    spCheckedState = false
                                }
                                TextField(
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedContainerColor = Color.Transparent
                                    ),
                                    value = "",
                                    onValueChange = { },
                                    placeholder = {
                                        Text(
                                            "Enter the S.L. amount",
                                            fontFamily = mDerivDigitFamily,
                                            color = Color.Gray
                                        )
                                    },
                                    singleLine = true
                                )
                            }

                        }
                    }


                    Row(
                        modifier = Modifier
                            .align(Alignment.Start)

                            .padding(
                                bottom = 5.dp,
                                top = 10.dp,
                                start = (screenWidth * 0.1).dp,
                                end = (screenWidth * 0.1).dp
                            )
                    ) {

                        ExtendedFloatingActionButton(
                            onClick = {
                            },
                            shape = RectangleShape,
                            icon = {
                                Icon(
                                    painter = painterResource(
                                        id = R.drawable.down_trend_round_svgrepo_com
                                    ), ""
                                )
                            },
                            text = {
                                Text(
                                    text = "Down",
                                    fontFamily = mDerivTextFamily,
                                    fontSize = 20.sp
                                )
                            },
                        )
                        Spacer(Modifier.weight(3f))
                        ExtendedFloatingActionButton(
                            onClick = {
                            },
                            shape = RectangleShape,
                            icon = {
                                Icon(
                                    painter = painterResource(
                                        id = R.drawable.up_trend_round_svgrepo_com
                                    ), ""
                                )
                            },
                            text = {
                                Text(
                                    text = "Up",
                                    fontFamily = mDerivTextFamily,
                                    fontSize = 20.sp
                                )
                            },
                        )
                    }

                }

                //....
            }
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