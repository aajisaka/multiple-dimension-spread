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
import java.nio.ByteBuffer;

import java.util.List;
import java.util.ArrayList;

import jp.co.yahoo.dataplatform.schema.objects.*;

import jp.co.yahoo.dataplatform.mds.binary.ColumnBinaryMakerConfig;
import jp.co.yahoo.dataplatform.mds.binary.ColumnBinaryMakerCustomConfigNode;
import jp.co.yahoo.dataplatform.mds.spread.column.ICell;
import jp.co.yahoo.dataplatform.mds.spread.column.ICellManager;
import jp.co.yahoo.dataplatform.mds.spread.column.IColumn;
import jp.co.yahoo.dataplatform.mds.spread.column.PrimitiveCell;
import jp.co.yahoo.dataplatform.mds.spread.column.PrimitiveColumn;
import jp.co.yahoo.dataplatform.mds.spread.column.ColumnType;
import jp.co.yahoo.dataplatform.mds.spread.column.filter.IFilter;
import jp.co.yahoo.dataplatform.mds.spread.column.index.ICellIndex;
import jp.co.yahoo.dataplatform.mds.spread.column.index.DefaultCellIndex;
import jp.co.yahoo.dataplatform.mds.spread.analyzer.IColumnAnalizeResult;
import jp.co.yahoo.dataplatform.mds.spread.expression.IExpressionIndex;
import jp.co.yahoo.dataplatform.mds.compressor.DefaultCompressor;
import jp.co.yahoo.dataplatform.mds.binary.ColumnBinary;
import jp.co.yahoo.dataplatform.mds.binary.UTF8BytesLinkObj;
import jp.co.yahoo.dataplatform.mds.binary.maker.index.RangeByteIndex;
import jp.co.yahoo.dataplatform.mds.binary.maker.index.RangeShortIndex;
import jp.co.yahoo.dataplatform.mds.binary.maker.index.RangeIntegerIndex;
import jp.co.yahoo.dataplatform.mds.binary.maker.index.RangeLongIndex;
import jp.co.yahoo.dataplatform.mds.binary.maker.index.RangeFloatIndex;
import jp.co.yahoo.dataplatform.mds.binary.maker.index.RangeDoubleIndex;
import jp.co.yahoo.dataplatform.mds.binary.maker.index.RangeStringIndex;
import jp.co.yahoo.dataplatform.mds.blockindex.BlockIndexNode;
import jp.co.yahoo.dataplatform.mds.blockindex.ByteRangeBlockIndex;
import jp.co.yahoo.dataplatform.mds.blockindex.ShortRangeBlockIndex;
import jp.co.yahoo.dataplatform.mds.blockindex.IntegerRangeBlockIndex;
import jp.co.yahoo.dataplatform.mds.blockindex.LongRangeBlockIndex;
import jp.co.yahoo.dataplatform.mds.blockindex.FloatRangeBlockIndex;
import jp.co.yahoo.dataplatform.mds.blockindex.DoubleRangeBlockIndex;
import jp.co.yahoo.dataplatform.mds.blockindex.StringRangeBlockIndex;
import jp.co.yahoo.dataplatform.mds.inmemory.IMemoryAllocator;

public class ConstantColumnBinaryMaker implements IColumnBinaryMaker{

  @Override
  public ColumnBinary toBinary(final ColumnBinaryMakerConfig commonConfig , final ColumnBinaryMakerCustomConfigNode currentConfigNode , final IColumn column , final MakerCache makerCache ) throws IOException{
    throw new UnsupportedOperationException( "Constant binary maker not support column to binary." );
  }

  @Override
  public int calcBinarySize( final IColumnAnalizeResult analizeResult ){
    throw new UnsupportedOperationException( "Constant binary maker not support column to binary." );
  }

