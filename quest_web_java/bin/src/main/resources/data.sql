SET @id_now = 1;
INSERT IGNORE INTO dash_attack (id, damage, endlag, frame, name, creation_date, updated_date) VALUES (@id_now, 11, 25, 5, null, NOW(), NOW());
INSERT IGNORE INTO up_tilt (id, damage, endlag, frame, name, creation_date, updated_date) VALUES (@id_now, 6, 12, 8, null, NOW(), NOW());
INSERT IGNORE INTO down_tilt (id, damage, endlag, frame, name, creation_date, updated_date) VALUES (@id_now, 4, 12, 5, null, NOW(), NOW());
INSERT IGNORE INTO forward_tilt (id, damage, endlag, frame, name, creation_date, updated_date) VALUES (@id_now, 13, 24, 12, null, NOW(), NOW());
INSERT IGNORE INTO video (id, yt_channel, url, creation_date, updated_date) VALUES (@id_now, "JoeK", "https://www.youtube.com/watch?v=eDFsbqmgOVg", NOW(), NOW());
INSERT IGNORE INTO charac (id, name, speed, weight, creation_date, updated_date, up_tilt_id, forward_id, down_tilt_id, dash_attack_id, video_id ) 
    VALUES (@id_now, 'Wario', 1.65, 107, NOW(), NOW(), @id_now, @id_now, @id_now, @id_now, @id_now);

SET @id_now = 2;
INSERT IGNORE INTO dash_attack (id, damage, endlag, frame, name, creation_date, updated_date) VALUES (@id_now, 8, 19, 7, null, NOW(), NOW());
INSERT IGNORE INTO up_tilt (id, damage, endlag, frame, name, creation_date, updated_date) VALUES (@id_now, 5, 13, 8, null, NOW(), NOW());
INSERT IGNORE INTO down_tilt (id, damage, endlag, frame, name, creation_date, updated_date) VALUES (@id_now, 7, 12, 9, null, NOW(), NOW());
INSERT IGNORE INTO forward_tilt (id, damage, endlag, frame, name, creation_date, updated_date) VALUES (@id_now, 9, 16, 9, null, NOW(), NOW());
INSERT IGNORE INTO video (id, yt_channel, url, creation_date, updated_date) VALUES (@id_now, "IzAw", "https://www.youtube.com/watch?v=YDYO5vVrsk8", NOW(), NOW());
INSERT IGNORE INTO charac (id, name, speed, weight, creation_date, updated_date, up_tilt_id, forward_id, down_tilt_id, dash_attack_id, video_id )
    VALUES (2, 'Toon Link', 1.906, 91, NOW(), NOW(), @id_now, @id_now, @id_now, @id_now, @id_now);

SET @id_now = 3;
INSERT IGNORE INTO dash_attack (id, damage, endlag, frame, name, creation_date, updated_date) VALUES (@id_now, 6, 16, 4, null, NOW(), NOW());
INSERT IGNORE INTO up_tilt (id, damage, endlag, frame, name, creation_date, updated_date) VALUES (@id_now, 8, 20, 3, null, NOW(), NOW());
INSERT IGNORE INTO down_tilt (id, damage, endlag, frame, name, creation_date, updated_date) VALUES (@id_now, 8, 19, 7, null, NOW(), NOW());
INSERT IGNORE INTO forward_tilt (id, damage, endlag, frame, name, creation_date, updated_date) VALUES (@id_now, 6, 15, 6, null, NOW(), NOW());
INSERT IGNORE INTO video (id, yt_channel, url, creation_date, updated_date) VALUES (@id_now, "IzAw", "https://www.youtube.com/watch?v=xVbrtGvncd8", NOW(), NOW());
INSERT IGNORE INTO charac (id, name, speed, weight, creation_date, updated_date, up_tilt_id, forward_id, down_tilt_id, dash_attack_id, video_id )
    VALUES (3, 'Fox', 2.402, 77, NOW(), NOW(), @id_now, @id_now, @id_now, @id_now, @id_now);

