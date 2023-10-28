package com.full.crm.login.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.full.crm.network.API
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun Login(modifier: Modifier = Modifier, loginViewModel: LoginViewModel) {
    val scope = rememberCoroutineScope()

    val password: String by loginViewModel.password.observeAsState(initial = "")
    val username: String by loginViewModel.username.observeAsState(initial = "")

    //TODO: Hacer que el boton de iniciar sesion haga una peticion a la API
    //TODO: Limitar el numero de caracteres en password
    //TODO: Manejar el texto desde fuera

    Box(
        modifier = modifier
            .requiredWidth(width = 360.dp)
            .requiredHeight(height = 800.dp)
            .background(color = Color.White)
    ) {
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(y = (-32).dp)
                .requiredWidth(width = 427.dp)
                .requiredHeight(height = 377.dp)
                .clip(shape = RoundedCornerShape(71.dp))
                .background(
                    brush = Brush.linearGradient(
                        0f to Color(0xff3ed8f8),
                        1f to Color(0xff1976d2),
                        start = Offset(554f, -124.5f),
                        end = Offset(32.5f, 385f)
                    )
                ))
        ElevatedCard(
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .offset(
                    x = 0.5.dp,
                    y = (-44.5).dp
                )
                .requiredWidth(width = 329.dp)
                .requiredHeight(height = 413.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEFEFE)),
            elevation = CardDefaults.cardElevation(defaultElevation = 50.dp)
        ){}
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 129.dp,
                    y = 91.dp
                )
                .requiredSize(size = 103.dp)
        ) {
            ElevatedCard(
                modifier = Modifier
                    .requiredSize(size = 103.dp),
                shape = RoundedCornerShape(35.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEFEFE)),
                elevation = CardDefaults.cardElevation(defaultElevation = 50.dp)
            ){}
        }
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 50.dp,
                    y = 457.dp
                )
                .requiredWidth(width = 263.dp)
                .requiredHeight(height = 47.dp)
                .clickable {
                    var res = "";

                    scope.launch {
                        async {
                            //res = API.service.devUser()
                        }.await()

                        Log.d("API", res)
                    }
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(3.dp))
                    .background(color = Color(0xff1976d2)))
            Text(
                text = "INICIAR SESION",
                color = Color.White,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 14.sp),
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(align = Alignment.CenterVertically))
        }

        Input(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 48.dp,
                    y = 323.dp
                )
                .requiredWidth(width = 265.dp)
                .requiredHeight(height = 75.dp),
            label = "Contraseña",
            placeholder = "●●●●●●●",
            type = KeyboardType.Password,
            value = password,
            onInputChanged = { loginViewModel.onLoginChanged(username = username, password = it) }
        )

        Input(
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(y = 228.dp)
                .requiredWidth(width = 264.dp)
                .requiredHeight(height = 73.dp),
            label = "Usuario / Correo",
            placeholder = "Escribe tu usuario o correo",
            value = username,
            onInputChanged = { loginViewModel.onLoginChanged(username = it, password = password) }
        )


        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 50.dp,
                    y = 420.dp
                )
                .requiredWidth(width = 98.dp)
                .requiredHeight(height = 15.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredSize(size = 13.dp)
                    .clip(shape = RoundedCornerShape(3.dp))
                    .background(color = Color(0xfff4f4f4))
                    .border(
                        border = BorderStroke(1.dp, Color.Black),
                        shape = RoundedCornerShape(3.dp)
                    ))
            Text(
                text = "Recuérdame",
                color = Color(0xffbfc4cf),
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 14.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 17.dp,
                        y = -3.dp
                    )
                    .wrapContentHeight(align = Alignment.CenterVertically))
        }
        Text(
            text = "¿Has olvidado tu contraseña?",
            color = Color(0xff26a69a),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 11.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(
                    x = 3.5.dp,
                    y = 525.dp
                )
                .wrapContentHeight(align = Alignment.CenterVertically))
        Box(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .padding(bottom = 50.dp)
                .requiredWidth(width = 263.dp)
                .requiredHeight(height = 47.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(width = 263.dp)
                    .requiredHeight(height = 47.dp)
                    .clip(shape = RoundedCornerShape(3.dp))
                    .background(color = Color(0xff1976d2)))
            Text(
                text = "INICIAR SESION CON GOOGLE",
                color = Color.White,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 13.sp),
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 64.dp,
                        y = 15.dp
                    )
                    .wrapContentHeight(align = Alignment.CenterVertically))
            Box(
                modifier = Modifier
                    .align(alignment = Alignment.TopStart)
                    .offset(
                        x = 9.dp,
                        y = 5.dp
                    )
                    .requiredSize(size = 37.dp)
            ) {
                Box(
                    modifier = Modifier
                        .requiredSize(size = 37.dp)
                        .clip(shape = CircleShape)
                        .background(color = Color(0xffd9d9d9)))
            }
        }
    }
}

@Composable
fun Input(modifier: Modifier = Modifier, label: String, placeholder: String,
          type: KeyboardType = KeyboardType.Text, onInputChanged: (String) -> Unit,
          value: String) {
    Box(
        modifier = modifier
    ) {
        Text(
            text = label,
            color = Color(0xffbfc4cf),
            style = TextStyle(
                fontSize = 14.sp),
            modifier = Modifier
                .requiredWidth(width = 245.dp))

        TextField(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(
                    x = 1.dp,
                    y = 27.dp
                )
                .requiredWidth(width = 263.dp)
                .requiredHeight(height = 46.dp)
                .clip(shape = RoundedCornerShape(3.dp))
                .background(color = Color(0xfff4f4f4)),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xfff4f4f4),
            ),
            keyboardOptions = KeyboardOptions(keyboardType = type),
            textStyle = TextStyle(fontSize = 14.sp),
            value = value,
            placeholder =
            {
                Text(
                    text = placeholder,
                    fontSize = 12.sp,
                )
            },
            onValueChange = { onInputChanged(it) },
            visualTransformation =
            if (type == KeyboardType.Password)
                PasswordVisualTransformation()
            else VisualTransformation.None,
        )
    }
}