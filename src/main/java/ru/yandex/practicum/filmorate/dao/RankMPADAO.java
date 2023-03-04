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
import ru.yandex.practicum.filmorate.model.RankMPA;
import ru.yandex.practicum.filmorate.type.RankMPAIdType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class RankMPADAO implements ItemDAO<RankMPAIdType, RankMPA> {
    public static final String ID_FIELD = "rankMPA_id";
    public static final String NAME_FIELD = "rankMPA_name";
    private final NamedParameterJdbcTemplate jdbcNamedTemplate;

    private String idNotFoundMsg(RankMPAIdType _id) {
        return String.format("Не найден рейтинг с кодом %s!", _id);
    }

    public RankMPA read(final RankMPAIdType _id) {
        String sqlStatement = String.format("select * from RankMPA where %1$s = :%1$s", ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, _id.getValue());
        RankMPA result;
        try {
            result = jdbcNamedTemplate.queryForObject(sqlStatement, sqlParams, (rs, row) -> makeRankMPA(rs));
        } catch (EmptyResultDataAccessException ex){
            throw new KeyNotFoundException(idNotFoundMsg(_id), this.getClass(), log);
        }

        log.info("Выполнено {}.read({})", this.getClass().getName(), _id);

        return result;
    }

    public List<RankMPA> selectAll() {
        final String sqlStatement = String.format("select * from RankMPA order by %1$s", ID_FIELD);
        List<RankMPA> result = jdbcNamedTemplate.query(sqlStatement, (rs, row) -> makeRankMPA(rs));

        log.info("Выполнено {}.selectAll()", this.getClass().getName());

        return result;
    }

    public RankMPA create(RankMPA _source) {
        final String sqlStatement = String.format("insert into RankMPA (%1$s) values ( :%1$s )", NAME_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(NAME_FIELD, _source.getName());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcNamedTemplate.update(sqlStatement, sqlParams, keyHolder, new String[]{ID_FIELD});
        RankMPAIdType newId = RankMPAIdType.of(Objects.requireNonNull(keyHolder.getKey()).intValue());
        RankMPA result = (RankMPA) _source.clone(); //Развязываем образец и результат
        result.setId(newId);
        log.info("Выполнено {}.create({}) => {}", this.getClass().getName(), _source, newId);
        return result;
    }

    public void update(RankMPA _rankMPA) {
        final String sqlStatement = String.format("update RankMPA set %1$s = :%1$s where %2$s = :%2$s", NAME_FIELD, ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(NAME_FIELD, _rankMPA.getName())
                .addValue(ID_FIELD, _rankMPA.getId().getValue());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount==0) {
            throw new KeyNotFoundException(idNotFoundMsg(_rankMPA.getId()), this.getClass(), log);
        }

        log.info("Выполнено {}.update({})", this.getClass().getName(), _rankMPA);
    }

    public void delete(RankMPAIdType _id) {
        final String sqlStatement = String.format("delete from RankMPA where %1$s = :%1$s", ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, _id.getValue());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(_id), this.getClass(), log);
        }

        log.info("Выполнено {}.delete({})", this.getClass().getName(), _id);
    }

    public static RankMPA makeRankMPA(ResultSet _rs) throws SQLException {
        log.debug("Вызов {}.makeRankMPA({})", RankMPADAO.class.getName(), _rs);
        return RankMPA.builder()
                .id(RankMPAIdType.of(_rs.getInt(ID_FIELD)))
                .name(_rs.getString(NAME_FIELD))
                .build();
    }

}
