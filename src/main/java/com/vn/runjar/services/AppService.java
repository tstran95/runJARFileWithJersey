package com.vn.runjar.services;

import com.vn.runjar.model.ClassInfo;

import java.io.IOException;

public interface AppService {
    void fly(ClassInfo classInfo) throws IOException;
}
