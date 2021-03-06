/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.co.yahoo.dataplatform.mds.spread.expand;

import jp.co.yahoo.dataplatform.config.Configuration;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

public class TestExpandFunctionFactory {

    @Test
    public void T_get_null() throws IOException {
        Configuration conf = new Configuration();
        assertEquals( ExpandFunctionFactory.get(conf).getExpandColumnName().size(), 0 );
    }

    @Test
    public void T_get_notNull() throws IOException {
        Configuration conf = new Configuration();
        conf.set( "spread.reader.expand.column" , "{ \"base\" : { \"node\" : \"val_node\" ,  \"link_name\" : \"val_link_name\" , \"child_node\" : { \"node\" : \"val_child_node\"  , \"link_name\" : \"val_chilc_linkname\" } } }" );
        List<String[]> columnNames = ExpandFunctionFactory.get(conf).getExpandColumnName();
        assertEquals(columnNames.get(0)[0], "val_node");
    }

}
