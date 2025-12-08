package com.todo.just_do_it
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.todo.just_do_it.data.Todo
import com.todo.just_do_it.model.TodoModel
import kotlinx.coroutines.launch
import com.todo.just_do_it.screen.EditToDoScreen
import com.todo.just_do_it.ui.theme.JustDoItTheme

class EditToDoActivity: ComponentActivity()  {
    private val todoModel: TodoModel by viewModels()
    private var selectedTodo: Todo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val todoId = intent.getIntExtra("id", -1)


        lifecycleScope.launch {
            selectedTodo=todoModel.getToDoById(todoId)

            setContent {
            JustDoItTheme(){

                EditToDoScreen(
                    todo = selectedTodo,
                    onSave = {updatedTodo ->
                        todoModel.updateTodo(updatedTodo)
                        finish()
                    },
                    onBack = { finish() }
                )
            }
            }
        }

    }

}