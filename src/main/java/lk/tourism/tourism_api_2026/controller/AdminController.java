package lk.tourism.tourism_api_2026.controller;

import lk.tourism.tourism_api_2026.controller.request.*;
import lk.tourism.tourism_api_2026.controller.response.AdminSignInResponse;
import lk.tourism.tourism_api_2026.controller.response.AdminSignOutResponse;
import lk.tourism.tourism_api_2026.controller.response.MakeTourGuideInactiveResponse;
import lk.tourism.tourism_api_2026.service.AdminService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admins")
@AllArgsConstructor
public class AdminController {

    private AdminService adminService;

    @PostMapping(headers = "X-Api-Version=v1")
    void addAdminV1(@RequestBody CreateAdminRequest rq){
        log.trace("received request : {}", rq);
        adminService.create(rq);

    }

    @PostMapping(value = "/sign-in", headers = "X-Api-Version=v1")
    AdminSignInResponse adminSignInV1(@RequestBody AdminSignInRequest rq){
        log.trace("received request : {}", rq);

        return AdminSignInResponse
                .builder()
                .sessionCode(adminService.signIn(rq).getSessionCode())
                .build();

    }

    @PutMapping(value = "/sign-out", headers = "X-Api-Version=v1")
    AdminSignOutResponse adminSignOutV1(@RequestBody AdminSignOutRequest rq){
        log.trace("received request : {}", rq);

        return AdminSignOutResponse
                .builder()
                .isSignedOut(adminService.signOut(rq))
                .build();

    }

    @PostMapping(value = "/{admin-session-code}/tour-guides", headers = "X-Api-Version=v1")
    void addTourGuideV1(@PathVariable("admin-session-code") String adminSessionCode, @RequestBody CreateTourGuideRequest rq){
        log.trace("received path variable : {}", adminSessionCode);
        log.trace("received request : {}", rq);
        adminService.createTourGuide(adminSessionCode, rq);

    }

    @PutMapping(value = "/{admin-session-code}/tour-guides/make-tour-guide-inactive", headers = "X-Api-Version=v1")
    MakeTourGuideInactiveResponse makeTourGuideInactiveV1(@PathVariable("admin-session-code") String adminSessionCode, @RequestBody MakeTourGuideInactiveRequest rq){
        log.trace("received path variable : {}", adminSessionCode);
        log.trace("received request : {}", rq);

        return MakeTourGuideInactiveResponse
                .builder()
                .isTourGuideInactive(adminService.makeTourGuideInactive(adminSessionCode, rq))
                .build();

    }

}
