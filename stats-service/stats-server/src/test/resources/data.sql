DELETE FROM ENDPOINT_HITS;

INSERT INTO ENDPOINT_HITS(app, uri, ip, timestamp)
VALUES ('main-service', '/events/1', '127.0.0.1', now() - interval '1' day),
       ('main-service', '/events/1', '127.0.0.1', now() - interval '2' day),
       ('main-service', '/events/1', '127.0.0.2', now() - interval '3' day),
       ('main-service', '/events/1', '127.0.0.2', now() - interval '4' day),
       ('main-service', '/events/1', '127.0.0.3', now() - interval '5' day),
       ('main-service', '/events/2', '127.0.0.4', now() - interval '6' day),
       ('main-service', '/events/2', '127.0.0.4', now() - interval '7' day),
       ('main-service', '/events/2', '127.0.0.4', now() - interval '8' day),
       ('main-service', '/events/2', '127.0.0.5', now() - interval '9' day);