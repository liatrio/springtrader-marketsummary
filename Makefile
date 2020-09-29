MAKEFLAGS+=--silent

.PHONY: skaffold-dev

local:
	kubectl config use-context docker-desktop
	DB_HOSTNAME=marketsummary-mongodb \
	DB_DATABASE_NAME=marketsummary \
	skaffold run -n springtrader-marketsummary --port-forward --tail

local-delete:
	kubectl config use-context docker-desktop
	skaffold delete -n springtrader-marketsummary