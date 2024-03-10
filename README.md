# reactive-spring-edu

_Java 21, Spring Boot 3_

## Usage

### cURL

```shell
# \curl -N -s -v http://localhost:8080/{insert endpoint here}
# Example:
\curl -N -s -v http://localhost:8080/temperature/sse/app-events
```

### Browser

Open page: `http://localhost:8080/{insert endpoint here}.html` \
Example: `http://localhost:8080/temperature/sse/app-events.html`

## Changelog

### v0.0.1

+ `GET /temperature/sse/app-events` - get random temperature async as SSE. Based on Spring Application Events.

### v0.0.2

+ `GET /temperature/sse/rx` - get random temperature async as SSE. Based on RxJava v3.x.
