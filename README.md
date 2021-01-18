# neighbour-tour

In order to use OAuth authentication you must obtain credentials from [Google Docs](https://developers.google.com/identity/protocols/oauth2/openid-connect#appsetup)
and enter them in application.properties.

After that you can start the application with:
```sh
mvnw.cmd spring-boot:run

./mvnw spring-boot:run
```

The index.html contains two links - to login and to the form that can call the tour API. The form and the calls it makes to the API are protected with OAuth so only an authenticated user may use them.