SET @id_now = 4;
INSERT IGNORE INTO dash_attack (id, damage, endlag, frame, name, creation_date, updated_date) VALUES (@id_now, 23, 10, 9, null, NOW(), NOW());
INSERT IGNORE INTO up_tilt (id, damage, endlag, frame, name, creation_date, updated_date) VALUES (@id_now, 10, 27, 5, null, NOW(), NOW());
INSERT IGNORE INTO down_tilt (id, damage, endlag, frame, name, creation_date, updated_date) VALUES (@id_now, 6, 17, 6, null, NOW(), NOW());
INSERT IGNORE INTO forward_tilt (id, damage, endlag, frame, name, creation_date, updated_date) VALUES (@id_now, 9, 25, 7, null, NOW(), NOW());
INSERT IGNORE INTO video (id, yt_channel, url, creation_date, updated_date) VALUES (@id_now, "Choctopus", "https://www.youtube.com/watch?v=1TekTES3m6U", NOW(), NOW());
INSERT IGNORE INTO charac (id, name, speed, weight, creation_date, updated_date, up_tilt_id, forward_id, down_tilt_id, dash_attack_id, video_id )
    VALUES (4, 'Donkey Kong', 1.873, 127, NOW(), NOW(), @id_now, @id_now, @id_now, @id_now, @id_now);

SET @id_now = 5;
INSERT IGNORE INTO dash_attack (id, damage, endlag, frame, name, creation_date, updated_date) VALUES (@id_now, 11, 23, 6, null, NOW(), NOW());
INSERT IGNORE INTO up_tilt (id, damage, endlag, frame, name, creation_date, updated_date) VALUES (@id_now, 5, 13, 7, null, NOW(), NOW());
INSERT IGNORE INTO down_tilt (id, damage, endlag, frame, name, creation_date, updated_date) VALUES (@id_now, 6, 10, 7, null, NOW(), NOW());
INSERT IGNORE INTO forward_tilt (id, damage, endlag, frame, name, creation_date, updated_date) VALUES (@id_now, 10, 21, 6, null, NOW(), NOW());
INSERT IGNORE INTO video (id, yt_channel, url, creation_date, updated_date) VALUES (@id_now, "ESAM", "https://www.youtube.com/watch?v=DYDBSOURMok", NOW(), NOW());
INSERT IGNORE INTO charac (id, name, speed, weight, creation_date, updated_date, up_tilt_id, forward_id, down_tilt_id, dash_attack_id, video_id)
    VALUES (5, 'Pikachu', 2.039, 79, NOW(), NOW(), @id_now, @id_now, @id_now, @id_now, @id_now);

SET @id_now = 6;
INSERT IGNORE INTO dash_attack (id, damage, endlag, frame, name, creation_date, updated_date) VALUES (@id_now, 45, 2, 2, null, NOW(), NOW());
INSERT IGNORE INTO up_tilt (id, damage, endlag, frame, name, creation_date, updated_date) VALUES (@id_now, 45, 2, 2, null, NOW(), NOW());
INSERT IGNORE INTO down_tilt (id, damage, endlag, frame, name, creation_date, updated_date) VALUES (@id_now, 45, 2, 2, null, NOW(), NOW());
INSERT IGNORE INTO forward_tilt (id, damage, endlag, frame, name, creation_date, updated_date) VALUES (@id_now, 45, 2, 2, null, NOW(), NOW());
INSERT IGNORE INTO video (id, yt_channel, url, creation_date, updated_date) VALUES (@id_now, "ETNA.io", "https://www.youtube.com/watch?v=kw9h7C2Li-s", NOW(), NOW());
INSERT IGNORE INTO charac (id, name, speed, weight, creation_date, updated_date, up_tilt_id, forward_id, down_tilt_id, dash_attack_id, video_id)
    VALUES (5, 'Yann Alancon', 8.787, 80, NOW(), NOW(), @id_now, @id_now, @id_now, @id_now, @id_now);