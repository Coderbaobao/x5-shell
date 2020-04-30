package com.example.chuangjue.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.example.chuangjue.bean.PrintItemObj;
import com.example.chuangjue.bean.TableItem;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

import static android.content.ContentValues.TAG;
import static com.example.chuangjue.utils.PrintContentsExamples.chuanjue;


public class AidlUtil {
    public static final String TAG = "AidlUtil";
    public static final float FONT_SIZE_BIG = 36f;
    public static final float FONT_SIZE_MIDDIM = 26f;
    public static final float FONT_SIZE_MIDSMALL = 21f;
    public static final float FONT_SIZE_SMALL = 16f;
    private static final String SERVICE＿PACKAGE = "woyou.aidlservice.jiuiv5";
    private static final String SERVICE＿ACTION = "woyou.aidlservice.jiuiv5.IWoyouService";

    private IWoyouService woyouService;
    private static AidlUtil mAidlUtil = new AidlUtil();
    private Context context;
    /**打印列数、每列宽度、每列的对齐方式*/
    private String[] text = new String[3];
    private int[] width = new int[]{6, 20, 8};
    private int[] align = new int[]{0, 0, 0};
    public static ICallback callback = null;

    private AidlUtil() {
    }


    public static AidlUtil getInstance() {
        return mAidlUtil;
    }

    /**
     * 连接服务
     *
     * @param context context
     */
    public void connectPrinterService(Context context) {
        this.context = context.getApplicationContext();
        Intent intent = new Intent();
        intent.setPackage(SERVICE＿PACKAGE);
        intent.setAction(SERVICE＿ACTION);
        context.getApplicationContext().startService(intent);
        context.getApplicationContext().bindService(intent, connService, Context.BIND_AUTO_CREATE);

        callback = new ICallback.Stub() {
            @Override
            public void onRunResult(final boolean success) throws RemoteException {
            }
            @Override
            public void onReturnString(final String value) throws RemoteException {
            }
            @Override
            public void onRaiseException(int code, final String msg) throws RemoteException {
            }
        };
    }

    /**
     * 断开服务
     *
     * @param context context
     */
    public void disconnectPrinterService(Context context) {
        if (woyouService != null) {
            context.getApplicationContext().unbindService(connService);
            woyouService = null;
        }
    }

