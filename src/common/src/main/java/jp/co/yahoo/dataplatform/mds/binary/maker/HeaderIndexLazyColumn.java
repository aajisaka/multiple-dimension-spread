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
package jp.co.yahoo.dataplatform.mds.binary.maker;

import java.io.IOException;

import jp.co.yahoo.dataplatform.mds.spread.column.ColumnType;
import jp.co.yahoo.dataplatform.mds.spread.column.filter.IFilter;
import jp.co.yahoo.dataplatform.mds.spread.column.index.ICellIndex;

public class HeaderIndexLazyColumn extends LazyColumn{

  private final ICellIndex rangeIndex;

  public HeaderIndexLazyColumn( final String columnName , final ColumnType columnType , final IColumnManager columnManager , final ICellIndex rangeIndex ){
    super( columnName , columnType , columnManager );
    this.rangeIndex = rangeIndex;
  }
 
  @Override
  public boolean[] filter( final IFilter filter , final boolean[] filterArray ) throws IOException{
    boolean[] result = rangeIndex.filter( filter , filterArray );
    if( result != null ){
      return result;
    }
    return super.filter( filter , filterArray );
  }

}
