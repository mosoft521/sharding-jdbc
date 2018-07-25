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
import io.shardingsphere.core.common.base.AbstractSQLAssertTest;
import io.shardingsphere.core.common.env.ShardingTestStrategy;
import io.shardingsphere.core.constant.DatabaseType;
import io.shardingsphere.core.constant.SQLType;
import io.shardingsphere.core.integrate.fixture.ComplexKeysModuloDatabaseShardingAlgorithm;
import io.shardingsphere.core.integrate.jaxb.SQLShardingRule;
import io.shardingsphere.core.integrate.jaxb.helper.SQLAssertJAXBHelper;
import io.shardingsphere.core.jdbc.core.datasource.ShardingDataSource;
import io.shardingsphere.core.rule.ShardingRule;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class AbstractNullableShardingTableOnlyTest extends AbstractSQLAssertTest {
    
    public AbstractNullableShardingTableOnlyTest(final String testCaseName, final String sql, final DatabaseType type, final List<SQLShardingRule> sqlShardingRules) {
        super(testCaseName, sql, type, sqlShardingRules);
    }
    
    protected static Collection<Object[]> dataParameters(final SQLType... sqlTypes) {
        List<Object[]> result = new LinkedList<>();
        for (SQLType each : sqlTypes) {
            result.addAll(SQLAssertJAXBHelper.getDataParameters("integrate/assert/select_aggregate.xml", each));
            result.addAll(SQLAssertJAXBHelper.getDataParameters("integrate/assert/select_nullable.xml", each));
        }
        return result;
    }
    
    protected static List<String> getInitFiles() {
        return Arrays.asList(
                "integrate/dataset/sharding/nullable/init/nullable_0.xml",
                "integrate/dataset/sharding/nullable/init/nullable_1.xml",
                "integrate/dataset/sharding/nullable/init/nullable_2.xml",
                "integrate/dataset/sharding/nullable/init/nullable_3.xml",
                "integrate/dataset/sharding/nullable/init/nullable_4.xml",
                "integrate/dataset/sharding/nullable/init/nullable_5.xml",
                "integrate/dataset/sharding/nullable/init/nullable_6.xml",
                "integrate/dataset/sharding/nullable/init/nullable_7.xml",
                "integrate/dataset/sharding/nullable/init/nullable_8.xml",
                "integrate/dataset/sharding/nullable/init/nullable_9.xml");
    }
    
    @Override
    protected ShardingTestStrategy getShardingStrategy() {
        return ShardingTestStrategy.nullable;
    }
    
    @Override
    protected List<String> getInitDataSetFiles() {
        return AbstractNullableShardingTableOnlyTest.getInitFiles();
    }
    
    @Override
    protected final Map<DatabaseType, ShardingDataSource> getDataSources() throws SQLException {
        if (!getShardingDataSources().isEmpty()) {
            return getShardingDataSources();
        }
        Map<DatabaseType, Map<String, DataSource>> dataSourceMap = createDataSourceMap();
        for (Map.Entry<DatabaseType, Map<String, DataSource>> each : dataSourceMap.entrySet()) {
            ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
            TableRuleConfiguration tableRuleConfig = new TableRuleConfiguration();
            tableRuleConfig.setLogicTable("t_order");
            shardingRuleConfig.getTableRuleConfigs().add(tableRuleConfig);
            shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new ComplexShardingStrategyConfiguration("user_id", new ComplexKeysModuloDatabaseShardingAlgorithm()));
            ShardingRule shardingRule = new ShardingRule(shardingRuleConfig, each.getValue().keySet());
            getShardingDataSources().put(each.getKey(), new ShardingDataSource(each.getValue(), shardingRule));
        }
        return getShardingDataSources();
    }
}
