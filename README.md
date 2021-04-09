# Simple RESTful Spring application

This Simple Spring Boot RESTful application using to save incoming Templates and send a substituted template on others
urls. H2 used as database, java.net.http as HttpClient.

## Usage

---
To run spring application (only with Java11+)

```shell
$ ./gradlew bootRun  
```

## Urls

---

Application have several endpoints:

- POST
    - `localhost:8080/template/create` - save the template to the database
    - `localhost:8080/template/send` - substitute values in the template and send it to other users
- GET
    - `localhost:8080/template/list` - show all templates from database

### Request example

`POST: /template/create/`

```json
{
  "templateId": "template1",
  "template": "Hello $name$.",
  "recipients": [
    "http://some.server.url/endpoint",
    "http://some.other.url/endpoint"
  ]
}
```

`POST: /template/send/`

```json
{
  "templateId": "template1",
  "variables": [
    {
      "name": "World"
    }
  ]
}
```

To see more [examples](https://github.com/PaGr0m/spring-boot-analytics-service/tree/main/requests)