MAKEFLAGS+=--silent

.PHONY: skaffold-dev

skaffold-local:
	kubectl config use-context docker-desktop && \
	DB_HOSTNAME=marketsummary-mongodb \
	DB_DATABASE_NAME=marketsummary \
	skaffold run -n springtrader-marketsummary --port-forward --tail