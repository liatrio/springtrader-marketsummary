MAKEFLAGS+=--silent

.PHONY: skaffold-dev

namespace=springtrader-marketsummary

create-ns:
	kubectl create ns ${namespace} --dry-run -o yaml | kubectl apply -f -

local: create-ns
	kubectl config use-context docker-desktop
	skaffold run -n ${namespace} --port-forward --tail

local-delete:
	kubectl config use-context docker-desktop
	skaffold delete -n ${namespace}
