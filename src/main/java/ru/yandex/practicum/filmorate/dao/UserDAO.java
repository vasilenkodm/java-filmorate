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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.type.UserIdType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDAO implements ItemDAO<UserIdType, User> {

    public static final String ID_FIELD = "user_id";
    public static final String NAME_FIELD = "user_name";
    public static final String EMAIL_FIELD = "email";
    public static final String LOGIN_FIELD = "login";
    public static final String BIRTHDAY_FIELD = "birthday";
    public static final String PROPOSER_ID = "proposer_id";
    public static final String INVITED_ID = "invited_id";

    private final NamedParameterJdbcTemplate jdbcNamedTemplate;

    public String idNotFoundMsg(UserIdType id) {
        return idNotFoundMsg(id.toString());
    }

    private String idNotFoundMsg(String id) {
        return String.format("Не найден пользователь с кодом %s!", id);
    }

    public boolean notExists(UserIdType id) {
        String sqlStatement = String.format("select count(*) from UserInfo where %1$s = :%1$s", ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, id.getValue());
        Integer count = Objects.requireNonNull(jdbcNamedTemplate.queryForObject(sqlStatement, sqlParams, Integer.class));
        log.info("Выполнено {}.exists({})", this.getClass().getName(), id);
        return count == 0;
    }

    public User read(final UserIdType id) {
        String sqlStatement = String.format("select * from UserInfo where %1$s = :%1$s", ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, id.getValue());
        User result;
        try {
            result = jdbcNamedTemplate.queryForObject(sqlStatement, sqlParams, (rs, row) -> makeUser(rs));
        } catch (EmptyResultDataAccessException ex) {
            throw new KeyNotFoundException(idNotFoundMsg(id), this.getClass(), log);
        }

        log.info("Выполнено {}.read({})", this.getClass().getName(), id);

        return result;
    }

    public List<User> selectAll() {
        final String sqlStatement = String.format("select * from UserInfo order by %1$s", ID_FIELD);
        List<User> result = jdbcNamedTemplate.query(sqlStatement, (rs, row) -> makeUser(rs));

        log.info("Выполнено {}.selectAll()", this.getClass().getName());

        return result;
    }

    public User create(User item) {
        final String sqlStatement = String.format("insert into UserInfo (%1$s, %2$s, %3$s, %4$s) values ( :%1$s, :%2$s, :%3$s, :%4$s )"
                , NAME_FIELD, LOGIN_FIELD, EMAIL_FIELD, BIRTHDAY_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(NAME_FIELD, item.getName())
                .addValue(LOGIN_FIELD, item.getLogin())
                .addValue(EMAIL_FIELD, item.getEmail())
                .addValue(BIRTHDAY_FIELD, item.getBirthday());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcNamedTemplate.update(sqlStatement, sqlParams, keyHolder, new String[]{ID_FIELD});
        UserIdType newId = UserIdType.of(Objects.requireNonNull(keyHolder.getKey()).longValue());
        User result = (User) item.clone(); //Развязываем образец и результат
        result.setId(newId);
        log.info("Выполнено {}.create({}) => {}", this.getClass().getName(), item, newId);
        return result;
    }

    public void update(User item) {
        final String sqlStatement = String.format("update UserInfo set %1$s = :%1$s, %2$s = :%2$s, %3$s = :%3$s, %4$s = :%4$s where %5$s = :%5$s",
                NAME_FIELD, LOGIN_FIELD, EMAIL_FIELD, BIRTHDAY_FIELD, ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(NAME_FIELD, item.getName())
                .addValue(LOGIN_FIELD, item.getLogin())
                .addValue(EMAIL_FIELD, item.getEmail())
                .addValue(BIRTHDAY_FIELD, item.getBirthday())
                .addValue(ID_FIELD, item.getId().getValue());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount==0) {
            throw new KeyNotFoundException(idNotFoundMsg(item.getId()), this.getClass(), log);
        }

        log.info("Выполнено {}.update({})", this.getClass().getName(), item);
    }

    public void delete(UserIdType id) {
        final String sqlStatement = String.format("delete from UserInfo where %1$s = :%1$s", ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, id.getValue());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount == 0) {
            throw new KeyNotFoundException(idNotFoundMsg(id), this.getClass(), log);
        }

        log.info("Выполнено {}.delete({})", this.getClass().getName(), id);
    }

    private static User makeUser(ResultSet rs) throws SQLException {
        log.debug("Вызов {}.makeUser({})", UserDAO.class.getName(), rs);
        return User.builder()
                .id(UserIdType.of(rs.getLong(ID_FIELD)))
                .name(rs.getString(NAME_FIELD))
                .email(rs.getString(EMAIL_FIELD))
                .login(rs.getString(LOGIN_FIELD))
                .birthday(rs.getDate(BIRTHDAY_FIELD).toLocalDate())
                .build();
    }

    public List<User> getFriends(UserIdType id) {
        final String sqlStatement = String.format(" select u.* " +
                " from Friendship inv /* Приглашение к дружбе */ " +
                " join UserInfo u " +
                "    on u.user_id = inv.invited_id " +
                " where inv.proposer_id = :%1$s " +
                " order by u.user_id ", ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, id.getValue());

        List<User> result = jdbcNamedTemplate.query(sqlStatement, sqlParams, (rs, row) -> makeUser(rs));

        log.info("Выполнено {}.getFriends({})", this.getClass().getName(), id);

        return result;
    }

    public List<User> getCommonFriends(UserIdType id1, UserIdType id2) {
        if (notExists(id1)) throw new KeyNotFoundException(idNotFoundMsg(id1), this.getClass(), log);
        if (notExists(id2)) throw new KeyNotFoundException(idNotFoundMsg(id2), this.getClass(), log);

        final String sqlStatement = String.format("    select * " +
                "    from " +
                "    (select inv.invited_id " +
                "    from Friendship inv /* Приглашение к дружбе */ " +
                "    where inv.proposer_id = :%1$s " +
                "    ) f1 " +
                "    join " +
                "    (select inv.invited_id " +
                "    from Friendship inv /* Приглашение к дружбе */ " +
                "    where inv.proposer_id = :%2$s " +
                "    ) f2 on f2.invited_id = f1.invited_id " +
                "    join UserInfo u " +
                "        on u.user_id = f1.invited_id " +
                "    order by u.user_id;    ", PROPOSER_ID, INVITED_ID);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(PROPOSER_ID, id1.getValue())
                .addValue(INVITED_ID, id2.getValue());

        List<User> result = jdbcNamedTemplate.query(sqlStatement, sqlParams, (rs, row) -> makeUser(rs));

        log.info("Выполнено {}.getCommonFriends({}, {})", this.getClass().getName(), id1, id2);

        return result;
    }

    public void addFriend(UserIdType idFrom, UserIdType idTo) {
        final String sqlStatement = String.format("insert into Friendship ( %1$s , %2$s ) values ( :%1$s , :%2$s )"
                , PROPOSER_ID, INVITED_ID);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(PROPOSER_ID, idFrom.getValue())
                .addValue(INVITED_ID, idTo.getValue());
        int rowCount = 0;
        try {
            rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);
        } catch (Exception e) {
            log.debug("Ошибка выполнения запроса: {}", e.getMessage());
        }
        if (rowCount == 0) {
            log.debug("0 обработанных строк для запроса {} [{}]", sqlStatement, sqlParams);
            throw new KeyNotFoundException(idNotFoundMsg(idFrom + " или " + idTo), this.getClass(), log);
        }
        log.info("Выполнено {}.makeFriendship({}, {})", this.getClass().getName(), idFrom, idTo);
    }

    public void removeFriend(UserIdType idFrom, UserIdType idTo) {
        final String sqlStatement = String.format(" delete from Friendship " +
                " where %1$s = :%1$s and %2$s = :%2$s ", PROPOSER_ID, INVITED_ID);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(PROPOSER_ID, idFrom.getValue())
                .addValue(INVITED_ID, idTo.getValue());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount == 0) {
            log.debug("0 обработанных строк для запроса {} [{}]", sqlStatement, sqlParams);
            throw new KeyNotFoundException(idNotFoundMsg(idFrom + " или " + idTo), this.getClass(), log);
        }
        log.info("Выполнено {}.breakFriendship({}, {})", this.getClass().getName(), idFrom, idTo);
    }
}
