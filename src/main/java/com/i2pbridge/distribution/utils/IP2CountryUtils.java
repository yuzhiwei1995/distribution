package com.i2pbridge.distribution.utils;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

public class IP2CountryUtils {
    private static File database = new File("src/main/java/com/i2pbridge/distribution/utils/GeoLite2-Country.mmdb");
    public static String ip2Country(String ip){
        String result = null;
        try {
            DatabaseReader reader = new DatabaseReader.Builder(database).build();
            InetAddress ipAddress = InetAddress.getByName(ip);
            CountryResponse response = reader.country(ipAddress);

            Country country = response.getCountry();
//            System.out.println(country.getIsoCode());            // 'US'
//            System.out.println(country.getName());               // 'United States'
//            System.out.println(country.getNames().get("zh-CN")); // '美国'
//            result = country.getNames().get("zh-CN");
            result = country.getIsoCode();
        } catch (IOException | GeoIp2Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
