package com.vn.runjar.validation;

import com.vn.runjar.constant.Constant;
import com.vn.runjar.exception.VNPAYException;
import com.vn.runjar.model.ClassInfo;

import java.util.Objects;

public class Validator {
    public static void checkInput(ClassInfo classInfo) {
        if (Objects.isNull(classInfo)) {
            throw new VNPAYException(Constant.CLASS_INFO_NULL);
        }

    }
}
