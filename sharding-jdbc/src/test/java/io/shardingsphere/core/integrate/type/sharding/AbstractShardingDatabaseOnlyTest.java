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

package io.shardingsphere.core.integrate.type.sharding;

import io.shardingsphere.core.api.config.ShardingRuleConfiguration;
import io.shardingsphere.core.api.config.TableRuleConfiguration;
import io.shardingsphere.core.api.config.strategy.ComplexShardingStrategyConfiguration;
import io.shardingsphere.core.api.config.strategy.NoneShardingStrategyConfiguration;
import io.shardingsphere.core.common.base.AbstractSQLAssertTest;
import io.shardingsphere.core.common.env.ShardingTestStrategy;
import io.shardingsphere.core.constant.DatabaseType;
import io.shardingsphere.core.fixture.IncrementKeyGenerator;
import io.shardingsphere.core.integrate.fixture.ComplexKeysModuloDatabaseShardingAlgorithm;
import io.shardingsphere.core.integrate.jaxb.SQLShardingRule;
import io.shardingsphere.core.jdbc.core.datasource.ShardingDataSource;
import io.shardingsphere.core.rule.ShardingRule;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class AbstractShardingDatabaseOnlyTest extends AbstractSQLAssertTest {
    
    public AbstractShardingDatabaseOnlyTest(final String testCaseName, final String sql, final DatabaseType type, final List<SQLShardingRule> sqlShardingRules) {
        super(testCaseName, sql, type, sqlShardingRules);
    }
    
    protected static List<String> getInitFiles() {
        return Arrays.asList(
                "integrate/dataset/sharding/db/init/db_0.xml",
                "integrate/dataset/sharding/db/init/db_1.xml",
                "integrate/dataset/sharding/db/init/db_2.xml",
                "integrate/dataset/sharding/db/init/db_3.xml",
                "integrate/dataset/sharding/db/init/db_4.xml",
                "integrate/dataset/sharding/db/init/db_5.xml",
                "integrate/dataset/sharding/db/init/db_6.xml",
                "integrate/dataset/sharding/db/init/db_7.xml",
                "integrate/dataset/sharding/db/init/db_8.xml",
                "integrate/dataset/sharding/db/init/db_9.xml");
    }
    
    @Override
    protected ShardingTestStrategy getShardingStrategy() {
        return ShardingTestStrategy.db;
    }
    
    @Override
    protected List<String> getInitDataSetFiles() {
        return AbstractShardingDatabaseOnlyTest.getInitFiles();
    }
    
    @Override
    protected Map<DatabaseType, ShardingDataSource> getDataSources() throws SQLException {
        if (!getShardingDataSources().isEmpty()) {
            return getShardingDataSources();
        }
        Map<DatabaseType, Map<String, DataSource>> dataSourceMap = createDataSourceMap();
        for (Map.Entry<DatabaseType, Map<String, DataSource>> each : dataSourceMap.entrySet()) {
            final ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
            TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
            orderTableRuleConfig.setLogicTable("t_order");
            orderTableRuleConfig.setLogicIndex("t_order_index");
            orderTableRuleConfig.setKeyGeneratorColumnName("order_id");
            orderTableRuleConfig.setKeyGenerator(new IncrementKeyGenerator());
            shardingRuleConfig.getTableRuleConfigs().add(orderTableRuleConfig);
            TableRuleConfiguration orderItemTableRuleConfig = new TableRuleConfiguration();
            orderItemTableRuleConfig.setLogicTable("t_order_item");
            TableRuleConfiguration logTableRuleConfig = new TableRuleConfiguration();
            logTableRuleConfig.setLogicIndex("t_log_index");
            logTableRuleConfig.setLogicTable("t_log");
            TableRuleConfiguration tempLogTableRuleConfig = new TableRuleConfiguration();
            tempLogTableRuleConfig.setLogicTable("t_temp_log");
            shardingRuleConfig.getTableRuleConfigs().add(logTableRuleConfig);
            shardingRuleConfig.getTableRuleConfigs().add(tempLogTableRuleConfig);
            shardingRuleConfig.getTableRuleConfigs().add(orderItemTableRuleConfig);
            shardingRuleConfig.getBindingTableGroups().add("t_order, t_order_item");
            shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new ComplexShardingStrategyConfiguration("user_id", new ComplexKeysModuloDatabaseShardingAlgorithm()));
            shardingRuleConfig.setDefaultTableShardingStrategyConfig(new NoneShardingStrategyConfiguration());
            getShardingDataSources().put(each.getKey(), new ShardingDataSource(each.getValue(), new ShardingRule(shardingRuleConfig, each.getValue().keySet())));
        }
        return getShardingDataSources();
    }
}
