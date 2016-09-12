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
     * Modified from http://stackoverflow.com/questions/7519339
     */
    public static class DefaultHashMap<K,V> extends HashMap<K,V> {
        public V get(Object k, V defaultValue) {
            if (k instanceof String)
                k = ((String)k).toLowerCase();
            return containsKey(k) ? super.get(k) : defaultValue;
        }
    }

    public static final int MAX_ROUTES = 9;
    // Mapping between route name and desired order on the main page
    public static final DefaultHashMap<String, Integer> ROUTE_ORDER;
    // Mapping between route name and encoded polyline
    public static final DefaultHashMap<String, String> ROUTE_POLYLINE;

    static {
        ROUTE_ORDER = new DefaultHashMap<>();
        ROUTE_ORDER.put("buff bus",                 0);
        ROUTE_ORDER.put("will vill football",       0);
        ROUTE_ORDER.put("will vill basketball",     0);
        ROUTE_ORDER.put("hop clockwise",            1);
        ROUTE_ORDER.put("hop counter clockwise",    2);
        ROUTE_ORDER.put("athens court shuttle",     3);
        ROUTE_ORDER.put("late night black",         4);
        ROUTE_ORDER.put("late night silver",        5);
        ROUTE_ORDER.put("late night gold",          6);
        ROUTE_ORDER.put("discovery express loop",   7);
        ROUTE_ORDER.put("stampede",                 8);
    }

    static {
        ROUTE_POLYLINE = new DefaultHashMap<>();
        ROUTE_POLYLINE.put("buff bus",                  "wybsFnwkaSAXPRTT@HALMV{@hBu@xAe@fAMVYV]RYF[DqA?aA@kB@sA@oA@qBAo@Bm@Fi@Bu@@eF@uA@yA@sA?cBCeA@{AA}AAgAAo@?e@?[B]Da@He@N]LOJCRERCd@G|@WbCOpA[pBM|@Ip@GjAWfGMhCMrCCnC?~@D~@A~@?t@DJRBN?|C@xA?|A?|@An@?d@Fp@\\`A~AnCxEj@~@Pl@Hb@@n@@`A?`C?pD?x@AdAUL?bBZ\\d@`@RVBRCR[v@Wj@Sd@Sn@i@dAgA`C_AtBiA~BQXWTSBWMWWEKIQ?k@@iB?w@?[A[OKMKYCcBAiBA}A?g@AUIUOIWEw@@cE@qJ@gEB_F?cE?{GBiB?cDBcD\\cHLkD?qADy@Hu@d@}Cl@yDFi@JeABw@@ODKJENGh@QZIZGz@EbAA|@?~A@l@@nB?hBBzA@~BGfA?vD?pACjACnA?bB?v@?xC?pCCt@@\\GZQXWT_@Pi@f@iAd@_Ax@eBb@}@HQCSIKUUMKK?KJ");
        ROUTE_POLYLINE.put("will vill football",        "uybsFjwkaSC\\PTTTBFAJkA`CcAxB]v@_@`@SLSH_@DU@]ASAIIMWIGk@@w@?GDOTILMB]?UBk@FaA?a@@CDC^?~@@|BAvAGjBIvCUvGWvGI|CC|D`@hK?t@H~AAf@Of@]b@}HhF}@v@k@p@o@lAg@~AQl@q@tC[TUBQAKIIWCY?gB?uB?eFAaBAq@Es@Mm@Um@u@oAcB{C{@_B]c@WSYM_@IoF@qB?g@Go@?MCMIGOIc@?eEBmADoADkANkDJmCBsBBq@Fq@`@mCh@kDNs@LcADgA@a@HKLElA]~@KrCApMDdHApB?dBIr@?nACp@@h@?~A?|A?tB?nA?b@I\\SXYRa@t@eBfBwDd@aABMGIIKWUMKQH");
        ROUTE_POLYLINE.put("will vill basketball",      "wybsFtwkaSAVFFLJJHFL?JGPqBfE_@t@Ub@W^o@\\SBU@c@?]CW_@eACe@BU`@UHu@Bo@Fs@?s@ZBdCClBa@bPa@fKIpGPlEJjCFzBF~Aa@hAkAz@_F`Dy@f@iAlAm@bA{AbG_BjF_BxDiDpHu@zA_@b@UBSE[UOYCUAcFE[IIIKMGiC@wA?QAkB@]KKKGKGOEU?oE?cI?qC?oCHk@?wA@cD?aKBsC@_FHcD\\_IDeCDmAd@{Cz@yFHw@FkA@OFMjAc@j@Ij@GhC?nC?pCD|ABrBAxBIjB@jD?lBGhB@b@Et@?lBBlBAnCCV?VEZKXURYPa@Vq@l@qA|AcDRg@?OAKe@e@M?KJ");
        ROUTE_POLYLINE.put("hop clockwise",             "{ggsFzzlaSZMXc@NMJGJChGB`AJXN?Ts@fDKz@A`D?xAItCCnE?vCDpIBp@LN~A@dG?fCBpAAjFAxAAp@@jAFdAJbBX`A\\jBhAp@`@j@Ll@HfF@`A?HB@^?jBAhD@t@IZATFN@fJBnEH^\\\\RBtD@d@Bl@AzA?PHLXApEFl@N^TVLJJLBRETIR{A|Cq@vAMZUn@aBpDYj@CTCZAv@G~@Cl@?`H?dR@`JCRIDwA?cG@iC?q@Fi@R{@f@c@Zc@ViBb@sBh@aCj@uBf@i@Fe@@g@Cq@IcBSs@Ka@?[DuCt@wCr@]BS?QCGGCKk@sEm@cF@MLIvCw@HMFQBUCm@g@sDu@cGw@cGs@iFEOMGQ?}A\\y@PIEGQgAcJg@cFOqAy@iG_AcIKs@_AcIgAuIsAsK[sC}AmMoAoKq@wFkA_JScAu@gGAm@D[NSVErBEnB?`GF`@DPLLZBl@?h@Cb@I`@QVc@Zm@^[^GXAf@P|AF\\JHP?j@QPEtB@v@?TA");
        ROUTE_POLYLINE.put("hop counter clockwise",     "}egsFrxlaSk@n@i@Ly@BiBCeARMDSGMmB@a@F[JQVWb@S\\URULSJi@BsA?{@Gk@OUuBA}BLkBBiA?w@@q@B}@B]BELALFr@NtAz@|GtApLdA|HN~@VrBj@`F\\~BTpBV|@tB`Q`BlNb@rDNxAlBnOj@rEp@pFd@fEBXCLaD~@KNAd@`@rDhBdOz@dGNPPHPBfCq@NAFFZlCVzBd@rDH`@RFh@?NA\\CnA_@~D_Aj@GT?~Df@x@Fb@Cb@GfAS|Ae@bCo@nCo@v@[n@g@ROXO\\OXIRE|E@|A?|D?N?FEB_@?}U?yM?}ABm@DKFAP@\\?ZI\\C|EBPA@QAyI?iBASQAOCc@?G?IIBSDQPg@\\y@FWAUG_@[[QWM]As@?}DAQSWGEIC[?aA?oC?wBAUEUUKUC[?_@AmJ?kDBqF@WBU?c@?y@A_@GI]C}C?_BAs@AYEc@I]Qq@a@{@m@_@Wg@Mq@QgAQoAM_AEoC?eEAkC@cBAuHEi@?GGCSAi@AgI?uB@oABuC@aC?_HBYZyA^{AAOIK]K_@Gc@E[?mC?wA?UBMH");
        ROUTE_POLYLINE.put("athens court shuttle",      "{~esFpyoaSARBHDFDDLCFU?uFESEA{I@[ACIC_d@@]?wCBIDGFElHG~ABz@DnBTj@Jx@XxChBjAZb@BxGBH@FPDl]BZDLJNJHRFlJ@DBRRDP?|EF\\DLj@l@L^ARwHtPk@r@mA|@mDxBeHbEUBOAKGGM?q@GcA?yLAi@E_@{@qBY]IGSG[AcG?GSAcECSEEMAGFCT");
        ROUTE_POLYLINE.put("late night black",          "_`gsFjxqaS~F{APSHSe@qENWlM{CzAc@tEkAf@ShB{@vCwArFeCzLmHzB{Ah@y@lE_Jp@eBz@kB@_@Mg@q@u@EQEUAm@@uD@QHQFAHApEBVFLFLBPMfEkJx@sDx@eD^{@l@aAz@q@~@i@bI}ERWDa@Be@GcACw@SwEK_D@{ALWNO|EEdF@`A^h@\\hBfBh@^XEr@e@hAy@rBkAzFkD`LcHnG_ElBuApAoAlA}An@aAn@uAl@{AdBwExC{H`BqCr@cA`AcAhAcARWFUCc@Qq@Ik@Ck@AwD?eF@ySA_J?sFDqB@q@D]RG~@ADOEMaA@c@HGx@?ly@Bv@Dt@E^C`@iCdCmA|AaA`BiAnCeAxCgBzEo@hBk@pAo@fAo@`Au@x@w@t@}BzAaJvFkKvG_B`AiBhAqBnAwAx@aL`HgNvIsD`CcBlAs@r@e@|@_@bAaBhH_@lAgCvFaBdEeHpNkAtCsApCo@rAY^eEjCcF|CkEfC_ChAqHlDu@VuKnC}IvBkHfBiBb@MPAVh@bEPVLFLB`AUHA");
        ROUTE_POLYLINE.put("late night silver",         "_`gsFjxqaSfG_BPQD[Q_BUcBDWLI|Cy@~D}@lEeAjFuApAi@tCwArFkCfL{G`DuBl@u@fBoD`DeHz@kBA[C]m@s@Um@?eFFc@ROpCBdA@b@NPDTM|BmFx@qBh@qBp@aD\\qAj@qAn@u@l@g@vCeBxGaEfDaCdC}AlCgBrEmCzIqF~FuD`OiJzBuAxB}AvB{Bp@cAn@kA`AaC|AeEzCiIp@qAr@gAxBkCzAkANc@Kg@YmAC]AY?w@?_E?y@EWK@CN@dG@hBFr@Ah@Gp@eCbCu@z@s@dAk@dAu@jB}@dCiEdLa@|@g@v@{@fAy@x@gBrAgBhAyWrPkVdOgOpJsD~BmA`AgAdBkBtHi@jBsAzCuDpIkDfH{BnEg@zAkAfC{@fBy@x@kFdDwJzF_MvFgKlC}GbBuFrAkG~AIHEJ?Nd@vDFXNLRDfAUHA");
        ROUTE_POLYLINE.put("late night gold",           "y_gsFhxqaSdGaBNMD[i@iEBMJIjK}BhJ_CdAe@`LiFfJoFzFmD\\[Va@bFqKd@eAx@oB@]CWW]WUSYEW@aF@YHQLEzEBTLBZcG|LmChGqBlEc@t@i@`@gQrK{MdGcKlCcE`AaE~@UKS_@gA_JgAkIGM[E_J~BIPAXjEf]JPTHfAUNC");
        ROUTE_POLYLINE.put("discovery express loop",    "emesFdyjaS@vB`AtF^hDfJIRDDdLNzCAvKEbC}AvLS~Eo@tMA`ZGzLTd@LwO?_MBmKLoBj@yOh@_DdAoHFsUIkc@e@}FsCZiB|@eAr@aAbAg@bA_@~@_@rC@N");
        ROUTE_POLYLINE.put("stampede",                  "edfsF`tkaSBrL???`H??e@@??sAC??cAC???gA???i@???qA???wA???w@??AkC???yA???C???qC???yA???sC???_@??@cC??DaE??@_A???[???]??@yA??@}C???eH???uC???sA??AcC???qB??Ns@??BK??@C??@E??BE??DI??NQ??Z]??h@I??ZG??RC??x@G??bAG??t@C??|@???x@D??Z@??hAB??~@@??l@???jAC??d@A??v@G??`@E??NC??^G??\\I??l@Q??NE??XK??h@S??^C??PA??F???J???F???D@??D@??FB??DB??DB??HF??HF??XZ??r@zB??n@lD??RxA??LzA??Dv@??Br@??@L??@N??@R??Df@??@V??LnB??B|A???rC???T???lA???V??A~@??GrA??IlB??AhC???`@???b@???\\??AjC??@jB??FlC??DhB???lA??@vB??HT??EfB??AjA??Cj@??CfB??{@tH??UbBA?Gt@@?KjC??YvG??Cf@??AX??AJ??K`C??ClB??C~C???~B??@dF??EfI??AtC??Cz@???|A??@hC???fC???xC???jG???L??@J??@H??BF??BD??DF??FF??HD??b@F??zHA??\\@??HB??JJ??J\\??BM??FO??HG??HC??HA??j@???xCB??J@??HB??RJ??D@B@CAB@{AbD??qAnC??OL??GD??G@??I@??I???EA??EA??[]??IK??IQ??CM??AM???O??@oE");
    }
}
