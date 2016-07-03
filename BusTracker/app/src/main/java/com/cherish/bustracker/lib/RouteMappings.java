package com.cherish.bustracker.lib;

import java.util.HashMap;

/**
 * Created by Falcon on 6/5/2016.
 * Maintains a mapping of route names to certain information about them
 */
public class RouteMappings {
    /*
     * Slightly modified HashMap that returns a given default value
     * if the requested value is not in the map.
     * It also does a case-insensitive match
     * Modified from http://stackoverflow.com/questions/7519339
     */
    public static class DefaultHashMap<K,V> extends HashMap<K,V> {
        @Override
        public V get(Object k) {
            if (k instanceof String)
                return this.get(k, null);
            return super.get(k);
        }

        public V get(Object k, V defaultValue) {
            if (k instanceof String)
                k = ((String)k).toLowerCase();
            return containsKey(k) ? super.get(k) : defaultValue;
        }
    }

    // Mapping between route name and desired order on the main page
    public static final DefaultHashMap<String, Integer> ROUTES_MAP;
    // Mapping between route name and encoded polyline
    public static final DefaultHashMap<String, String> POLYLINE_MAP;

    static {
        ROUTES_MAP = new DefaultHashMap<>();
        ROUTES_MAP.put("buff bus",                  0);
        ROUTES_MAP.put("will vill football",        0);
        ROUTES_MAP.put("will vill basketball",      0);
        ROUTES_MAP.put("hop clockwise",             1);
        ROUTES_MAP.put("hop counter clockwise",     2);
        ROUTES_MAP.put("athens court shuttle",      3);
        ROUTES_MAP.put("late night black",          4);
        ROUTES_MAP.put("late night silver",         5);
        ROUTES_MAP.put("late night gold",           6);
        ROUTES_MAP.put("discovery express loop",    7);
    }

