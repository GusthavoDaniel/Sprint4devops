#!/usr/bin/env bash
set -euo pipefail

: "${RG:?RG não definido}"
: "${APP_NAME:?APP_NAME não definido}"

az webapp log tail --resource-group "$RG" --name "$APP_NAME"
