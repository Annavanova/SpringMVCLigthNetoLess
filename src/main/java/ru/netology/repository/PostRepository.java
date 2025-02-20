package ru.netology.repository;


import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class PostRepository {
    private final Map<Long, Post> posts = new ConcurrentHashMap<>(){};
    private final AtomicLong counter = new AtomicLong(0L);

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() != 0 && !posts.containsKey(post.getId())) {
            throw new NotFoundException();
        }

        if (post.getId() == 0) {
            var newId = counter.incrementAndGet();
            post.setId(newId);
        }

        posts.put(post.getId(), post);
        return post;
    }

    public void removeById(long id) {
        if (!posts.containsKey(id))
            throw new NotFoundException();
        posts.remove(id);
    }
/*    private final AtomicLong postID;
    private final ConcurrentHashMap<Long, Post> posts;

    public PostRepository() {
        postID = new AtomicLong(0);
        posts = new ConcurrentHashMap<>();
    }

    public List<Post> all() {
        if (posts.isEmpty()) {
            return Collections.emptyList();
        } else {
            Collection<Post> values = posts.values();
            return values.stream()
                    .filter(x -> !x.isRemoved())
                    .collect(Collectors.toList());
        }
    }
    public Optional<Post> getById(long id) {
        if (!posts.get(id).isRemoved()) {
            try {
                return Optional.ofNullable(posts.get(id));
            } catch (Exception exception) {
                throw new NotFoundException(exception);
            }
        }
        return Optional.empty();
    }

    public Post save(Post post) {
        long postExistingID = post.getId();
        if (postExistingID > 0 && posts.containsKey(postExistingID)) {
            if (!posts.get(postExistingID).isRemoved()) {
                posts.replace(postExistingID, post);
            } else {
                throw new NotFoundException("Элемент не найден");
            }
        } else {
            long newPostId = postExistingID == 0 ? postID.incrementAndGet() : postExistingID;
            post.setId(newPostId);
            posts.put(newPostId, post);
        }
        return post;
    }

    public Optional<Post> removeById(long id) {
        if (!posts.get(id).isRemoved()) {
            try {
                posts.get(id).setRemoved(true);
                return Optional.ofNullable(posts.get(id));
            } catch (Exception exception) {
                throw new NotFoundException(exception);
            }
        } else {
            throw new NotFoundException("Элемент удален");
        }
    }*/
}
