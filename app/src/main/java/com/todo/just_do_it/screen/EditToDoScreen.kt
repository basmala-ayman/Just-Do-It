package com.todo.just_do_it.screen
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.todo.just_do_it.data.Todo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditToDoScreen(todo: Todo?, onSave: (Todo) -> Unit){
    if (todo == null) {
        Text("Error: Todo not found")
        return
    }
    var title by remember { mutableStateOf(todo.title) }
    var description by remember { mutableStateOf(todo.description ?: "") }
    var done by remember { mutableStateOf(todo.isDone) }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Edit Todo") }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val updated = todo.copy(
                        title = title,
                        description = description.ifBlank { null },
                        isDone = done
                    )
                    onSave(updated)
                }
            ) {
                Text("Save")
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            Row {
                Checkbox(checked = done, onCheckedChange = { done = it })
                Text("Done?")
            }
        }
    }
}