  @Override
  public IColumn toColumn( final ColumnBinary columnBinary ) throws IOException{
    IColumn result = new PrimitiveColumn( columnBinary.columnType , columnBinary.columnName );
    ICellManager cellManager;
    
    ByteBuffer wrapBuffer = ByteBuffer.wrap( columnBinary.binary , columnBinary.binaryStart , columnBinary.binaryLength );
    switch( columnBinary.columnType ){
      case BOOLEAN:
        if( wrapBuffer.get() == 1 ){
          cellManager = new ConstantCellManager( columnBinary.columnType , new BooleanObj( true ) , columnBinary.rowCount );
        }
        else{
          cellManager = new ConstantCellManager( columnBinary.columnType , new BooleanObj( false ) , columnBinary.rowCount );
        }
        break;
      case BYTE:
        byte byteValue = wrapBuffer.get();
        cellManager = new ConstantCellManager( columnBinary.columnType , new ByteObj( byteValue ) , columnBinary.rowCount );
        cellManager.setIndex( new RangeByteIndex( byteValue , byteValue ) );
        break;
      case SHORT:
        short shortValue = wrapBuffer.getShort();
        cellManager = new ConstantCellManager( columnBinary.columnType , new ShortObj( shortValue ) , columnBinary.rowCount );
        cellManager.setIndex( new RangeShortIndex( shortValue , shortValue ) );
        break;
      case INTEGER:
        int intValue = wrapBuffer.getInt();
        cellManager = new ConstantCellManager( columnBinary.columnType , new IntegerObj( intValue ) , columnBinary.rowCount );
        cellManager.setIndex( new RangeIntegerIndex( intValue , intValue ) );
        break;
      case LONG:
        long longValue = wrapBuffer.getLong();
        cellManager = new ConstantCellManager( columnBinary.columnType , new LongObj( longValue ) , columnBinary.rowCount );
        cellManager.setIndex( new RangeLongIndex( longValue , longValue ) );
        break;
      case FLOAT:
        float floatValue = wrapBuffer.getFloat();
        cellManager = new ConstantCellManager( columnBinary.columnType , new FloatObj( floatValue ) , columnBinary.rowCount );
        cellManager.setIndex( new RangeFloatIndex( floatValue , floatValue ) );
        break;
      case DOUBLE:
        double doubleValue = wrapBuffer.getDouble();
        cellManager = new ConstantCellManager( columnBinary.columnType , new DoubleObj( doubleValue ) , columnBinary.rowCount );
        cellManager.setIndex( new RangeDoubleIndex( doubleValue , doubleValue ) );
        break;
      case STRING:
        int stringLength = wrapBuffer.getInt();
        byte[] stringBytes = new byte[stringLength];
        wrapBuffer.get( stringBytes );
        cellManager = new ConstantCellManager( columnBinary.columnType , new UTF8BytesLinkObj( stringBytes , 0 , stringBytes.length ) , columnBinary.rowCount );
        String string = new String( stringBytes , 0 , stringBytes.length , "UTF-8" );
        cellManager.setIndex( new RangeStringIndex( string , string , false ) );
        break;
      case BYTES:
        int byteLength = wrapBuffer.getInt();
        byte[] byteBytes = new byte[byteLength];
        wrapBuffer.get( byteBytes );
        cellManager = new ConstantCellManager( columnBinary.columnType , new BytesObj( byteBytes , 0 , byteBytes.length ) , columnBinary.rowCount );
        break;
      default:
        throw new IOException( "Unknown primitive type." );
    }
    result.setCellManager( cellManager );
    return result;
  }

