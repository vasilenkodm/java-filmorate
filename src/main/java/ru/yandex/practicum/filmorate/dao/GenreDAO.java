package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.KeyNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.type.GenreIdType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDAO implements ItemDAO<GenreIdType, Genre> {

    public static final String ID_FIELD = "genre_id";
    public static final String NAME_FIELD = "genre_name";
    private final NamedParameterJdbcTemplate jdbcNamedTemplate;

    private String idNotFoundMsg(GenreIdType id) {
        return String.format("Не найден жанр с кодом %s!", id);
    }

    public Genre read(final GenreIdType id) {
        String sqlStatement = String.format("select * from Genre where %1$s = :%1$s", ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, id.getValue());
        Genre result;
        try {
            result = jdbcNamedTemplate.queryForObject(sqlStatement, sqlParams, (rs, row) -> makeGenre(rs));
        } catch (EmptyResultDataAccessException ex) {
            throw new KeyNotFoundException(idNotFoundMsg(id), this.getClass(), log);
        }

        log.info("Выполнено {}.read({})", this.getClass().getName(), id);

        return result;
    }

    public List<Genre> selectAll() {
        final String sqlStatement = String.format("select * from Genre order by %1$s", ID_FIELD);
        List<Genre> result = jdbcNamedTemplate.query(sqlStatement, (rs, row) -> makeGenre(rs));

        log.info("Выполнено {}.selectAll()", this.getClass().getName());

        return result;
    }

    public Genre create(Genre item) {
        final String sqlStatement = String.format("insert into Genre (%1$s) values ( :%1$s )", NAME_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(NAME_FIELD, item.getName());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcNamedTemplate.update(sqlStatement, sqlParams, keyHolder, new String[]{ID_FIELD});
        GenreIdType newId = GenreIdType.of(Objects.requireNonNull(keyHolder.getKey()).intValue());
        Genre result = (Genre) item.clone(); //Развязываем образец и результат
        result.setId(newId);
        log.info("Выполнено {}.create({}) => {}", this.getClass().getName(), item, newId);
        return result;
    }

    public void update(Genre item) {
        final String sqlStatement = String.format("update Genre set %1$s = :%1$s where %2$s = :%2$s", NAME_FIELD, ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(NAME_FIELD, item.getName())
                .addValue(ID_FIELD, item.getId().getValue());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(item.getId()), this.getClass(), log);
        }

        log.info("Выполнено {}.update({})", this.getClass().getName(), item);
    }

    public void delete(GenreIdType id) {
        final String sqlStatement = String.format("delete from Genre where %1$s = :%1$s", ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, id.getValue());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(id), this.getClass(), log);
        }

        log.info("Выполнено {}.delete({})", this.getClass().getName(), id);
    }

    public static Genre makeGenre(ResultSet rs) throws SQLException {
        log.debug("Вызов {}.makeGenre({})", GenreDAO.class.getName(), rs);
        return Genre.builder()
                .id(GenreIdType.of(rs.getInt(ID_FIELD)))
                .name(rs.getString(NAME_FIELD))
                .build();
    }

}
