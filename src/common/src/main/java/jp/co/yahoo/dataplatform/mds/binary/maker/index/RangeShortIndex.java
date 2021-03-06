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
package jp.co.yahoo.dataplatform.mds.binary.maker.index;

import java.io.IOException;

import jp.co.yahoo.dataplatform.mds.spread.column.filter.IFilter;
import jp.co.yahoo.dataplatform.mds.spread.column.filter.NumberFilter;
import jp.co.yahoo.dataplatform.mds.spread.column.filter.NumberRangeFilter;
import jp.co.yahoo.dataplatform.mds.spread.column.index.ICellIndex;

public class RangeShortIndex implements ICellIndex{

  private final short min;
  private final short max;

  public RangeShortIndex( final short min , final short max ){
    this.min = min;
    this.max = max;
  }

  @Override
  public boolean[] filter( final IFilter filter , final boolean[] filterArray ) throws IOException{
    switch( filter.getFilterType() ){
      case NUMBER:
        NumberFilter numberFilter = (NumberFilter)filter;
        short setNumber;
        try{
          setNumber = numberFilter.getNumberObject().getShort();
        }catch( NumberFormatException e ){
          return null;
        }
        switch( numberFilter.getNumberFilterType() ){
          case EQUAL:
            if( setNumber < min || max < setNumber  ){
              return filterArray;
            }
            return null;
          case LT:
            if( setNumber <= min ){
              return filterArray;
            }
            return null;
          case LE:
            if( setNumber < min ){
              return filterArray;
            }
            return null;
          case GT:
            if( max <= setNumber ){
              return filterArray;
            }
            return null;
          case GE:
            if( max < setNumber ){
              return filterArray;
            }
            return null;
          default:
            return null;
        }
      case NUMBER_RANGE:
        NumberRangeFilter numberRangeFilter = (NumberRangeFilter)filter;
        short setMin;
        short setMax;
        try{
          setMin = numberRangeFilter.getMinObject().getShort();
          setMax = numberRangeFilter.getMaxObject().getShort();
        }catch( NumberFormatException e ){
          return null;
        }
        boolean invert = numberRangeFilter.isInvert();
        if( invert ){
          return null;
        }
        boolean minHasEquals = numberRangeFilter.isMinHasEquals();
        boolean maxHasEquals = numberRangeFilter.isMaxHasEquals();
        if( minHasEquals && maxHasEquals ){
          if( ( setMax < min || max < setMin ) ){
            return filterArray;
          }
          return null;
        }
        else if( minHasEquals ){
          if( ( setMax < min || max <= setMin ) ){
            return filterArray;
          }
          return null;
        }
        else if( maxHasEquals ){
          if( ( setMax <= min || max < setMin ) ){
            return filterArray;
          }
          return null;
        }
        else{
          if( ( setMax <= min || max <= setMin ) ){
            return filterArray;
          }
          return null;
        }
      default:
        return null;
    }
  }

}
