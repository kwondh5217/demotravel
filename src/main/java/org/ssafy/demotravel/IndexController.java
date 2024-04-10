package org.ssafy.demotravel;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/customError")
    public ResponseEntity error(HttpSession httpSession){
        Object user = httpSession.getAttribute("user");
        if(user == null){
            throw new IllegalArgumentException("no session");
        }
        return ResponseEntity.ok("index");
    }
}
