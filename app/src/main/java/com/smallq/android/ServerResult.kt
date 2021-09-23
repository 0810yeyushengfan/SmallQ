package com.smallq.android
/**
 * retCode:等于0时表示无错误，其余值表示有错误，错误时，errMsg有值，否则无值
 * errMsg:出错时的信息
 * data:真正返回的数据，其类型由参数T决定
 */
data class ServerResult<T>(var retCode: Int,var errMsg: String?,var data: T?) {
    constructor(retCode: Int) : this(retCode,null,null) {
        this.retCode = retCode
    }

    constructor(retCode: Int, errMsg: String): this(retCode,errMsg,null) {
        this.retCode = retCode
        this.errMsg = errMsg
    }
}