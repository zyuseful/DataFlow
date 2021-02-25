package com.fupin832.datago.odscanal.tables;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.table.api.DataTypes;
import org.apache.flink.table.api.TableSchema;
import org.apache.flink.table.types.AtomicDataType;
import org.apache.flink.table.types.DataType;
import org.apache.flink.table.types.logical.BigIntType;
import org.apache.flink.table.types.logical.LogicalType;

import javax.annotation.Nullable;

public class TableData {
    public static void main(String[] args) {
        DataType bigint = DataTypes.BIGINT();
        LogicalType logicalType = bigint.getLogicalType();
        System.out.println(logicalType.asSerializableString());
        System.out.println(logicalType.asSummaryString());
        System.out.println(logicalType.toString());

        AtomicDataType d = new AtomicDataType(new BigIntType());

        /*
        TableSchema.builder()
                .field("1", DataTypes.BIGINT())
                .field("2", DataTypes.STRING())
                .field("3", DataTypes.DECIMAL(10, 4))
                .field("4", DataTypes.TIMESTAMP(3))
                .build();

        List rows = new ArrayList<>();
        Row of = Row.of(
                1,
                "apple",
                Timestamp.valueOf("2020-03-18 12:12:14").getTime(),
                Date.valueOf("2020-03-18"),
                Timestamp.valueOf("2020-03-18 12:12:14"),
                Time.valueOf("12:12:14"),
                LocalDateTime.of(2020, 3, 18, 12, 12, 14, 1000),
                LocalDate.of(2020, 3, 18),
                LocalTime.of(12, 13, 14, 2000),
                "test1",
                true);
        rows.add(of);
        Object field = of.getField(1);
         */
    }

}

class TB extends TableSchema {
    //List<String> columns;
//    List<TableColumn> columns
    /**
     * @param fieldNames
     * @param fieldTypes
     * @deprecated Use the {@link Builder} instead.
     */
    public TB(String[] fieldNames, TypeInformation<?>[] fieldTypes) {
        super(fieldNames, fieldTypes);
    }

    public static <T> T checkNotNull(@Nullable T reference, @Nullable String errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    public <T> T checkNotNull1(@Nullable T reference, @Nullable String errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

}