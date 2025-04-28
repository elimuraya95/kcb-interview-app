# KCB Interview App

### Tools Used
- Java 17
- Spring Boot v3.4.5
- H2 Database
- Jib
- Lombok


### Runnning the Application

To run the application, follow the following steps:

- Build the docker image using the command below:
```
./gradlew jibDockerBuild
```

- Run the application in the docker container using the command below:
```
docker run -d --name elimbaru/kcb-interview-app -p 8080:8080 elimbaru/kcb-interview-app
```

- You can also execute the command below, to run the application outside a docker container: 
```
./gradlew bootRun
```

The application is now available at port 8080.



### REST API Endpoints
Below are the API endpoints available:

#### Initiating a B2C transaction
- This is a POST request to initiate a B2C transaction, with the following payload.
- POST localhost:8080/api/v1/b2c/initiate-transaction
```
{
    "phoneNumber": "254700112233", // phone number of the client to receive funds
    "amount": 100, // amount to be transacted
    "narration": "Test transaction", // narration of the transaction
    "momoProvider": "MPESA" // MobileMoney provider to use eg MPESA|AIRTEL
}
```

We get the following response, if the request is processed successfully
```
{
    "code": 200,
    "message": "Processed B2C transaction successfully!",
    "data": {
        "transactionId": "b16dbd9f-86f5-4a96-9aa7-33a19c3b01c7",
        "phoneNumber": "254700112233",
        "amount": 100.0,
        "narration": "Test transaction",
        "momoProvider": "MPESA"
    }
}
```

If an error occurs, or validation of the payload fails, we get the following response
```
{
    "code": 400,
    "message": "Phone number is required",
    "data": null
}
```

#### Fetch a B2C transaction
- This is a GET request to fetch a previously initiated a B2C transaction. This can be used to check the processing state of a given transaction by a client
- GET localhost:8080/api/v1/b2c/fetch-transaction/b16dbd9f-86f5-4a96-9aa7-33a19c3b01c5

```
{
    "code": 200,
    "message": "Fetched B2C transaction successfully!",
    "data": {
        "transactionId": "b16dbd9f-86f5-4a96-9aa7-33a19c3b01c7",
        "phoneNumber": "254700112233",
        "amount": 100,
        "narration": "Test transaction",
        "momoProvider": "MPESA"
    }
}
```


If a transaction does not exist, or the provided transaction is invalid, below will be the response
```
{
    "code": 404,
    "message": "Invalid transaction id provided!",
    "data": null
}
```

