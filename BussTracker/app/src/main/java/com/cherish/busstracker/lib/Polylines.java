package com.cherish.busstracker.lib;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Falcon on 9/2/2015.
 *
 * Adapted from http://stackoverflow.com/questions/6802483/how-to-directly-initialize-a-hashmap-in-a-literal-way
 */
public class Polylines
{
    public static final String BUFFBUS_ENCODED_POLYLINE = "wybsFnwkaSAXPRTT@HALMV{@hBu@xAe@fAMVYV]RYF[DqA?aA@kB@sA@oA@qBAo@Bm@Fi@Bu@@eF@uA@yA@sA?cBCeA@{AA}AAgAAo@?e@?[B]Da@He@N]LOJCRERCd@G|@WbCOpA[pBM|@Ip@GjAWfGMhCMrCCnC?~@D~@A~@?t@DJRBN?|C@xA?|A?|@An@?d@Fp@\\`A~AnCxEj@~@Pl@Hb@@n@@`A?`C?pD?x@AdAUL?bBZ\\d@`@RVBRCR[v@Wj@Sd@Sn@i@dAgA`C_AtBiA~BQXWTSBWMWWEKIQ?k@@iB?w@?[A[OKMKYCcBAiBA}A?g@AUIUOIWEw@@cE@qJ@gEB_F?cE?{GBiB?cDBcD\\cHLkD?qADy@Hu@d@}Cl@yDFi@JeABw@@ODKJENGh@QZIZGz@EbAA|@?~A@l@@nB?hBBzA@~BGfA?vD?pACjACnA?bB?v@?xC?pCCt@@\\GZQXWT_@Pi@f@iAd@_Ax@eBb@}@HQCSIKUUMKK?KJ";

    public static final Map<Integer, String> POLYLINE_MAP;
    static {
        POLYLINE_MAP = new HashMap<>();
        POLYLINE_MAP.put(1, BUFFBUS_ENCODED_POLYLINE);
    }
}
