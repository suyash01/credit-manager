package com.suyash.creditmanager.domain.util

import com.google.gson.annotations.SerializedName
import com.suyash.creditmanager.R

enum class CardType(val id: Int) {
    @SerializedName("VISA")
    VISA(R.drawable.visa_icon),
    @SerializedName("MASTERCARD")
    MASTERCARD(R.drawable.mastercard_icon),
    @SerializedName("AMEX")
    AMEX(R.drawable.amex_icon),
    @SerializedName("DISCOVER")
    DISCOVER(R.drawable.discover_icon),
    @SerializedName("DCI")
    DCI(R.drawable.dci_icon),
    @SerializedName("JCB")
    JCB(R.drawable.jcb_icon),
    @SerializedName("RUPAY")
    RUPAY(R.drawable.rupay_icon);
}
