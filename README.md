# Register

## Opis Projektu

Testowa aplikacja CNApp oparta na protokole SSMMP. Interfejsem użytkownika jest klient wiersza poleceń (CLI), który komunikuje się z systemem mikrousług poprzez API Gateway. Żądania i odpowiedzi przesyłane są w formie obiektów klasy String, a cała architektura jest bezstanowa.

## Rola Komponentu

Ta mikrousługa odpowiada za obsługę procesu rejestracji nowych użytkowników. Odbiera żądania typu `registration_request` od API Gateway, a następnie zapisuje dane nowego użytkownika (login i hasło) w bazie danych.

## Konfiguracja

Ten komponent wymaga następujących zmiennych środowiskowych w pliku `.env`:

REGISTRATION_MICROSERVICE_PORT=

DB_HOST=

DB_PORT=

DB_NAME=

DB_USER=

DB_PASSWORD=


## Uruchomienie

Serwis można uruchomić, wykonując główną metodę `main` w klasie `Register.java`