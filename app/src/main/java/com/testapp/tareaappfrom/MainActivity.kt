package com.testapp.tareaappfrom
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EncuestaApp()
        }
    }
}

@Composable
fun EncuestaApp() {
    var aceptacion by remember { mutableStateOf(false) }
    var respuestas by remember { mutableStateOf(Array(5) { -1 }) }
    var otroTextos by remember { mutableStateOf(Array(5) { "" }) }
    var coloresPregunta by remember { mutableStateOf(Array(5) { Color.Black }) }
    var showAlert by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Hacer el contenido desplazable
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "Encuesta de Aceptacion",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Checkbox(
                checked = aceptacion,
                onCheckedChange = {
                    aceptacion = it
                    if (!it) {
                        respuestas = Array(5) { -1 }
                        otroTextos = Array(5) { "" }
                        coloresPregunta = Array(5) { Color.Black }
                    }
                }
            )
            Text(
                text = "Acepta los terminos",
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(165.dp)
                .padding(10.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Por favor, califica tu acuerdo con las siguientes afirmaciones utilizando una escala del 1 al 5, donde 1 representa 'Totalmente en desacuerdo' y 5 'Totalmente de acuerdo'",
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(16.dp))

        val preguntas = listOf(
            "¿Cree que su Universidad es la mejor de Managua?",
            "¿Cree que la calidad de su Universidad es la mejor?",
            "¿Cree que las clases que se imparten le ayudarán a su vida como profesional?",
            "¿Cree que la clase de Programación Orientada a Objetos le contribuirá a su futuro profesional?",
            "¿Considera que hacer la tarea con carácter individual y honesto le ayudará en el futuro profesional?"
        )

        preguntas.forEachIndexed { index, pregunta ->
            Pregunta(
                pregunta = pregunta,
                respuesta = respuestas[index],
                onRespuestaChange = { respuesta ->
                    respuestas = respuestas.toMutableList().also { it[index] = respuesta }.toTypedArray()
                },
                otroTexto = otroTextos[index],
                onOtroTextoChange = { texto ->
                    otroTextos = otroTextos.toMutableList().also { it[index] = texto }.toTypedArray()
                },
                habilitado = aceptacion,
                color = coloresPregunta[index]
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Verificar que todas las preguntas tienen un valor válido
            val todasRespuestasValidas = respuestas.all { it != -1 && (it != 6 || otroTextos[respuestas.indexOf(it)].isNotBlank()) }
            if (!todasRespuestasValidas) {
                // Actualizar colores para preguntas no válidas
                respuestas.forEachIndexed { index, respuesta ->
                    coloresPregunta = coloresPregunta.toMutableList().also {
                        it[index] = if (respuesta == -1 || (respuesta == 6 && otroTextos[index].isBlank())) Color.Red else Color.Black
                    }.toTypedArray()
                }
            } else {
                // Resetear colores
                coloresPregunta = Array(5) { Color.Black }
                // Mostrar la alerta
                showAlert = true
            }
        }) {
            Text("Enviar")
        }

        // Alerta de éxito
        if (showAlert) {
            AlertDialog(
                onDismissRequest = { showAlert = false },
                title = { Text("¡Éxito!") },
                text = { Text("El formulario se envió exitosamente.") },
                confirmButton = {
                    Button(onClick = { showAlert = false }) {
                        Text("Aceptar")
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
fun Pregunta(
    pregunta: String,
    respuesta: Int,
    onRespuestaChange: (Int) -> Unit,
    otroTexto: String,
    onOtroTextoChange: (String) -> Unit,
    habilitado: Boolean,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = pregunta,
            color = color,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            (1..5).forEach { opcion ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = respuesta == opcion,
                        onClick = { if (habilitado) onRespuestaChange(opcion) },
                        enabled = habilitado
                    )
                    Text(text = opcion.toString())
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = respuesta == 6,
                    onClick = { if (habilitado) onRespuestaChange(6) },
                    enabled = habilitado
                )
                Text("Otro")
            }
        }
        if (respuesta == 6) {
            BasicTextField(
                value = otroTexto,
                onValueChange = onOtroTextoChange,
                enabled = habilitado,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Gray)
                    .padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EncuestaApp()
}
