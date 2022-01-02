package com.sdin.tourstamprally.utill

data class Quadruple<out A, out B, out C, out D>(
    val background: A,
    val title_color: B,
    val backBtn_ic: C,
    val tab_ic : D
)