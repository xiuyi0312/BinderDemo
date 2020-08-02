package com.op.binderserverdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.op.binderserverdemo.service.AIDLReporterService;
import com.op.binderserverdemo.service.ReporterService;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private IBinder reporter;
    private IReporter reporterAIDL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportManually();
            }
        });
        findViewById(R.id.tv_report_aidl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportByAIDL();
            }
        });
        bindService(new Intent(this, ReporterService.class), serviceConnectionManually, BIND_AUTO_CREATE);
        bindService(new Intent(this, AIDLReporterService.class), serviceConnectionAIDL, BIND_AUTO_CREATE);
    }

    /**
     * whether read or write, firstly write the interface token or enforce the interface, and then
     * read the parameter or write the data.
     */
    private void reportManually() {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeInterfaceToken("reporter");
        data.writeString("today is " + new Date().toGMTString());
        data.writeInt(1);

        try {
            Log.d("Reporter", Thread.currentThread().getName() + " send data");
            reporter.transact(ReporterService.REPORT_CODE, data, reply, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        reply.enforceInterface("reporter");
        int result = reply.readInt();
        Log.d("Reporter", Thread.currentThread().getName() + " result = " + result);
    }

    private void reportByAIDL() {
        try {
            int result = reporterAIDL.report("today is " + new Date().toString(), 1);
            Log.d("Reporter", Thread.currentThread().getName() + " result by AIDL = " + result);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection serviceConnectionManually = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            reporter = service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            reporter = null;
        }
    };

    private ServiceConnection serviceConnectionAIDL = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            reporterAIDL = IReporter.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            reporterAIDL = null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnectionAIDL);
        unbindService(serviceConnectionManually);
    }
}