package com.aplussoft.weatherapp.core.util


enum class WindDirection  (){
    N(),
    NE(),
    E (),

    SE (),
    S (),
    SW (),
    W (),

    NW ();

    companion object{
        fun fromDegree(degree: Double): WindDirection {
            return when (degree) {
                in 337.5..360.0, in 0.0..22.5 -> N
                in 22.5..67.5 -> NE
                in 67.5..112.5 -> E
                in 112.5..157.5 -> SE
                in 157.5..202.5 -> S
                in 202.5..247.5 -> SW
                in 247.5..292.5 -> W
                in 292.5..337.5 -> NW
                else -> throw IllegalArgumentException("Invalid degree")
            }

        }
    }
}



