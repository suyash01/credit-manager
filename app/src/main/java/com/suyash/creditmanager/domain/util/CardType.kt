package com.suyash.creditmanager.domain.util

import com.suyash.creditmanager.R

enum class CardType(val id: Int) {
    VISA(R.drawable.visa_icon),
    MASTERCARD(R.drawable.mastercard_icon),
    AMEX(R.drawable.amex_icon),
    DISCOVER(R.drawable.discover_icon),
    DCI(R.drawable.dci_icon),
    JCB(R.drawable.jcb_icon),
    RUPAY(R.drawable.rupay_icon);
}
