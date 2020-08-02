// IReporter.aidl
package com.op.binderserverdemo;

// Declare any non-default types here with import statements

interface IReporter {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    int report(String values, int type);
}
