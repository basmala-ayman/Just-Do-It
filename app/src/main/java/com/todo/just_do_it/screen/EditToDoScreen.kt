package com.todo.just_do_it.screen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.todo.just_do_it.data.Todo
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditToDoScreen(todo: Todo?, onSave: (Todo) -> Unit, onBack:() -> Unit){
    if (todo == null) {
        //error screen
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: Todo not found")
        }
        return
    }
    var title by remember { mutableStateOf(todo.title) }
    var description by remember { mutableStateOf(todo.description ?: "") }
    var done by remember { mutableStateOf(todo.isDone) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Edit Task ✏️",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )

        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background) // Cream Background
                .padding(16.dp)
        ) {
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text("Task Details", fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Title Input
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(Modifier.height(16.dp))

                    // Description Input
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        minLines = 3,
                        maxLines = 5
                    )

                    Spacer(Modifier.height(16.dp))

                    // Status Checkbox Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = done,
                            onCheckedChange = { done = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(
                            text = if(done) "Completed!" else "Mark as Completed",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Push button to bottom

            // Big Beautiful Save Button
            Button(
                onClick = {
                    val updated = todo.copy(
                        title = title,
                        description = description.ifBlank { null },
                        isDone = done
                    )
                    onSave(updated)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Save Changes", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}