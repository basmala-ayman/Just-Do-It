package com.todo.just_do_it.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.todo.just_do_it.data.Todo
import androidx.compose.material.icons.filled.Settings
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoList(
    // declare functions and variables we use for this screen
    todos: List<Todo>,
    onAddClick: (String, String?) -> Unit,
    onDoneClick: (Todo) -> Unit,
    onDeleteClick: (Todo) -> Unit,
    onClickToDO: (String) -> Unit,
    onSettingsClick: () -> Unit

) {
    var titleInput by rememberSaveable {
        mutableStateOf("")
    }
    var descriptionInput by rememberSaveable {
        mutableStateOf("")
    }
    var taskDelete by remember {
        mutableStateOf<Todo?>(null)
    }
    var errorMessage by remember {
        mutableStateOf(false)
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Just Do It ✨",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButtonPosition = FabPosition.Center,

        floatingActionButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp), // Leave some space from the edges
                horizontalArrangement = Arrangement.SpaceBetween, // Push buttons to Left and Right
                verticalAlignment = Alignment.CenterVertically
            ) {
                // --- LEFT BUTTON (Settings) ---
                FloatingActionButton(
                    onClick = onSettingsClick,
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings"
                    )
                }

                // --- RIGHT BUTTON (Add Task) ---
                FloatingActionButton(
                    onClick = {
                        if (titleInput.isNotBlank()) {
                            onAddClick(titleInput, descriptionInput.ifBlank { null })
                            titleInput = ""
                            descriptionInput = ""
                        }else{
                            errorMessage = true
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task")
                }
            }
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background) // Use our Cream color
                .padding(16.dp)
        ) {

            if (taskDelete != null) {
                AlertDialog(
                    onDismissRequest = {taskDelete = null},
                    icon = { Icon(Icons.Default.Warning, contentDescription = null)},
                    title = { Text("Delete Task")},
                    text = { Text("Are you sure you want to delete this task?")},
                    confirmButton = {
                        TextButton(onClick = {
                            onDeleteClick(taskDelete!!)
                            taskDelete = null
                        }) {
                            Text("Yes, Delete", color = MaterialTheme.colorScheme.error)
                        }
                    },

                    dismissButton = {
                        TextButton(onClick = { taskDelete = null }) {
                            Text("No, Cancel")
                        }
                    }

                )
            }

            if (errorMessage){
                AlertDialog(
                    onDismissRequest = { errorMessage = false }, // Close if clicked outside
                    title = { Text("Missing Information 📝") },
                    text = { Text("Please write the task name before adding.") },
                    confirmButton = {
                        TextButton(onClick = { errorMessage = false }) {
                            Text("OK")
                        }
                    },
                    icon = { Icon(Icons.Default.Warning, contentDescription = null) }
                )

            }



            // title input field
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "New Task",
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Styled Title Input
                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text("What needs to be done?") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(Modifier.height(8.dp))


                    // Styled Description Input
                    OutlinedTextField(
                        value = descriptionInput,
                        onValueChange = { descriptionInput = it },
                        label = { Text("Details (optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 3
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                "Your Tasks",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )

            // list Section
            LazyColumn(
                contentPadding = PaddingValues(bottom = 80.dp), // Space for FAB
                verticalArrangement = Arrangement.spacedBy(12.dp) // Space between items
            ) {
                items(todos) { todo ->
                    TodoItemCard(
                        todo = todo,
                        onDoneClick = onDoneClick,
                        onDeleteClick = { taskDelete = it },
                        onCardClick = { onClickToDO(todo.id) }
                    )
                }
            }
        }
    }
}


@Composable
fun TodoItemCard(
    todo: Todo,
    onDoneClick: (Todo) -> Unit,
    onDeleteClick: (Todo) -> Unit,
    onCardClick: () -> Unit
) {
    // Animate color change when done
    val backgroundColor by animateColorAsState(
        if (todo.isDone) MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
        else MaterialTheme.colorScheme.surface
    )

    Card(
        onClick = { onCardClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = todo.isDone,
                onCheckedChange = { onDoneClick(todo) },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.secondary
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = todo.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textDecoration = if (todo.isDone) TextDecoration.LineThrough else null,
                    color = if (todo.isDone) Color.Gray else MaterialTheme.colorScheme.onSurface
                )

                if (!todo.description.isNullOrBlank()) {
                    Text(
                        text = todo.description,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 2
                    )
                }
            }

            IconButton(onClick = { onDeleteClick(todo) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
