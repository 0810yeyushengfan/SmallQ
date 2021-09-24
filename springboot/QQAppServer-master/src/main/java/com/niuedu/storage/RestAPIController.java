package com.niuedu.storage;

import com.niuedu.model.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RestAPIController {
    private List<Student> studentList=new ArrayList<>();
    public RestAPIController(){
        studentList.add(new Student(0,"xxx",false,new Date()));
        studentList.add(new Student(0,"yyy",false,new Date()));
        studentList.add(new Student(0,"zzz",true,new Date()));
        studentList.add(new Student(0,"ccc",false,new Date()));
    }

    @GetMapping("/get_student")
    public Student getStudent(@RequestParam("id") int index){
        return studentList.get(index);
    }
}
