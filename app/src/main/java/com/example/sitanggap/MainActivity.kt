package com.example.sitanggap

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.sitanggap.ui.theme.SitanggapTheme
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sitanggap.ui.theme.DarkBlue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SitanggapTheme {
                MainActivityContent()
            }
        }
    }
}


enum class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    BERANDA("beranda", "beranda", Icons.Default.Home, "Beranda"),
    PENGADUAN("pengaduan", "pengaduan", Icons.Default.Face, "Pengaduan"),
    SARAN("saran", "saran", Icons.Default.MailOutline, "Profile"),
    PROFILE("profile", "profile", Icons.Default.Person, "Profile")
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MainActivityContent() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val startDestination = Destination.BERANDA
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }
    val currentDestination = Destination.entries[selectedDestination].route
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBlue,
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = { /* TODO: aksi tombol kiri */ }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Icon",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: aksi notifikasi */ }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifikasi",
                            tint = Color.White
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            when (currentDestination) {
                Destination.PENGADUAN.route -> {
                    FloatingActionButtonAdd(onClick = {
                        val intent = Intent(context, AddPengaduanAcitivity::class.java)
                        context.startActivity(intent)
                    })
                }

                Destination.SARAN.route -> {
                    FloatingActionButtonAdd(onClick = {
                        val intent = Intent(context, AddSaranActivity::class.java)
                        context.startActivity(intent)
                    })
                }
            }
        },
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                Destination.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selectedDestination == index,
                        onClick = {
                            navController.navigate(route = destination.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                            selectedDestination = index
                        },
                        icon = {
                            Icon(
                                destination.icon,
                                contentDescription = destination.contentDescription
                            )
                        },
                        label = { Text(destination.label) }
                    )
                }
            }
        }) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = startDestination.route,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            composable(Destination.BERANDA.route) { BerandaScreen() }
            composable(Destination.PENGADUAN.route) {
                WelcomeMessage()
            }
            composable(Destination.SARAN.route) {
                SaranScreen()
            }
            composable(Destination.PROFILE.route) {
                DescriptionSection()
            }

        }


    }
}

@Composable
fun Layout(modifier: Modifier, children: @Composable () -> Unit) {
    Column(
        modifier = modifier
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        children()
    }
}

@Composable
fun BerandaScreen() {
    Layout(modifier = Modifier) {
        WelcomeMessage()
        DescriptionSection()
        MemberSection(
            title = "Anggota Kelompok",
            members = listOf(
                Pair("Laila Qadriyah", "2111522022"),
                Pair("Alya Zurlhanifa", "2111522028"),
                Pair("Varissa Anzani Badri", "2111522036"),
            )
        )
    }
}

@Composable
fun WelcomeMessage() {
    Text(
        text = "Selamat Datang",
        modifier = Modifier
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontSize = 40.sp,
        fontWeight = FontWeight.SemiBold,
        color = DarkBlue,
        fontFamily = FontFamily.Serif
    )
}

@Composable
fun SectionHeaderText(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
    )
}

@Composable
fun SectionCard(
    title: String,
    children: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .dropShadow(
                shape = RoundedCornerShape(20.dp),
                shadow = Shadow(
                    radius = 4.dp,
                    spread = 2.dp,
                    color = Color(0x40000000),
                    offset = DpOffset(x = 0.dp, 5.dp)
                )
            ),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkBlue.copy(alpha = 0.45f),
        ),

        ) {
        Column(modifier = Modifier.padding(10.dp, 20.dp)) {
            SectionHeaderText(title)
            children()
        }
    }
}


@Composable
fun DescriptionSection() {
    SectionCard(title = "SITanggap") {
        Text(
            text = "SITanggap adalah aplikasi Android yang memudahkan warga melaporkan kerusakan fasilitas publik dan menyampaikan aspirasi dengan foto serta lokasi GPS, guna mendukung kolaborasi warga dan pemerintah menuju kota berkelanjutan..",
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier.padding(24.dp),
            textAlign = TextAlign.Center
        )

    }
}

