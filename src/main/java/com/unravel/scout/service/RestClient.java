package com.unravel.scout.service;

import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RestClient {

  public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

  OkHttpClient client = new OkHttpClient();

  public String get(String url) throws IOException {
    Request request = new Request.Builder().url(url).build();
    try (Response response = client.newCall(request).execute()) {
      return response.body().string();
    }
  }

  public String post(String url, String json) throws IOException {
    RequestBody body = RequestBody.create(json, JSON);
    Request request = new Request.Builder().url(url).post(body).build();
    try (Response response = client.newCall(request).execute()) {
      return response.body().string();
    }
  }
}
