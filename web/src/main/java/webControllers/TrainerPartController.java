package webControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
public class TrainerPartController extends BaseController {

    @Autowired
    InterfaceUserService userService;

    @Autowired
    InterfaceTrainingPlanService trainingPlanService;

    @Autowired
    InterfaceExerciseTypeService exerciseTypeService;

    @Autowired
    InterfaceTrainingService trainingService;

    @Autowired
    InterfaceRequestService requestService;

    @GetMapping(value = "/user/{id}/training_plans")
    public ModelAndView getTrainingPlansView(
        HttpSession session,
        @PathVariable int id)
    {
        try {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("common/training_plan_list");

            RuntimeProfile profile = getProfile(session);

            TrainingPlan[] trainingPlans = trainingPlanService.getTrainingPlanByUserID(id, profile.getUser().getID());

            modelAndView.addObject("profile", profile);
            modelAndView.addObject("trainingPlans", trainingPlans);
            
            return modelAndView;
        } catch (CoreException e) {
            
            return null;
        }
    }

    @GetMapping(value = "/training_plan/{id}")
    public ModelAndView getTrainingPlanInfoView(
        HttpSession session,
        @PathVariable int id)
    {
        try {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("common/training_plan_info");

            RuntimeProfile profile = getProfile(session);

            TrainingPlan trainingPlan = trainingPlanService.getTrainingPlanByID(id, profile.getUser().getID());
            ApproachPlan[] approachPlans = trainingPlanService.getApproachPlanByTrainingPlanID(id, profile.getUser().getID());

            modelAndView.addObject("profile", profile);
            modelAndView.addObject("trainingPlan", trainingPlan);
            modelAndView.addObject("approachPlans", approachPlans);
            
            return modelAndView;
        } catch (CoreException e) {
            
            return null;
        }
    }

    @GetMapping(value = "/user/{id}/training_plan")
    public RedirectView createTrainingPlan(
        HttpSession session, 
        @PathVariable int id
        )
    {

        try {
            RuntimeProfile profile = getProfile(session);

            int tpId = trainingPlanService.createTrainingPlan(id, profile.getUser().getID());

            return new RedirectView("/training_plan/" + tpId);

        } catch (CoreException e) {

            return new RedirectView("/");
        }
    }

    @GetMapping(value = "/training_plan/{id}/delete")
    public RedirectView deleteTrainingPlan(
        HttpSession session, 
        @PathVariable int id
        )
    {

        try {
            RuntimeProfile profile = getProfile(session);

            trainingPlanService.removeTrainingPlan(id, profile.getUser().getID());

            return new RedirectView("/" );

        } catch (CoreException e) {

            return new RedirectView("/");
        }
    }

    @GetMapping(value = "/training_plan/{id}/approach_plan")
    public ModelAndView getApproachPlanCreateForm(
        HttpSession session,
        @PathVariable int id,
        @RequestParam(name ="withError", defaultValue = "false") String withError,
        @RequestParam(name ="legend", defaultValue = "") String errLegend,
        @RequestParam(name ="message", defaultValue = "") String errMessage)
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("common/create_approach_plan_form");

        modelAndView.addObject("withError", Boolean.valueOf(withError));
        modelAndView.addObject("legend", errLegend);
        modelAndView.addObject("message", errMessage);

        try {
            modelAndView.addObject("exerciseTypes", exerciseTypeService.getExerciseTypeList());
        } catch (CoreException e) {}
        
        modelAndView.addObject("trainingPlanId", id);

