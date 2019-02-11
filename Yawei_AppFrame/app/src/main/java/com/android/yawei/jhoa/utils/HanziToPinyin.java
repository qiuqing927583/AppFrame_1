package com.android.yawei.jhoa.utils;

import android.content.Context;

import com.yawei.jhoa.mobile.R;

import java.io.UnsupportedEncodingException;

/**
 * TODO 获取字符串收个文字（首字母）
 * class : HanziToPinyin
 * author: Yusz
 * Date: 2018-8-22
 */

public class HanziToPinyin {

    static final int GB_SP_DIFF = 160;
    // 存放国标一级汉字不同读音的起始区位码
    static final int[] secPosValueList = { 1601, 1637, 1833, 2078, 2274, 2302,
            2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027,
            4086, 4390, 4558, 4684, 4925, 5249, 5600 };
    // 存放国标一级汉字不同读音的起始区位码对应读音
    static final char[] firstLetter = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'w', 'x',
            'y', 'z' };

    public static String getSpells(String characters) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < characters.length(); i++) {

            char ch = characters.charAt(i);
            if ((ch >> 7) == 0) {
                // 判断是否为汉字，如果左移7为为0就不是汉字，否则是汉字
                return String.valueOf(ch).toLowerCase();
            } else {
                char spell = getFirstLetter(ch);
                buffer.append(String.valueOf(spell));
                break;
            }
        }
        return buffer.toString().toLowerCase();
    }

    // 获取一个汉字的首字母
    public static Character getFirstLetter(char ch) {

        byte[] uniCode = null;
        try {
            uniCode = String.valueOf(ch).getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        if (uniCode[0] < 128 && uniCode[0] > 0) { // 非汉字
            return null;
        } else {
            return convert(uniCode);
        }
    }

    /**
     * 获取一个汉字的拼音首字母。 GB码两个字节分别减去160，转换成10进制码组合就可以得到区位码
     * 例如汉字“你”的GB码是0xC4/0xE3，分别减去0xA0（160）就是0x24/0x43
     * 0x24转成10进制就是36，0x43是67，那么它的区位码就是3667，在对照表中读音为‘n’
     */
    static char convert(byte[] bytes) {
        char result = '-';
        int secPosValue = 0;
        int i;
        for (i = 0; i < bytes.length; i++) {
            bytes[i] -= GB_SP_DIFF;
        }
        secPosValue = bytes[0] * 100 + bytes[1];
        for (i = 0; i < 23; i++) {
            if (secPosValue >= secPosValueList[i]
                    && secPosValue < secPosValueList[i + 1]) {
                result = firstLetter[i];
                break;
            }
        }
        return result;
    }

    /**
     * 根据字母获取一个颜色
     * @param ch
     */
    public static int GetWordsByLetter(Context context,String ch){
        int color;
        switch (ch){
            case "a":
                color = context.getResources().getColor(R.color.blue);
                break;
            case "b":
                color = context.getResources().getColor(R.color.mediumvioletred);
                break;
            case "c":
                color = context.getResources().getColor(R.color.swiperefresh_color2);
                break;
            case "d":
                color = context.getResources().getColor(R.color.swiperefresh_color4);
                break;
            case "e":
                color = context.getResources().getColor(R.color.yellow);
                break;
            case "f":
                color = context.getResources().getColor(R.color.swipe_color_3);
                break;
            case "g":
                color = context.getResources().getColor(R.color.icontabsel);
                break;
            case "h":
                color =  context.getResources().getColor(R.color.RoundFillColor);
                break;
            case "i":
                color = context.getResources().getColor(R.color.blue);
                break;
            case "j":
                color = context.getResources().getColor(R.color.icontabsel);
                break;
            case "k":
                color = context.getResources().getColor(R.color.colorPrimary);
                break;
            case "l":
                color =  context.getResources().getColor(R.color.deeppink);
                break;
            case "m":
                color = context.getResources().getColor(R.color.swiperefresh_color2);
                break;
            case "n":
                color = context.getResources().getColor(R.color.swiperefresh_color4);
                break;
            case "o":
                color = context.getResources().getColor(R.color.mediumvioletred);
                break;
            case "p":
                color = context.getResources().getColor(R.color.yellow);
                break;
            case "q":
                color = context.getResources().getColor(R.color.icontabsel);
                break;
            case "r":
                color = context.getResources().getColor(R.color.swiperefresh_color3);
                break;
            case "s":
                color = context.getResources().getColor(R.color.pen9);
                break;
            case "t":
                color = context.getResources().getColor(R.color.pen12);
                break;
            case "u":
                color = context.getResources().getColor(R.color.colorAccent);
                break;
            case "v":
                color = context.getResources().getColor(R.color.pen12);
                break;
            case "w":
                color = context.getResources().getColor(R.color.swiperefresh_color3);
                break;
            case "x":
                color =  context.getResources().getColor(R.color.swipe_color_3);
                break;
            case "y":
                color = context.getResources().getColor(R.color.mediumvioletred);
                break;
            case "z":
                color = context.getResources().getColor(R.color.pen12);
                break;
            case "0":
                color = context.getResources().getColor(R.color.mediumvioletred);
                break;
            case "1":
                color = context.getResources().getColor(R.color.mediumvioletred);
                break;
            case "2":
                color = context.getResources().getColor(R.color.swiperefresh_color4);
                break;
            case "3":
                color = context.getResources().getColor(R.color.swiperefresh_color4);
                break;
            case "4":
                color = context.getResources().getColor(R.color.blue);
                break;
            case "5":
                color = context.getResources().getColor(R.color.blue);
                break;
            case "6":
                color = context.getResources().getColor(R.color.swiperefresh_color2);
                break;
            case "7":
                color = context.getResources().getColor(R.color.blue);
                break;
            case "8":
                color = context.getResources().getColor(R.color.swiperefresh_color2);
                break;
            case "9":
                color = context.getResources().getColor(R.color.swiperefresh_color2);
                break;
            default:
                color = context.getResources().getColor(R.color.swiperefresh_color3);
                break;
        }
        return  color;
    }

}
