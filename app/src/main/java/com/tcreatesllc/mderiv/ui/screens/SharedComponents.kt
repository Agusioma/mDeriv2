package com.tcreatesllc.mderiv.ui.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.tcreatesllc.mderiv.R

@Composable
fun TextTitle(caption: String,
              mods: Modifier
){
    Text(
        text = caption,
        color = MaterialTheme.colorScheme.primary,
        fontFamily = mDerivTextFamily,
        modifier = mods,
        textAlign = TextAlign.Start,
        fontWeight = FontWeight.Bold,
        fontSize = 27.sp
    )
}

@Composable
fun TextTitleCaption(caption: String,
                     mods: Modifier
){
    Text(
        text = caption,
        color = MaterialTheme.colorScheme.primary,
        fontFamily = mDerivTextFamily,
        modifier = mods,
        textAlign = TextAlign.Start,
        fontSize = 27.sp
    )
}


@Composable
fun TextTitleCaptionSmall(caption: String,
                          mods: Modifier
){
    Text(
        text = caption,
        color = MaterialTheme.colorScheme.primary,
        fontFamily = mDerivTextFamily,
        modifier = mods,
        textAlign = TextAlign.Start,
        fontSize = 20.sp
    )
}

@Composable
fun TextTitleCaptionSmallBold(caption: String,
                              mods: Modifier
){
    Text(
        text = caption,
        color = MaterialTheme.colorScheme.primary,
        fontFamily = mDerivTextFamily,
        modifier = mods,
        textAlign = TextAlign.Start,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )
}

@Composable
fun TextSubTitle(caption: String,
                 mods: Modifier
) {
    Text(
        text = caption,
        color = MaterialTheme.colorScheme.primary,
        fontFamily = mDerivTextFamily,
        modifier = mods,
        textAlign = TextAlign.Start,
        fontSize = 15.sp
    )
}

val mDerivDigitFamily = FontFamily(

    Font(R.font.carlito_regular, FontWeight.Light),
    Font(R.font.carlito_regular, FontWeight.Normal),
    Font(R.font.carlito_italic, FontWeight.Normal, FontStyle.Italic),
    //Font(R.font.redhatdisplay_medium, FontWeight.Medium),
    Font(R.font.carlito_bolditalic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.carlito_bold, FontWeight.Bold)
)

val mDerivTextFamily = FontFamily(
    Font(R.font.redhatdisplay_light, FontWeight.Light),
    Font(R.font.redhatdisplay_regular, FontWeight.Normal),
    Font(R.font.redhatdisplay_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.redhatdisplay_medium, FontWeight.Medium),
    Font(R.font.redhatdisplay_bold, FontWeight.Bold)
)