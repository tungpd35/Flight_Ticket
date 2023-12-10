package com.example.airlineticket.controllers;

import com.example.airlineticket.models.*;
import com.example.airlineticket.repositories.AgencyRepository;
import com.example.airlineticket.repositories.CustomerRepository;
import com.example.airlineticket.repositories.OrderRepository;
import com.example.airlineticket.repositories.UserRepository;
import com.example.airlineticket.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.NumberFormat;
import java.time.Instant;
import java.util.*;

@Controller
@RequestMapping("/")
public class AirlineController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @GetMapping("/airline")
    public String airlineHome(Model model) {
        List<OrderDetail> orderDetailList = orderRepository.findAll();
        List<User> users = userRepository.findAll();
        List<Customer> customerList = customerRepository.findAll();
        List<Long> listrevenue = new ArrayList<>();
        List<Integer> revenueMonth = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for(int i=0;i<=11;i++) {
            int finalI = i;
            listrevenue.add(orderDetailList.stream().filter(orderDetail -> orderDetail.getDate().getMonth() == finalI).count()) ;
        }
        for(int i=0;i<=11;i++) {
            int finalI = i;
            revenueMonth.add(orderDetailList.stream().filter(orderDetail -> orderDetail.getDate().getMonth()==finalI).mapToInt(OrderDetail::getTotalPrice).sum() / 1000000);
        }
        System.out.println(listrevenue.size());
        int count = 0;
        for (User user:users) {
            if (user.getRole()==Role.AGENCY) {
                count+=1;
            }
        }
        int revenue = 0;
        int totalTicket = 0;
        Instant instant = Instant.now();
        Date currentDate = Date.from(instant);
        for (OrderDetail order:orderDetailList ) {
            if(order.getStatus().equals("done") && order.getDate().getDay() == currentDate.getDay() ) {
                revenue += order.getTotalPrice();
                totalTicket += 1;
            }
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        model.addAttribute("revenue", numberFormat.format(revenue));
        model.addAttribute("ticketnumber", totalTicket);
        model.addAttribute("agencynumber", count);
        model.addAttribute("customernumber", customerList.size());
        model.addAttribute("listticketnumber", listrevenue);
        model.addAttribute("revenuemonth", revenueMonth);
        return  "airlinehome";
    }
    @GetMapping("/airline/list-agency")
    public String listAgency(Model model) {
        List<User> users = userRepository.findAllByRole(Role.AGENCY);
        model.addAttribute("agencies", users);
        return "airlineagencyman";
    }
    @GetMapping("/airline/agency/detail")
    public String listAgencyDetail(Model model, @RequestParam Long id) {
        User users = userRepository.findUserById(id);
        model.addAttribute("agencies", users);
        int revenue = 0;
        List<OrderDetail> orderDetailList = users.getOrderDetailList();
        List<Long> listrevenue = new ArrayList<>();
        List<Integer> revenueMonth = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for(int i=0;i<=11;i++) {
            int finalI = i;
            listrevenue.add(orderDetailList.stream().filter(orderDetail -> orderDetail.getDate().getMonth() == finalI).count()) ;
        }
        for(int i=0;i<=11;i++) {
            int finalI = i;
            revenueMonth.add(orderDetailList.stream().filter(orderDetail -> orderDetail.getDate().getMonth()==finalI).mapToInt(OrderDetail::getTotalPrice).sum() / 1000000);
        }
        int totalTicket = 0;
        Instant instant = Instant.now();
        Date currentDate = Date.from(instant);
        for (OrderDetail order:orderDetailList ) {
            if(order.getStatus().equals("done") && order.getDate().getDay() == currentDate.getDay() ) {
                revenue += order.getTotalPrice();
                totalTicket += 1;
            }
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        model.addAttribute("revenue", numberFormat.format(revenue));
        model.addAttribute("ticketnumber", totalTicket);
        model.addAttribute("listticketnumber", listrevenue);
        model.addAttribute("revenuemonth", revenueMonth);
        model.addAttribute("customernumber", orderDetailList.size());
        return "agencydetail";
    }
    @GetMapping("/error")
    public @ResponseBody String errorPage(HttpServletResponse response) {
        if (response.getStatus() == 403) {
            return "Xin lỗi bạn không có quyền!";
        }
        else {
            return "Lỗi trang";
        }
    }
    @PostMapping("/airline/register")
    public @ResponseBody ResponseEntity<?> register(@RequestBody User user) throws Exception {
        if(userRepository.findUserByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("Email already exists");
        } else {
            User newUser = new User(user.getEmail(), passwordEncoder.encode(user.getPassword()), Role.AIRLINE);
            userRepository.save(newUser);
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body("Successfully");
        }
    }
}
