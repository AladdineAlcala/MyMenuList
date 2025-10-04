package com.prac101.mymenulist_di.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.prac101.mymenulist_di.R

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
){
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp, bottom = 100.dp, start = 16.dp, end = 16.dp)
            .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(15.dp))
    ) {
        // Use a Box to easily layer the button on top of the ConstraintLayout
        Box(modifier = Modifier.fillMaxSize()) {

            // --- THIS IS THE NEW BACK BUTTON ---
            // It's placed inside the Box but outside the ConstraintLayout
            IconButton(
                onClick = onBack, // Use the passed-in navigation lambda
                modifier = Modifier
                    .padding(8.dp) // Add some padding from the edge
                    .align(Alignment.TopStart) // Align it to the top-left of the Box
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            // --- END OF NEW BACK BUTTON ---

            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (image, nameText, emailText, statsRow, btnFollow, btnMessage) = createRefs()
                Image(
                    painter = painterResource(id = R.drawable.profile123),
                    contentDescription = "profile",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            color = Color.Red,
                            shape = CircleShape
                        )
                        .constrainAs(image) {
                            top.linkTo(parent.top, margin = 32.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    contentScale = ContentScale.Crop
                )
                Text(text = "Aladdine Alcala", modifier = Modifier.constrainAs(nameText) {
                    top.linkTo(image.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
                Text(text = "Email : aladdinealcala@yahoo.com", modifier = Modifier.constrainAs(emailText) {
                    top.linkTo(nameText.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .constrainAs(statsRow) {
                            top.linkTo(emailText.bottom)
                        }
                ) {
                    CreateProfileStats("Followers", statsCount = 100)
                    CreateProfileStats("Following", statsCount = 110)
                    CreateProfileStats("Posts", statsCount = 200)
                }
                createHorizontalChain(btnFollow, btnMessage, chainStyle = ChainStyle.Spread)
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.constrainAs(btnFollow) {
                        top.linkTo(statsRow.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(btnMessage.start)
                    }
                ) {
                    Text(text = "Follow user")
                }
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.constrainAs(btnMessage) {
                        top.linkTo(btnFollow.top)
                        start.linkTo(btnFollow.end)
                        end.linkTo(parent.end)
                    }
                ) {
                    Text(text = "Message")
                }
            }
        }
    }
}
@Composable
fun CreateProfileStats(profileText: String,statsCount:Int){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = statsCount.toString(), fontWeight = FontWeight.Bold)
        Text(text = profileText)
    }
}