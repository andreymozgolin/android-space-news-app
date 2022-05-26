package ru.andreymozgolin.spacenews

import android.app.Application

class SpaceNewsApplication: Application() {
    val component = DaggerApplicationComponent.factory().create(this)
}