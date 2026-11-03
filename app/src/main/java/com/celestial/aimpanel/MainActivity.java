package com.celestial.aimpanel;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;
import android.view.Gravity;

public class MainActivity extends Activity {
    private final int blue = Color.rgb(40, 190, 255);

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(28, 36, 28, 28);
        root.setBackgroundColor(Color.rgb(12, 18, 30));

        TextView title = new TextView(this);
        title.setText("CELESTIAL AIM PANEL");
        title.setTextColor(blue);
        title.setTextSize(23);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 8, 0, 18);
        root.addView(title);

        TextView info = new TextView(this);
        info.setText("Painel flutuante de treinamento visual. Os recursos são demonstrativos e não interagem com o Free Fire.");
        info.setTextColor(Color.WHITE);
        info.setTextSize(15);
        info.setPadding(0, 0, 0, 20);
        root.addView(info);

        Button start = new Button(this);
        start.setText("ATIVAR PAINEL FLUTUANTE");
        start.setTextColor(Color.WHITE);
        start.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.rgb(0, 115, 175)));
        root.addView(start);
        start.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
                Intent permission = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivity(permission);
            } else {
                startService(new Intent(this, OverlayService.class));
                finish();
            }
        });

        TextView note = new TextView(this);
        note.setText("\nComo usar:\n1. Permita 'Aparecer sobre outros apps'.\n2. Volte ao app e ative o painel.\n3. Arraste o painel pela barra superior.\n4. Feche pelo botão X.");
        note.setTextColor(Color.LTGRAY);
        note.setTextSize(14);
        root.addView(note);
        setContentView(root);
    }
}
