package com.xk.common.base;

import com.xk.upms.model.po.UpmsSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.util.List;

public interface BaseRepository extends JpaRepository<UpmsSystem, Long>, Serializable {

    @Query(value = "SELECT SUBSTR(a.COLUMN_COMMENT, 4)\n" +
            "FROM  information_schema.COLUMNS a \n" +
            "WHERE a.TABLE_NAME = 'upms_system'\n" +
            "ORDER BY SUBSTR(a.COLUMN_COMMENT, 1, 2)", nativeQuery = true)
    List<Object> queryTableComment(String tableName);

}
