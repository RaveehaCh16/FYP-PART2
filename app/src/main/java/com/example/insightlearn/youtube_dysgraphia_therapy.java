package com.example.insightlearn;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;

public class youtube_dysgraphia_therapy extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check);
        WebView webView = findViewById(R.id.webView);
        String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/IPs3QuIO0EM?si=LWNYaji5UnDE-tXP\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
        webView.loadData(video, "text/html","utf-8" );
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
    }

}
