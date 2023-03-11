INSERT INTO USERS (email, name) VALUES ('user1@ya.ru', 'user1');

INSERT INTO CATEGORIES (name) VALUES ('category1'), ('category2');

INSERT INTO EVENTS (title, annotation, description, event_date, latitude,longitude, paid, participant_limit, request_moderation, initiator, category, published, published_on, state, created_on)
VALUES ('title1', 'anno1', 'desc1', now() + interval '1' day, 55, 65, false, 10, true, 1, 1, false, null, 'SEND_TO_REVIEW', now() - interval '10' day),
       ('title2', 'anno2', 'desc2', now() + interval '1' day, 50, 60, false, 5, true, 1, 2, true, now(), 'SEND_TO_REVIEW', now() - interval 'y' day);