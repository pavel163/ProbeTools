package com.ebr163.probetools.http;

import java.util.List;
import java.util.Map;

/**
 * Created by Ergashev on 22.12.16.
 */

public interface HttpData {

    String getUrl();

    String getBody();

    List<String> getQueryParams();

    Map<String, String> getHeaders();
}
