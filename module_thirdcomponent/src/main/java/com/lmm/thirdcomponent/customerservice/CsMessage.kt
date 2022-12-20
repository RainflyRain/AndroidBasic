package com.yf.smart.weloopx.app.customerservice

/**
 * Created by zpf on 2022/7/12.
 */
@Suppress("unused")
enum class CsMessageStatus(value: Int) {
    DRAFT(-1),
    SENDING(0),
    SUCCESS(1),
    FAIL(2),
    READ(3),
    UNREAD(4)
}

data class CsMessage(
    var title: String = "",
    var content: String = "",
    var status: CsMessageStatus = CsMessageStatus.READ,
    var count: Int = 0
)