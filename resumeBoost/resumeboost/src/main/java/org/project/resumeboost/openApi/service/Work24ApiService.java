package org.project.resumeboost.openApi.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface Work24ApiService {

  JsonNode getWork24Data(int page, int display);

}
