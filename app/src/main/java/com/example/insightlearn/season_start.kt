import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.insightlearn.R

class seasonstart : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.season_game) // replace with your layout name

        val getStartedButton = findViewById<Button>(R.id.getStarted)
        getStartedButton.setOnClickListener {
            val intent = Intent(this, SeasonRulesActivity::class.java)
            startActivity(intent)
        }
    }
}
