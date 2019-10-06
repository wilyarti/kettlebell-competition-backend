# Kettlebell Competition Backend
A ktor server running the necessary API to run the kettlebell-competion app.

It connects to a MySQL database and exchanges data with the front end.

## Building
Run 
> ./gradlew clean build

## Running
Run
> java -jar build/libs/kettlebell-competition-server-0.0.1-all.jar

## Recommendations
I would recommend putting this server behind NGINX if exposing it to the internet.

