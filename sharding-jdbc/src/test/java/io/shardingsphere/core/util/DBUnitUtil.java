/*
 * Copyright 2016-2018 shardingsphere.io.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingsphere.core.util;

import io.shardingsphere.core.common.env.DatabaseEnvironment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.ext.h2.H2Connection;
import org.dbunit.ext.mssql.MsSqlConnection;
import org.dbunit.ext.mysql.MySqlConnection;
import org.dbunit.ext.oracle.OracleConnection;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;

import java.sql.Connection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DBUnitUtil {
    
    /**
     * Get connection.
     * 
     * @param dbEnv Database environment
     * @param connection connection
     * @return database connection
     * @throws DatabaseUnitException DatabaseUnitException
     */
    public static IDatabaseConnection getConnection(final DatabaseEnvironment dbEnv, final Connection connection) throws DatabaseUnitException {
        switch (dbEnv.getDatabaseType()) {
            case H2:
                return new H2Connection(connection, "PUBLIC");
            case MySQL:
                return new MySqlConnection(connection, null);
            case PostgreSQL:
                DatabaseConnection databaseConnection = new DatabaseConnection(connection);
                databaseConnection.getConfig().setProperty("http://www.dbunit.org/properties/datatypeFactory", new PostgresqlDataTypeFactory());
                return databaseConnection;
            case Oracle:
                return new OracleConnection(connection, "JDBC");
            case SQLServer:
                return new MsSqlConnection(connection);
            default:
                throw new UnsupportedOperationException(dbEnv.getDatabaseType().name());
        }
    }
}