    static {
        POLYLINE_MAP = new DefaultHashMap<>();
        POLYLINE_MAP.put("buff bus",                "wybsFnwkaSAXPRTT@HALMV{@hBu@xAe@fAMVYV]RYF[DqA?aA@kB@sA@oA@qBAo@Bm@Fi@Bu@@eF@uA@yA@sA?cBCeA@{AA}AAgAAo@?e@?[B]Da@He@N]LOJCRERCd@G|@WbCOpA[pBM|@Ip@GjAWfGMhCMrCCnC?~@D~@A~@?t@DJRBN?|C@xA?|A?|@An@?d@Fp@\\`A~AnCxEj@~@Pl@Hb@@n@@`A?`C?pD?x@AdAUL?bBZ\\d@`@RVBRCR[v@Wj@Sd@Sn@i@dAgA`C_AtBiA~BQXWTSBWMWWEKIQ?k@@iB?w@?[A[OKMKYCcBAiBA}A?g@AUIUOIWEw@@cE@qJ@gEB_F?cE?{GBiB?cDBcD\\cHLkD?qADy@Hu@d@}Cl@yDFi@JeABw@@ODKJENGh@QZIZGz@EbAA|@?~A@l@@nB?hBBzA@~BGfA?vD?pACjACnA?bB?v@?xC?pCCt@@\\GZQXWT_@Pi@f@iAd@_Ax@eBb@}@HQCSIKUUMKK?KJ");
        POLYLINE_MAP.put("will vill football",      "uybsFjwkaSC\\PTTTBFAJkA`CcAxB]v@_@`@SLSH_@DU@]ASAIIMWIGk@@w@?GDOTILMB]?UBk@FaA?a@@CDC^?~@@|BAvAGjBIvCUvGWvGI|CC|D`@hK?t@H~AAf@Of@]b@}HhF}@v@k@p@o@lAg@~AQl@q@tC[TUBQAKIIWCY?gB?uB?eFAaBAq@Es@Mm@Um@u@oAcB{C{@_B]c@WSYM_@IoF@qB?g@Go@?MCMIGOIc@?eEBmADoADkANkDJmCBsBBq@Fq@`@mCh@kDNs@LcADgA@a@HKLElA]~@KrCApMDdHApB?dBIr@?nACp@@h@?~A?|A?tB?nA?b@I\\SXYRa@t@eBfBwDd@aABMGIIKWUMKQH");
        POLYLINE_MAP.put("will vill basketball",    "wybsFtwkaSAVFFLJJHFL?JGPqBfE_@t@Ub@W^o@\\SBU@c@?]CW_@eACe@BU`@UHu@Bo@Fs@?s@ZBdCClBa@bPa@fKIpGPlEJjCFzBF~Aa@hAkAz@_F`Dy@f@iAlAm@bA{AbG_BjF_BxDiDpHu@zA_@b@UBSE[UOYCUAcFE[IIIKMGiC@wA?QAkB@]KKKGKGOEU?oE?cI?qC?oCHk@?wA@cD?aKBsC@_FHcD\\_IDeCDmAd@{Cz@yFHw@FkA@OFMjAc@j@Ij@GhC?nC?pCD|ABrBAxBIjB@jD?lBGhB@b@Et@?lBBlBAnCCV?VEZKXURYPa@Vq@l@qA|AcDRg@?OAKe@e@M?KJ");
        POLYLINE_MAP.put("hop clockwise",           "{ggsFzzlaSZMXc@NMJGJChGB`AJXN?Ts@fDKz@A`D?xAItCCnE?vCDpIBp@LN~A@dG?fCBpAAjFAxAAp@@jAFdAJbBX`A\\jBhAp@`@j@Ll@HfF@`A?HB@^?jBAhD@t@IZATFN@fJBnEH^\\\\RBtD@d@Bl@AzA?PHLXApEFl@N^TVLJJLBRETIR{A|Cq@vAMZUn@aBpDYj@CTCZAv@G~@Cl@?`H?dR@`JCRIDwA?cG@iC?q@Fi@R{@f@c@Zc@ViBb@sBh@aCj@uBf@i@Fe@@g@Cq@IcBSs@Ka@?[DuCt@wCr@]BS?QCGGCKk@sEm@cF@MLIvCw@HMFQBUCm@g@sDu@cGw@cGs@iFEOMGQ?}A\\y@PIEGQgAcJg@cFOqAy@iG_AcIKs@_AcIgAuIsAsK[sC}AmMoAoKq@wFkA_JScAu@gGAm@D[NSVErBEnB?`GF`@DPLLZBl@?h@Cb@I`@QVc@Zm@^[^GXAf@P|AF\\JHP?j@QPEtB@v@?TA");
        POLYLINE_MAP.put("hop counter Clockwise",   "}egsFrxlaSk@n@i@Ly@BiBCeARMDSGMmB@a@F[JQVWb@S\\URULSJi@BsA?{@Gk@OUuBA}BLkBBiA?w@@q@B}@B]BELALFr@NtAz@|GtApLdA|HN~@VrBj@`F\\~BTpBV|@tB`Q`BlNb@rDNxAlBnOj@rEp@pFd@fEBXCLaD~@KNAd@`@rDhBdOz@dGNPPHPBfCq@NAFFZlCVzBd@rDH`@RFh@?NA\\CnA_@~D_Aj@GT?~Df@x@Fb@Cb@GfAS|Ae@bCo@nCo@v@[n@g@ROXO\\OXIRE|E@|A?|D?N?FEB_@?}U?yM?}ABm@DKFAP@\\?ZI\\C|EBPA@QAyI?iBASQAOCc@?G?IIBSDQPg@\\y@FWAUG_@[[QWM]As@?}DAQSWGEIC[?aA?oC?wBAUEUUKUC[?_@AmJ?kDBqF@WBU?c@?y@A_@GI]C}C?_BAs@AYEc@I]Qq@a@{@m@_@Wg@Mq@QgAQoAM_AEoC?eEAkC@cBAuHEi@?GGCSAi@AgI?uB@oABuC@aC?_HBYZyA^{AAOIK]K_@Gc@E[?mC?wA?UBMH");
        POLYLINE_MAP.put("athens court Shuttle",    "{~esFpyoaSARBHDFDDLCFU?uFESEA{I@[ACIC_d@@]?wCBIDGFElHG~ABz@DnBTj@Jx@XxChBjAZb@BxGBH@FPDl]BZDLJNJHRFlJ@DBRRDP?|EF\\DLj@l@L^ARwHtPk@r@mA|@mDxBeHbEUBOAKGGM?q@GcA?yLAi@E_@{@qBY]IGSG[AcG?GSAcECSEEMAGFCT");
        POLYLINE_MAP.put("late night Black",        "_`gsFjxqaS~F{APSHSe@qENWlM{CzAc@tEkAf@ShB{@vCwArFeCzLmHzB{Ah@y@lE_Jp@eBz@kB@_@Mg@q@u@EQEUAm@@uD@QHQFAHApEBVFLFLBPMfEkJx@sDx@eD^{@l@aAz@q@~@i@bI}ERWDa@Be@GcACw@SwEK_D@{ALWNO|EEdF@`A^h@\\hBfBh@^XEr@e@hAy@rBkAzFkD`LcHnG_ElBuApAoAlA}An@aAn@uAl@{AdBwExC{H`BqCr@cA`AcAhAcARWFUCc@Qq@Ik@Ck@AwD?eF@ySA_J?sFDqB@q@D]RG~@ADOEMaA@c@HGx@?ly@Bv@Dt@E^C`@iCdCmA|AaA`BiAnCeAxCgBzEo@hBk@pAo@fAo@`Au@x@w@t@}BzAaJvFkKvG_B`AiBhAqBnAwAx@aL`HgNvIsD`CcBlAs@r@e@|@_@bAaBhH_@lAgCvFaBdEeHpNkAtCsApCo@rAY^eEjCcF|CkEfC_ChAqHlDu@VuKnC}IvBkHfBiBb@MPAVh@bEPVLFLB`AUHA");
        POLYLINE_MAP.put("late night Silver",       "_`gsFjxqaSfG_BPQD[Q_BUcBDWLI|Cy@~D}@lEeAjFuApAi@tCwArFkCfL{G`DuBl@u@fBoD`DeHz@kBA[C]m@s@Um@?eFFc@ROpCBdA@b@NPDTM|BmFx@qBh@qBp@aD\\qAj@qAn@u@l@g@vCeBxGaEfDaCdC}AlCgBrEmCzIqF~FuD`OiJzBuAxB}AvB{Bp@cAn@kA`AaC|AeEzCiIp@qAr@gAxBkCzAkANc@Kg@YmAC]AY?w@?_E?y@EWK@CN@dG@hBFr@Ah@Gp@eCbCu@z@s@dAk@dAu@jB}@dCiEdLa@|@g@v@{@fAy@x@gBrAgBhAyWrPkVdOgOpJsD~BmA`AgAdBkBtHi@jBsAzCuDpIkDfH{BnEg@zAkAfC{@fBy@x@kFdDwJzF_MvFgKlC}GbBuFrAkG~AIHEJ?Nd@vDFXNLRDfAUHA");
        POLYLINE_MAP.put("late night Gold",         "y_gsFhxqaSdGaBNMD[i@iEBMJIjK}BhJ_CdAe@`LiFfJoFzFmD\\[Va@bFqKd@eAx@oB@]CWW]WUSYEW@aF@YHQLEzEBTLBZcG|LmChGqBlEc@t@i@`@gQrK{MdGcKlCcE`AaE~@UKS_@gA_JgAkIGM[E_J~BIPAXjEf]JPTHfAUNC");
        POLYLINE_MAP.put("discovery express Loop",  "emesFdyjaS@vB`AtF^hDfJIRDDdLNzCAvKEbC}AvLS~Eo@tMA`ZGzLTd@LwO?_MBmKLoBj@yOh@_DdAoHFsUIkc@e@}FsCZiB|@eAr@aAbAg@bA_@~@_@rC@N");
    }
}
