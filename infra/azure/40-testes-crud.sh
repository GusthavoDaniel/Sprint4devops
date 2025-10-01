#!/usr/bin/env bash
set -euo pipefail

: "${APP_NAME:?APP_NAME não definido}"
BASE="https://${APP_NAME}.azurewebsites.net"

echo ">>> HEALTH"
curl -i "$BASE/health" || true
echo -e "\n"

echo ">>> CREATE 1"
curl -s -X POST "$BASE/motos" -H "Content-Type: application/json" \
  -d '{"placa":"ABC1D23","modelo":"Honda CG 160","ano":2022}'
echo -e "\n\n"

echo ">>> CREATE 2"
curl -s -X POST "$BASE/motos" -H "Content-Type: application/json" \
  -d '{"placa":"EFG4H56","modelo":"Yamaha Fazer","ano":2021}'
echo -e "\n\n"

echo ">>> READ ALL"
curl -s "$BASE/motos"
echo -e "\n\n"

echo ">>> UPDATE (id=1 -> ano=2023)"
curl -s -X PUT "$BASE/motos/1" -H "Content-Type: application/json" \
  -d '{"placa":"ABC1D23","modelo":"Honda CG 160","ano":2023}'
echo -e "\n\n"

echo ">>> READ ONE (id=1)"
curl -s "$BASE/motos/1"
echo -e "\n\n"

echo ">>> DELETE (id=2)"
curl -s -X DELETE "$BASE/motos/2"
echo -e "\n\n"

echo ">>> READ ALL (final)"
curl -s "$BASE/motos"
echo -e "\n"
