package com.example.writesphere.controller;
import com.example.writesphere.service.UserService;
import com.example.writesphere.model.Post;
import com.example.writesphere.model.User;
import com.example.writesphere.service.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    // Home page - show all posts
    @GetMapping("/home")
    public String homePage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        // Refresh user from database every time
        User freshUser = userService.findByUsername(loggedInUser.getUsername());
        session.setAttribute("loggedInUser", freshUser);
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        model.addAttribute("user", freshUser);
        return "home";
    }

    // Show create post page
    @GetMapping("/post/create")
    public String createPostPage(HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
        return "create-post";
    }

    // Handle create post form
    @PostMapping("/post/create")
    public String createPost(@RequestParam String title,
                             @RequestParam String content,
                             @RequestParam String type,
                             HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setType(type);
        post.setUser(loggedInUser);
        postService.savePost(post);
        return "redirect:/home";
    }

    // View single post
    @GetMapping("/post/{id}")
    public String viewPost(@PathVariable Long id,
                           HttpSession session,
                           Model model) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
        Optional<Post> post = postService.getPostById(id);
        if (post.isPresent()) {
            model.addAttribute("post", post.get());
            return "view-post";
        }
        return "redirect:/home";
    }

    // Show edit post page
    @GetMapping("/post/edit/{id}")
    public String editPostPage(@PathVariable Long id, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Optional<Post> post = postService.getPostById(id);
        if (post.isEmpty() || !isOwner(post.get(), loggedInUser)) {
            return "redirect:/home";
        }
        model.addAttribute("post", post.get());
        return "edit-post";
    }

    // Handle edit post form
    @PostMapping("/post/edit/{id}")
    public String editPost(@PathVariable Long id,
                           @RequestParam String title,
                           @RequestParam String content,
                           @RequestParam String type,
                           HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Optional<Post> existingPost = postService.getPostById(id);
        if (existingPost.isEmpty() || !isOwner(existingPost.get(), loggedInUser)) {
            return "redirect:/home";
        }
        Post post = existingPost.get();
        post.setTitle(title);
        post.setContent(content);
        post.setType(type);
        postService.updatePost(post);
        return "redirect:/home";
    }

    // Profile page
    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        List<Post> userPosts = postService.getPostsByUser(loggedInUser);
        model.addAttribute("user", loggedInUser);
        model.addAttribute("posts", userPosts);
        model.addAttribute("totalPosts", userPosts.size());
        model.addAttribute("totalBlogs", userPosts.stream()
                .filter(p -> p.getType().equals("Blog")).count());
        model.addAttribute("totalArticles", userPosts.stream()
                .filter(p -> p.getType().equals("Article")).count());
        return "profile";
    }
    // Search posts
    @GetMapping("/search")
    public String searchPosts(@RequestParam String keyword, HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("posts", postService.searchPosts(keyword));
        model.addAttribute("user", loggedInUser);
        model.addAttribute("keyword", keyword);
        return "search-results";
    }
    // Delete post
    @PostMapping("/post/delete/{id}")
    public String deletePost(@PathVariable Long id, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Optional<Post> post = postService.getPostById(id);
        if (post.isEmpty() || !isOwner(post.get(), loggedInUser)) {
            return "redirect:/home";
        }
        postService.deletePost(id);
        return "redirect:/home";
    }

    // Ownership check used by edit and delete
    private boolean isOwner(Post post, User user) {
        return post.getUser().getId().equals(user.getId());
    }
}