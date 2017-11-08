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

import java.util.Set;
import java.util.HashSet;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

import jp.co.yahoo.dataplatform.config.Configuration;

import jp.co.yahoo.dataplatform.schema.objects.*;

import jp.co.yahoo.dataplatform.mds.spread.expression.*;
import jp.co.yahoo.dataplatform.mds.spread.column.filter.*;
import jp.co.yahoo.dataplatform.mds.spread.column.*;
import jp.co.yahoo.dataplatform.mds.binary.*;
import jp.co.yahoo.dataplatform.mds.binary.maker.*;

public class TestBooleanCellIndex{

  @DataProvider(name = "target_class")
  public Object[][] data1() throws IOException{
    return new Object[][] {
      { createBooleanTestData( "jp.co.yahoo.dataplatform.mds.binary.maker.DumpBooleanColumnBinaryMaker" ) },

      { createStringTestData( "jp.co.yahoo.dataplatform.mds.binary.maker.OptimizeStringColumnBinaryMaker" ) },
      { createStringTestData( "jp.co.yahoo.dataplatform.mds.binary.maker.OptimizeDumpStringColumnBinaryMaker" ) },

      { createBytesTestData( "jp.co.yahoo.dataplatform.mds.binary.maker.DumpBytesColumnBinaryMaker" ) },
    };
  }

  public IColumn createBooleanTestData( final String targetClassName ) throws IOException{
    IColumn column = new PrimitiveColumn( ColumnType.BOOLEAN , "column" );
    column.add( ColumnType.BOOLEAN , new BooleanObj( true ) , 0 );
    column.add( ColumnType.BOOLEAN , new BooleanObj( false ) , 1 );
    column.add( ColumnType.BOOLEAN , new BooleanObj( true ) , 2 );
    column.add( ColumnType.BOOLEAN , new BooleanObj( false ) , 3 );
    column.add( ColumnType.BOOLEAN , new BooleanObj( true ) , 4 );
    column.add( ColumnType.BOOLEAN , new BooleanObj( false ) , 5 );
    column.add( ColumnType.BOOLEAN , new BooleanObj( true ) , 6 );
    column.add( ColumnType.BOOLEAN , new BooleanObj( false ) , 7 );
    column.add( ColumnType.BOOLEAN , new BooleanObj( true ) , 8 );
    column.add( ColumnType.BOOLEAN , new BooleanObj( false ) , 9 );

    column.add( ColumnType.BOOLEAN , new BooleanObj( true ) , 20 );
    column.add( ColumnType.BOOLEAN , new BooleanObj( false ) , 21 );
    column.add( ColumnType.BOOLEAN , new BooleanObj( true ) , 22 );
    column.add( ColumnType.BOOLEAN , new BooleanObj( false ) , 23 );
    column.add( ColumnType.BOOLEAN , new BooleanObj( true ) , 24 );
    column.add( ColumnType.BOOLEAN , new BooleanObj( false ) , 25 );
    column.add( ColumnType.BOOLEAN , new BooleanObj( true ) , 26 );
    column.add( ColumnType.BOOLEAN , new BooleanObj( false ) , 27 );
    column.add( ColumnType.BOOLEAN , new BooleanObj( true ) , 28 );
    column.add( ColumnType.BOOLEAN , new BooleanObj( false ) , 29 );

    IColumnBinaryMaker maker = FindColumnBinaryMaker.get( targetClassName );
    ColumnBinaryMakerConfig defaultConfig = new ColumnBinaryMakerConfig();
    ColumnBinaryMakerCustomConfigNode configNode = new ColumnBinaryMakerCustomConfigNode( "root" , defaultConfig );
    ColumnBinary columnBinary = maker.toBinary( defaultConfig , null , column );
    return maker.toColumn( columnBinary );
  }

  public IColumn createStringTestData( final String targetClassName ) throws IOException{
    IColumn column = new PrimitiveColumn( ColumnType.STRING , "column" );
    column.add( ColumnType.STRING , new StringObj( "true" ) , 0 );
    column.add( ColumnType.STRING , new StringObj( "false" ) , 1 );
    column.add( ColumnType.STRING , new StringObj( "true" ) , 2 );
    column.add( ColumnType.STRING , new StringObj( "false" ) , 3 );
    column.add( ColumnType.STRING , new StringObj( "true" ) , 4 );
    column.add( ColumnType.STRING , new StringObj( "false" ) , 5 );
    column.add( ColumnType.STRING , new StringObj( "true" ) , 6 );
    column.add( ColumnType.STRING , new StringObj( "false" ) , 7 );
    column.add( ColumnType.STRING , new StringObj( "true" ) , 8 );
    column.add( ColumnType.STRING , new StringObj( "false" ) , 9 );

    column.add( ColumnType.STRING , new StringObj( "true" ) , 20 );
    column.add( ColumnType.STRING , new StringObj( "false" ) , 21 );
    column.add( ColumnType.STRING , new StringObj( "true" ) , 22 );
    column.add( ColumnType.STRING , new StringObj( "false" ) , 23 );
    column.add( ColumnType.STRING , new StringObj( "true" ) , 24 );
    column.add( ColumnType.STRING , new StringObj( "false" ) , 25 );
    column.add( ColumnType.STRING , new StringObj( "true" ) , 26 );
    column.add( ColumnType.STRING , new StringObj( "false" ) , 27 );
    column.add( ColumnType.STRING , new StringObj( "true" ) , 28 );
    column.add( ColumnType.STRING , new StringObj( "false" ) , 29 );

    IColumnBinaryMaker maker = FindColumnBinaryMaker.get( targetClassName );
    ColumnBinaryMakerConfig defaultConfig = new ColumnBinaryMakerConfig();
    ColumnBinaryMakerCustomConfigNode configNode = new ColumnBinaryMakerCustomConfigNode( "root" , defaultConfig );
    ColumnBinary columnBinary = maker.toBinary( defaultConfig , null , column );
    return maker.toColumn( columnBinary );
  }

