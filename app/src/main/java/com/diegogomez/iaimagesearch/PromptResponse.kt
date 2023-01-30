package com.diegogomez.iaimagesearch

import com.google.gson.annotations.SerializedName

data class PromptResponse (
    @SerializedName("images") var lexicarData:List<LexicartData>
)