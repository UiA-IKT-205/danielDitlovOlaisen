package com.example.todolist

interface UpdateList{

    fun modifyItem(itemUID :String, isDone : Boolean)
    fun onItemDelete(itemUID: String)
}