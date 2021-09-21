package ua.kostya.hw39.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.kostya.hw39.dto.Bet;
import ua.kostya.hw39.service.RaceService;


@Controller
@RequestMapping("")
public class RaceController {
    private final RaceService service;

    public RaceController(RaceService service) {
        this.service = service;
    }

    @GetMapping("/stats")
    public String getStats(Model model) {
        model.addAttribute("stats", service.getStats());
        return "statsView";
    }

    @GetMapping("/race/start")
    public String getRaceStart(Model model) {
        model.addAttribute("bet", new Bet());
        return "raceStartView";
    }

    @GetMapping("/race/{id}")
    public String getRaceInfo(@PathVariable Long id, Model model) {
        model.addAttribute("race", service.getRaceById(id));
        return "raceView";
    }

    @PostMapping("/race/start")
    public String submitRace(@ModelAttribute("bet") Bet bet, Model model) {
        System.out.println(bet);
        String message;

        if (bet.getBetHorsePosition() < 1 || bet.getBetHorsePosition() > bet.getHorsesTotal()) {
            message = "Wrong parameters combination!";
        } else {
            service.runRace(bet);
            message = "Request has been successfully processed!";
        }

        model.addAttribute("message", message);
        return "raceSubmittedView";
    }
}
