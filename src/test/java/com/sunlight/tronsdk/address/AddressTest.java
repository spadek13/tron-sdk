package com.sunlight.tronsdk.address;


import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertNotNull;

/**
 * @author: sunlight
 * @date: 2020/7/24 15:59
 */

public class AddressTest {
    private static final Logger LOGGER= LoggerFactory.getLogger(AddressTest.class);

    @Test
    public void testNewAddress() throws Exception {
        Address address = AddressHelper.newAddress();
        LOGGER.info(JSON.toJSONString(address));
        AddressHelper.decodeFromBase58Check("TYCw2XSJuQKWtqNMme4XQJX8MgtZ1R4Xwb");
        String addr =AddressHelper.privateKeyToBase58Address("83d8c98e422d73f035dc21543bce6ce7593830202715a2440553c65bbf55e4a0");

        assertNotNull(address);
        assertNotNull(address.getAddress());
        assertNotNull(address.getPrivateKey());
    }
}
