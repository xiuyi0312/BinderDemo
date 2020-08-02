package com.op.binderserverdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ReporterService extends Service {

    public static final String TAG = ReporterService.class.getSimpleName();
    public static final int REPORT_CODE = 502;

    private Reporter reporter = new Reporter();

//    @Override
//    public void onCreate() {
//        super.onCreate();
//    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return reporter;
    }

    public interface IReporter {
        /**
         * get the parameters from client and return the value we want to reply
         *
         * @param values a parameter we get from client
         * @param type   another parameter we get from client
         * @return the value we reply to client
         */
        int report(String values, int type);
    }

    /**
     * when extends Binder, this Business Class can be returned by onBind
     */
    public final class Reporter extends Binder implements IReporter {

        @Override
        public int report(String values, int type) {
            return (int) System.currentTimeMillis();
        }

        /**
         * override this to do the appropriate unmarshalling of transactions.
         *
         * @param code  The action to perform.  This should
         *              be a number between {@link #FIRST_CALL_TRANSACTION} and
         *              {@link #LAST_CALL_TRANSACTION}.
         * @param data  Marshalled data being received from the caller.
         * @param reply If the caller is expecting a result back, it should be marshalled
         *              in to here.
         * @param flags Additional operation flags.  Either 0 for a normal
         *              RPC, or {@link #FLAG_ONEWAY} for a one-way RPC.
         * @return Return true on a successful call; returning false is generally used to
         * indicate that you did not understand the transaction code.
         * @throws RemoteException
         */
        @Override
        protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case REPORT_CODE:
                    // received data
                    data.enforceInterface("reporter");
                    String values = data.readString();
                    int type = data.readInt();
                    Log.d(TAG, Thread.currentThread().getName() + " : data is " + values);

                    int result = report(values, type);

                    // data to send
                    reply.writeInterfaceToken("reporter");
                    reply.writeInt(result);
                    return true;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }
}
