package com.szcbit.SGFlexSys;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.text.method.DateTimeKeyListener;

public class ClsTransactionCheck {
    private int[] crc_ccitt_table = {0x0, 0x1189, 0x2312, 0x329B, 0x4624, 0x57AD, 0x6536, 0x74BF, 0x8C48, 0x9DC1,
            0xAF5A, 0xBED3, 0xCA6C, 0xDBE5, 0xE97E, 0xF8F7, 0x1081, 0x108, 0x3393, 0x221A,
            0x56A5, 0x472C, 0x75B7, 0x643E, 0x9CC9, 0x8D40, 0xBFDB, 0xAE52, 0xDAED, 0xCB64,
            0xF9FF, 0xE876, 0x2102, 0x308B, 0x210, 0x1399, 0x6726, 0x76AF, 0x4434, 0x55BD,
            0xAD4A, 0xBCC3, 0x8E58, 0x9FD1, 0xEB6E, 0xFAE7, 0xC87C, 0xD9F5, 0x3183, 0x200A,
            0x1291, 0x318, 0x77A7, 0x662E, 0x54B5, 0x453C, 0xBDCB, 0xAC42, 0x9ED9, 0x8F50,
            0xFBEF, 0xEA66, 0xD8FD, 0xC974, 0x4204, 0x538D, 0x6116, 0x709F, 0x420, 0x15A9,
            0x2732, 0x36BB, 0xCE4C, 0xDFC5, 0xED5E, 0xFCD7, 0x8868, 0x99E1, 0xAB7A, 0xBAF3,
            0x5285, 0x430C, 0x7197, 0x601E, 0x14A1, 0x528, 0x37B3, 0x263A,  0xDECD, 0xCF44,
            0xFDDF, 0xEC56, 0x98E9, 0x8960, 0xBBFB, 0xAA72, 0x6306, 0x728F, 0x4014, 0x519D,
            0x2522, 0x34AB, 0x630, 0x17B9, 0xEF4E, 0xFEC7, 0xCC5C, 0xDDD5, 0xA96A, 0xB8E3,
            0x8A78, 0x9BF1, 0x7387, 0x620E, 0x5095, 0x411C, 0x35A3, 0x242A, 0x16B1, 0x738,
            0xFFCF, 0xEE46, 0xDCDD, 0xCD54, 0xB9EB, 0xA862, 0x9AF9, 0x8B70, 0x8408, 0x9581,
            0xA71A, 0xB693, 0xC22C, 0xD3A5, 0xE13E, 0xF0B7, 0x840, 0x19C9, 0x2B52, 0x3ADB,
            0x4E64, 0x5FED, 0x6D76, 0x7CFF, 0x9489, 0x8500, 0xB79B, 0xA612, 0xD2AD, 0xC324,
            0xF1BF, 0xE036, 0x18C1, 0x948, 0x3BD3, 0x2A5A, 0x5EE5, 0x4F6C, 0x7DF7, 0x6C7E,
            0xA50A, 0xB483, 0x8618, 0x9791, 0xE32E, 0xF2A7, 0xC03C, 0xD1B5, 0x2942, 0x38CB,
            0xA50, 0x1BD9, 0x6F66, 0x7EEF, 0x4C74, 0x5DFD, 0xB58B, 0xA402, 0x9699, 0x8710,
            0xF3AF, 0xE226, 0xD0BD, 0xC134, 0x39C3, 0x284A, 0x1AD1, 0xB58, 0x7FE7, 0x6E6E,
            0x5CF5, 0x4D7C, 0xC60C, 0xD785, 0xE51E, 0xF497, 0x8028, 0x91A1, 0xA33A, 0xB2B3,
            0x4A44, 0x5BCD, 0x6956, 0x78DF, 0xC60, 0x1DE9, 0x2F72, 0x3EFB, 0xD68D, 0xC704,
            0xF59F, 0xE416, 0x90A9, 0x8120, 0xB3BB, 0xA232, 0x5AC5, 0x4B4C, 0x79D7, 0x685E,
            0x1CE1, 0xD68, 0x3FF3, 0x2E7A, 0xE70E, 0xF687, 0xC41C, 0xD595, 0xA12A, 0xB0A3,
            0x8238, 0x93B1, 0x6B46, 0x7ACF, 0x4854, 0x59DD, 0x2D62, 0x3CEB, 0xE70, 0x1FF9,
            0xF78F, 0xE606, 0xD49D, 0xC514, 0xB1AB, 0xA022, 0x92B9, 0x8330, 0x7BC7, 0x6A4E,
            0x58D5, 0x495C, 0x3DE3, 0x2C6A, 0x1EF1, 0xF78};

    public long TransactionCheck;
    String SecCode;

    public Activity activity;

    public short GetTransactionCheck(String SecurityID)
    {
        Calendar now = null;
        now = Calendar.getInstance();

        return CalcTransactionCheck(SecurityID, now);

    }


    public long GetSeceretCode(long SeceretCode)
    {
        Calendar now = null;
        now = Calendar.getInstance();

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
        String strDate= formatter.format(date);

        String day = (strDate.substring(0, 2));
        String month = (strDate.substring(3, 5));
        String year = (strDate.substring(6, 8));
        String hour = (strDate.substring(9, 11));
        String min = (strDate.substring(12, 14));
        String sec = (strDate.substring(15, 17));

        String SecCode = (day + month + year+ hour+ min+ sec);

        long secco = Long.parseLong(SecCode);

        return  secco;

    }



