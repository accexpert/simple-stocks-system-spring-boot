package com.acc.stocks.repositories;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IDataRepository<T> {
    void addRecord(T stock) throws SQLException;
    T getRecord(Integer id) throws SQLException;
    T getRecord(String type) throws SQLException;
    void updateRecord(T stock) throws SQLException;
    void removeRecord(T stock) throws SQLException;
    T getLastRecord() throws SQLException;
    List<T> getRecords() throws SQLException;
}
