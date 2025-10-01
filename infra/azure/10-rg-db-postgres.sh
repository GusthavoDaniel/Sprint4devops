#!/usr/bin/env bash
set -euo pipefail

: "${AZ_REGION:?AZ_REGION não definido}"
: "${RG:?RG não definido}"
: "${PG_NAME:?PG_NAME não definido}"
: "${PG_ADMIN_USER:?PG_ADMIN_USER não definido}"
: "${PG_ADMIN_PASS:?PG_ADMIN_PASS não definido}"
: "${PG_DB:?PG_DB não definido}"

MY_IP="$(curl -s ifconfig.me || echo 0.0.0.0)"

echo ">>> Criando Resource Group"
az group create -n "$RG" -l "$AZ_REGION" -o table

echo ">>> Criando PostgreSQL Flexible Server ($PG_NAME)"
az postgres flexible-server create \
  --resource-group "$RG" --name "$PG_NAME" --location "$AZ_REGION" \
  --admin-user "$PG_ADMIN_USER" --admin-password "$PG_ADMIN_PASS" \
  --tier Burstable --sku-name B1ms --storage-size 32 --version 16 \
  --yes -o table

echo ">>> Criando database ($PG_DB)"
az postgres flexible-server db create \
  --resource-group "$RG" --server-name "$PG_NAME" --database-name "$PG_DB" -o table

echo ">>> Liberando seu IP ($MY_IP) no firewall"
az postgres flexible-server firewall-rule create \
  --resource-group "$RG" --name "$PG_NAME" \
  --rule-name allow-my-ip --start-ip-address "$MY_IP" --end-ip-address "$MY_IP" -o table

PG_HOST="${PG_NAME}.postgres.database.azure.com"
echo ">>> JDBC (use no App Service):"
echo "jdbc:postgresql://${PG_HOST}:5432/${PG_DB}?sslmode=require"

echo ">>> Agora, no Portal do Azure, abra o PostgreSQL -> Query Editor e rode db/script_bd.sql"