  @Override
  public void loadInMemoryStorage( final ColumnBinary columnBinary , final IMemoryAllocator allocator ) throws IOException{
    ByteBuffer wrapBuffer = ByteBuffer.wrap( columnBinary.binary , columnBinary.binaryStart , columnBinary.binaryLength );
    switch( columnBinary.columnType ){
      case BOOLEAN:
        boolean booleanObj = wrapBuffer.get() == 1;
        for( int i = 0 ; i < columnBinary.rowCount ; i++ ){
          allocator.setBoolean( i , booleanObj );
        }
        break;
      case BYTE:
        byte byteObj = wrapBuffer.get();
        for( int i = 0 ; i < columnBinary.rowCount ; i++ ){
          allocator.setByte( i , byteObj );
        }
        break;
      case SHORT:
        short shortObj = wrapBuffer.getShort();
        for( int i = 0 ; i < columnBinary.rowCount ; i++ ){
          allocator.setShort( i , shortObj );
        }
        break;
      case INTEGER:
        int intObj = wrapBuffer.getInt();
        for( int i = 0 ; i < columnBinary.rowCount ; i++ ){
          allocator.setInteger( i , intObj );
        }
        break;
      case LONG:
        long longObj = wrapBuffer.getLong();
        for( int i = 0 ; i < columnBinary.rowCount ; i++ ){
          allocator.setLong( i , longObj );
        }
        break;
      case FLOAT:
        float floatObj = wrapBuffer.getFloat();
        for( int i = 0 ; i < columnBinary.rowCount ; i++ ){
          allocator.setFloat( i , floatObj );
        }
        break;
      case DOUBLE:
        double doubleObj = wrapBuffer.getDouble();
        for( int i = 0 ; i < columnBinary.rowCount ; i++ ){
          allocator.setDouble( i , doubleObj );
        }
        break;
      case STRING:
        int stringLength = wrapBuffer.getInt();
        byte[] stringBytes = new byte[stringLength];
        wrapBuffer.get( stringBytes );
        String utf8 = new String( stringBytes , "UTF-8" );
        for( int i = 0 ; i < columnBinary.rowCount ; i++ ){
          allocator.setString( i , utf8 );
        }
        break;
      case BYTES:
        int byteLength = wrapBuffer.getInt();
        byte[] byteBytes = new byte[byteLength];
        wrapBuffer.get( byteBytes );
        for( int i = 0 ; i < columnBinary.rowCount ; i++ ){
          allocator.setBytes( i , byteBytes );
        }
        break;
      default:
        throw new IOException( "Unknown primitive type." );
    }
  }

  @Override
  public void setBlockIndexNode( final BlockIndexNode parentNode , final ColumnBinary columnBinary ) throws IOException{
    BlockIndexNode currentNode = parentNode.getChildNode( columnBinary.columnName );
    ByteBuffer wrapBuffer = ByteBuffer.wrap( columnBinary.binary , columnBinary.binaryStart , columnBinary.binaryLength );
    switch( columnBinary.columnType ){
      case BYTE:
        byte byteValue = wrapBuffer.get();
        currentNode.setBlockIndex( new ByteRangeBlockIndex( byteValue , byteValue ) );
        break;
      case SHORT:
        short shortValue = wrapBuffer.getShort();
        currentNode.setBlockIndex( new ShortRangeBlockIndex( shortValue , shortValue ) );
        break;
      case INTEGER:
        int intValue = wrapBuffer.getInt();
        currentNode.setBlockIndex( new IntegerRangeBlockIndex( intValue , intValue ) );
        break;
      case LONG:
        long longValue = wrapBuffer.getLong();
        currentNode.setBlockIndex( new LongRangeBlockIndex( longValue , longValue ) );
        break;
      case FLOAT:
        float floatValue = wrapBuffer.getFloat();
        currentNode.setBlockIndex( new FloatRangeBlockIndex( floatValue , floatValue ) );
        break;
      case DOUBLE:
        double doubleValue = wrapBuffer.getDouble();
        currentNode.setBlockIndex( new DoubleRangeBlockIndex( doubleValue , doubleValue ) );
        break;
      case STRING:
        int stringLength = wrapBuffer.getInt();
        byte[] stringBytes = new byte[stringLength];
        wrapBuffer.get( stringBytes );
        String string = new String( stringBytes , 0 , stringBytes.length , "UTF-8" );
        currentNode.setBlockIndex( new StringRangeBlockIndex( string , string ) );
        break;
      default:
        currentNode.disable();
    }
  }

  public class ConstantCellManager implements ICellManager{

    private final ICell cell;
    private final PrimitiveObject value;
    private final int length;

    private ICellIndex index = new DefaultCellIndex();

    public ConstantCellManager( final ColumnType columnType , final PrimitiveObject value , final int length ){
      this.value = value;
      this.length = length;
      cell = new PrimitiveCell( columnType , value );
    }

    @Override
    public void add( final ICell cell , final int index ){
      throw new UnsupportedOperationException( "Constant column is read only." );
    }

    @Override
    public ICell get( final int index , final ICell defaultCell ){
      if( length <= index ){
        return defaultCell;
      }

      return cell;
    }

    @Override
    public int getMaxIndex(){
      return length - 1;
    }

    @Override
    public int size(){
      return length;
    }

