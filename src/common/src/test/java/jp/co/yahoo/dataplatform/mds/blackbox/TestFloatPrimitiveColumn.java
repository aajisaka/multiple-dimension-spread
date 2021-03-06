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
package jp.co.yahoo.dataplatform.mds.blackbox;

import java.io.IOException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

import jp.co.yahoo.dataplatform.config.Configuration;

import jp.co.yahoo.dataplatform.schema.objects.*;

import jp.co.yahoo.dataplatform.mds.spread.expression.*;
import jp.co.yahoo.dataplatform.mds.spread.column.filter.*;
import jp.co.yahoo.dataplatform.mds.spread.column.*;
import jp.co.yahoo.dataplatform.mds.binary.*;
import jp.co.yahoo.dataplatform.mds.binary.maker.*;

public class TestFloatPrimitiveColumn {

  @DataProvider(name = "target_class")
  public Object[][] data1() throws IOException{
    return new Object[][] {
      { "jp.co.yahoo.dataplatform.mds.binary.maker.DumpFloatColumnBinaryMaker" },
      { "jp.co.yahoo.dataplatform.mds.binary.maker.RangeDumpFloatColumnBinaryMaker" },
      { "jp.co.yahoo.dataplatform.mds.binary.maker.OptimizeFloatColumnBinaryMaker" },
      { "jp.co.yahoo.dataplatform.mds.binary.maker.UnsafeRangeDumpFloatColumnBinaryMaker" },
      { "jp.co.yahoo.dataplatform.mds.binary.maker.UnsafeOptimizeFloatColumnBinaryMaker" },
    };
  }

  public IColumn createNotNullColumn( final String targetClassName ) throws IOException{
    IColumn column = new PrimitiveColumn( ColumnType.FLOAT , "column" );
    column.add( ColumnType.FLOAT , new FloatObj( Float.MAX_VALUE ) , 0 );
    column.add( ColumnType.FLOAT , new FloatObj( Float.MIN_VALUE ) , 1 );
    column.add( ColumnType.FLOAT , new FloatObj( -200.0f ) , 2 );
    column.add( ColumnType.FLOAT , new FloatObj( -300.1f ) , 3 );
    column.add( ColumnType.FLOAT , new FloatObj( -400.2f ) , 4 );
    column.add( ColumnType.FLOAT , new FloatObj( -500.3f ) , 5 );
    column.add( ColumnType.FLOAT , new FloatObj( -600.4f ) , 6 );
    column.add( ColumnType.FLOAT , new FloatObj( 700.5f ) , 7 );
    column.add( ColumnType.FLOAT , new FloatObj( 800.6f ) , 8 );
    column.add( ColumnType.FLOAT , new FloatObj( 900.7f ) , 9 );
    column.add( ColumnType.FLOAT , new FloatObj( 0.0f ) , 10 );

    IColumnBinaryMaker maker = FindColumnBinaryMaker.get( targetClassName );
    ColumnBinaryMakerConfig defaultConfig = new ColumnBinaryMakerConfig();
    ColumnBinaryMakerCustomConfigNode configNode = new ColumnBinaryMakerCustomConfigNode( "root" , defaultConfig );
    ColumnBinary columnBinary = maker.toBinary( defaultConfig , null , column );
    return FindColumnBinaryMaker.get( columnBinary.makerClassName ).toColumn( columnBinary );
  }

  public IColumn createNullColumn( final String targetClassName ) throws IOException{
    IColumn column = new PrimitiveColumn( ColumnType.FLOAT , "column" );

    IColumnBinaryMaker maker = FindColumnBinaryMaker.get( targetClassName );
    ColumnBinaryMakerConfig defaultConfig = new ColumnBinaryMakerConfig();
    ColumnBinaryMakerCustomConfigNode configNode = new ColumnBinaryMakerCustomConfigNode( "root" , defaultConfig );
    ColumnBinary columnBinary = maker.toBinary( defaultConfig , null , column );
    return  FindColumnBinaryMaker.get( columnBinary.makerClassName ).toColumn( columnBinary );
  }

