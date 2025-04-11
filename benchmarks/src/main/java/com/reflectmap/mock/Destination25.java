package com.reflectmap.mock;

import com.reflectmap.annotation.FieldMapping;
import lombok.Getter;
import lombok.Setter;

/**
 * A destination class with 25 fields.
 * Each field is annotated to map from the corresponding field in Source25.
 */
@Setter
@Getter
public class Destination25 {
    @FieldMapping(srcType = Source25.class, srcFieldName = "value1")
    private String destValue1;
    @FieldMapping(srcType = Source25.class, srcFieldName = "value2")
    private String destValue2;
    @FieldMapping(srcType = Source25.class, srcFieldName = "value3")
    private String destValue3;
    @FieldMapping(srcType = Source25.class, srcFieldName = "value4")
    private String destValue4;
    @FieldMapping(srcType = Source25.class, srcFieldName = "value5")
    private String destValue5;
    @FieldMapping(srcType = Source25.class, srcFieldName = "value6")
    private String destValue6;
    @FieldMapping(srcType = Source25.class, srcFieldName = "value7")
    private String destValue7;
    @FieldMapping(srcType = Source25.class, srcFieldName = "value8")
    private String destValue8;
    @FieldMapping(srcType = Source25.class, srcFieldName = "value9")
    private String destValue9;
    @FieldMapping(srcType = Source25.class, srcFieldName = "value10")
    private String destValue10;
    @FieldMapping(srcType = Source25.class, srcFieldName = "value11")
    private String destValue11;
    @FieldMapping(srcType = Source25.class, srcFieldName = "value12")
    private String destValue12;
    @FieldMapping(srcType = Source25.class, srcFieldName = "value13")
    private String destValue13;
    @FieldMapping(srcType = Source25.class, srcFieldName = "value14")
    private String destValue14;
    @FieldMapping(srcType = Source25.class, srcFieldName = "value15")
    private String destValue15;
    @FieldMapping(srcType = Source25.class, srcFieldName = "value16")
    private String destValue16;
    @FieldMapping(srcType = Source25.class, srcFieldName = "value17")
    private String destValue17;
    @FieldMapping(srcType = Source25.class, srcFieldName = "value18")
    private String destValue18;
    @FieldMapping(srcType = Source25.class, srcFieldName = "value19")
    private String destValue19;
    @FieldMapping(srcType = Source25.class, srcFieldName = "value20")
    private String destValue20;
    @FieldMapping(srcType = Source25.class, srcFieldName = "value21")
    private String destValue21;
    @FieldMapping(srcType = Source25.class, srcFieldName = "value22")
    private String destValue22;
    @FieldMapping(srcType = Source25.class, srcFieldName = "value23")
    private String destValue23;
    @FieldMapping(srcType = Source25.class, srcFieldName = "value24")
    private String destValue24;
    @FieldMapping(srcType = Source25.class, srcFieldName = "value25")
    private String destValue25;
}