    @Override
    public void clear(){

    }

    @Override
    public void setIndex( final ICellIndex index ){
      this.index = index;
    }

    @Override
    public List<Integer> filter( final IFilter filter ) throws IOException{
      switch( filter.getFilterType() ){
        case NOT_NULL:
          return null;
        case NULL:
          return new ArrayList<Integer>();
        default:
          return index.filter( filter );
      }
    }

    @Override
    public PrimitiveObject[] getPrimitiveObjectArray(final IExpressionIndex indexList , final int start , final int length ){
      PrimitiveObject[] result = new PrimitiveObject[length];
      for( PrimitiveObject obj : result ){
        obj = value;
      }
      return result;
    }

    @Override
    public void setPrimitiveObjectArray(final IExpressionIndex indexList , final int start , final int length , final IMemoryAllocator allocator ){
      for( int i = 0 ; i < length ; i++ ){
        try{
          allocator.setPrimitiveObject( i , value );
        }catch( IOException e ){
          allocator.setNull( i );
        }
      }
    }

  }

  public static ColumnBinary createColumnBinary( final PrimitiveObject value , final String columnName , final int size ) throws IOException{
    ColumnType type;
    byte[] valueBinary;
    int logicalDataSize;
    switch( value.getPrimitiveType() ){
      case BOOLEAN:
        type = ColumnType.BOOLEAN;
        valueBinary = new byte[Byte.BYTES];
        if( value.getBoolean() ){
          valueBinary[0] = 1;
        }
        logicalDataSize = Byte.BYTES * size;
        break;
      case BYTE:
        type = ColumnType.BYTE;
        valueBinary = new byte[Byte.BYTES];
        valueBinary[0] = value.getByte();
        logicalDataSize = Byte.BYTES * size;
        break;
      case SHORT:
        type = ColumnType.SHORT;
        valueBinary = new byte[Short.BYTES];
        ByteBuffer.wrap( valueBinary ).putShort( value.getShort() );
        logicalDataSize = Short.BYTES * size;
        break;
      case INTEGER:
        type = ColumnType.INTEGER;
        valueBinary = new byte[Integer.BYTES];
        ByteBuffer.wrap( valueBinary ).putInt( value.getInt() );
        logicalDataSize = Integer.BYTES * size;
        break;
      case LONG:
        type = ColumnType.LONG;
        valueBinary = new byte[Long.BYTES];
        ByteBuffer.wrap( valueBinary ).putLong( value.getLong() );
        logicalDataSize = Long.BYTES * size;
        break;
      case FLOAT:
        type = ColumnType.FLOAT;
        valueBinary = new byte[Float.BYTES];
        ByteBuffer.wrap( valueBinary ).putFloat( value.getFloat() );
        logicalDataSize = Float.BYTES * size;
        break;
      case DOUBLE:
        type = ColumnType.DOUBLE;
        valueBinary = new byte[Double.BYTES];
        ByteBuffer.wrap( valueBinary ).putDouble( value.getDouble() );
        logicalDataSize = Double.BYTES * size;
        break;
      case STRING:
        type = ColumnType.STRING;
        byte[] stringBytes = value.getBytes();
        valueBinary = new byte[Integer.BYTES + stringBytes.length];
        ByteBuffer stringWrapBuffer = ByteBuffer.wrap( valueBinary );
        stringWrapBuffer.putInt( stringBytes.length );
        stringWrapBuffer.put( stringBytes );
        logicalDataSize = ( stringBytes.length * Character.BYTES ) * size;
        break;
      case BYTES:
        type = ColumnType.BYTES;
        byte[] bytes = value.getBytes();
        valueBinary = new byte[Integer.BYTES + bytes.length];
        ByteBuffer wrapBuffer = ByteBuffer.wrap( valueBinary );
        wrapBuffer.putInt( bytes.length );
        wrapBuffer.put( bytes );
        logicalDataSize = bytes.length * size;
        break;
      default:
        throw new IOException( "Unknown primitive type." );
    }

    return new ColumnBinary( ConstantColumnBinaryMaker.class.getName() , DefaultCompressor.class.getName() , columnName , type , size , valueBinary.length , logicalDataSize , 1 , valueBinary , 0 , valueBinary.length , null );
  }

}
