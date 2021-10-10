package zozo.company.service;

import zozo.company.exception.NotFoundException;
import zozo.company.model.Post;
import zozo.company.repository.PostRepository;

import java.util.List;

public class PostService {
  private final PostRepository repository;

  public PostService(PostRepository repository) {

    this.repository = repository;
  }


  public List<Post> all() {

    return repository.all();
  }

  public Post getById(long id) {

    return repository.getById(id).orElseThrow(NotFoundException::new);
  }

  public Post save(Post post) {

    return repository.save(post);
  }

  public boolean removeById(long id) {

    return repository.removeById(id);
  }
}

