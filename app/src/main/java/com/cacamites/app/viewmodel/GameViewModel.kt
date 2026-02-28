package com.cacamites.app.viewmodel

import androidx.lifecycle.ViewModel
import com.cacamites.app.repository.GameRepository

class GameViewModel : ViewModel() {
    val repository = GameRepository()
}
