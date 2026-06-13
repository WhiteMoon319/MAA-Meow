#!/system/bin/sh
# Shizuku embedded launcher for MaaMeow
# Usage: adb shell sh /sdcard/Android/data/com.aliothmoon.maameow/start.sh

PKG="com.aliothmoon.maameow"
APK=$(pm path "$PKG" 2>/dev/null | head -1 | sed 's/package://')

if [ -z "$APK" ]; then
    echo "Error: $PKG not installed"
    exit 1
fi

echo "Starting Shizuku server from: $APK"
CLASSPATH="$APK" app_process /system/bin rikka.shizuku.server.ShizukuService &
echo "Shizuku server started (pid $!)"