  public IColumn createBytesTestData( final String targetClassName ) throws IOException{
    IColumn column = new PrimitiveColumn( ColumnType.BYTES , "column" );
    column.add( ColumnType.BYTES , new BytesObj( "true".getBytes() ) , 0 );
    column.add( ColumnType.BYTES , new BytesObj( "false".getBytes() ) , 1 );
    column.add( ColumnType.BYTES , new BytesObj( "true".getBytes() ) , 2 );
    column.add( ColumnType.BYTES , new BytesObj( "false".getBytes() ) , 3 );
    column.add( ColumnType.BYTES , new BytesObj( "true".getBytes() ) , 4 );
    column.add( ColumnType.BYTES , new BytesObj( "false".getBytes() ) , 5 );
    column.add( ColumnType.BYTES , new BytesObj( "true".getBytes() ) , 6 );
    column.add( ColumnType.BYTES , new BytesObj( "false".getBytes() ) , 7 );
    column.add( ColumnType.BYTES , new BytesObj( "true".getBytes() ) , 8 );
    column.add( ColumnType.BYTES , new BytesObj( "false".getBytes() ) , 9 );

    column.add( ColumnType.BYTES , new BytesObj( "true".getBytes() ) , 20 );
    column.add( ColumnType.BYTES , new BytesObj( "false".getBytes() ) , 21 );
    column.add( ColumnType.BYTES , new BytesObj( "true".getBytes() ) , 22 );
    column.add( ColumnType.BYTES , new BytesObj( "false".getBytes() ) , 23 );
    column.add( ColumnType.BYTES , new BytesObj( "true".getBytes() ) , 24 );
    column.add( ColumnType.BYTES , new BytesObj( "false".getBytes() ) , 25 );
    column.add( ColumnType.BYTES , new BytesObj( "true".getBytes() ) , 26 );
    column.add( ColumnType.BYTES , new BytesObj( "false".getBytes() ) , 27 );
    column.add( ColumnType.BYTES , new BytesObj( "true".getBytes() ) , 28 );
    column.add( ColumnType.BYTES , new BytesObj( "false".getBytes() ) , 29 );

    IColumnBinaryMaker maker = FindColumnBinaryMaker.get( targetClassName );
    ColumnBinaryMakerConfig defaultConfig = new ColumnBinaryMakerConfig();
    ColumnBinaryMakerCustomConfigNode configNode = new ColumnBinaryMakerCustomConfigNode( "root" , defaultConfig );
    ColumnBinary columnBinary = maker.toBinary( defaultConfig , null , column );
    return maker.toColumn( columnBinary );
  }

  public void dumpFilterResult( final boolean[] result ){
    System.out.println( "-----------------------" );
    System.out.println( "String cell index test result." );
    System.out.println( "-----------------------" );
    for( int i = 0 ; i < result.length ; i++ ){
      System.out.println( String.format( "index:%d = %s" , i , Boolean.toString( result[i] ) ) );
    }
  }

  @Test( dataProvider = "target_class" )
  public void T_match_1( final IColumn column ) throws IOException{
    int[] mustReadIndex = { 0 , 2 , 6 , 8 , 20 , 22 , 24 , 26 , 28 };
    IFilter filter = new BooleanFilter( true );
    boolean[] filterResult = new boolean[30];
    filterResult = column.filter( filter , filterResult );
    if( filterResult == null ){
      assertTrue( true );
      return;
    }
    for( int i = 0 ; i < mustReadIndex.length ; i++ ){
      assertTrue( filterResult[mustReadIndex[i]] );
    }
  }

  @Test( dataProvider = "target_class" )
  public void T_match_2( final IColumn column ) throws IOException{
    int[] mustReadIndex = { 1 , 3 , 5 , 7 , 9 , 21 , 23 , 25 , 27 , 29 };
    IFilter filter = new BooleanFilter( false );
    boolean[] filterResult = new boolean[30];
    filterResult = column.filter( filter , filterResult );
    if( filterResult == null ){
      assertTrue( true );
      return;
    }
    for( int i = 0 ; i < mustReadIndex.length ; i++ ){
      assertTrue( filterResult[mustReadIndex[i]] );
    }
  }

}
