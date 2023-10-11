package com.tcreatesllc.mderiv.ui.screens

import android.util.Log
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import com.google.gson.JsonParser
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
        mutableStateOf(mainViewModel.userLoginID.value)
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

    var btnCheckStatus by remember {
        mutableStateOf(false)
    }

    var btnTradeCheckStatus by remember {
        mutableStateOf(false)
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var showBottomSheet2 by remember { mutableStateOf(false) }

    selectedItemMarkets = mainViewModel.currentTradeSymbolKey.observeAsState().value
    var (spCheckedState, spOnStateChange) = remember { mutableStateOf(false) }
    var (slCheckedState, slOnStateChange) = remember { mutableStateOf(true) }

    var textStake by rememberSaveable { mutableStateOf("") }
    var textSL by rememberSaveable { mutableStateOf("0.10") }
    var textSP by rememberSaveable { mutableStateOf("0.10") }
    var textMul by rememberSaveable { mutableStateOf(listItemsMultipliers[0].substring(1)) }

    var dummyText by rememberSaveable { mutableStateOf("") }
    var dialogFired by rememberSaveable { mutableStateOf(false) }


    var listOpenPositions = mainViewModel.listOpenPositions.observeAsState()
    /*val recentTenOpenPositions by viewModel.recentTenPositions.collect{
        listOf(it)
    }

   // viewModel.getOpenPositions()*/


    val openDialog = remember { mutableStateOf(false) }
    var openDialogError = remember { mutableStateOf(false) }
    var openDialogInfoDummy = remember { mutableStateOf(false) }
    var openDialogInfoDummy2 = remember { mutableStateOf(false) }

    if (mainViewModel.openDialogError.observeAsState().value == true) {
        openDialogError.value = true
        openDialog.value = false
    }

    if (mainViewModel.btnCheckStatus.observeAsState().value == true) {
        btnCheckStatus = true
    } else {
        btnCheckStatus = false
    }

    /*if (mainViewModel.openDialog.observeAsState()
        openDialog.value = true
        //openDialog.value = false
    }*/


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

            TextSubTitleBold("Recent Positions", txtSubTitleMods)
            /*          val txtTitleModsViewAll = Modifier
                          .padding(horizontal = (screenWidth * 0.05).dp)
          */
            Spacer(Modifier.weight(3f))

            TextTitleCaptionSmallBold("${accBalance.value} ${accCurr.value}", txtTitleMods)
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
                                if (selectedItem == selectedOption.value) {

                                    Log.i("SAME?", "${selectedItem} ${selectedOption.value}")
                                } else {
                                    Log.i("SAME? NO", "${selectedItem} ${selectedOption.value}")
                                    //openDialogInfoDummy.value = true
                                }
                                selectedItem = selectedOption.value
                                Log.d(
                                    "CLICKED",
                                    selectedOption.value.substring(
                                        1,
                                        selectedOption.value.length - 1
                                    )
                                )
                                mainViewModel.userLoginID.value = selectedOption.value
                                mainViewModel.accTokenMapping.value?.keys

                                mainViewModel.getAuthTokenFromDB(JsonParser().parse(selectedOption.value).asString)
                                mainViewModel.refreshIt.value = true
                                mainViewModel.getRecentTenPos(mainViewModel.userLoginID.value.toString())
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
                                mainViewModel.currentTradeSymbol.value = clickedContractList.get(12)
                                mainViewModel.currentTradeSymbolKey.value =
                                    listItemsMarkets.entries.find {
                                        it.value == clickedContractList.get(12)
                                    }?.key

                                mainViewModel.clickedContractList.value = clickedContractList
                                mainViewModel.clickedContractThresholdMarker.value =
                                    clickedContractList.get(8).toFloat()
                                mainViewModel.clickedContractID.value = holderListContract[0]
                                showBottomSheet2 = true

                                mainViewModel.streamContract.value == "YES"
                                mainViewModel.stopIt.value = true
                                mainViewModel.subcribeIt.value = true

                                /*dummyText = clickedContractList.get(11).replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.ROOT
                                    ) else it.toString()
                                }*/


                            },
                            label = { Text("View details") }
                        )
                    }
                }
            }
            LazyColumn(
                userScrollEnabled = true,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .height((screenHeight * 0.60).dp)
                    .background(Color.Transparent)
                    .padding(start = 0.dp, end = 10.dp)
            ) {
                if (listOpenPositions.value?.isNotEmpty() == true) {
                    listOpenPositions.value?.forEach {
                        it.values.forEach {
                            item {

                                statementsCard(holderListContract = it)
                            }
                        }
                    }
                } else {
                    item {

                        TextSubTitle(
                            "The 10 recently traded\ncontracts will appear here.\nHappy trading!",
                            txtTitleModsCenter
                        )
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
                containerColor = colorResource(id = R.color.red),
                contentColor = colorResource(id = R.color.white),
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
                text = { Text(text = "Trade", fontFamily = mDerivDigitFamily, fontSize = 20.sp) },
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
                            containerColor = colorResource(id = R.color.red),
                            contentColor = colorResource(id = R.color.white),
                            onClick = {
                                mainViewModel.textOption.value = "MULTDOWN"

                                if (textSP !== null) {
                                    mainViewModel.textSP.value = textSP
                                } else {
                                    mainViewModel.textSP.value = "0.10"

                                }

                                if (textSL !== null) {
                                    mainViewModel.textSL.value = textSL
                                } else {
                                    mainViewModel.textSL.value = "0.10"
                                }
                                if (mainViewModel.textStake.value?.isNotEmpty() == true) {
                                    mainViewModel.tradeIt.value = true
                                    mainViewModel.stopIt.value = true
                                    showBottomSheet = false
                                    if (openDialogError.value == true) {
                                        openDialog.value = false
                                    } else {
                                        openDialog.value = true
                                    }
                                } else {
                                    openDialogInfoDummy.value = true
                                }


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
                                    fontFamily = mDerivDigitFamily,
                                    fontSize = 20.sp
                                )
                            },
                        )
                        Spacer(Modifier.weight(3f))
                        ExtendedFloatingActionButton(

                            containerColor = colorResource(id = R.color.green),
                            contentColor = colorResource(id = R.color.white),
                            onClick = {
                                mainViewModel.textOption.value = "MULTUP"
                                if (textSP !== null) {
                                    mainViewModel.textSP.value = textSP
                                } else {
                                    mainViewModel.textSP.value = "0.10"

                                }

                                if (textSL !== null) {
                                    mainViewModel.textSL.value = textSL
                                } else {
                                    mainViewModel.textSL.value = "0.10"
                                }
                                if (mainViewModel.textStake.value?.isNotEmpty() == true) {
                                    mainViewModel.tradeIt.value = true
                                    mainViewModel.stopIt.value = true
                                    showBottomSheet = false
                                    if (openDialogError.value == true) {
                                        openDialog.value = false
                                    } else {
                                        openDialog.value = true
                                    }
                                } else {
                                    openDialogInfoDummy.value = true
                                }

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
                                    fontFamily = mDerivDigitFamily,
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

                            TextSubTitleBold("Contract", txtTitleModsCenter)
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
                                mainViewModel.clickedContractList.value = holderListContract.value
                                mainViewModel.clickedContractThresholdMarker.value =
                                    holderListContract.value?.get(8)?.toFloat()
                                Log.i(
                                    "TTT",
                                    mainViewModel.clickedContractThresholdMarker.value.toString()
                                )
                                var indicativeAmt: MutableState<String> =
                                    remember { mutableStateOf("0") }
                                var pOl: MutableState<String> = remember { mutableStateOf("0") }
                                var statusOoC: MutableState<String> =
                                    remember { mutableStateOf("0") }
                                if (mainViewModel.boolFire.value == false) {
                                    indicativeAmt.value = holderListContract.value?.get(6) ?: ""
                                    pOl.value = holderListContract.value?.get(9) ?: ""
                                    statusOoC.value = holderListContract.value?.get(11) ?: ""
                                } else {
                                    indicativeAmt.value =
                                        mainViewModel.textIndicativeAmt.value.toString()
                                    pOl.value = mainViewModel.textProfitOrLoss.value.toString()
                                    statusOoC.value = mainViewModel.textStatus.value.toString()
                                }

                                //, , , ,, holderListContract[5]
                                ElevatedCard(
                                    shape = RectangleShape,
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = 0.dp
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
                                    mainViewModel.textIndicativeAmt.observeAsState().value?.let {
                                        Balance(
                                            "Buy. Price",
                                            holderListContract.value?.get(3) ?: "",
                                            "Indicative Amt.",
                                            it
                                        )
                                    }

                                    if (holderListContract.value?.get(11).toString() == "open") {

                                        mainViewModel.btnCheckStatus.value = true
                                        //Log.i("buy_ResponseF1TS", "${mainViewModel.btnCheckStatus.value} ${holderListContract.value?.get(11).toString()}")
                                    } else {
                                        mainViewModel.btnCheckStatus.value = false
                                        //Log.i("buy_ResponseF2TS", "${mainViewModel.btnCheckStatus.value} $")
                                    }

                                    mainViewModel.textProfitOrLoss.observeAsState().value?.let {
                                        mainViewModel.textStatus.observeAsState().value?.let { it1 ->
                                            Balance(
                                                "Profit/Loss",
                                                it,
                                                "Status",
                                                it1.replaceFirstChar {
                                                    if (it.isLowerCase()) it.titlecase(
                                                        Locale.ROOT
                                                    ) else it.toString()
                                                }
                                            )
                                        }
                                    }

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

                                        it.getValue(mainViewModel.clickedContractID.value).let {
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
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(id = R.color.green),
                                    contentColor = colorResource(id = R.color.white)
                                ),
                                onClick = {
                                    showBottomSheet2 = false
                                },
                                shape = RectangleShape,
                            ) {
                                Text(
                                    text = "Back",
                                    fontFamily = mDerivDigitFamily,
                                    fontSize = 20.sp
                                )
                            }
                            Spacer(Modifier.weight(3f))
                            Button(
                                enabled = btnCheckStatus,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(id = R.color.red),
                                    contentColor = colorResource(id = R.color.white)
                                ),
                                onClick = {
                                    mainViewModel.cancelIt.value = true
                                    dummyText = "Sold. Check its sell details on the left."
                                    openDialogInfoDummy2.value = true

                                },
                                shape = RectangleShape,
                            ) {
                                Text(
                                    text = "Close Trade",
                                    fontFamily = mDerivDigitFamily,
                                    fontSize = 20.sp
                                )
                            }
                        }

                    }
                }

                //....
            }
        }

        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onDismissRequest.
                    //openDialog.value = false
                }
            ) {
                Surface(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    shape = MaterialTheme.shapes.extraSmall,
                    tonalElevation = AlertDialogDefaults.TonalElevation
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextSubTitleBold("SUCCESS", txtTitleModsCenter)
                        Spacer(modifier = Modifier.height(15.dp))
                        TextSubTitle(
                            "Your trade has been placed. If successful, you can view the trade's realtime updates by clicking on View details on the left panel",
                            txtTitleModsCenter
                        )

                        /* Text(
                             textAlign = TextAlign.Center,
                             text = "This area typically contains the supportive text " +
                                     "which presents the details regarding the Dialog's purpose.",
                         )*/
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.green),
                                contentColor = colorResource(id = R.color.white)
                            ),
                            shape = RectangleShape,
                            onClick = {

                                openDialog.value = false
                            }
                        ) {
                            Text(
                                "OK, dismiss this",
                                fontFamily = mDerivDigitFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        if (openDialogError.value) {
            AlertDialog(
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onDismissRequest.
                    //openDialog.value = false
                }
            ) {
                Surface(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    shape = MaterialTheme.shapes.extraSmall,
                    tonalElevation = AlertDialogDefaults.TonalElevation
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextSubTitleBold("FAILED", txtTitleModsCenter)
                        Spacer(modifier = Modifier.height(15.dp))
                        mainViewModel.errorMessage.observeAsState().value?.let {
                            TextSubTitle(
                                it,
                                txtTitleModsCenter
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.green),
                                contentColor = colorResource(id = R.color.white)
                            ),
                            shape = RectangleShape,
                            onClick = {
                                openDialogError.value = false
                                mainViewModel.openDialogError.value = false

                            }
                        ) {
                            Text(
                                "OK, dismiss this",
                                fontFamily = mDerivDigitFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        if (openDialogInfoDummy.value) {
            AlertDialog(
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onDismissRequest.
                    //openDialog.value = false
                }
            ) {
                Surface(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    shape = MaterialTheme.shapes.extraSmall,
                    tonalElevation = AlertDialogDefaults.TonalElevation
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextSubTitleBold("FAILED", txtTitleModsCenter)
                        Spacer(modifier = Modifier.height(15.dp))
                        TextSubTitle(
                            "Please enter a stake amount",
                            txtTitleModsCenter
                        )

                        /* Text(
                             textAlign = TextAlign.Center,
                             text = "This area typically contains the supportive text " +
                                     "which presents the details regarding the Dialog's purpose.",
                         )*/
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.green),
                                contentColor = colorResource(id = R.color.white)
                            ),
                            shape = RectangleShape,
                            onClick = {
                                openDialogInfoDummy.value = false
                            }
                        ) {
                            Text(
                                "OK, dismiss this",
                                fontFamily = mDerivDigitFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        if (openDialogInfoDummy2.value) {
            AlertDialog(
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onDismissRequest.
                    //openDialog.value = false
                }
            ) {
                Surface(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    shape = MaterialTheme.shapes.extraSmall,
                    tonalElevation = AlertDialogDefaults.TonalElevation
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextSubTitleBold("INFO", txtTitleModsCenter)
                        Spacer(modifier = Modifier.height(15.dp))
                        TextSubTitle("This contract's status is ${dummyText}.", txtTitleModsCenter)

                        /* Text(
                             textAlign = TextAlign.Center,
                             text = "This area typically contains the supportive text " +
                                     "which presents the details regarding the Dialog's purpose.",
                         )*/
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.green),
                                contentColor = colorResource(id = R.color.white)
                            ),
                            shape = RectangleShape,
                            onClick = {
                                openDialogInfoDummy2.value = false
                            }
                        ) {
                            Text(
                                "OK, dismiss this",
                                fontFamily = mDerivDigitFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
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
