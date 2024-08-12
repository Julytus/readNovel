INSERT INTO roles (id, name) VALUES 
(1, 'USER'),
(2, 'ADMIN'),
(3, 'POSTER');

-- Inserting Permissions
INSERT INTO permissions (name, api_path, method, module, created_at, updated_at) VALUES
('createChapter', '/chapters', 'POST', 'Chapter', NOW(), NOW()),
('deleteChapter', '/chapters', 'DELETE', 'Chapter', NOW(), NOW()),
('updateChapter', '/chapters', 'PUT', 'Chapter', NOW(), NOW()),

('createNovel', '/novels', 'POST', 'Novel', NOW(), NOW()),
('updateNovelImage', '/novels/upload/{id}', 'POST', 'Novel', NOW(), NOW()),
('deleteNovel', '/novels/{id}', 'DELETE', 'Novel', NOW(), NOW()),
('updateNovel', '/novels/{id}', 'PUT', 'Novel', NOW(), NOW()),

('createPermission', '/permissions', 'POST', 'Permission', NOW(), NOW()),
('updatePermission', '/permissions', 'PUT', 'Permission', NOW(), NOW()),
('deletePermission', '/permissions', 'DELETE', 'Permission', NOW(), NOW()),

('getAllRoles', '/roles', 'GET', 'Role', NOW(), NOW()),
('createRole', '/roles', 'POST', 'Role', NOW(), NOW()),
('deleteRole', '/roles/{id}', 'DELETE', 'Role', NOW(), NOW()),
('updateRole', '/roles/{id}', 'PUT', 'Role', NOW(), NOW()),

('searchUser', '/users', 'GET', 'User', NOW(), NOW()),
('getUserDetail', '/users/detail', 'GET', 'User', NOW(), NOW()),
('updateUserDetail', '/users/detail/{userId}', 'PUT', 'User', NOW(), NOW()),
('updateAvatar', '/users/avatar_upload/{id}', 'POST', 'User', NOW(), NOW());

-- Getting the last inserted permission ids
SET @permission_id_1 = LAST_INSERT_ID();

-- Assuming each permission_id above is incremented sequentially from @permission_id_1 
-- and assigning the appropriate role mappings:

-- Insert Role-Permission Mappings
INSERT INTO permission_role (permission_id, role_id) VALUES
(@permission_id_1, 2), (@permission_id_1, 3), -- createChapter
(@permission_id_1 + 1, 2), (@permission_id_1 + 1, 3), -- deleteChapter
(@permission_id_1 + 2, 2), (@permission_id_1 + 2, 3), -- updateChapter

(@permission_id_1 + 3, 2), -- createNovel
(@permission_id_1 + 4, 2), (@permission_id_1 + 4, 3), -- updateNovelImage
(@permission_id_1 + 5, 2), -- deleteNovel
(@permission_id_1 + 6, 2), (@permission_id_1 + 6, 3), -- updateNovel

(@permission_id_1 + 7, 2), -- createPermission
(@permission_id_1 + 8, 2), -- updatePermission
(@permission_id_1 + 9, 2), -- deletePermission

(@permission_id_1 + 10, 3), -- getAllRoles
(@permission_id_1 + 11, 3), -- createRole
(@permission_id_1 + 12, 3), -- deleteRole
(@permission_id_1 + 13, 3), -- updateRole

(@permission_id_1 + 14, 3), -- searchUser
(@permission_id_1 + 15, 1), (@permission_id_1 + 15, 2), (@permission_id_1 + 15, 3), -- getUserDetail
(@permission_id_1 + 16, 1), (@permission_id_1 + 16, 2), (@permission_id_1 + 16, 3), -- updateUserDetail
(@permission_id_1 + 17, 1), (@permission_id_1 + 17, 2), (@permission_id_1 + 17, 3); -- updateAvatar

INSERT INTO content_types (id, name) VALUES (0, 'UNKNOWN');

-- Text (images)
INSERT INTO content_types (id, name) VALUES (10, 'MANGA');
INSERT INTO content_types (id, name) VALUES (11, 'MANHUA');
INSERT INTO content_types (id, name) VALUES (12, 'MANHWA');
INSERT INTO content_types (id, name) VALUES (13, 'DOUJINSHI');
INSERT INTO content_types (id, name) VALUES (14, 'HENTAI_MANGA');
INSERT INTO content_types (id, name) VALUES (15, 'YAOI');
INSERT INTO content_types (id, name) VALUES (16, 'YAOI_MANGA');
INSERT INTO content_types (id, name) VALUES (17, 'WEBCOMICS');
INSERT INTO content_types (id, name) VALUES (18, 'RUMANGA');
INSERT INTO content_types (id, name) VALUES (19, 'OEL_MANGA');
INSERT INTO content_types (id, name) VALUES (20, 'STRIP');
INSERT INTO content_types (id, name) VALUES (21, 'COMICS');
INSERT INTO content_types (id, name) VALUES (26, 'YURI');
INSERT INTO content_types (id, name) VALUES (27, 'YURI_MANGA');
INSERT INTO content_types (id, name) VALUES (28, 'HENTAI_MANHWA');

-- Text (books)
INSERT INTO content_types (id, name) VALUES (22, 'LIGHT_NOVEL');
INSERT INTO content_types (id, name) VALUES (23, 'NOVEL');
INSERT INTO content_types (id, name) VALUES (24, 'BOOK');
INSERT INTO content_types (id, name) VALUES (25, 'TEXT_PORN');

-- Video
INSERT INTO content_types (id, name) VALUES (30, 'ANIME');
INSERT INTO content_types (id, name) VALUES (31, 'HENTAI');
INSERT INTO content_types (id, name) VALUES (32, 'HENTAI_ANIME');
INSERT INTO content_types (id, name) VALUES (33, 'YAOI_ANIME');
INSERT INTO content_types (id, name) VALUES (34, 'DORAMA');
INSERT INTO content_types (id, name) VALUES (35, 'CARTOON');
INSERT INTO content_types (id, name) VALUES (36, 'MOVIES_TV');
INSERT INTO content_types (id, name) VALUES (37, 'MOVIE');
INSERT INTO content_types (id, name) VALUES (38, 'TV');
INSERT INTO content_types (id, name) VALUES (39, 'PORN');
INSERT INTO content_types (id, name) VALUES (40, 'YURI_ANIME');

-- Audio
INSERT INTO content_types (id, name) VALUES (50, 'PODCAST');
INSERT INTO content_types (id, name) VALUES (51, 'AUDIO');
INSERT INTO content_types (id, name) VALUES (52, 'AUDIO_MUSIC');
INSERT INTO content_types (id, name) VALUES (53, 'AUDIO_BOOK');
INSERT INTO content_types (id, name) VALUES (54, 'AUDIO_NOVEL');
INSERT INTO content_types (id, name) VALUES (55, 'AUDIO_LIGHT_NOVEL');
INSERT INTO content_types (id, name) VALUES (56, 'AUDIO_PORN');