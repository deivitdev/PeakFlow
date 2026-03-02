package com.deivitdev.peakflow.domain.model

sealed class WorkoutType {
    data object STRENGTH : WorkoutType()
    data object WALKING : WorkoutType()
    data object OTHER : WorkoutType()

    sealed class CYCLING : WorkoutType() {
        data object ROAD : CYCLING()
        data object MOUNTAIN : CYCLING()
        data object GRAVEL : CYCLING()
        data object GENERIC : CYCLING()
    }

    sealed class RUNNING : WorkoutType() {
        data object ROAD : RUNNING()
        data object TRAIL : RUNNING()
        data object GENERIC : RUNNING()
    }
}
