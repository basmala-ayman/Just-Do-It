package com.todo.just_do_it.data

import org.junit.Assert.*
import org.junit.Test
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any


class TodoRepoTest {
    @Mock
    lateinit var fakeDao: TodoDao

    @Mock
    lateinit var fakeFirestore: FirestoreRepo

    private lateinit var repo: TodoRepo

    @Before
    fun setup() {

        MockitoAnnotations.openMocks(this)

        repo = TodoRepo(fakeDao, fakeFirestore)
    }

    @Test
    fun insertTodo() =runTest {
        //insure that todo is saved to database and network
        repo.insertTodo("test todo" , "is test working?")
       //verify that function insertTodo is called
        //any ->random todo will be created as i do not care
        verify(fakeDao).insertTodo(any())
        verify(fakeFirestore).saveTodo(any())
    }

    @Test
    fun updateTodo()= runTest {
        val dummyToDo= Todo("123", "test update" , "testttt")
        repo.updateTodo(dummyToDo)
        verify(fakeDao).updateTodo(dummyToDo)
        verify(fakeFirestore).saveTodo(dummyToDo)

    }

    @Test
    fun deleteTodo()= runTest {
        val dummyToDo= Todo("123", "test delete" , "testttt")
        repo.deleteTodo(dummyToDo)
        verify(fakeDao).deleteTodo(dummyToDo)
        verify(fakeFirestore).deleteTodoById(dummyToDo.id)
    }

}