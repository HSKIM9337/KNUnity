package com.example.knunity.firebaseAuth

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class GMailSender : Authenticator() {


    val fromEmail = "hskim9337@knu.ac.kr"
    val password = "oduerzfklowbohcx"
    val code = (100..10000).random().toString()
//    val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
//        throwable.printStackTrace()
//    }


    // 보내는 사람 계정 확인
    override fun getPasswordAuthentication(): PasswordAuthentication {
        return PasswordAuthentication(fromEmail, password)
    }

    // 메일 보내기
    fun sendEmail(toEmail: String): String {

        CoroutineScope(Dispatchers.IO).launch {
            val props = Properties()



            props.setProperty("mail.transport.protocol", "smtp")
            props.setProperty("mail.host", "smtp.gmail.com")
            props.put("mail.smtp.auth", "true")
            props.put("mail.smtp.port", "465")
            props.put("mail.smtp.socketFactory.port", "465")
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
            props.put("mail.smtp.socketFactory.fallback", "false")
            props.put("mail.smtp.ssl.enable", "true")
            props.setProperty("mail.smtp.quitwait", "false")
            // 구글에서 지원하는 smtp 정보를 받아와 MimeMessage 객체에 전달
            val session = Session.getDefaultInstance(props, this@GMailSender)


            // 메시지 객체 만들기
            val message = MimeMessage(session)
            message.sender = InternetAddress(fromEmail)                                 // 보내는 사람 설정
            message.addRecipient(Message.RecipientType.TO, InternetAddress(toEmail))    // 받는 사람 설정
            message.subject =
                "verfication code"                                              // 이메일 제목
            message.setText("KNUnity 에서 보낸 코드입니다. 아래 비밀번호를 인증창에 입력해주세요\n" + "<" + code + ">")                                               // 이메일 내용

            // 전송
            Transport.send(message)


        }
        return code
    }
}