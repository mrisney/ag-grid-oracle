package com.itis.ag.grid.oracle.dao;

import com.itis.ag.grid.oracle.builder.OracleSqlQueryBuilder;
import com.itis.ag.grid.oracle.request.ColumnVO;
import com.itis.ag.grid.oracle.request.EnterpriseGetRowsRequest;
import com.itis.ag.grid.oracle.response.EnterpriseGetRowsResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.itis.ag.grid.oracle.builder.EnterpriseResponseBuilder.createResponse;
import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;

@Repository("tradeDao")
public class CrashesDao {

    private JdbcTemplate template;
    private OracleSqlQueryBuilder queryBuilder;

    @Autowired
    public CrashesDao(JdbcTemplate template) {
        this.template = template;
        queryBuilder = new OracleSqlQueryBuilder();
    }

    public EnterpriseGetRowsResponse getData(EnterpriseGetRowsRequest request) {
        String tableName = "mapview_crashes_mv"; // could be supplied in request as a lookup key?

        // first obtain the pivot values from the DB for the requested pivot columns
        Map<String, List<String>> pivotValues = getPivotValues(request.getPivotCols());

        // generate sql
        String sql = queryBuilder.createSql(request, tableName, pivotValues);

        // query db for rows
        List<Map<String, Object>> rows = template.queryForList(sql);

        // create response with our results
        return createResponse(request, rows, pivotValues);
    }

    private Map<String, List<String>> getPivotValues(List<ColumnVO> pivotCols) {
        return pivotCols.stream()
                .map(ColumnVO::getField)
                .collect(toMap(pivotCol -> pivotCol, this::getPivotValues, (a, b) -> a, LinkedHashMap::new));
    }

    private List<String> getPivotValues(String pivotColumn) {
        String sql = "SELECT DISTINCT %s FROM mapview_crashes_mv";
        return template.queryForList(format(sql, pivotColumn), String.class);
    }
}