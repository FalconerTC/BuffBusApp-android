package com.cherish.bustracker.lib;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Falcon on 9/2/2015.
 * <p/>
 * Adapted from http://stackoverflow.com/questions/6802483/how-to-directly-initialize-a-hashmap-in-a-literal-way
 */
public class Polylines {
    public static final String BUFFBUS_ENCODED_POLYLINE = "wybsFnwkaSAXPRTT@HALMV{@hBu@xAe@fAMVYV]RYF[DqA?aA@kB@sA@oA@qBAo@Bm@Fi@Bu@@eF@uA@yA@sA?cBCeA@{AA}AAgAAo@?e@?[B]Da@He@N]LOJCRERCd@G|@WbCOpA[pBM|@Ip@GjAWfGMhCMrCCnC?~@D~@A~@?t@DJRBN?|C@xA?|A?|@An@?d@Fp@\\`A~AnCxEj@~@Pl@Hb@@n@@`A?`C?pD?x@AdAUL?bBZ\\d@`@RVBRCR[v@Wj@Sd@Sn@i@dAgA`C_AtBiA~BQXWTSBWMWWEKIQ?k@@iB?w@?[A[OKMKYCcBAiBA}A?g@AUIUOIWEw@@cE@qJ@gEB_F?cE?{GBiB?cDBcD\\cHLkD?qADy@Hu@d@}Cl@yDFi@JeABw@@ODKJENGh@QZIZGz@EbAA|@?~A@l@@nB?hBBzA@~BGfA?vD?pACjACnA?bB?v@?xC?pCCt@@\\GZQXWT_@Pi@f@iAd@_Ax@eBb@}@HQCSIKUUMKK?KJ";
    public static final String FOOTBALL_ENCODED_POLYLINE = "uybsFjwkaSC\\PTTTBFAJkA`CcAxB]v@_@`@SLSH_@DU@]ASAIIMWIGk@@w@?GDOTILMB]?UBk@FaA?a@@CDC^?~@@|BAvAGjBIvCUvGWvGI|CC|D`@hK?t@H~AAf@Of@]b@}HhF}@v@k@p@o@lAg@~AQl@q@tC[TUBQAKIIWCY?gB?uB?eFAaBAq@Es@Mm@Um@u@oAcB{C{@_B]c@WSYM_@IoF@qB?g@Go@?MCMIGOIc@?eEBmADoADkANkDJmCBsBBq@Fq@`@mCh@kDNs@LcADgA@a@HKLElA]~@KrCApMDdHApB?dBIr@?nACp@@h@?~A?|A?tB?nA?b@I\\SXYRa@t@eBfBwDd@aABMGIIKWUMKQH";
    public static final String LNG_ENCODED_POLYLINE = "y_gsFhxqaSdGaBNMD[i@iEBMJIjK}BhJ_CdAe@`LiFfJoFzFmD\\[Va@bFqKd@eAx@oB@]CWW]WUSYEW@aF@YHQLEzEBTLBZcG|LmChGqBlEc@t@i@`@gQrK{MdGcKlCcE`AaE~@UKS_@gA_JgAkIGM[E_J~BIPAXjEf]JPTHfAUNC";
    public static final String LNB_ENCODED_POLYLINE = "_`gsFjxqaS~F{APSHSe@qENWlM{CzAc@tEkAf@ShB{@vCwArFeCzLmHzB{Ah@y@lE_Jp@eBz@kB@_@Mg@q@u@EQEUAm@@uD@QHQFAHApEBVFLFLBPMfEkJx@sDx@eD^{@l@aAz@q@~@i@bI}ERWDa@Be@GcACw@SwEK_D@{ALWNO|EEdF@`A^h@\\hBfBh@^XEr@e@hAy@rBkAzFkD`LcHnG_ElBuApAoAlA}An@aAn@uAl@{AdBwExC{H`BqCr@cA`AcAhAcARWFUCc@Qq@Ik@Ck@AwD?eF@ySA_J?sFDqB@q@D]RG~@ADOEMaA@c@HGx@?ly@Bv@Dt@E^C`@iCdCmA|AaA`BiAnCeAxCgBzEo@hBk@pAo@fAo@`Au@x@w@t@}BzAaJvFkKvG_B`AiBhAqBnAwAx@aL`HgNvIsD`CcBlAs@r@e@|@_@bAaBhH_@lAgCvFaBdEeHpNkAtCsApCo@rAY^eEjCcF|CkEfC_ChAqHlDu@VuKnC}IvBkHfBiBb@MPAVh@bEPVLFLB`AUHA";
    public static final String LNS_ENCODED_POLYLINE = "_`gsFjxqaSfG_BPQD[Q_BUcBDWLI|Cy@~D}@lEeAjFuApAi@tCwArFkCfL{G`DuBl@u@fBoD`DeHz@kBA[C]m@s@Um@?eFFc@ROpCBdA@b@NPDTM|BmFx@qBh@qBp@aD\\qAj@qAn@u@l@g@vCeBxGaEfDaCdC}AlCgBrEmCzIqF~FuD`OiJzBuAxB}AvB{Bp@cAn@kA`AaC|AeEzCiIp@qAr@gAxBkCzAkANc@Kg@YmAC]AY?w@?_E?y@EWK@CN@dG@hBFr@Ah@Gp@eCbCu@z@s@dAk@dAu@jB}@dCiEdLa@|@g@v@{@fAy@x@gBrAgBhAyWrPkVdOgOpJsD~BmA`AgAdBkBtHi@jBsAzCuDpIkDfH{BnEg@zAkAfC{@fBy@x@kFdDwJzF_MvFgKlC}GbBuFrAkG~AIHEJ?Nd@vDFXNLRDfAUHA";
    public static final String HOPCLOCK_ENCODED_POLYLINE = "{ggsFzzlaSZMXc@NMJGJChGB`AJXN?Ts@fDKz@A`D?xAItCCnE?vCDpIBp@LN~A@dG?fCBpAAjFAxAAp@@jAFdAJbBX`A\\jBhAp@`@j@Ll@HfF@`A?HB@^?jBAhD@t@IZATFN@fJBnEH^\\\\RBtD@d@Bl@AzA?PHLXApEFl@N^TVLJJLBRETIR{A|Cq@vAMZUn@aBpDYj@CTCZAv@G~@Cl@?`H?dR@`JCRIDwA?cG@iC?q@Fi@R{@f@c@Zc@ViBb@sBh@aCj@uBf@i@Fe@@g@Cq@IcBSs@Ka@?[DuCt@wCr@]BS?QCGGCKk@sEm@cF@MLIvCw@HMFQBUCm@g@sDu@cGw@cGs@iFEOMGQ?}A\\y@PIEGQgAcJg@cFOqAy@iG_AcIKs@_AcIgAuIsAsK[sC}AmMoAoKq@wFkA_JScAu@gGAm@D[NSVErBEnB?`GF`@DPLLZBl@?h@Cb@I`@QVc@Zm@^[^GXAf@P|AF\\JHP?j@QPEtB@v@?TA";
    public static final String HOPCOUNTER_ENCODED_POLYLINE = "}egsFrxlaSk@n@i@Ly@BiBCeARMDSGMmB@a@F[JQVWb@S\\URULSJi@BsA?{@Gk@OUuBA}BLkBBiA?w@@q@B}@B]BELALFr@NtAz@|GtApLdA|HN~@VrBj@`F\\~BTpBV|@tB`Q`BlNb@rDNxAlBnOj@rEp@pFd@fEBXCLaD~@KNAd@`@rDhBdOz@dGNPPHPBfCq@NAFFZlCVzBd@rDH`@RFh@?NA\\CnA_@~D_Aj@GT?~Df@x@Fb@Cb@GfAS|Ae@bCo@nCo@v@[n@g@ROXO\\OXIRE|E@|A?|D?N?FEB_@?}U?yM?}ABm@DKFAP@\\?ZI\\C|EBPA@QAyI?iBASQAOCc@?G?IIBSDQPg@\\y@FWAUG_@[[QWM]As@?}DAQSWGEIC[?aA?oC?wBAUEUUKUC[?_@AmJ?kDBqF@WBU?c@?y@A_@GI]C}C?_BAs@AYEc@I]Qq@a@{@m@_@Wg@Mq@QgAQoAM_AEoC?eEAkC@cBAuHEi@?GGCSAi@AgI?uB@oABuC@aC?_HBYZyA^{AAOIK]K_@Gc@E[?mC?wA?UBMH";
    public static final String BASKETBALL_ENCODED_POLYLINE = "wybsFtwkaSAVFFLJJHFL?JGPqBfE_@t@Ub@W^o@\\SBU@c@?]CW_@eACe@BU`@UHu@Bo@Fs@?s@ZBdCClBa@bPa@fKIpGPlEJjCFzBF~Aa@hAkAz@_F`Dy@f@iAlAm@bA{AbG_BjF_BxDiDpHu@zA_@b@UBSE[UOYCUAcFE[IIIKMGiC@wA?QAkB@]KKKGKGOEU?oE?cI?qC?oCHk@?wA@cD?aKBsC@_FHcD\\_IDeCDmAd@{Cz@yFHw@FkA@OFMjAc@j@Ij@GhC?nC?pCD|ABrBAxBIjB@jD?lBGhB@b@Et@?lBBlBAnCCV?VEZKXURYPa@Vq@l@qA|AcDRg@?OAKe@e@M?KJ";
    public static final String ATHENS_ENCODED_POLYLINE = "{~esFpyoaSARBHDFDDLCFU?uFESEA{I@[ACIC_d@@]?wCBIDGFElHG~ABz@DnBTj@Jx@XxChBjAZb@BxGBH@FPDl]BZDLJNJHRFlJ@DBRRDP?|EF\\DLj@l@L^ARwHtPk@r@mA|@mDxBeHbEUBOAKGGM?q@GcA?yLAi@E_@{@qBY]IGSG[AcG?GSAcECSEEMAGFCT";

    public static final Map<Integer, String> POLYLINE_MAP;

    static {
        POLYLINE_MAP = new HashMap<>();
        POLYLINE_MAP.put(1, BUFFBUS_ENCODED_POLYLINE);
        POLYLINE_MAP.put(2, FOOTBALL_ENCODED_POLYLINE);
        POLYLINE_MAP.put(3, LNG_ENCODED_POLYLINE);
        POLYLINE_MAP.put(4, LNB_ENCODED_POLYLINE);
        POLYLINE_MAP.put(5, LNS_ENCODED_POLYLINE);
        POLYLINE_MAP.put(6, HOPCLOCK_ENCODED_POLYLINE);
        POLYLINE_MAP.put(7, HOPCOUNTER_ENCODED_POLYLINE);
        POLYLINE_MAP.put(8, BASKETBALL_ENCODED_POLYLINE);
        POLYLINE_MAP.put(9, ATHENS_ENCODED_POLYLINE);
    }
}
