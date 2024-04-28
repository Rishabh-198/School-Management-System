//This is done by JDBC. See below for JPA
// package com.RS.SMS.Repository;
//
//import com.RS.SMS.model.Holiday;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//import com.RS.SMS.Repository.HolidaysRepository;
//
//import java.util.List;
//
///*
//@Repository stereotype annotation is used to add a bean of this class
//type to the Spring context and indicate that given Bean is used to perform
//DB related operations and
//* */
//@Repository
//public class HolidaysRepository {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    public HolidaysRepository(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    public List<Holiday> findAllHolidays() {
//        String sql = "SELECT * FROM holidays";
//        var rowMapper = BeanPropertyRowMapper.newInstance(Holiday.class);
//        return jdbcTemplate.query(sql, rowMapper);
//    }
//
//}

package com.RS.SMS.Repository;
import com.RS.SMS.model.Holiday;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidaysRepository extends CrudRepository<Holiday, String> {


}
