#!/usr/bin/env bash
set -euo pipefail

: "${AZ_REGION:?AZ_REGION não definido}"
: "${RG:?RG não definido}"
: "${APP_PLAN:?APP_PLAN não definido}"
: "${APP_NAME:?APP_NAME não definido}"
: "${PG_NAME:?PG_NAME não definido}"
: "${PG_DB:?PG_DB não definido}"
: "${PG_ADMIN_USER:?PG_ADMIN_USER não definido}"
: "${PG_ADMIN_PASS:?PG_ADMIN_PASS não definido}"

PG_HOST="${PG_NAME}.postgres.database.azure.com"

echo ">>> Criando App Service Plan (Linux B1)"
az appservice plan create \
  --name "$APP_PLAN" --resource-group "$RG" --is-linux --sku B1 -l "$AZ_REGION" -o table

echo ">>> Criando Web App ($APP_NAME) - Java 17 Linux"
az webapp create \
  --resource-group "$RG" --plan "$APP_PLAN" --name "$APP_NAME" \
  --runtime "JAVA:17-java17" -o table

echo ">>> Configurando App Settings (datasource + run from package)"
az webapp config appsettings set \
  --resource-group "$RG" --name "$APP_NAME" --settings \
  "SPRING_DATASOURCE_URL=jdbc:postgresql://${PG_HOST}:5432/${PG_DB}?sslmode=require" \
  "SPRING_DATASOURCE_USERNAME=${PG_ADMIN_USER}" \
  "SPRING_DATASOURCE_PASSWORD=${PG_ADMIN_PASS}" \
  "WEBSITE_RUN_FROM_PACKAGE=1" -o table

APP_URL="https://${APP_NAME}.azurewebsites.net"
echo ">>> App criado: $APP_URL"
