package br.com.backend.common.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class CopyUtils {
    public static void copyNonNullProperties(Object source, Object destination, String... ignoreProperties) {
        Set<String> ignorePropertiesSet = getNullPropertyNames(source);
        for (String propertyName : ignoreProperties) {
            ignorePropertiesSet.add(propertyName);
        }
        BeanUtils.copyProperties(source, destination, ignorePropertiesSet.toArray(new String[0]));
    }

    public static Set<String> getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        return emptyNames;
    }
}