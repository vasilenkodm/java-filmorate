-- Инициализация значений в справочнике RankMPA
merge into RankMPA (rankMPA_name) key (rankMPA_name)
    values ('G'), ('PG'), ('PG-13'), ('R'), ('NC-17');

-- Инициализация значений в справочнике Genre
merge into Genre (genre_name) key (genre_name)
    values('Комедия'), ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик');
