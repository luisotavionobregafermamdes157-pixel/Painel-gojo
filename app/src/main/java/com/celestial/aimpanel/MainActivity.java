package com.celestial.aimpanel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity {
    private WindowManager windowManager;
    private CirculoView circulo;
    private float raio = 150f;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 48, 40, 40);
        layout.setBackgroundColor(Color.rgb(12, 18, 30));

        TextView titulo = new TextView(this);
        titulo.setText("FF ULTIMATE 2.0 PRO");
        titulo.setTextColor(Color.rgb(40, 190, 255));
        titulo.setTextSize(24f);
        TextView subtitulo = new TextView(this);
        subtitulo.setText("PAINEL VISUAL • MODO DE DEMONSTRAÇÃO");
        subtitulo.setTextColor(Color.LTGRAY);
        subtitulo.setTextSize(12f);
        subtitulo.setPadding(0, 8, 0, 22);
        TextView instrucoes = new TextView(this);
        instrucoes.setText("O painel e o círculo são visuais. Os controles não leem nem modificam o Free Fire.");
        instrucoes.setTextColor(Color.WHITE);
        instrucoes.setTextSize(15f);
        instrucoes.setPadding(0, 0, 0, 18);

        Button abrir = new Button(this);
        abrir.setText("Abrir painel flutuante");
        abrir.setOnClickListener(v -> {
            if (!pedirPermissaoOverlay()) startService(new Intent(this, OverlayService.class));
        });
        Button fechar = new Button(this);
        fechar.setText("Fechar painel flutuante");
        fechar.setOnClickListener(v -> stopService(new Intent(this, OverlayService.class)));

        TextView fovLabel = new TextView(this);
        fovLabel.setText("Tamanho do círculo FOV visual");
        fovLabel.setTextColor(Color.WHITE);
        fovLabel.setPadding(0, 18, 0, 4);
        SeekBar tamanho = new SeekBar(this);
        tamanho.setMax(300);
        tamanho.setProgress(100);
        tamanho.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar s, int p, boolean user) {
                raio = 50f + p;
                if (circulo != null) { circulo.raio = raio; circulo.invalidate(); }
            }
            @Override public void onStartTrackingTouch(SeekBar s) {}
            @Override public void onStopTrackingTouch(SeekBar s) {}
        });
        Button ativar = new Button(this);
        ativar.setText("Ativar círculo visual");
        ativar.setOnClickListener(v -> { if (!pedirPermissaoOverlay()) mostrarCirculo(); });
        Button desativar = new Button(this);
        desativar.setText("Desativar círculo visual");
        desativar.setOnClickListener(v -> removerCirculo());

        layout.addView(titulo); layout.addView(subtitulo); layout.addView(instrucoes);
        layout.addView(abrir); layout.addView(fechar); layout.addView(fovLabel);
        layout.addView(tamanho); layout.addView(ativar); layout.addView(desativar);
        setContentView(layout);
    }

    private boolean pedirPermissaoOverlay() {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            Intent i = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
            startActivity(i);
            return true;
        }
        return false;
    }

    private void mostrarCirculo() {
        if (circulo != null) return;
        CirculoView view = new CirculoView();
        view.raio = raio;
        int tipo = Build.VERSION.SDK_INT >= 26
            ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            : WindowManager.LayoutParams.TYPE_PHONE;
        WindowManager.LayoutParams p = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
            tipo, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT);
        p.gravity = Gravity.TOP | Gravity.START;
        windowManager.addView(view, p);
        circulo = view;
    }

    private void removerCirculo() {
        if (circulo != null) {
            try { windowManager.removeView(circulo); }
            catch (IllegalArgumentException ignored) {}
            circulo = null;
        }
    }

    @Override protected void onDestroy() { removerCirculo(); super.onDestroy(); }

    private class CirculoView extends View {
        float raio = 150f;
        private final Paint pintura = new Paint(Paint.ANTI_ALIAS_FLAG);
        CirculoView() {
            super(MainActivity.this);
            pintura.setColor(Color.MAGENTA);
            pintura.setStyle(Paint.Style.STROKE);
            pintura.setStrokeWidth(4f);
        }
        @Override protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawCircle(getWidth()/2f, getHeight()/2f, raio, pintura);
        }
    }
}