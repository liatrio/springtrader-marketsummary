MAKEFLAGS+=--silent

.PHONY: skaffold-dev

skaffold-dev:
	kubectl config use-context docker-desktop && \
	DB_HOSTNAME=marketsummary-mongodb \
	DB_DATABASE_NAME=marketsummary \
	skaffold dev -n springtrader-marketsummary --port-forward