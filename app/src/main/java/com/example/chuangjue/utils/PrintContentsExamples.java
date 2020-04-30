package com.example.chuangjue.utils;

import com.example.chuangjue.bean.PrintItemObj;

import java.util.ArrayList;

import static com.example.chuangjue.utils.AidlUtil.FONT_SIZE_BIG;
import static com.example.chuangjue.utils.AidlUtil.FONT_SIZE_MIDDIM;
import static com.example.chuangjue.utils.AidlUtil.FONT_SIZE_MIDSMALL;
import static com.example.chuangjue.utils.AidlUtil.FONT_SIZE_SMALL;

/**
 * Created by Administrator on 2017/7/26.
 */

public class PrintContentsExamples {



    /**
     * 成功交易明细打印信息
     */
    public ArrayList<PrintItemObj> printTransactionList() {
        ArrayList<PrintItemObj> list = new ArrayList<PrintItemObj>();

        list.add(new PrintItemObj("      澳门华星", FONT_SIZE_BIG, true));
        list.add(new PrintItemObj("      谢谢惠顾",FONT_SIZE_MIDSMALL, false));
        list.add(new PrintItemObj("台号：11",FONT_SIZE_SMALL, false));
        list.add(new PrintItemObj("零售单号:0221213421345245", FONT_SIZE_SMALL, false));
        list.add(new PrintItemObj("店名：华星皇朝店     收银员：001", FONT_SIZE_SMALL, false));
        list.add(new PrintItemObj("数量  菜品名称        金额", FONT_SIZE_SMALL, false));
        list.add(new PrintItemObj("------------------------------"));
        list.add(new PrintItemObj(" 1  <套餐>下午茶D     36.00",FONT_SIZE_MIDDIM, false));
        list.add(new PrintItemObj(" 1    >>炸鸡脾(碎上)   0.00",FONT_SIZE_MIDDIM, false));
        list.add(new PrintItemObj(" 1    >>柠檬水         0.00",FONT_SIZE_MIDDIM, false));
        list.add(new PrintItemObj(" 1  朱古力西多士       29.00",FONT_SIZE_MIDDIM, false));
        list.add(new PrintItemObj(" 1  冻朱古力(少|冰)    21.00",FONT_SIZE_MIDDIM, false));
        list.add(new PrintItemObj(" 1  叉烧肠            22.00",FONT_SIZE_MIDDIM, false));
        list.add(new PrintItemObj(" 1  冻咖4啡(少|甜)      21.00",FONT_SIZE_MIDDIM, false));
        list.add(new PrintItemObj("------------------------------"));
        list.add(new PrintItemObj("应收:129.00   实收:129.00",FONT_SIZE_SMALL, false));
        list.add(new PrintItemObj("打印日期:2019-06-01 16:06",FONT_SIZE_SMALL, false));
        list.add(new PrintItemObj("      谢谢惠顾",FONT_SIZE_SMALL, false));
        list.add(new PrintItemObj(""));
        list.add(new PrintItemObj(""));
        list.add(new PrintItemObj(""));
        return list;
    }
   public static String chuanjue =
                    "澳门华星\n" +
                    "谢谢惠顾\n" +
                    "台号：11\n" +
                    "零售单号:0221213421345245\n" +
                    "店名：华星皇朝店     收银员：001\n" +
                    "数量  菜品名称        金额\n" +
                    "------------------------\n" +
                    " 1  <套餐>下午茶D     36.00\n" +
                    " 1    >>炸鸡脾(碎上)   0.00\n" +
                    " 1    >>柠檬水        0.00\n" +
                    " 1  朱古力西多士       29.00\n" +
                    " 1  冻朱古力(少|冰)    21.00\n" +
                    " 1  叉烧肠            22.00\n" +
                    " 1  冻咖啡(少|舔)      21.00\n" +
                    "------------------------\n" +
                    "应收:129.00   实收:129.00\n" +
                    "打印日期:2019-06-01 16:06\n" +
                    "谢谢惠顾\n \n \n";



}
