package gzb.frame.factory.v4.entity;

import gzb.frame.annotation.DecoratorEnd;
import gzb.frame.annotation.DecoratorStart;
import gzb.frame.factory.GzbOneInterface;

import java.lang.reflect.Field;

public class DecoratorEntity {
    public GzbOneInterface call;
    public ClassEntity classEntity;
    public String name;
    public Field[]fields = null;
    public Class<?>[]fieldTypes = null;
    public String[]fieldNames;
    public DecoratorStart decoratorStart;
    public DecoratorEnd decoratorEnd;

}