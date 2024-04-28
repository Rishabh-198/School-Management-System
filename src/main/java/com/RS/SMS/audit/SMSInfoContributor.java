package com.RS.SMS.audit;


import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SMSInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, String> eazyMap = new HashMap<String, String>();
        eazyMap.put("App Name", "RS School");
        eazyMap.put("App Description", "RS School Web Application for Students and Admin");
        eazyMap.put("App Version", "1.0.0");
        eazyMap.put("Contact Email", "info@rssms.com");
        eazyMap.put("Contact Mobile", "+91 9026202255");
        builder.withDetail("RSsms-info", eazyMap);
    }

}