        return modelAndView;
    }

    @PostMapping(value = "/training_plan/{id}/approach_plan")
    public RedirectView createApproachPlan(
        HttpSession session, 
        RedirectAttributes attributes,
        @PathVariable int id,
        @ModelAttribute("form") CreateApproachPlanForm createApproachPlanForm
        )
    {
        try {
            int expectedAmount = Integer.valueOf(createApproachPlanForm.getFormNumber());

            if (expectedAmount <= 0) {
                throw new NumberFormatException();
            }

            ExerciseType exerciseType = exerciseTypeMap(createApproachPlanForm.getFormString());
            
            if (exerciseType == null) {
                throw new NumberFormatException();
            }

            RuntimeProfile profile = getProfile(session);
            int reqId = profile.getUser().getID();
            int approachPlanId = trainingPlanService.createApproachPlan(id, reqId);

            ApproachPlan approachPlan = trainingPlanService.getApproachPlanByID(approachPlanId, reqId);
            approachPlan.setExpectedAmount(expectedAmount);
            approachPlan.setType(exerciseType);
            trainingPlanService.alterApproachPlan(approachPlan, reqId);

            return new RedirectView("/training_plan/" + id);

        } catch (NumberFormatException e) {
            attributes.addAttribute("withError", true);
            attributes.addAttribute("legend", "Неверный формат ввода числа");
            attributes.addAttribute("message", "Введите целое положительное число");

            return new RedirectView("/training_plan/" + id + "/approach_plan");
        } catch (CoreException e) {
            attributes.addAttribute("withError", true);
            attributes.addAttribute("legend", "Операция не выполнена.");
            attributes.addAttribute("message", "Попробуйте еще.");

            return new RedirectView("/training_plan/" + id + "/approach_plan");
        }
    }

    @GetMapping(value = "/approach_plan/{id}/delete")
    public RedirectView removeApproachPlan(
        HttpSession session, 
        @PathVariable int id
        )
    {

        try {
            RuntimeProfile profile = getProfile(session);
            ApproachPlan ap = trainingPlanService.getApproachPlanByID(id, profile.getUser().getID());

            trainingPlanService.removeApproachPlan(id, profile.getUser().getID());

            return new RedirectView("/training_plan/" + ap.getTrainingPlanID());

        } catch (CoreException e) {
            return new RedirectView("/");
        }
    }

    @GetMapping(value = "/training_plan/{id}/assign")
    public ModelAndView getAssignTrainingPlanToUserForm(
        HttpSession session, 
        @PathVariable int id,
        @RequestParam(name ="withError", defaultValue = "false") String withError,
        @RequestParam(name ="legend", defaultValue = "") String errLegend,
        @RequestParam(name ="message", defaultValue = "") String errMessage) 
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("common/assign_training_plan");

        RuntimeProfile profile = getProfile(session);
        modelAndView.addObject("profile", profile);

        User[] users = new User[]{};

        try {
            users = userService.getSignedUsers(profile.getUser().getID());
        } catch (CoreException e) {}

        modelAndView.addObject("users", users);

        modelAndView.addObject("withError", Boolean.valueOf(withError));
        modelAndView.addObject("legend", errLegend);
        modelAndView.addObject("message", errMessage);
        
        return modelAndView;
    }

    @PostMapping(value = "/training_plan/{id}/assign")
    public RedirectView assignTrainingPlanToUser(
        HttpSession session, 
        RedirectAttributes attributes,
        @PathVariable int id,
        @ModelAttribute("enterUserIdForm") LogInForm logInForm
        )
    {

        try {
            int userId = Integer.valueOf(logInForm.getUserId());
            RuntimeProfile profile = getProfile(session);

            trainingService.createTraininByTrainingPlanID(userId, id, profile.getUser().getID());

            return new RedirectView("/training_plan/" + id);

        } catch (NumberFormatException e) {
            attributes.addAttribute("withError", true);
            attributes.addAttribute("legend", "Неверный формат ввода");
            attributes.addAttribute("message", "'" + logInForm.getUserId() + "' не является числом");

            return new RedirectView("/training_plan/" + id + "/assign");
        } catch (CoreException e) {
            attributes.addAttribute("withError", true);
            attributes.addAttribute("legend", "Не удалось найти пользователя");
            attributes.addAttribute("message", "Пользователь с id " + logInForm.getUserId() + " не найден.");

            return new RedirectView("/training_plan/" + id + "/assign");
        }
    }

    @GetMapping(value = "/request/{id}/assign")
    public ModelAndView getAssignRequestForm(
        HttpSession session, 
        @PathVariable int id,
        @RequestParam(name ="withError", defaultValue = "false") String withError,
        @RequestParam(name ="legend", defaultValue = "") String errLegend,
        @RequestParam(name ="message", defaultValue = "") String errMessage) 
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("common/assign_request_form");
        modelAndView.addObject("id", id);

        RuntimeProfile profile = getProfile(session);
        modelAndView.addObject("profile", profile);

        TrainingPlan[] plans = new TrainingPlan[]{};

        try {
            plans = trainingPlanService.getTrainingPlanByUserID(profile.getUser().getID(), profile.getUser().getID());
        } catch (CoreException e) {}

        modelAndView.addObject("plans", plans);

        modelAndView.addObject("withError", Boolean.valueOf(withError));
        modelAndView.addObject("legend", errLegend);
        modelAndView.addObject("message", errMessage);
        
        return modelAndView;
    }

    @PostMapping(value = "/request/{id}/assign")
    public RedirectView postAssignRequestForm(
        HttpSession session, 
        RedirectAttributes attributes,
        @PathVariable int id,
        @ModelAttribute("form") OneFieldForm form
        )
    {

        try {
            int trainingPlanId = Integer.valueOf(form.getField1());
            RuntimeProfile profile = getProfile(session);
            Request request = requestService.getRequest(id, profile.getUser().getID());

            trainingService.createTraininByTrainingPlanID(request.getUserFromId(), trainingPlanId, profile.getUser().getID());
            request.setSatisfied(true);

            requestService.alterRequest(request, profile.getUser().getID());

            return new RedirectView("/user/" + profile.getUser().getID() + "/requests");

        } catch (NumberFormatException e) {
            attributes.addAttribute("withError", true);
            attributes.addAttribute("legend", "Неверный формат ввода");
            attributes.addAttribute("message", "'" + form.getField1() + "' не является числом");

            return new RedirectView("/request/" + id + "/assign");
        } catch (CoreException e) {
            attributes.addAttribute("withError", true);
            attributes.addAttribute("legend", "Что-то пошло не так");
            attributes.addAttribute("message", "Попробуйте позже");

            return new RedirectView("/request/" + id + "/assign");
        }
    }

}

