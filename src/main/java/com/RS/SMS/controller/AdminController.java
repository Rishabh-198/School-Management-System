package com.RS.SMS.controller;

import com.RS.SMS.Repository.CoursesRepository;
import com.RS.SMS.Repository.PersonRepository;
import com.RS.SMS.Repository.SmsClassRepository;
import com.RS.SMS.model.Courses;
import com.RS.SMS.model.Person;
import com.RS.SMS.model.SmsClass;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    SmsClassRepository smsClassRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    CoursesRepository coursesRepository;

    @RequestMapping("/displayClasses")
    public ModelAndView displayClasses(Model model) {
        List<SmsClass> smsClasses = smsClassRepository.findAll();
        ModelAndView modelAndView = new ModelAndView("classes.html");
        modelAndView.addObject("smsClasses",smsClasses);
        modelAndView.addObject("smsClass", new SmsClass());
        return modelAndView;
    }

    @PostMapping("/addNewClass")
    public ModelAndView addNewClass(Model model, @ModelAttribute("smsClass") SmsClass smsClass) {
        smsClassRepository.save(smsClass);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
        return modelAndView;
    }

    @RequestMapping("/deleteClass")
    public ModelAndView deleteClass(Model model, @RequestParam int id) {
        Optional<SmsClass> smsClass = smsClassRepository.findById(id);
        for(Person person : smsClass.get().getPersons()){
            person.setSmsClass(null);
            personRepository.save(person);
        }
        smsClassRepository.deleteById(id);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
        return modelAndView;
    }

    @GetMapping("/displayStudents")
    public ModelAndView displayStudents(Model model, @RequestParam int classId, HttpSession session,
                                        @RequestParam(value = "error", required = false) String error) {
        String errorMessage = null;
        ModelAndView modelAndView = new ModelAndView("students.html");
        Optional<SmsClass> smsClass = smsClassRepository.findById(classId);
        modelAndView.addObject("smsClass",smsClass.get());
        modelAndView.addObject("person",new Person());
        session.setAttribute("smsClass",smsClass.get());
        if(error != null) {
            errorMessage = "Invalid Email entered!!";
            modelAndView.addObject("errorMessage", errorMessage);
        }
        return modelAndView;
    }

    @PostMapping("/addStudent")
    public ModelAndView addStudent(Model model, @ModelAttribute("person") Person person, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        SmsClass smsClass = (SmsClass) session.getAttribute("smsClass");
        Person personEntity = personRepository.readByEmail(person.getEmail());
        if(personEntity==null || !(personEntity.getPersonId()>0)){
            modelAndView.setViewName("redirect:/admin/displayStudents?classId="+smsClass.getClassId()
                    +"&error=true");
            return modelAndView;
        }
        personEntity.setSmsClass(smsClass);
        personRepository.save(personEntity);
        smsClass.getPersons().add(personEntity);
        smsClassRepository.save(smsClass);
        modelAndView.setViewName("redirect:/admin/displayStudents?classId="+smsClass.getClassId());
        return modelAndView;
    }

    @GetMapping("/deleteStudent")
    public ModelAndView deleteStudent(Model model, @RequestParam int personId, HttpSession session) {
        SmsClass smsClass = (SmsClass) session.getAttribute("smsClass");
        Optional<Person> person = personRepository.findById(personId);
        person.get().setSmsClass(null);
        smsClass.getPersons().remove(person.get());
        SmsClass smsClassSaved = smsClassRepository.save(smsClass);
        session.setAttribute("smsClass",smsClassSaved);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayStudents?classId="+smsClass.getClassId());
        return modelAndView;
    }


    @GetMapping("/displayCourses")
    public ModelAndView displayCourses(Model model) {

        //This will give in the order in which we added
        // List<Courses> courses = coursesRepository.findAll();

        //By using Sorting of JPA we can retrieve in our own order

        /* This is Static sorting
        List<Courses> courses = coursesRepository.findByOrderByName(); //Default order is ascending
        //or          List<Courses> courses = coursesRepository.findByOrderByNameDesc();
        */

        // This is dynamic sorting. We use sort class here
        List<Courses> courses = coursesRepository.findAll(Sort.by("name").descending());




        ModelAndView modelAndView = new ModelAndView("courses_secure.html");
        modelAndView.addObject("courses",courses);
        modelAndView.addObject("course", new Courses());
        return modelAndView;
    }

    @PostMapping("/addNewCourse")
    public ModelAndView addNewCourse(Model model, @ModelAttribute("course") Courses course) {
        ModelAndView modelAndView = new ModelAndView();
        coursesRepository.save(course);
        modelAndView.setViewName("redirect:/admin/displayCourses");
        return modelAndView;
    }


    @GetMapping("/viewStudents")
    public ModelAndView viewStudents(Model model, @RequestParam int id
            ,HttpSession session,@RequestParam(required = false) String error) {
        String errorMessage = null;
        ModelAndView modelAndView = new ModelAndView("course_students.html");
        Optional<Courses> courses = coursesRepository.findById(id);
        modelAndView.addObject("courses",courses.get());
        modelAndView.addObject("person",new Person());
        session.setAttribute("courses",courses.get());
        if(error != null) {
            errorMessage = "Invalid Email entered!!";
            modelAndView.addObject("errorMessage", errorMessage);
        }
        return modelAndView;
    }

    @PostMapping("/addStudentToCourse")
    public ModelAndView addStudentToCourse(Model model, @ModelAttribute("person") Person person,
                                           HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        Courses courses = (Courses) session.getAttribute("courses");
        Person personEntity = personRepository.readByEmail(person.getEmail());
        if(personEntity==null || !(personEntity.getPersonId()>0)){
            modelAndView.setViewName("redirect:/admin/viewStudents?id="+courses.getCourseId()
                    +"&error=true");
            return modelAndView;
        }
        personEntity.getCourses().add(courses);
        courses.getPersons().add(personEntity);
        personRepository.save(personEntity);
        session.setAttribute("courses",courses);
        modelAndView.setViewName("redirect:/admin/viewStudents?id="+courses.getCourseId());
        return modelAndView;
    }

    @GetMapping("/deleteStudentFromCourse")
    public ModelAndView deleteStudentFromCourse(Model model, @RequestParam int personId,
                                                HttpSession session) {
        Courses courses = (Courses) session.getAttribute("courses");
        Optional<Person> person = personRepository.findById(personId);
        person.get().getCourses().remove(courses);
        courses.getPersons().remove(person);
        personRepository.save(person.get());
        session.setAttribute("courses",courses);
        ModelAndView modelAndView = new
                ModelAndView("redirect:/admin/viewStudents?id="+courses.getCourseId());
        return modelAndView;
    }
}
