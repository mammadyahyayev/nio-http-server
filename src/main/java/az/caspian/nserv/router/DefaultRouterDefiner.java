package az.caspian.nserv.router;

import java.util.List;

public class DefaultRouterDefiner implements RouterDefiner {
  @Override
  public List<Router> getRouters() {
    return List.of(
        new Router("", "index.html"),
        new Router("/", "index.html"),
        new Router("/students", "students.html"),
        new Router("/teachers", "teachers.html")
    );
  }
}
