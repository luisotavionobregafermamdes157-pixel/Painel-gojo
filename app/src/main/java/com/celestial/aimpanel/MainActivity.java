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

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 60, 40, 40);
        layout.setBackgroundColor(Color.rgb(12, 18, 30));

        TextView titulo = new TextView(this);
        titulo.setText("Painel FF — círculo de FOV");
        titulo.setTextColor(Color.rgb(40, 190, 255));
        titulo.setTextSize(22f);

        TextView instrucoes = new TextView(this);
        instrucoes.setText("Ajuste o tamanho e toque em Ativar. O círculo é visual e não controla a mira.");
        instrucoes.setTextColor(Color.WHITE);
        instrucoes.setTextSize(16f);
        instrucoes.setPadding(0, 20, 0, 20);

        SeekBar tamanho = new SeekBar(this);
        tamanho.setMax(300);
        tamanho.setProgress(100);
        tamanho.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                raio = 50f + progress;
                if (circulo != null) {
                    circulo.raio = raio;
                    circulo.invalidate();
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        Button ativar = new Button(this);
        ativar.setText("Ativar círculo");
        ativar.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            } else {
                mostrarCirculo();
            }
        });

        Button desativar = new Button(this);
        desativar.setText("Desativar círculo");
        desativar.setOnClickListener(v -> removerCirculo());

        layout.addView(titulo);
        layout.addView(instrucoes);
        layout.addView(tamanho);
        layout.addView(ativar);
        layout.addView(desativar);
        setContentView(layout);
    }

    private void mostrarCirculo() {
        if (circulo != null) return;
        CirculoView view = new CirculoView();
        view.raio = raio;

        int tipoJanela = Build.VERSION.SDK_INT >= 26
            ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            : WindowManager.LayoutParams.TYPE_PHONE;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            tipoJanela,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.TOP | Gravity.START;
        windowManager.addView(view, params);
        circulo = view;
    }

    private void removerCirculo() {
        if (circulo != null) {
            try {
                windowManager.removeView(circulo);
            } catch (IllegalArgumentException ignored) {}
            circulo = null;
        }
    }

    @Override
    protected void onDestroy() {
        removerCirculo();
        super.onDestroy();
    }

    private class CirculoView extends View {
        float raio = 150f;
        private final Paint pintura = new Paint(Paint.ANTI_ALIAS_FLAG);

        CirculoView() {
            super(MainActivity.this);
            pintura.setColor(Color.MAGENTA);
            pintura.setStyle(Paint.Style.STROKE);
            pintura.setStrokeWidth(4f);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, raio, pintura);
        }
    }
}
