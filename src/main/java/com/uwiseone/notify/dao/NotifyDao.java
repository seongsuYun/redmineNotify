package com.uwiseone.notify.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class NotifyDao implements CommonDao {
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public List<Map<String, Object>> getIssueList() {
		StringBuffer query = new StringBuffer();
		query.append(" SELECT                                                                   ");
		query.append("    CONCAT('ISSUE[신규] ',SUBJECT,' 										");
		query.append("http://qa.dawins.net/issues/', ID) AS STR									");
		query.append(" FROM ISSUES                                                              ");
		query.append(" WHERE ASSIGNED_TO_ID = 100                                               ");
		query.append("   AND STATUS_ID <> 5                                                     ");
		query.append("   AND CREATED_ON BETWEEN DATE_ADD(NOW(), INTERVAL -3 MINUTE) AND NOW()   ");
		
		List<Map<String, Object>> list = jdbcTemplate.query(
				query.toString(),
				new Object[]{},
				new RowMapper<Map<String, Object>>() {
			@Override
			public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, Object> returnValue = new HashMap<String, Object>();
				returnValue.put("STR", rs.getString("STR"));
				return returnValue;
			}
		});
		
		return list;
	}
}
