package com.smartlab.wpqrscanner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebViewFragment extends Fragment {


    ProgressBar progressBar;
    WebView webView;
    @Override
    public void onSaveInstanceState(Bundle outState) {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setMax(100);

        webView = view.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://ge.smartlab.com.my/helpdesk/m/req.wp?p=1");
        progressBar.setProgress(0);

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100)
                    progressBar.setVisibility(View.INVISIBLE);
                else
                    progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
            }
        });
        // Inflate the layout for this fragment
        return view;

    }
}