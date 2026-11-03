# Celestial Aim Panel — Android

Aplicativo Android nativo em Java com painel flutuante para demonstração e treino visual offline.

## Recursos visuais
- Painel flutuante que pode ser arrastado, minimizado e fechado.
- Controles demonstrativos: assistência simulada, ESP visual, FOV e suavização.
- Zona de treino configurada conceitualmente para a região do pescoço de alvos fictícios.

## Limites
Este projeto não interage com Free Fire ou Free Fire MAX. Não captura a tela do jogo, não lê ou escreve memória de processos, não injeta código e não automatiza a mira. Os controles são apenas demonstrações visuais dentro do aplicativo.

## Compilar localmente
Requisitos: JDK 17, Android SDK Platform 35 e Gradle 8.9.

```bash
gradle --no-daemon assembleDebug
```

APK gerado em `app/build/outputs/apk/debug/app-debug.apk`.

## Gerar APK pelo GitHub
1. Abra a aba **Actions** deste repositório.
2. Se necessário, habilite GitHub Actions.
3. Execute **Build Android APK** com **Run workflow**, ou faça um novo push.
4. Abra a execução concluída e baixe o artefato **CelestialAimPanel-debug**.

O APK de depuração é um artefato de teste, não um lançamento assinado para Play Store.
