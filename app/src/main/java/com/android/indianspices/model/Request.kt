package com.android.indianspices.model

class Request
{
    var name:String?=null
    var phone:String?=null
    var address:String?=null
    var pincode:String?=null
    var total:String?=null
    var orders:List<Orders>?=null

    constructor()

    constructor(userName:String?,userPhone:String?,userAddress:String?,userPinCode:String?,total:String?,foodOrders:List<Orders>?)
    {
        this.name=userName
        this.phone=userPhone
        this.address=userAddress
        this.pincode=userPinCode
        this.total=total
        this.orders=foodOrders



    }


}