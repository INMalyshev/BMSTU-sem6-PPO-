package webControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpSession;

import exceptions.*;
import interfaces.services.*;
import model.*;
import webModels.*;

@RestController
public class AuthController extends BaseController {

    @Autowired
    InterfaceUserService userService;

    @Autowired
    InterfaceTrainingPlanService trainingPlanService;

    @Autowired
    InterfaceExerciseTypeService exerciseTypeService;

    @Autowired
    InterfaceTrainingService trainingService;

    @GetMapping(value = "/login")
    public ModelAndView getLogInForm(
        HttpSession session, 
        @RequestParam(name ="withError", defaultValue = "false") String withError,
        @RequestParam(name ="legend", defaultValue = "") String errLegend,
        @RequestParam(name ="message", defaultValue = "") String errMessage) 
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("common/log_in");

        modelAndView.addObject("withError", Boolean.valueOf(withError));
        modelAndView.addObject("legend", errLegend);
        modelAndView.addObject("message", errMessage);
        
        return modelAndView;
    }

    @PostMapping(value = "/login")
    public RedirectView postLogInForm(
        HttpSession session, 
        RedirectAttributes attributes,
        @ModelAttribute("enterUserIdForm") LogInForm logInForm
        )
    {

        try {
            int id = Integer.valueOf(logInForm.getUserId());

            User user = userService.getUser(id, id);
            RuntimeProfile profile = getProfile(session);

            profile.setAuthorized(true);
            profile.setUser(user);

            return new RedirectView("/");

        } catch (NumberFormatException e) {
            attributes.addAttribute("withError", true);
            attributes.addAttribute("legend", "Неверный формат ввода");
            attributes.addAttribute("message", "'" + logInForm.getUserId() + "' не является числом");

            return new RedirectView("/login");
        } catch (CoreException e) {
            attributes.addAttribute("withError", true);
            attributes.addAttribute("legend", "Не удалось найти пользователя");
            attributes.addAttribute("message", "Пользователь с id " + logInForm.getUserId() + " не найден.");

            return new RedirectView("/login");
        }
    }

    @GetMapping(value = "/logout")
    public RedirectView logOut(
        HttpSession session,
        RedirectAttributes attributes
        )
    {
        RuntimeProfile profile = getProfile(session);

        profile.setAuthorized(false);
        profile.setUser(null);

        return new RedirectView("/");
    }

    @GetMapping(value = "/signin")
    public ModelAndView getSignInForm(
        HttpSession session, 
        @RequestParam(name ="withError", defaultValue = "false") String withError,
        @RequestParam(name ="legend", defaultValue = "") String errLegend,
        @RequestParam(name ="message", defaultValue = "") String errMessage) 
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("common/sign_in");

        modelAndView.addObject("withError", Boolean.valueOf(withError));
        modelAndView.addObject("legend", errLegend);
        modelAndView.addObject("message", errMessage);
        
        return modelAndView;
    }

    @PostMapping(value = "/signin")
    public RedirectView signIn(
        HttpSession session, 
        RedirectAttributes attributes,
        @ModelAttribute("enterUserIdForm") SignInForm signInForm
        )
    {

        try {
            String name = signInForm.getName();

            if (name.length() < 1) {
                throw new NumberFormatException();
            }

            int id = userService.createUser();
            User user = userService.getUser(id, id);
            user.setName(name);
            userService.alterUser(user, id);

            RuntimeProfile profile = getProfile(session);
            profile.setAuthorized(true);
            profile.setUser(user);

            return new RedirectView("/");

        } catch (NumberFormatException e) {
            attributes.addAttribute("withError", true);
            attributes.addAttribute("legend", "Неверный формат ввода");
            attributes.addAttribute("message", "Имя должно содержать не менее одного символа");

            return new RedirectView("/signin");
        } catch (CoreException e) {
            attributes.addAttribute("withError", true);
            attributes.addAttribute("legend", "Операция не выполнена.");
            attributes.addAttribute("message", "Попробуйте еще.");

            return new RedirectView("/signin");
        }
    }
    
}
