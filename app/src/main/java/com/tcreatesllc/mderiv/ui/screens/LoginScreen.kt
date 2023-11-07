package com.tcreatesllc.mderiv.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
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
fun LoginScreen(mainViewModel: MainViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val screenWidth = LocalConfiguration.current.screenWidthDp


    //var listYou: List<TransactionDetails> = listOf(recentTenOpenPositions) as List<TransactionDetails>


    //var stoplosscheckstate = remember { mutableStateOf(true) }
    //var

    //Log.i("oooo", ((screenHeight * 0.5).toString()))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = (screenWidth * 0.03).dp),
        //verticalArrangement = Arrangement.Center,
        //horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val txtTitleMods = Modifier
            .align(Alignment.Start)
            .padding(top = (screenHeight * 0.00).dp, end = 20.dp)

        val txtSubTitleMods = Modifier
            .align(Alignment.Start)
            .padding(top = 5.dp)

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
                .padding(bottom = 5.dp, top = 5.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = (screenWidth * 0.03).dp,
                        vertical = (screenWidth * 0.05).dp
                    ),
                //verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextSubTitleBold("Dtraders", txtTitleModsCenter)
            }
        }
        Row(

            modifier = Modifier
                .padding(bottom = 5.dp, top = 5.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = (screenWidth * 0.03).dp,
                        vertical = (screenWidth * 0.15).dp
                    ),
                //verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                TextTitle("Login", txtTitleMods)
                TextSubTitle(
                    "Hello Super Trader.\nClick the button below to proceed with the login. It will take you to the Deriv website where once you login, you will be directed back to this app",
                    txtSubTitleMods
                )
            }
            /*          val txtTitleModsViewAll = Modifier
                          .padding(horizontal = (screenWidth * 0.05).dp)
          */
            //Spacer(Modifier.weight(3f))
        }
        Spacer(Modifier.height((screenWidth * 0.4).dp))
        Row(

            modifier = Modifier
                .padding(bottom = 5.dp, top = 5.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = (screenWidth * 0.03).dp,
                        //vertical = (screenWidth * 0.15).dp
                    ),
                //verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val context = LocalContext.current
                val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse("https://oauth.deriv.com/oauth2/authorize?app_id=38697")) }

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.red),
                        contentColor  = colorResource(id = R.color.white)),
                    onClick = {
                        context.startActivity(intent)
                    },
                    shape = CircleShape,
                ) {
                    Text(text = "Proceed", fontFamily = mDerivDigitFamily, fontSize = 20.sp)
                }
            }
            /*          val txtTitleModsViewAll = Modifier
                          .padding(horizontal = (screenWidth * 0.05).dp)
          */
            //Spacer(Modifier.weight(3f))
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