@Composable
fun MemberSection(
    title: String,
    members: List<Pair<String, String>>
) {
    SectionCard(title = title) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            for (member in members) {
                MemberCard(name = member.first, nim = member.second, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun MemberCard(
    name: String,
    nim: String,
    modifier: Modifier
) {
    val painter = painterResource(id = R.drawable.ic_launcher_foreground)
    Box(
        contentAlignment = Alignment.Center, modifier = modifier

    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0f),
            ),
        ) {
            Card(shape = CircleShape, modifier = Modifier.padding(20.dp)) {
                Image(

                    modifier = Modifier
                        .background(color = DarkBlue)
                        .clip(CircleShape),
                    painter = painter,
                    alpha = DefaultAlpha,
                    alignment = Alignment.Center,
                    contentDescription = "Member Photo",
                    contentScale = ContentScale.Fit,

                    )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .dropShadow(
                        shape = RoundedCornerShape(20.dp),
                        shadow = Shadow(
                            radius = 5.dp,
                            spread = 2.dp,
                            color = Color(0x40000000),
                            offset = DpOffset(x = 0.dp, 5.dp)
                        )
                    )
                    .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            ) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),

                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = name,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = nim,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DarkBlue,
                        textAlign = TextAlign.Center
                    )
                }
            }

        }
    }

}

@Preview(showBackground = true)
@Composable
fun MemberCardPreview() {
    SitanggapTheme {
        Row {
            MemberCard(name = "John Doe", nim = "2111522002", modifier = Modifier.weight(1f))
            MemberCard(name = "John Doe", nim = "2111522002", modifier = Modifier.weight(1f))
        }

    }
}

@Composable
fun SaranScreen() {
    Layout(modifier = Modifier) {
        ListSaran(
            list = listOf(
                Pair(
                    "Tambah Fitur Dark Mode",
                    "Agar pengguna dapat menggunakan aplikasi dengan nyaman di malam hari."
                ),
                Pair(
                    "Integrasi dengan Media Sosial",
                    "Memudahkan berbagi laporan ke platform sosial."
                ),
                Pair(
                    "Notifikasi Real-time",
                    "Memberikan update langsung tentang status laporan pengguna."
                ),
            )
        )
    }
}

@Composable
fun ListSaran(list: List<Pair<String, String>>) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        for (saran in list) {
            SaranCard(title = saran.first, description = saran.second)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaranCard(title: String, description: String) {
    Box(
        modifier = Modifier
            .dropShadow(
                shape = RoundedCornerShape(10.dp),
                shadow = Shadow(
                    radius = 5.dp,
                    spread = 2.dp,
                    color = Color(0x40000000),
                    offset = DpOffset(x = 0.dp, 5.dp)
                )
            )
            .background(color = Color.White, shape = RoundedCornerShape(10.dp))
    ) {

        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()

        ) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(text = description, maxLines = 3, overflow = TextOverflow.Ellipsis)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                ButtonTransparent(Icons.Default.Edit, onLick = {}, contentDescription = "Edit")
                ButtonTransparent(Icons.Default.Delete, onLick = {}, contentDescription = "Delete")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SaranScreenPreview() {

    SitanggapTheme {
        SaranScreen()
    }
}

@Composable
fun ButtonTransparent(imageVector: ImageVector, onLick: () -> Unit, contentDescription: String) {
    Button(
        onClick = onLick, colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black
        )
    ) {
        Icon(imageVector = imageVector, contentDescription = contentDescription)
    }
}


@Composable
fun FloatingActionButtonAdd(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = DarkBlue,
        contentColor = Color.White
    ) {
        Icon(Icons.Filled.Add, contentDescription = "Tambah")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHeader(title: String, onClick: () -> Unit) {
    Column {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.Black
            ),
            navigationIcon = {
                IconButton(onClick = onClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
        )

        HorizontalDivider(
            color = Color.Black,
            thickness = 1.dp
        )
    }
}
