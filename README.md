# StaiNex

A public transport helper that reminds you when your stop is approaching.

Buses full of people, unfamiliar routes, or falling asleep during a commute can make it easy to miss your stop. StaiNex aims to help passengers know when to get off without constantly checking their location.

> Early development: the project currently contains Java logic and PostgreSQL integration. The mobile app and automatic trip tracking are planned.

## How it will work

1. Select the transport type and route number.
2. Choose your starting stop and destination.
3. Receive a notification when your destination is one or two stops away.

The initial prototype will simulate a journey in the console. Later versions will use the phone’s location to track progress.

## Current progress

- Validate stop IDs and names.
- Calculate remaining stops using positions in an ordered route.
- Store stops in PostgreSQL.
- Read stops from the database through JDBC.
- Manage dependencies and compile the project with Maven.

## Roadmap

- Import public transport data from GTFS feeds.
- Store routes and their stop sequences.
- Build a console-based simulation.
- Build a minimal Android client with Kotlin.
- Add location tracking and arrival notifications.

Initial transport data integration will focus on Chișinău.

## Technologies

**Currently used:** Java, JDBC, PostgreSQL, Maven, Docker.

**Planned:** Spring Boot and Kotlin for Android.
