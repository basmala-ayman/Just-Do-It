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
fun TodoList(
    todos: List<Todo>,
    onAddClick: (String, String?) -> Unit,
    onDoneClick: (Todo) -> Unit,
    onDeleteClick: (Todo) -> Unit,
    onTodoClick: (Int) -> Unit
) {
    var titleInput by rememberSaveable {
        mutableStateOf("")
    }
    var descriptionInput by rememberSaveable {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Just Do It - To Do App") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (titleInput.isNotBlank()) {
                    onAddClick(titleInput, descriptionInput.ifBlank { null })
                    titleInput = ""
                    descriptionInput = ""
                }
            }) {
                Text("Add")
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(12.dp)
        ) {
            // title input field
            OutlinedTextField(
                value = titleInput,
                onValueChange = { titleInput = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            // description input field
            OutlinedTextField(
                value = descriptionInput,
                onValueChange = { descriptionInput = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            LazyColumn {
                items(todos) { todo ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(modifier = Modifier.weight(1f)) {
                            // done checkbox
                            Checkbox(checked = todo.isDone, onCheckedChange = { onDoneClick(todo) })

                            Spacer(Modifier.width(8.dp))

                            Text(
                                text = todo.title,
                                modifier = Modifier
                                    .clickable { onTodoClick(todo.id) }
                                    .padding(vertical = 4.dp)
                            )
                        }
                        Text(
                            text = "Delete",
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable { onDeleteClick(todo) }
                        )
                    }
                    Divider()
                }
            }
        }
    }


}









