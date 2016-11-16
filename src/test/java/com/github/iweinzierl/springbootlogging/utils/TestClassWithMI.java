package com.github.iweinzierl.springbootlogging.utils;

import com.github.iweinzierl.springbootlogging.annotation.LoggingOption;
import com.github.iweinzierl.springbootlogging.annotation.MethodLogging;

public class TestClassWithMI {

    @MethodLogging(options = {LoggingOption.METHOD_INVOCATION})
    public String sayHello(String firstname, String lastname) {
        return "Hello " + firstname + " " + lastname;
    }
}