    public boolean isConnect() {
        return woyouService != null;
    }

    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
        }
    };

    public ICallback generateCB(final PrinterCallback printerCallback){
        return new ICallback.Stub(){


            @Override
            public void onRunResult(boolean isSuccess) throws RemoteException {

            }

            @Override
            public void onReturnString(String result) throws RemoteException {
                printerCallback.onReturnString(result);
                Toast.makeText(context,result,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRaiseException(int code, String msg) throws RemoteException {

            }
        };
    }

    /**
     * 设置打印浓度
     */
    private int[] darkness = new int[]{0x0600, 0x0500, 0x0400, 0x0300, 0x0200, 0x0100, 0,
            0xffff, 0xfeff, 0xfdff, 0xfcff, 0xfbff, 0xfaff};

    public void setDarkness(int index) {
        if (woyouService == null) {
//            Toast.makeText(context, R.string.toast_2,Toast.LENGTH_LONG).show();
            return;
        }

        int k = darkness[index];
        try {
            woyouService.sendRAWData(ESCUtil.setPrinterDarkness(k), null);
            woyouService.printerSelfChecking(null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取得打印机系统信息，放在list中
     *
     * @return list
     */
    public List<String> getPrinterInfo(PrinterCallback printerCallback) {
        if (woyouService == null) {
//            Toast.makeText(context,R.string.toast_2,Toast.LENGTH_LONG).show();
            return null;
        }

        List<String> info = new ArrayList<>();
        try {
            woyouService.getPrintedLength(generateCB(printerCallback));
            info.add(woyouService.getPrinterSerialNo());
            info.add(woyouService.getPrinterModal());
            info.add(woyouService.getPrinterVersion());
            info.add(printerCallback.getResult());
            info.add("");
            //info.add(woyouService.getServiceVersion());
            PackageManager packageManager = context.getPackageManager();
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(SERVICE＿PACKAGE, 0);
                if(packageInfo != null){
                    info.add(packageInfo.versionName);
                    info.add(packageInfo.versionCode+"");
                }else{
                    info.add("");info.add("");
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 初始化打印机
     */
    public void initPrinter() {
        if (woyouService == null) {
//            Toast.makeText(context,R.string.toast_2,Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.printerInit(null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印二维码
     */
    public void printQr(String data, int modulesize, int errorlevel) {
        if (woyouService == null) {
//            Toast.makeText(context, R.string.toast_2,Toast.LENGTH_LONG).show();
            return;
        }


        try {
            woyouService.setAlignment(1, null);
            woyouService.printQRCode(data, modulesize, errorlevel, null);
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印条形码
     */
    public void printBarCode(String data, int symbology, int height, int width, int textposition) {
        if (woyouService == null) {
//            Toast.makeText(context,R.string.toast_2,Toast.LENGTH_LONG).show();
            return;
        }


        try {
            woyouService.printBarCode(data, symbology, height, width, textposition, null);
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印文字
     */
    public void printText(String content, float size, boolean isBold, boolean isUnderLine) {
        if (woyouService == null) {
//            Toast.makeText(context,R.string.toast_2,Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if (isBold) {
                woyouService.sendRAWData(ESCUtil.boldOn(), null);
            } else {
                woyouService.sendRAWData(ESCUtil.boldOff(), null);
            }

            if (isUnderLine) {
                woyouService.sendRAWData(ESCUtil.underlineWithOneDotWidthOn(), null);
            } else {
                woyouService.sendRAWData(ESCUtil.underlineOff(), null);
            }

            woyouService.printTextWithFont(content, null, size, null);
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    /*
     *打印图片
     */
    public void printBitmap(Bitmap bitmap) {
        if (woyouService == null) {
//            Toast.makeText(context,R.string.toast_2,Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.setAlignment(1, null);
            woyouService.printBitmap(bitmap, null);
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     *  打印图片和文字按照指定排列顺序
     */
    public void printBitmap(Bitmap bitmap, int orientation) {
        if (woyouService == null) {
            Toast.makeText(context,"服务已断开！",Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if(orientation == 0){
                woyouService.printBitmap(bitmap, null);
                woyouService.printText("横向排列\n", null);
                woyouService.printBitmap(bitmap, null);
                woyouService.printText("横向排列\n", null);
            }else{
                woyouService.printBitmap(bitmap, null);
                woyouService.printText("\n纵向排列\n", null);
                woyouService.printBitmap(bitmap, null);
                woyouService.printText("\n纵向排列\n", null);
            }
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    /**
     * 打印文字不带回调
     */
    public void printTextWithTrans(List<PrintItemObj> list) throws RemoteException {
        if (woyouService == null) {
            Toast.makeText(context, "未开启打印服务", Toast.LENGTH_LONG).show();
            return;
        }
        //不对之前的内容进行清空
//        woyouService.enterPrinterBuffer(false);

        for (int i = 0;i<list.size();i++) {
//            Log.e(TAG, "printTextWithTrans: " + list.get(i).toString());
            PrintItemObj printItemObj = list.get(i);
            try {
                if (printItemObj.getBold()) {
                    woyouService.sendRAWData(ESCUtil.boldOn(), null);
                } else {
                    woyouService.sendRAWData(ESCUtil.boldOff(), null);
                }

                woyouService.printTextWithFont(printItemObj.getContent(), null, printItemObj.getTextSize(), null);
                woyouService.lineWrap(1, null);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 打印文字不带回调
     */
    public static void printTextWithTranss(List<PrintItemObj> list) throws RemoteException {
        for (int i = 0;i<list.size();i++) {
            System.out.println(list.get(i).toString());
        }


    }

    /**
     * 打印表格
     */
    public void printTable(LinkedList<TableItem> list) {
        if (woyouService == null) {
//            Toast.makeText(context,R.string.toast_2,Toast.LENGTH_LONG).show();
            return;
        }

        try {
            for (TableItem tableItem : list) {
                Log.i("kaltin", "printTable: "+tableItem.getText()[0]+tableItem.getText()[1]+tableItem.getText()[2]);
                woyouService.printColumnsText(tableItem.getText(), tableItem.getWidth(), tableItem.getAlign(), null);
            }
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /*
     * 空打三行！
     */
    public void print3Line(){
        if (woyouService == null) {
//            Toast.makeText(context,R.string.toast_2,Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void sendRawData(byte[] data) {
        if (woyouService == null) {
//            Toast.makeText(context,R.string.toast_2,Toast.LENGTH_LONG).show();
            return;
        }

        try {
//            woyouService.sendRAWData(data, null);
//
            woyouService.printTextWithFont(chuanjue, null, 22, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void send(String data){
        try {
        JSONArray menus = new JSONArray(data);
        Log.e(TAG, "data: " + menus);

        for (int i = 0; i < menus.length(); i++) {
            JSONObject menuOb = menus.getJSONObject(i);
            String meal = menuOb.optString("meal");
            if(!meal.equals("")){
                JSONArray mealList = menuOb.getJSONArray("list");
                text[0] = meal;
                text[1] = mealList.getJSONObject(0).optString("number");
                text[2] = menuOb.optString("price");
                Log.e(TAG, "meal: " + menuOb);
                for (int j = 0; j < mealList.length() ; j++) {
                    JSONObject list = mealList.getJSONObject(j);
                    text[0] = " >>" + list.optString("title");
                    text[1] = list.optString("number");
                    text[2] = list.optString("price");
                    Log.e(TAG, "list: " + list);
                }
            }else {
                text[0] = menuOb.optString("title");
                text[1] = menuOb.optString("number");
                text[2] = menuOb.optString("price");
                Log.e(TAG, "menuOb: " + menuOb);
            }
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPrint(String data) {
        try {
            JSONObject info = new JSONObject(data);
            Log.e(TAG, "info: " + info);
            woyouService.setAlignment(1, callback);
            woyouService.printTextWithFont( info.optString("business") + "\n", "", 32, callback);
            woyouService.printTextWithFont("谢谢惠顾 \n", "", 26, callback);
            woyouService.setAlignment(0, callback);
            woyouService.printTextWithFont("台号：" + info.optString("table") + "\n", "", 26, callback);
            woyouService.printTextWithFont("零售单号：" + info.optString("order") + "\n", "", 26, callback);
            woyouService.printTextWithFont("店名：" + info.optString("store") + "\n", "", 26, callback);
            woyouService.printTextWithFont("收银员：" + info.optString("user") + "\n", "", 26, callback);
            woyouService.sendRAWData(BytesUtil.line(), callback);
            //_______________________________________________________________________________________________________________________________
            text[0] = "数量";
            text[1] = "单品名称";
            text[2] = "金额";
            woyouService.printColumnsText(text, width, align, callback);
            JSONArray menus = info.getJSONArray("menu");
            Log.e(TAG, "data: " + menus);

            for (int i = 0; i < menus.length(); i++) {
                JSONObject menuOb = menus.getJSONObject(i);
                Log.e(TAG, "menuOb: " + menuOb);
                String meal = menuOb.optString("meal");
                if(!meal.equals("")){
                    text[0] = menuOb.optString("number");
                    text[1] = meal;
                    text[2] = menuOb.optString("price");
                    woyouService.printColumnsText(text, width, align, callback);

                    JSONArray mealList = menuOb.getJSONArray("list");
                    Log.e(TAG, "mealList: " + mealList);
                    for (int j = 0; j < mealList.length() ; j++) {
                        JSONObject list = mealList.getJSONObject(j);
                        Log.e(TAG, "list: " + list);
                        text[0] = list.optString("number");
                        text[1] = " >>" + list.optString("title");
                        text[2] = list.optString("price");
                        woyouService.printColumnsText(text, width, align, callback);
                    }
                }else {
                    text[0] = menuOb.optString("number");
                    text[1] = menuOb.optString("title");
                    text[2] = menuOb.optString("price");
                    woyouService.printColumnsText(text, width, align, callback);
                }
            }
            woyouService.sendRAWData(BytesUtil.line(), callback);
//            ______________________________________________________________________________
            woyouService.printTextWithFont("应收:" + info.optString("receivable") + "(元)\n", "", 26, callback);
            woyouService.printTextWithFont("实收:" + info.optString("netReceipts")  + "(元)\n", "", 26, callback);

            woyouService.printTextWithFont("打印日期:" + DateTimes.getTime2() + "\n", "", 26, callback);
            woyouService.sendRAWData(BytesUtil.line(), callback);
            woyouService.setAlignment(1, callback);
//            ______________________________________________________________________________
            woyouService.printTextWithFont("谢谢惠顾", "", 32, callback);
            woyouService.lineWrap(4, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
