package com.sunlight.tronsdk;

import com.alibaba.fastjson.JSONObject;
import com.sunlight.tronsdk.address.AccountResource;
import com.sunlight.tronsdk.address.AddressHelper;
import com.sunlight.tronsdk.utils.TokenConverter;
import com.zhy.http.okhttp.OkHttpUtils;
import okhttp3.MediaType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static com.sunlight.tronsdk.constant.CoinConstant.TRX_DECIMAL;

/**
 * TRX查询工具类
 *
 * @author: sunlight
 * @date: 2020/7/28 11:04
 */
public class TrxQuery {

    /**
     * 查询最新区块数据
     *
     * @return 数据
     * @throws Exception 异常
     */
    public static String getLatestBlock() throws Exception {
        String route = "/wallet/getnowblock";

        String rp = OkHttpUtils.postString()
                .url(SdkConfig.getInstance().getNodeServer() + route)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute()
                .body()
                .string();
        return rp;
    }

    public static String getBlockByHeight(BigInteger height) throws Exception {
        String route = "/wallet/getblockbynum";

        Map<String, BigInteger> params = new HashMap<>();
        params.put("num", height);
        String rp = OkHttpUtils.postString()
                .url(SdkConfig.getInstance().getNodeServer() + route)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute()
                .body()
                .string();
        return rp;
    }

    public static String getTransactionById(String transactionId) throws Exception {
        String route = "/wallet/gettransactionbyid";
        Map<String, String> params = new HashMap<>();
        params.put("value", transactionId);

        String rp = OkHttpUtils.postString()
                .url(SdkConfig.getInstance().getNodeServer() + route)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute()
                .body()
                .string();
        return rp;
    }

    public static String getTransactionInfoById(String transactionId) throws Exception {
        String route = "/wallet/gettransactioninfobyid";

        Map<String, String> params = new HashMap<>();
        params.put("value", transactionId);
        String rp = OkHttpUtils.postString()
                .url(SdkConfig.getInstance().getNodeServer() + route)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute()
                .body()
                .string();
        return rp;
    }

    public static AccountResource getAccountResource(String address) throws Exception {
        String route = "/wallet/getaccountresource";
        Map<String, String> params = new HashMap<>();
        params.put("address", AddressHelper.toHexString(address));
        String rp = OkHttpUtils.postString()
                .url(SdkConfig.getInstance().getNodeServer() + route)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute()
                .body()
                .string();
        JSONObject result = JSONObject.parseObject(rp);
        Long freeNetUsed = result.getLong("freeNetUsed");
        Long freeNetLimit = result.getLong("freeNetLimit");
        Long energyUsed = result.getLong("EnergyUsed");
        Long energyLimit = result.getLong("EnergyLimit");
        Long totalNetLimit = result.getLong("TotalNetLimit");
        Long totalNetWeight = result.getLong("TotalNetWeight");
        Long totalEnergyLimit = result.getLong("TotalEnergyLimit");
        Long totalEnergyWeight = result.getLong("TotalEnergyWeight");
        return new AccountResource(
                freeNetUsed == null ? 0L : freeNetUsed,
                freeNetLimit == null ? 0L : freeNetLimit,
                energyUsed == null ? 0L : energyUsed,
                energyLimit == null ? 0L : energyLimit,
                totalNetLimit == null ? 0L : totalNetLimit,
                totalNetWeight == null ? 0L : totalNetWeight,
                totalEnergyLimit == null ? 0L : totalEnergyLimit,
                totalEnergyWeight == null ? 0L : totalEnergyWeight
        );
    }

    /**
     * 查询地址带宽余额
     *
     * @param address 地址（API设计成这样,莫法）
     * @return
     * @throws Exception
     */
    public static Long getAddressNetBalance(String address) throws Exception {
        AccountResource accountResource = getAccountResource(address);
        return accountResource.getFreeNetLimit() - accountResource.getFreeNetUsed();
    }

    /**
     * 查询地址能量余额
     *
     * @param address 地址（API设计成这样,莫法）
     * @return
     * @throws Exception
     */
    public static Long getAddressEnergyBalance(String address) throws Exception {
        AccountResource accountResource = getAccountResource(address);
        return accountResource.getEnergyLimit() - accountResource.getEnergyUsed();
    }

    /**
     * 查询带宽费率,也就是冻结1个TRX能换取多少资源
     *
     * @param address 地址（API设计成这样,莫法）
     * @return
     * @throws Exception
     */
    public static Long getNetRate(String address) throws Exception {
        AccountResource accountResource = getAccountResource(address);
        return accountResource.getTotalNetLimit() / accountResource.getTotalNetWeight();
    }

    /**
     * 查询能量费率,也就是冻结1个TRX能换取多少资源
     *
     * @param address 地址（API设计成这样,莫法）
     * @return
     * @throws Exception
     */
    public static Long getEnergyRate(String address) throws Exception {
        AccountResource accountResource = getAccountResource(address);
        return accountResource.getTotalEnergyLimit() / accountResource.getTotalEnergyWeight();
    }

    public static BigDecimal getTrxBalance(String address) throws Exception {
        String route = "/wallet/getaccount";
        Map<String, Object> params = new HashMap<>();
        params.put("address", address);
        params.put("visible", true);


        String rp = OkHttpUtils.postString()
                .url(SdkConfig.getInstance().getNodeServer() + route)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute()
                .body()
                .string();
        JSONObject body = JSONObject.parseObject(rp);
        if (body.isEmpty()) {
            return BigDecimal.ZERO;
        } else {
            return TokenConverter.tokenBigIntegerToBigDecimal(body.getBigInteger("balance"), TRX_DECIMAL);
        }
    }


}
