# Jumbo

Jumbo retail application

## Running locally

You'll need:

* [Java 17](https://www.oracle.com/java/technologies/downloads/#java17/)
* [Docker](https://docs.docker.com/get-docker/)
* [Docker compose](https://docs.docker.com/compose/install/)

Then you'll be able to run:

```sh
make start-local-stack
```

To run the prerequisites docker containers and:

```sh
make start
```

To start the backend app and:


```sh
make  watch-logs
```

To tail all logs and:

```sh
make stop
```

To stop the stack