  public IColumn createHasNullColumn( final String targetClassName ) throws IOException{
    IColumn column = new PrimitiveColumn( ColumnType.FLOAT , "column" );
    column.add( ColumnType.FLOAT , new FloatObj( (float)0 ) , 0 );
    column.add( ColumnType.FLOAT , new FloatObj( (float)4 ) , 4 );
    column.add( ColumnType.FLOAT , new FloatObj( (float)8 ) , 8 );

    IColumnBinaryMaker maker = FindColumnBinaryMaker.get( targetClassName );
    ColumnBinaryMakerConfig defaultConfig = new ColumnBinaryMakerConfig();
    ColumnBinaryMakerCustomConfigNode configNode = new ColumnBinaryMakerCustomConfigNode( "root" , defaultConfig );
    ColumnBinary columnBinary = maker.toBinary( defaultConfig , null , column );
    return FindColumnBinaryMaker.get( columnBinary.makerClassName ).toColumn( columnBinary );
  }

  public IColumn createLastCellColumn( final String targetClassName ) throws IOException{
    IColumn column = new PrimitiveColumn( ColumnType.FLOAT , "column" );
    column.add( ColumnType.FLOAT , new FloatObj( Float.MAX_VALUE ) , 10000 );

    IColumnBinaryMaker maker = FindColumnBinaryMaker.get( targetClassName );
    ColumnBinaryMakerConfig defaultConfig = new ColumnBinaryMakerConfig();
    ColumnBinaryMakerCustomConfigNode configNode = new ColumnBinaryMakerCustomConfigNode( "root" , defaultConfig );
    ColumnBinary columnBinary = maker.toBinary( defaultConfig , null , column );
    return FindColumnBinaryMaker.get( columnBinary.makerClassName ).toColumn( columnBinary );
  }

  @Test( dataProvider = "target_class" )
  public void T_notNull_1( final String targetClassName ) throws IOException{
    IColumn column = createNotNullColumn( targetClassName );
    assertEquals( ( (PrimitiveObject)( column.get(0).getRow() ) ).getFloat() , Float.MAX_VALUE );
    assertEquals( ( (PrimitiveObject)( column.get(1).getRow() ) ).getFloat() , Float.MIN_VALUE );
    assertEquals( ( (PrimitiveObject)( column.get(2).getRow() ) ).getFloat() , -200.0f );
    assertEquals( ( (PrimitiveObject)( column.get(3).getRow() ) ).getFloat() , -300.1f );
    assertEquals( ( (PrimitiveObject)( column.get(4).getRow() ) ).getFloat() , -400.2f );
    assertEquals( ( (PrimitiveObject)( column.get(5).getRow() ) ).getFloat() , -500.3f );
    assertEquals( ( (PrimitiveObject)( column.get(6).getRow() ) ).getFloat() , -600.4f );
    assertEquals( ( (PrimitiveObject)( column.get(7).getRow() ) ).getFloat() , 700.5f );
    assertEquals( ( (PrimitiveObject)( column.get(8).getRow() ) ).getFloat() , 800.6f );
    assertEquals( ( (PrimitiveObject)( column.get(9).getRow() ) ).getFloat() , 900.7f );
    assertEquals( ( (PrimitiveObject)( column.get(10).getRow() ) ).getFloat() , 0.0f );
  }

  @Test( dataProvider = "target_class" )
  public void T_null_1( final String targetClassName ) throws IOException{
    IColumn column = createNullColumn( targetClassName );
    assertNull( column.get(0).getRow() );
    assertNull( column.get(1).getRow() );
  }

  @Test( dataProvider = "target_class" )
  public void T_hasNull_1( final String targetClassName ) throws IOException{
    IColumn column = createHasNullColumn( targetClassName );
    assertEquals( ( (PrimitiveObject)( column.get(0).getRow() ) ).getFloat() , (float)0 );
    assertNull( column.get(1).getRow() );
    assertNull( column.get(2).getRow() );
    assertNull( column.get(3).getRow() );
    assertEquals( ( (PrimitiveObject)( column.get(4).getRow() ) ).getFloat() , (float)4 );
    assertNull( column.get(5).getRow() );
    assertNull( column.get(6).getRow() );
    assertNull( column.get(7).getRow() );
    assertEquals( ( (PrimitiveObject)( column.get(8).getRow() ) ).getFloat() , (float)8 );
  }

  @Test( dataProvider = "target_class" )
  public void T_lastCell_1( final String targetClassName ) throws IOException{
    IColumn column = createLastCellColumn( targetClassName );
    for( int i = 0 ; i < 10000 ; i++ ){
      assertNull( column.get(i).getRow() );
    }
    assertEquals( ( (PrimitiveObject)( column.get(10000).getRow() ) ).getFloat() , Float.MAX_VALUE );
  }

}
