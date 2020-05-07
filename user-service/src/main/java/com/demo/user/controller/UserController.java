package com.demo.user.controller;

import com.demo.user.service.UserService;
import com.demo.user.domain.CommonResult;
import com.demo.user.domain.User;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ApiOperation("create user")
    public ResponseEntity<Object> create(@RequestBody User user) {
        userService.create(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}", headers = "api-version=1")
    public User getUser(@PathVariable long id) {
        return userService.getUser(id);
    }

    @GetMapping(value = "/{id}", headers = "api-version=2")
    public CommonResult<User> getUser2(@PathVariable long id) {
        User user = userService.getUser(id);
        return new CommonResult<>(user);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation("get user by id")
    @ApiImplicitParam(name = "api-version", allowableValues = "1,2", paramType = "header")
    public CommonResult<User> getUserLatest(@PathVariable long id) {
        return getUser2(id);
    }

    @GetMapping
    @ApiOperation("find user")
    @ApiResponses({
            @ApiResponse(code = 400, message = "Required parameters [ids] or [username]")
    })
    public Object getUserByIds(
            @RequestParam(required = false) List<Long> ids,
            @RequestParam(required = false) String username
    ) {
        if (ids != null) {
            return userService.getUserByIds(ids);
        }
        if (username != null) {
            return userService.getByUsername(username);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping
    @ApiOperation("update user")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "user", required = true, paramType = "body", dataTypeClass = User.class, value = "{ \"username\": \"hello\" }",
                    examples = @Example(@ExampleProperty(value = "{ \"username\": \"hello\" }", mediaType = MediaType.APPLICATION_JSON_VALUE)))
    )
    public ResponseEntity<Object> update(@RequestBody User user) {
        userService.update(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("delete user")
    public ResponseEntity<Object> delete(@PathVariable long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
