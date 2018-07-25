/*
 * Copyright 1999-2015 dangdang.com.
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

package io.shardingsphere.dbtest.config.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class DDLDataSetAssert implements DataSetAssert {

    @XmlAttribute(name = "id")
    private String id;

    @XmlAttribute(name = "init-sql")
    private String initSql;
    
    @XmlAttribute(name = "sharding-rule-types")
    private String shardingRuleTypes;
    
    @XmlAttribute(name = "database-types")
    private String databaseTypes;
    
    @XmlAttribute(name = "clean-sql")
    private String cleanSql;

    @XmlAttribute(name = "expected-data-file")
    private String expectedDataFile;
    
    @XmlAttribute(name = "sql")
    private String sql;
    
    @XmlAttribute(name = "table")
    private String table;

    @XmlElement(name = "parameter")
    private ParameterDefinition parameter = new ParameterDefinition();
    
    @XmlElement(name = "subAssert")
    private List<AssertSubDefinition> subAsserts = new ArrayList<>();
    
    private String path;
    
    @Override
    public String toString() {
        return id;
    }
}
