package com.example.airlineticket.controllers;

import com.example.airlineticket.models.*;
import com.example.airlineticket.payload.Request;
import com.example.airlineticket.payload.Response;
import com.example.airlineticket.repositories.OrderRepository;
import com.example.airlineticket.repositories.PriceRepository;
import com.example.airlineticket.repositories.UserRepository;
import com.example.airlineticket.security.JwtAuthenticationFilter;
import com.example.airlineticket.security.JwtTokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/")
public class AgencyController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PriceRepository priceRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    private final String apiKey = "1c9a25ade10dbff62d20d5275413b1cc";
    private String ApiUrl = "http://api.aviationstack.com/v1/flights?access_key=" + apiKey + "&airline_iata=VN";
    private final RestTemplate restTemplate = new RestTemplate();


    @GetMapping("/")
    public String homePage() {
       return "home";
    }
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @PostMapping("/customer/register")
    public @ResponseBody ResponseEntity<?> register(@RequestBody User user) throws Exception {
        if(userRepository.findUserByEmail(user.getEmail()) != null) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("Email already exists");
        } else {
            User newUser = new User(user.getEmail(), passwordEncoder.encode(user.getPassword()), Role.AGENCY);
            userRepository.save(newUser);
            return ResponseEntity.status(HttpStatusCode.valueOf(200)).body("Successfully");
        }
    }
    @GetMapping("/flight/search")
    public String flightSearch(@RequestParam String date, @RequestParam String departure, @RequestParam String arrival, @RequestParam int ADT, @RequestParam int CHD, Model model ) throws JsonProcessingException {
        ApiUrl += "&dep_iata="  + departure;
        ApiUrl += "&arr_iata=" + arrival;
        String jsonResponse = restTemplate.getForObject(ApiUrl,String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode jsonNode =  objectMapper.readTree(jsonResponse);
        List<DataFlight> dataFlights = objectMapper.readValue(jsonNode.get("data").toPrettyString(), new TypeReference<List<DataFlight>>(){});

        System.out.println(dataFlights.get(0).getDeparture().getAirport());
        model.addAttribute("price", 100000);
        model.addAttribute("totalResult", objectMapper.readTree(jsonResponse).get("pagination").get("total").asText());
        model.addAttribute("flights", dataFlights);
        model.addAttribute("date",date);
        model.addAttribute("departure",departure);
        model.addAttribute("arrival",arrival);
        model.addAttribute("ADT", ADT);
        model.addAttribute("CHD",CHD);
        return "flights";
    }
    @GetMapping("/flight/passengerinfo")
    public String passengerInfo(@RequestParam String flight, @RequestParam String date, @RequestParam String departure, @RequestParam String arrival,@RequestParam int ADT, @RequestParam int CHD, Model model) throws JsonProcessingException {
        ApiUrl+= "&flight_iata=" + flight;
        String jsonResponse = restTemplate.getForObject(ApiUrl,String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode jsonNode =  objectMapper.readTree(jsonResponse);
        List<DataFlight> dataFlight = objectMapper.readValue(jsonNode.get("data").toPrettyString(), new TypeReference<List<DataFlight>>(){});
        System.out.println(dataFlight.get(0).getDeparture().getAirport());
        model.addAttribute("flight",dataFlight);
        model.addAttribute("ADT", ADT);
        model.addAttribute("CHD", CHD);

        Duration duration = Duration.between(dataFlight.get(0).getDeparture().getScheduled().toInstant(), dataFlight.get(0).getArrival().getScheduled().toInstant());
        model.addAttribute("durationhour",duration.toHours());
        model.addAttribute("durationmin", duration.toHours() % 60);
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        model.addAttribute("price", 1000000);
        model.addAttribute("priceReal", 1000000);
        model.addAttribute("tax", 500000);
        model.addAttribute("taxReal", 1000000);
        model.addAttribute("totalPrice",500000);
        return "passengerinfo";
    }
    @PostMapping("/flight/passengerinfo")
    public @ResponseBody String payment(@RequestBody OrderDetail orderDetail) throws UnsupportedEncodingException {
        PaymentController paymentController = new PaymentController();
        Payment payment = paymentController.create(orderDetail.getTotalPrice());
        orderDetail.setTxnRef(payment.getTxnRef());
        orderRepository.save(orderDetail);
        return payment.getUrl();
    }
    @GetMapping("/booking/success")
    public String booking(@RequestParam String vnp_TxnRef, HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user =  userRepository.findUserByEmail(customUserDetails.getUsername());
        OrderDetail orderDetail = orderRepository.findByTxnRef(vnp_TxnRef);
        user.getOrderDetailList().add(orderDetail);
        orderDetail.setStatus("done");
        Instant instant = Instant.now();
        orderDetail.setDate(Date.from(instant));
        orderRepository.save(orderDetail);
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("phamductung69@gmail.com");
        msg.setTo(orderDetail.getEmail());
        msg.setSubject("Thanh toán thành công vé máy bay");
        msg.setText("Mã đặt chỗ của bạn là XYZ");
        System.out.println(orderDetail.getEmail());
        javaMailSender.send(msg);
        return "bookingsuccess";
    }
}
