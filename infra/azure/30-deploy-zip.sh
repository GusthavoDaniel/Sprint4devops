#!/usr/bin/env bash
set -euo pipefail

: "${RG:?RG não definido}"
: "${APP_NAME:?APP_NAME não definido}"

echo ">>> Build Maven (skip tests)"
mvn -q -DskipTests clean package

ZIPFILE="app.zip"
rm -f "$ZIPFILE"
echo ">>> Empacotando JAR em $ZIPFILE"
cd target
JARFILE="$(ls *.jar | head -n1)"
zip -q -r "../$ZIPFILE" "$JARFILE"
cd ..

echo ">>> Deploy ZIP para o App Service"
az webapp deploy \
  --resource-group "$RG" --name "$APP_NAME" \
  --src-path "$ZIPFILE" --type zip -o table

APP_URL="https://${APP_NAME}.azurewebsites.net"
echo ">>> Acesse o healthcheck: $APP_URL/health"

# opcional: aguarda ficar no ar
echo ">>> Verificando /health (até 10 tentativas)"
for i in {1..10}; do
  if curl -fsS "$APP_URL/health" >/dev/null; then
    echo "OK! Aplicação respondeu /health."
    exit 0
  fi
  sleep 5
done

echo "Aviso: /health ainda não respondeu. Veja os logs com 50-logs.sh."
