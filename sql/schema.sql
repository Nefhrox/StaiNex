BEGIN;

CREATE TABLE IF NOT EXISTS stops
(
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS routes
(
    id TEXT PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS route_stops
(
    route_id TEXT NOT NULL REFERENCES routes(id),
    stop_index INTEGER NOT NULL CHECK (stop_index >= 0),
    stop_id TEXT NOT NULL REFERENCES stops(id),
    PRIMARY KEY (route_id, stop_index)
);

COMMIT;
