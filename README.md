# FF Ultimate 2.0 PRO — Android

Aplicativo Android nativo em Java com painel flutuante e círculo FOV estritamente visuais, para demonstração.

## Recursos
- Painel flutuante arrastável, minimizável e fechável.
- Controles demonstrativos de aimbot, ESP, indicador de vida, FOV e suavização.
- Círculo FOV visual ajustável.
- Interface identificada como FF Ultimate 2.0 PRO.

## Limites técnicos
Os controles são apenas elementos de interface. O aplicativo não interage com Free Fire ou Free Fire MAX, não identifica jogadores, não captura a tela do jogo, não lê nem escreve memória de processos, não injeta bibliotecas e não automatiza a mira. Não há biblioteca nativa ffultimate neste projeto.

## Compilar
Requisitos: JDK 17, Android SDK Platform 35 e Gradle 8.9.

    gradle --no-daemon assembleDebug

APK gerado em app/build/outputs/apk/debug/app-debug.apk.

## Baixar pelo GitHub Actions
Abra a aba Actions, selecione Build Android APK e baixe o artefato FF-Ultimate-2.0-PRO-debug após a execução concluir.
