package controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @RequestMapping("/helloWorld")
    public String helloWorld(){
        System.out.println("HelloController.helloWorld");
        return "/index.jsp";
    }
}
