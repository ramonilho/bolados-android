package br.com.ramonilho.bolados.activity

import br.com.ramonilho.bolados.R

class SplashActivity : android.support.v7.app.AppCompatActivity() {
    //Tempo que o splashscreen ficará visivel (em milissegundos)
    private val SPLASH_DISPLAY_LENGTH = 1000 //3500

    public override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //Executando o método que iniciará nossa animação
        carregar()
    }

    private fun carregar() {
        val anim = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.animation_splash)
        anim.reset()
        //Pegando o nosso objeto criado no layout
        val iv = findViewById(R.id.splash) as android.widget.ImageView

        // Caso a imagem exista, executar a animacao
        if (iv != null) {
            iv.clearAnimation()
            iv.startAnimation(anim)
        }

        android.os.Handler().postDelayed({
            // Após o tempo definido irá executar a próxima tela
            val intent = android.content.Intent(this@SplashActivity, MainActivity::class.java)
            intent.flags = android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)

            // Finaliza a atividade atual
            this@SplashActivity.finish()
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }
}