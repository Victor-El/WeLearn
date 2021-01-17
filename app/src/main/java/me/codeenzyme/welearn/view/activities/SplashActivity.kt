package me.codeenzyme.welearn.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import me.codeenzyme.welearn.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            if (Firebase.auth.currentUser != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            else {
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
        }, 500)
    }
}