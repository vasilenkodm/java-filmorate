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

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDAO implements BaseItemDAO<UserIdType, User> {

    public static final String ID_FIELD = "user_id";
    public static final String NAME_FIELD = "user_name";
    public static final String EMAIL_FIELD = "email";
    public static final String LOGIN_FIELD = "login";
    public static final String BIRTHDAY_FIELD = "birthday";
    private final NamedParameterJdbcTemplate jdbcNamedTemplate;

    private String idNotFoundMsg(UserIdType _id) {
        return String.format("Не найден пользователь с кодом %s!", _id);
    }
    public User read(final UserIdType _id) {
        String sqlStatement = String.format("select * from User where %1$s = :%1$s", ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, _id.getValue());
        User result;
        try {
            result = jdbcNamedTemplate.queryForObject(sqlStatement, sqlParams, (rs, row) -> makeUser(rs));
        } catch (EmptyResultDataAccessException ex){
            throw new KeyNotFoundException(idNotFoundMsg(_id), this.getClass(), log);
        }

        log.info("Выполнено {}.read({})", this.getClass().getName(), _id);

        return result;
    }

    public List<User> selectAll() {
        final String sqlStatement = String.format("select * from User order by %1$s", LOGIN_FIELD);
        List<User> result = jdbcNamedTemplate.query(sqlStatement, (rs, row) -> makeUser(rs));

        log.info("Выполнено {}.selectAll()", this.getClass().getName());

        return result;
    }

    public UserIdType create(User _user) {
        final String sqlStatement = String.format("insert into User (%1$s, %2$s, %3$s, %4$s) values ( :%1$s, :%2$s, :%3$s, :%4$s )"
                                        , NAME_FIELD, LOGIN_FIELD, EMAIL_FIELD, BIRTHDAY_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(NAME_FIELD, _user.getName())
                .addValue(LOGIN_FIELD, _user.getLogin())
                .addValue(EMAIL_FIELD, _user.getEmail())
                .addValue(BIRTHDAY_FIELD, _user.getBirthday());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcNamedTemplate.update( sqlStatement, sqlParams, keyHolder, new String[]{ID_FIELD});

        log.info("Выполнено {}.create({}) =>", this.getClass().getName(), _user, keyHolder.getKey());

        return UserIdType.of(keyHolder.getKey().longValue());
    }

    public void update(User _user) {
        final String sqlStatement = String.format("update User set %1$s = :%1$s, %2$s = :%2$s, %3$s = :%3$s, %4$s = :%4$s where %5$s = :%5$s",
                            NAME_FIELD, LOGIN_FIELD, EMAIL_FIELD, BIRTHDAY_FIELD, ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(NAME_FIELD, _user.getName())
                .addValue(LOGIN_FIELD, _user.getLogin())
                .addValue(EMAIL_FIELD, _user.getEmail())
                .addValue(BIRTHDAY_FIELD, _user.getBirthday())
                .addValue(ID_FIELD, _user.getId().getValue());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount==0) {
            throw new KeyNotFoundException(idNotFoundMsg(_user.getId()), this.getClass(), log);
        }

        log.info("Выполнено {}.update({})", this.getClass().getName(), _user);
    }

    public void delete(UserIdType _id) {
        final String sqlStatement = String.format("delete from User where %1$s = :%1$s", ID_FIELD);
        SqlParameterSource sqlParams = new MapSqlParameterSource()
                .addValue(ID_FIELD, _id.getValue());
        int rowCount = jdbcNamedTemplate.update(sqlStatement, sqlParams);

        if (rowCount==0) {
            throw new KeyNotFoundException(idNotFoundMsg(_id), this.getClass(), log);
        }

        log.info("Выполнено {}.delete({})", this.getClass().getName(), _id);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        return User.builder()
                    .id(UserIdType.of(rs.getLong(ID_FIELD)))
                    .name(rs.getString(NAME_FIELD))
                    .email(rs.getString(EMAIL_FIELD))
                    .login(rs.getString(LOGIN_FIELD))
                    .birthday(rs.getDate(NAME_FIELD).toLocalDate())
                    .build();
    }

    public void sendFriendshipInvite(UserIdType _id) {

    }

    public void acceptFriendshipInvite(UserIdType _id) {

    }

    public void breakFriendship(UserIdType _id) {

    }
}
