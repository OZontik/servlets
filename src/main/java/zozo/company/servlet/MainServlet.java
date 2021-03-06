package zozo.company.servlet;

import zozo.company.controller.PostController;
import zozo.company.exception.NotFoundException;
import zozo.company.repository.PostRepository;
import zozo.company.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  public static final String GET = "GET";
  public static final String POST = "POST";
  public static final String DELETE = "DELETE";

  private PostController controller;

  @Override
  public void init() {
    final var repository = new PostRepository();
    final var service = new PostService(repository);
    controller = new PostController(service);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      final var id = getId(path);
      // primitive routing
      if (method.equals(GET)) {
        if (id > 0) {
          controller.getById(id, resp);
        } else {
          controller.all(resp);
        }
        return;
      }
      if (method.equals(POST) && (id == -1)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals(DELETE) && (id > 0)) {
        controller.removeById(id, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      if (e instanceof NotFoundException) {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
      } else {
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      }
    }
  }

  private long getId(String path) {
    return path.matches("/api/posts/\\d+") ? Long.parseLong(path.substring(path.lastIndexOf("/") + 1)) : -1;
  }
}


