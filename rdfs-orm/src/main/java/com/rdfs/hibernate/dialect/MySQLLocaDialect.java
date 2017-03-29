package com.rdfs.hibernate.dialect;

import org.hibernate.Hibernate;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StringType;

public class MySQLLocaDialect extends MySQL5Dialect {
      
        public MySQLLocaDialect(){  
            super();  
            registerFunction("convert_gbk", new SQLFunctionTemplate( new StringType(), "convert(?1 using gbk)"));
        }  
    }  