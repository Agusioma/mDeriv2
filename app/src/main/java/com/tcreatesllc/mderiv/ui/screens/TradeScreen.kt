package com.tcreatesllc.mderiv.ui.screens

import android.util.Log
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tcreatesllc.mderiv.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tcreatesllc.mderiv.ui.AppViewModelProvider
import com.tcreatesllc.mderiv.ui.charts.ComposeChart1
import com.tcreatesllc.mderiv.viewmodels.MainViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.MutableLiveData
import com.tcreatesllc.mderiv.ui.charts.ComposeChart2
import java.lang.Exception
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeScreen(mainViewModel: MainViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val screenWidth = LocalConfiguration.current.screenWidthDp

    var accBalance = mainViewModel.accountBalance.observeAsState(0.0f)
    var accCurr = mainViewModel.accountCurr.observeAsState("EUR")
    var accList = mainViewModel.accountList.observeAsState()

    //var listAccReady = viewModel.listAccsReady.observeAsState("NO")

    var listItems: MutableMap<String, String> = mutableMapOf(Pair("Account", "oo"))
    val listItemsMarkets: Map<String, String> = mapOf(
        Pair("AUD/JPY", "frxAUDJPY"),
        Pair("AUD/USD", "frxAUDUSD"),
        Pair("EUR/AUD", "frxEURAUD"),
        Pair("EUR/CHF", "frxEURCHF"),
        Pair("EUR/GBP", "frxEURGBP"),
        Pair("EUR/JPY", "frxEURJPY"),
        Pair("EUR/USD", "frxEURUSD"),
        Pair("GBP/AUD", "frxGBPAUD"),
        Pair("GBP/JPY", "frxGBPJPY"),
        Pair("GBP/USD", "frxGBPUSD"),
        Pair("USD/CAD", "frxUSDCAD"),
        Pair("USD/CHF", "frxUSDCHF"),
        Pair("USD/JPY", "frxUSDJPY"),
        Pair("Volatility 10 Index", "R_10"),
        Pair("Volatility 10 (1s) Index", "1HZ10V"),
        Pair("Volatility 25 Index", "R_25"),
        Pair("Volatility 25 (1s) Index", "1HZ25V"),
        Pair("Volatility 50 Index", "R_50"),
        Pair("Volatility 50 (1s) Index", "1HZ50V"),
        Pair("Volatility 75 Index", "R_75"),
        Pair("Volatility 75 (1s) Index", "1HZ75V"),
        Pair("Volatility 100 Index", "R_100"),
        Pair("Volatility 100 (1s) Index", "1HZ100V"),
        Pair("Boom 1000 Index", "BOOM1000"),
        Pair("Boom 500 Index", "BOOM500"),
        Pair("Crash 1000 Index", "CRASH1000"),
        Pair("Crash 500 Index", "CRASH500"),
        Pair("Jump 10 Index", "JD10"),
        Pair("Jump 25 Index", "JD25"),
        Pair("Jump 50 Index", "JD50"),
        Pair("Jump 75 Index", "JD75"),
        Pair("Jump 100 Index", "JD100"),
        Pair("Step Index", "stpRNG"),
        Pair("Gold Basket", "WLDXAU"),
        Pair("AUD Basket", "WLDAUD"),
        Pair("EUR Basket", "WLDEUR"),
        Pair("GBP Basket", "WLDGBP"),
        Pair("USD Basket", "WLDUSD"),
        Pair("BTC/USD", "cryBTCUSD"),
        Pair("ETH/USD", "cryETHUSD"),
    )

    accList.value?.forEach { it ->
        var accno = it.get("loginid")
        var label = ""
        if (it.get("is_virtual") == "0") {
            label = "REAL"
        } else {
            label = "DEMO"
        }

        if (accno != null) {
            listItems.put("${accno.substring(1, accno.length - 1)} (${label})", "${accno}")
        }
        listItems.remove("Account")

    }

    Log.d("oooo", listItems.keys.toString())
    //var accBalance: Mu
    // = viewModel.accountBalance.value
    //val listItems = arrayOf("VR4257389", "CR3572933", "CR1123412", "CR3518444")


    val listItemsMultipliers = arrayOf(
        "×1",
        "×2",
        "×3",
        "×4",
        "×5",
        "×10",
        "×20",
        "×30",
        "×40",
        "×50",
        "×60",
        "×75",
        "×100",
        "×150",
        "×200",
        "×300",
        "×400",
        "×500",
        "×1000",
        "×1500",
        "×2500",
        "×5000"

    )
    val contextForToast = LocalContext.current.applicationContext

    // state of the menu
    var isExpanded by remember {
        mutableStateOf(false)
    }

    // remember the selected item
    var selectedItem by remember {
        mutableStateOf("CR5937830")
    }

    var isExpandedMarkets by remember {
        mutableStateOf(false)
    }

    // remember the selected item
    var selectedItemMarkets by remember {
        mutableStateOf(mainViewModel.currentTradeSymbolKey.value)
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
    var showBottomSheet2 by remember { mutableStateOf(false) }


    var (spCheckedState, spOnStateChange) = remember { mutableStateOf(false) }
    var (slCheckedState, slOnStateChange) = remember { mutableStateOf(true) }

    var textStake by rememberSaveable { mutableStateOf("") }
    var textSL by rememberSaveable { mutableStateOf("0.10") }
    var textSP by rememberSaveable { mutableStateOf("0.10") }
    var textMul by rememberSaveable { mutableStateOf(listItemsMultipliers[0].substring(1)) }

    var listOpenPositions = mainViewModel.listOpenPositions.observeAsState()
    /*val recentTenOpenPositions by viewModel.recentTenPositions.collect{
        listOf(it)
    }

   // viewModel.getOpenPositions()*/



    val openDialog = remember { mutableStateOf(false) }


    //var listYou: List<TransactionDetails> = listOf(recentTenOpenPositions) as List<TransactionDetails>


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

            TextTitleCaptionSmall("${accBalance.value} ${accCurr.value}", txtTitleMods)
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
                    selectedItem?.let {
                        OutlinedTextField(

                            value = it.substring(1, it.length - 1),
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
                                    text = selectedOption.key,
                                    fontFamily = mDerivDigitFamily,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            onClick = {
                                selectedItem = selectedOption.value
                                Log.d(
                                    "CLICKED",
                                    selectedOption.value.substring(
                                        1,
                                        selectedOption.value.length - 1
                                    )
                                )

                                mainViewModel.userLoginID.value = selectedOption.value.substring(
                                    1,
                                    selectedOption.value.length - 1
                                )
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
            fun statementsCard(
                holderListContract: List<String>
            ) {
                //, , , ,, holderListContract[5]
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




                    Balance("Market", holderListContract[1], "Option", holderListContract[2])
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
                    Balance(
                        "Buy. Price",
                        holderListContract[3],
                        "Multiplier",
                        holderListContract[7]
                    )


                    Balance(
                        "Take Profit",
                        holderListContract[4],
                        "Stop Loss",
                        holderListContract[5]
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(start = 20.dp, end = 20.dp, top = 0.dp, bottom = 0.dp)
                    ) {
                        AssistChip(
                            onClick = {
                                var clickedContractList: MutableList<String> = mutableListOf()
                                for (e in holderListContract.withIndex()) {
                                    clickedContractList.add(e.index, e.value)
                                }
                                mainViewModel.clickedContractList.value = clickedContractList
                                mainViewModel.clickedContractID.value = holderListContract[0]

                                showBottomSheet2 = true

                                mainViewModel.streamContract.value == "YES"
                                mainViewModel.subcribeIt.value = true

                            },
                            label = { Text("View details") }
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

                listOpenPositions.value?.forEach {
                    it.values.forEach {
                        item {

                            statementsCard(holderListContract = it)
                        }
                    }
                }
            }

            ComposeChart1(
                chartEntryModelProducer = mainViewModel.customStepChartEntryModelProducer,
                mainViewModel
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
                    selectedItemMarkets?.let {
                        OutlinedTextField(

                            value = it,
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
                                    text = selectedOption.key,
                                    fontFamily = mDerivDigitFamily,
                                    fontSize = 15.sp,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            onClick = {
                                selectedItemMarkets = selectedOption.key
                                mainViewModel.currentTradeSymbol.value = selectedOption.value
                                mainViewModel.currentTradeSymbolKey.value = selectedOption.key
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
                    .height((screenHeight * 0.97).dp)
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
                                value = textStake,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                maxLines = 1,
                                onValueChange = {
                                    textStake = it
                                    mainViewModel.textStake.value = textStake
                                },
                                placeholder = {
                                    Text(
                                        "0.0",
                                        fontFamily = mDerivDigitFamily,
                                        color = Color.LightGray
                                    )
                                },
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
                                                textMul = selectedOption.substring(1)

                                                mainViewModel.textMul.value = textMul
                                                //Log.d("SMI", selectedOption.substring(1))
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
                                "Win ${textMul.toInt()}% of your stake for every 1% rise in ${mainViewModel.currentTradeSymbolKey.value}.",
                                txtTitleModsCenter
                            )
                            Row(modifier = Modifier.width((screenWidth * 0.2).dp)) {
                                TextSubTitle(
                                    "Stop profit:",
                                    txtTitleModsStart.padding(top = 0.dp, bottom = 0.dp)
                                )
                                TextSubTitleBold(
                                    "$textSP",
                                    txtTitleMods.padding(top = 0.dp, bottom = 0.dp)
                                )
                            }
                            Row(modifier = Modifier.width((screenWidth * 0.2).dp)) {
                                TextSubTitle(
                                    "Stop loss:",
                                    txtTitleModsStart.padding(top = 0.dp, bottom = 0.dp)
                                )
                                TextSubTitleBold(
                                    "$textSL",
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
                                    text = "Take Profit",
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
                                    value = textSP,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    maxLines = 1,
                                    onValueChange = {
                                        textSP = it
                                        mainViewModel.textSP.value = textSP
                                    },
                                    placeholder = {
                                        Text(
                                            "Enter the T.P. amount",
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
                                    value = textSL,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    maxLines = 1,
                                    onValueChange = {
                                        textSL = it
                                        mainViewModel.textSL.value = textSL
                                    },
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
                                mainViewModel.textOption.value = "MULTDOWN"

                                if (textSP == "") {
                                    mainViewModel.textSP.value = "0.0"
                                } else {
                                    mainViewModel.textSP.value = textSP
                                }

                                if (textSL == "") {
                                    mainViewModel.textSL.value = "0.0"
                                } else {
                                    mainViewModel.textSL.value = textSL
                                }

                                mainViewModel.tradeIt.value = true
                                showBottomSheet = false
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
                                mainViewModel.textOption.value = "MULTUP"
                                if (textSP == "") {
                                    mainViewModel.textSP.value = "0.10"
                                } else {
                                    mainViewModel.textSP.value = textSP
                                }

                                if (textSL == "") {
                                    mainViewModel.textSL.value = "0.10"
                                } else {
                                    mainViewModel.textSL.value = textSL
                                }

                                mainViewModel.tradeIt.value = true
                                showBottomSheet = false
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
        if (showBottomSheet2) {
            Log.i("kkk", mainViewModel.clickedContractID.value.toString())
            ModalBottomSheet(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 0.dp),
                onDismissRequest = {
                    showBottomSheet2 = false
                },
                sheetState = sheetState
            ) {
                // Sheet content
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    shape = MaterialTheme.shapes.large,
                    tonalElevation = AlertDialogDefaults.TonalElevation
                ) {
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

                            TextSubTitleBold("Open Positions", txtTitleModsCenter)
                            /*          val txtTitleModsViewAll = Modifier
                                          .padding(horizontal = (screenWidth * 0.05).dp)
                          */
                        }
                        Row(
                            modifier = Modifier
                                .width((screenWidth * 0.85).dp)
                                .padding(
                                    top = (screenHeight * 0.0).dp,
                                    bottom = (screenHeight * 0.0).dp
                                )

                            //.height((screenHeight * 0.2).dp)
                        ) {


                            @Composable
                            fun statementsCard(
                                holderListContract: MutableLiveData<List<String>>
                            ) {


                                //, , , ,, holderListContract[5]
                                ElevatedCard(
                                    shape = RectangleShape,
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 6.dp
                                    ),
                                    modifier = Modifier
                                        .padding(
                                            start = 0.dp,
                                            end = 0.dp,
                                            bottom = 5.dp,
                                            top = 5.dp
                                        )
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
                                                .padding(
                                                    start = 0.dp,
                                                    end = 0.dp,
                                                    top = 2.dp,
                                                    bottom = 2.dp
                                                )
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




                                    Balance(
                                        "Market",
                                        holderListContract.value?.get(1) ?: "",
                                        "Option",
                                        holderListContract.value?.get(2) ?: ""
                                    )
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
                                    Balance(
                                        "Buy. Price",
                                        holderListContract.value?.get(3) ?: "",
                                        "Multiplier",
                                        holderListContract.value?.get(7) ?: ""
                                    )

                                    Balance(
                                        "Profit/Loss",
                                        holderListContract.value?.get(9)?.replaceFirstChar {
                                            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                                        }
                                            ?: "",
                                        "Status",
                                        holderListContract.value?.get(11) ?: ""
                                    )

                                    Balance(
                                        "Take Profit",
                                        holderListContract.value?.get(4) ?: "",
                                        "Stop Loss",
                                        holderListContract.value?.get(5) ?: ""
                                    )

                                    /*
                                     e.value.contractID,0
                                        e.value.marketName,1
                                        e.value.contractType,2
                                        e.value.buyPrice,3
                                        e.value.stopLoss,4
                                        e.value.takeProfit,5
                                        e.value.indicativeAmt,6
                                        e.value.multiplierChosen,7
                                        e.value.entrySpot,8
                                        e.value.profitOrLoss,9
                                        e.value.tickSpotEntryTime,10
                                        e.value.contractStatusOpenOrClosed,11
                                        e.value.symbolName12
                                     */
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .height((screenHeight * 0.60).dp)
                                    .background(Color.Transparent)
                                    .padding(start = 0.dp, end = 10.dp)
                            ) {

                                listOpenPositions.value?.forEach {
                                   try {

                                            it.getValue(mainViewModel.clickedContractID.value).let{
                                                mainViewModel.clickedContractDetails.value = it
                                            }
                                            //Log.i("TEEEST", it.getValue("220437164568").toString())

                                   } catch (e: Exception) {

                                   }

                                    // it.getValue()

                                }


                                    statementsCard(
                                        mainViewModel.clickedContractDetails
                                    )

                            }

                            ComposeChart2(
                                chartEntryModelProducer = mainViewModel.customStepChartEntryModelProducer,
                                mainViewModel
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
                            Button(
                                onClick = {
                                    showBottomSheet2 = false
                                },
                                shape = RectangleShape,
                            ) {
                                Text(text = "Back", fontFamily = mDerivTextFamily, fontSize = 20.sp)
                            }
                            Spacer(Modifier.weight(3f))
                            Button(
                                onClick = {
                                },
                                shape = RectangleShape,
                            ) {
                                Text(
                                    text = "Close Trade",
                                    fontFamily = mDerivTextFamily,
                                    fontSize = 20.sp
                                )
                            }
                        }

                    }
                }

                //....
            }
        }

        /* if (openDialog.value) {
             AlertDialog(
                 onDismissRequest = {

                     // Dismiss the dialog when the user clicks outside the dialog or on the back
                     // button. If you want to disable that functionality, simply use an empty
                     // onDismissRequest.
                     openDialog.value = false
                 }
             ) {

             }
         }*/

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
