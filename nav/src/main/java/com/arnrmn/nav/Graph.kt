package com.arnrmn.nav

import android.os.Bundle
import androidx.annotation.NavigationRes

data class Graph(@NavigationRes val graphId: Int, val arguments: Bundle? = null)