# Simple RESTful Spring application

This applicaction allows you to manage templates. You can use variables in templates and then with additional request
substitute to actual values. Finally, using this app you can send resulting text to specified URLs.

[H2](https://www.h2database.com/html/main.html) is used as a database,
[java.net.http](https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/package-summary.html)
as HttpClient.

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

[Click to see more examples!](https://github.com/PaGr0m/spring-boot-analytics-service/tree/main/requests)