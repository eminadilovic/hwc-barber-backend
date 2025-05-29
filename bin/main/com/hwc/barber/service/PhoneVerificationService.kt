// package com.hwc.barber.service
//
// import com.twilio.Twilio
// import com.twilio.rest.verify.v2.service.Verification
// import com.twilio.rest.verify.v2.service.VerificationCheck
// import org.springframework.beans.factory.annotation.Value
// import org.springframework.data.redis.core.StringRedisTemplate
// import org.springframework.stereotype.Service
// import java.time.Duration
// import kotlin.random.Random
//
// @Service
// class PhoneVerificationService(
//     private val redisTemplate: StringRedisTemplate
// ) {
//     @Value("\${twilio.account-sid}")
//     private lateinit var accountSid: String
//
//     @Value("\${twilio.auth-token}")
//     private lateinit var authToken: String
//
//     @Value("\${twilio.verify-service-sid}")
//     private lateinit var verifyServiceSid: String
//
//     init {
//         Twilio.init(accountSid, authToken)
//     }
//
//     fun sendVerificationCode(phoneNumber: String): String {
//         val code = generateVerificationCode()
//         redisTemplate.opsForValue().set(
//             "phone_verification:$phoneNumber",
//             code,
//             Duration.ofMinutes(5)
//         )
//         
//         Verification.creator(verifyServiceSid, phoneNumber, "sms")
//             .create()
//         
//         return code
//     }
//     
//     fun verifyCode(phoneNumber: String, code: String): Boolean {
//         val verificationCheck = VerificationCheck.creator(verifyServiceSid)
//             .setTo(phoneNumber)
//             .setCode(code)
//             .create()
//         
//         return verificationCheck.status == "approved"
//     }
//     
//     private fun generateVerificationCode(): String {
//         return Random.nextInt(100000, 999999).toString()
//     }
// } 