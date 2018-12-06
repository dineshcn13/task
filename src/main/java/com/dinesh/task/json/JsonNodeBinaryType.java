package com.dinesh.task.json;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonNodeBinaryType extends AbstractSingleColumnStandardBasicType<JsonNode>{

	public JsonNodeBinaryType() {
        super( 
            JsonBinarySqlTypeDescriptor.INSTANCE, 
            JsonNodeTypeDescriptor.INSTANCE 
        );
    }

	@Override
	public String getName() {
		return "jsonb-node";
	}

}
