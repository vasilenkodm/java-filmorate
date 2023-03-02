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

    private String idNotFoundMsg(GenreIdType _id) {
        return String.format("Не найден жанр с кодом %s!", _id);
    }
    public Genre read(final GenreIdType _id) {
        String sqlStatement = String.format("select * from Genre where %1$s = :%1$s", ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, _id.getValue());
        Genre result;
        try {
            result = jdbcNamedTemplate.queryForObject(sqlStatement, sqlParams, (rs, row) -> makeGenre(rs));
        } catch (EmptyResultDataAccessException ex){
            throw new KeyNotFoundException(idNotFoundMsg(_id), this.getClass(), log);
        }

        log.info("Выполнено {}.read({})", this.getClass().getName(), _id);

        return result;
    }

    public List<Genre> selectAll() {
        final String sqlStatement = String.format("select * from Genre order by %1$s", ID_FIELD);
        List<Genre> result = jdbcNamedTemplate.query(sqlStatement, (rs, row) -> makeGenre(rs));

        log.info("Выполнено {}.selectAll()", this.getClass().getName());

        return result;
    }

    public GenreIdType create(Genre _genre) {
        final String sqlStatement = String.format("insert into Genre (%1$s) values ( :%1$s )", NAME_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(NAME_FIELD, _genre.getName());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcNamedTemplate.update(sqlStatement, sqlParams, keyHolder, new String[]{ID_FIELD});

        GenreIdType result = GenreIdType.of(Objects.requireNonNull(keyHolder.getKey()).intValue());
        log.info("Выполнено {}.create({}) => {}", this.getClass().getName(), _genre, result);
        return result;
    }

    public void update(Genre _genre) {
        final String sqlStatement = String.format("update Genre set %1$s = :%1$s where %2$s = :%2$s", NAME_FIELD, ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(NAME_FIELD, _genre.getName())
                .addValue(ID_FIELD, _genre.getId().getValue());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount==0) {
            throw new KeyNotFoundException(idNotFoundMsg(_genre.getId()), this.getClass(), log);
        }

        log.info("Выполнено {}.update({})", this.getClass().getName(), _genre);
    }

    public void delete(GenreIdType _id) {
        final String sqlStatement = String.format("delete from Genre where %1$s = :%1$s", ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, _id.getValue());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(_id), this.getClass(), log);
        }

        log.info("Выполнено {}.delete({})", this.getClass().getName(), _id);
    }

    public static Genre makeGenre(ResultSet _rs) throws SQLException {
        log.debug("Вызов {}.makeGenre({})", GenreDAO.class.getName(), _rs);
        return Genre.builder()
                .id(GenreIdType.of(_rs.getInt(ID_FIELD)))
                .name(_rs.getString(NAME_FIELD))
                .build();
    }

}
