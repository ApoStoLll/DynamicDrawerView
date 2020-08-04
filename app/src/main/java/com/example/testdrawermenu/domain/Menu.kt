package com.example.testdrawermenu.domain

data class Menu(
    val menu: List<MenuX>
)

data class MenuX(
    val function: String,
    val name: String,
    val `param`: String
)