    private int addMinutes(Calendar CurrTime, int addition, int retDateType)
    {
        CurrTime = Calendar.getInstance();
        switch(retDateType)
        {
            case 1:
                CurrTime.add(Calendar.MINUTE, addition);
                //common.DisplayMsg(Integer.toString(CurrTime.get(Calendar.DAY_OF_MONTH)), activity);
                return CurrTime.get(Calendar.DAY_OF_MONTH);
            case 2:
                CurrTime.add(Calendar.MINUTE, addition);
                //common.DisplayMsg(Integer.toString(CurrTime.get(Calendar.HOUR_OF_DAY)), activity);
                return CurrTime.get(Calendar.HOUR_OF_DAY);
            case 3:
                CurrTime.add(Calendar.MINUTE, addition);
                //common.DisplayMsg(Integer.toString(CurrTime.get(Calendar.MINUTE)), activity);
                return CurrTime.get(Calendar.MINUTE);
            default: return 0;
        }
    }

    private short CalcTransactionCheck(String SecurityID, Calendar CurrTime)
    {
        try
        {
            long lCurrMin=0;
            long lnextMin=0;
            long lTwoNextMin=0;



            if ((lCurrMin = Calccrc16(SecurityID +
                    Integer.toString(10 + CurrTime.get(Calendar.DAY_OF_MONTH)) +
                    Integer.toString(10 + CurrTime.get(Calendar.HOUR_OF_DAY)) +
                    Integer.toString(10 + CurrTime.get(Calendar.MINUTE)))) < 0)
                throw new Exception();

            if ((lnextMin = Calccrc16(SecurityID +
                    Integer.toString(10 + addMinutes(CurrTime, 1, 1)) +
                    Integer.toString(10 + addMinutes(CurrTime, 1, 2)) +
                    Integer.toString(10 + addMinutes(CurrTime, 1, 3)))) < 0)
                throw new Exception();

            if ((lTwoNextMin = Calccrc16(SecurityID +
                    Integer.toString(10 + addMinutes(CurrTime, 5, 1)) +
                    Integer.toString(10 + addMinutes(CurrTime, 5, 2)) +
                    Integer.toString(10 + addMinutes(CurrTime, 5, 3)))) < 0)
                throw new Exception();

            int LastDigit = 0;


            if(tryParseInt(SecurityID.substring(SecurityID.length() - 1, SecurityID.length())))
            {
                LastDigit = Integer.parseInt(SecurityID.substring(SecurityID.length() - 1, SecurityID.length()));
                LastDigit %= 4;
            }
            else
            {
                LastDigit = 4;
            }
            switch (LastDigit)
            {
                case 1:
                    TransactionCheck = Long.parseLong(String.format("%06d", lTwoNextMin).substring(0,6)) +
                            Long.parseLong(String.format("%06d", lCurrMin).substring(0,6)) +
                            Long.parseLong(String.format("%06d", lnextMin).substring(0,6));
                    break;
                case 2:
                    TransactionCheck = Long.parseLong(String.format("%06d", lnextMin).substring(0,6)) +
                            Long.parseLong(String.format("%06d", lTwoNextMin).substring(0,6)) +
                            Long.parseLong(String.format("%06d", lCurrMin).substring(0,6));
                    break;
                case 3:
                    TransactionCheck = Long.parseLong(String.format("%06d", lCurrMin).substring(0,6)) +
                            Long.parseLong(String.format("%06d", lTwoNextMin).substring(0,6)) +
                            Long.parseLong(String.format("%06d", lnextMin).substring(0,6));
                    break;
                default:
                    TransactionCheck = Long.parseLong(String.format("%06d", lCurrMin).substring(0,6)) +
                            Long.parseLong(String.format("%06d", lnextMin).substring(0,6)) +
                            Long.parseLong(String.format("%06d", lTwoNextMin).substring(0,6));
                    break;
            }
        }
        catch(Exception e)
        {
            TransactionCheck = -1;
            return 26;
        }

        return 0;
    }

    private long Calccrc16(String sMessage)
    {
        long iCRC;

        try
        {
            iCRC = 0;

            if ((sMessage.length() % 2) != 0) sMessage = '0' + sMessage;

            for (int i = 0; i <= sMessage.length() - 2; i = i + 2)
            {
                long hexToNum = StringToDec(sMessage.substring(i, i+2), (int)2);

                if (hexToNum >= 0)
                {

                    iCRC = crc_ccitt_table[(int) ((iCRC ^ hexToNum) & 0xFF)] ^ (iCRC >> 8);
                }
                else
                {
                    throw new Exception();
                }
            }
        }
        catch(Exception e)
        {
            iCRC = -1;
        }

        return iCRC;

    }

    private long StringToDec(String inputString,
                             int ExpstringSize)
    {
        String HexString = "";

        try
        {

            for(int i=inputString.length()+1; i<=ExpstringSize;i++) inputString += ' ';

            String DATA = inputString;
            String sValue;
            int IntValue;

            while (DATA.length() > 0)
            {

                sValue = DATA.substring(0, 1).toString();
                char[] cValue = sValue.substring(0,1).toCharArray();
                IntValue = (int)cValue[0];
                sValue = Integer.toString(IntValue,10);
                HexString += String.format("%03d", Integer.parseInt(sValue)).substring(0,3);

                DATA = DATA.substring(1, DATA.length());
            }
        }
        catch(Exception e)
        {
            return -1;
        }

        //return Convert.ToInt64(HexString);
        return Long.parseLong(HexString);
    }

    private boolean tryParseInt(String value)
    {
        try
        {
            Integer.parseInt(value);
            return true;
        } catch(NumberFormatException nfe)
        {
            return false;
        }
    }
}
