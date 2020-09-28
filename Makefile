MAKEFLAGS+=--silent

.PHONY: local

local:
	DB_HOSTNAME=marketsummary-mongodb \
	DB_DATABASE_NAME=local \
	skaffold dev -n springtrader-marketsummary --port-forward