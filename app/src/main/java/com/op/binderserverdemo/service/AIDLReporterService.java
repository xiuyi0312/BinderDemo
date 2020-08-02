package com.op.binderserverdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import com.op.binderserverdemo.IReporter;

public class AIDLReporterService extends Service {

    private ReporterAIDL reporter;

    public AIDLReporterService() {
        this.reporter = new ReporterAIDL();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return reporter;
    }

    public static final class ReporterAIDL extends IReporter.Stub {

        @Override
        public int report(String values, int type) {
            return 204;
        }
    }
}
