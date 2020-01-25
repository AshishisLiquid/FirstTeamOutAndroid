package com.firstteamout.farmershopping.POKO

class Users(var topUsers: List<User>)

class data(var data: User)

class User(var id : Int, var name: String, var uid: String, var email: String, var referral_code: String?, var email_verified_at: String?, var user_points: Int, var reward_code: String)