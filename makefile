.PHONY: dev

start-local-stack:
	docker-compose -f webservices/src/main/docker/keycloak.yml up -d > stack.log
	docker-compose -f webservices/src/main/docker/kafka.yml up -d > stack.log
	docker-compose -f webservices/src/main/docker/postgresql.yml up -d > stack.log

start: start-webservices

start-interactive: start-webservices watch-logs

stop: stop-webservices

clean: clean-webservices

watch: watch-logs

stop-local-stack:
	docker-compose -f webservices/src/main/docker/keycloak.yml down --remove-orphans

start-webservices:
	cd webservices && { ./mvnw & echo $$! > webservices.pid;} > webservices.log &
	echo "Webservices starting (can take a few minutes)"

stop-webservices:
ifneq ("$(wildcard webservices/webservices.pid)","")
	kill `cat webservices/webservices.pid` && rm webservices/webservices.pid
endif

watch-logs:
	tail -f webservices/stack.log webservices/webservices.log
