package xyz.icoding168.flyboot.common.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MyBeanSerializerModifier extends BeanSerializerModifier {

    //  數組類型
    private JsonSerializer _nullArrayJsonSerializer = new MyNullArrayJsonSerializer();
    // 字符串等類型
    private JsonSerializer _nullJsonSerializer = new MyNullJsonSerializer();

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
                                                     List beanProperties) {
        //循環所有的beanPropertyWriter
        for (int i = 0; i < beanProperties.size(); i++) {
            BeanPropertyWriter writer = (BeanPropertyWriter) beanProperties.get(i);
            //判斷字段的類型，如果是array，list，set則註冊nullSerializer
            if (isArrayType(writer)) {
                //給writer註冊一個自己的nullSerializer
                writer.assignNullSerializer(this._nullArrayJsonSerializer);
            } else {
                writer.assignNullSerializer(this._nullJsonSerializer);
            }
        }
        return beanProperties;
    }

    //判斷是什麼類型
    protected boolean isArrayType(BeanPropertyWriter writer) {
        Class clazz = writer.getPropertyType();
        return clazz.isArray() || clazz.equals(List.class) || clazz.equals(Set.class) || clazz.equals(Collection.class);
    }

}