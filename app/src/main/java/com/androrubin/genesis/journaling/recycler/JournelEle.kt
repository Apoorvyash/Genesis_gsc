package com.androrubin.genesis.journaling.recycler

import com.google.firebase.Timestamp

class JournelEle{
     var title:String?=null
     var desc:String?=null
     var date :String?=null


     constructor(){

     }

    constructor(title:String?,desc: String?,date:String?){

        this.title=title
        this.desc=desc
        this.date=date


    }
}
