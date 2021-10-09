## OkHttp使用

### 构建OkHttpClinet对象

OkHttpClient client = new OkHttpClient();

### 构建RequestBody对象

String json = "{}";
public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
RequestBody body = RequestBody.create(JSON, json);

### 构建Request对象
 Request request = new Request.Builder()
      .url(url)
      .post(body)
      .build();

### 构建Call对象
Call call = mclient.newCall(request);

### 发起请求
//同步
Response response = call.execute();
String result = response.body.string();
//异步
call.enqueue(new okhttp3.Callback() {
                 @Override
                 public void onFailure(Request request, IOException e) {
                     //请求失败
                 }

                 @Override
                 public void onResponse(Response response) throws IOException {
                     //请求成功
                 }
             })