package com.fupin832.datago.odscanal.etl;

import com.fupin832.datago.odscanal.convert.basic.ConvertFactory;
import com.fupin832.datago.odscanal.convert.sqlstr.SqlResult;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * FlatMessage 数据转换类
 * Canal String -> Canal FlatMessage
 *
 * @author zy
 * @date 2021/01/21
 */
public class FlatMessageMap implements MapFunction<String, SqlResult> {
    ConvertFactory<String,SqlResult> convertFactory;

    public static FlatMessageMap getInstance() {
        return new FlatMessageMap();
    }

    public FlatMessageMap setConvertFactory(ConvertFactory<String,SqlResult> convertFactory) {
        this.convertFactory = convertFactory;
        return this;
    }

    @Override
    public SqlResult map(String value) throws Exception {
        return this.convertFactory.convertTo(value);
    }
}
