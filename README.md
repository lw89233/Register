# Register

## Opis Projektu

Testowa aplikacja CNApp oparta na protokole SSMMP. Interfejsem użytkownika jest klient wiersza poleceń (CLI), który komunikuje się z systemem mikrousług poprzez API Gateway. Żądania i odpowiedzi przesyłane są w formie obiektów klasy String, a cała architektura jest bezstanowa.

## Rola Komponentu

Ta mikrousługa odpowiada za obsługę procesu rejestracji nowych użytkowników. Odbiera żądania typu `registration_request` od API Gateway, a następnie zapisuje dane nowego użytkownika (login i hasło) w bazie danych.

## Konfiguracja

Ten komponent wymaga następujących zmiennych środowiskowych w pliku `.env`:
```
REGISTRATION_MICROSERVICE_PORT=
DB_HOST=
DB_PORT=
DB_NAME=
DB_USER=
DB_PASSWORD=
```
## Wymagania

Do poprawnego działania tego komponentu wymagane jest uruchomienie i skonfigurowanie następujących usług:

* **Baza Danych MySQL**: Aplikacja łączy się z bazą danych w celu przechowywania informacji. Dane do połączenia należy umieścić w pliku `.env`.

## Uruchomienie

### Uruchomienie deweloperskie (lokalne)

Ta metoda jest przeznaczona do celów deweloperskich i buduje obraz lokalnie.

1.  **Sklonuj repozytorium**.
2.  **Skonfiguruj zmienne środowiskowe**: Utwórz plik `.env` w głównym katalogu projektu i uzupełnij go o wymagane wartości (możesz skorzystać z `.env.sample`).
3.  **Uruchom aplikację**: W głównym katalogu projektu wykonaj polecenie:
    ```bash
    docker compose up --build
    ```
    Spowoduje to zbudowanie obrazu Docker i uruchomienie kontenera z aplikacją.

### Uruchomienie produkcyjne (z Docker Hub)

Ta metoda wykorzystuje gotowy obraz z repozytorium Docker Hub.

1.  **Pobierz obraz**: Na serwerze docelowym wykonaj polecenie, aby pobrać najnowszą wersję obrazu z repozytorium na Docker Hub.
    ```bash
    docker pull lw89233/register:latest
    ```

2.  **Przygotuj pliki konfiguracyjne**: W jednym katalogu na serwerze umieść:
    * Uzupełniony plik `.env`.
    * Plik `docker-compose.prod.yml` o następującej treści:
        ```yaml
        services:
          register:
            image: lw89233/register:latest
            container_name: register-service
            restart: unless-stopped
            env_file:
              - .env
            ports:
              - "${REGISTRATION_MICROSERVICE_PORT}:${REGISTRATION_MICROSERVICE_PORT}"
        ```

3.  **Uruchom kontener**: W katalogu, w którym znajdują się pliki konfiguracyjne, wykonaj polecenie:
    ```bash
    docker compose -f docker-compose.prod.yml up -d
    ```
    Aplikacja zostanie uruchomiona w tle.