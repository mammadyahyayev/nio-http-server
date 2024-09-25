package az.caspian.nserv.http;

public interface HttpRequestListener {

  void onRequest(HttpRequest request, HttpResponse response);
}
