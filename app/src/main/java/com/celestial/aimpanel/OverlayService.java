package com.celestial.aimpanel;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.SeekBar;

public class OverlayService extends Service {
    private WindowManager wm;
    private View panel;
    private WindowManager.LayoutParams params;
    private boolean minimized = false;
    private final int bg = Color.rgb(13, 20, 34);
    private final int cyan = Color.rgb(60, 205, 255);

    @Override public void onCreate() {
        super.onCreate();
        wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        buildPanel();
    }

    private void buildPanel() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(12, 8, 12, 12);
        box.setBackgroundColor(bg);

        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        TextView title = new TextView(this);
        title.setText("CELESTIAL  •  AIM TRAINER");
        title.setTextColor(cyan);
        title.setTextSize(13);
        title.setPadding(4, 8, 8, 8);
        top.addView(title, new LinearLayout.LayoutParams(0, -2, 1));
        Button minimize = smallButton("—");
        Button close = smallButton("X");
        top.addView(minimize);
        top.addView(close);
        box.addView(top);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.addView(label("SIMULADOR VISUAL • NÃO CONTROLA O JOGO"));
        content.addView(label("ZONA DE TREINO: PESCOÇO • ALVOS FICTÍCIOS"));
        addToggle(content, "Aimbot Legit (simulado)");
        addToggle(content, "Aimbot Rage (simulado)");
        addToggle(content, "ESP Box (demonstração)");
        addToggle(content, "Linha ESP (demonstração)");
        addToggle(content, "Indicador de vida (demonstração)");
        addSlider(content, "FOV visual", 40, 360, 180);
        addSlider(content, "Smooth simulado", 1, 20, 7);
        box.addView(content);

        minimize.setOnClickListener(v -> {
            minimized = !minimized;
            content.setVisibility(minimized ? View.GONE : View.VISIBLE);
            minimize.setText(minimized ? "+" : "—");
        });
        close.setOnClickListener(v -> stopSelf());

        top.setOnTouchListener(new View.OnTouchListener() {
            float sx, sy; int ix, iy;
            @Override public boolean onTouch(View v, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    sx=e.getRawX(); sy=e.getRawY(); ix=params.x; iy=params.y; return true;
                }
                if (e.getAction() == MotionEvent.ACTION_MOVE) {
                    params.x=ix+(int)(e.getRawX()-sx);
                    params.y=iy+(int)(e.getRawY()-sy);
                    wm.updateViewLayout(panel, params); return true;
                }
                return true;
            }
        });

        panel = box;
        params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 30; params.y = 100;
        wm.addView(panel, params);
    }

    private void addToggle(LinearLayout parent, String text) {
        android.widget.Switch sw = new android.widget.Switch(this);
        sw.setText(text);
        sw.setTextColor(Color.WHITE);
        sw.setTextSize(12);
        sw.setPadding(2, 4, 2, 4);
        sw.setChecked(false);
        parent.addView(sw);
    }

    private void addSlider(LinearLayout parent, String name, int min, int max, int value) {
        TextView t = label(name + ": " + value);
        parent.addView(t);
        SeekBar s = new SeekBar(this);
        s.setMax(max-min); s.setProgress(value-min);
        s.setProgressTintList(android.content.res.ColorStateList.valueOf(cyan));
        s.setThumbTintList(android.content.res.ColorStateList.valueOf(cyan));
        parent.addView(s);
        s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar bar, int p, boolean user) { t.setText(name + ": " + (p+min)); }
            public void onStartTrackingTouch(SeekBar bar) {}
            public void onStopTrackingTouch(SeekBar bar) {}
        });
    }

    private TextView label(String text) {
        TextView t = new TextView(this);
        t.setText(text); t.setTextColor(Color.LTGRAY); t.setTextSize(11);
        t.setPadding(3, 5, 3, 5); return t;
    }

    private Button smallButton(String text) {
        Button b = new Button(this); b.setText(text); b.setTextSize(10);
        b.setTextColor(Color.WHITE);
        b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.rgb(0, 105, 160)));
        return b;
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) { return START_NOT_STICKY; }
    @Override public void onDestroy() {
        if (wm != null && panel != null) try { wm.removeView(panel); } catch (Exception ignored) {}
        super.onDestroy();
    }
    @Override public IBinder onBind(Intent intent) { return null; }
}
