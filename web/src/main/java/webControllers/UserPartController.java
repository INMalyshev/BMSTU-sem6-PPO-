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
public class UserPartController extends BaseController {
    
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

    @GetMapping(value = "/")
    public ModelAndView index(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("common/index");

        RuntimeProfile profile = getProfile(session);
        modelAndView.addObject("profile", profile);
        
        return modelAndView;
    }

    @GetMapping(value = "/user/{id}")
    public ModelAndView getUpdateUserFom(
        HttpSession session,
        @RequestParam(name ="withError", defaultValue = "false") String withError,
        @RequestParam(name ="legend", defaultValue = "") String errLegend,
        @RequestParam(name ="message", defaultValue = "") String errMessage, 
        @PathVariable int id)
    {
        try {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("common/certain_user");

            RuntimeProfile profile = getProfile(session);

            modelAndView.addObject("withError", Boolean.valueOf(withError));
            modelAndView.addObject("legend", errLegend);
            modelAndView.addObject("message", errMessage);

            User user = userService.getUser(id, profile.getUser().getID());
            modelAndView.addObject("user", user);
            
            return modelAndView;
        } catch (CoreException e) {
            
            return null;
        }
    }

    @PostMapping(value = "/user/{id}")
    public RedirectView updateUser(
        HttpSession session, 
        RedirectAttributes attributes,
        @ModelAttribute("CertainUserForm") CertainUserForm signInForm,
        @PathVariable int id
        )
    {

        try {
            String name = signInForm.getName();
            RuntimeProfile profile = getProfile(session);

            if (name.length() < 1) {
                throw new NumberFormatException();
            }

            User user = userService.getUser(id, profile.getUser().getID());
            user.setName(name);
            userService.alterUser(user, profile.getUser().getID());
            session.setAttribute("profile", profile);
            profile.setUser(user);

            session.setAttribute("profile", profile);

            return new RedirectView("/");

        } catch (NumberFormatException e) {
            attributes.addAttribute("withError", true);
            attributes.addAttribute("legend", "Неверный формат ввода");
            attributes.addAttribute("message", "Имя должно содержать не менее одного символа");

            return new RedirectView("/user/" + id);
        } catch (CoreException e) {
            attributes.addAttribute("withError", true);
            attributes.addAttribute("legend", "Операция не выполнена.");
            attributes.addAttribute("message", "Попробуйте еще.");

            return new RedirectView("/user/" + id);
        }
    }

    @GetMapping(value = "/user/{id}/trainings")
    public ModelAndView getUserTrainingsView(
        HttpSession session,
        RedirectAttributes attributes,
        @RequestParam(name ="order", defaultValue = "") String order,
        @PathVariable int id)
    {
        try {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("common/trainings");

            RuntimeProfile profile = getProfile(session);

            int reqId = profile.getUser().getID();
            Training[] trainings;
            
            if (order.equals("planned")) {
                trainings = trainingService.getPlannedTrainingsByUserID(id, reqId);
            } else if (order.equals("done")) {
                trainings = trainingService.getDoneTrainingsByUserID(id, reqId);
            } else {
                trainings = trainingService.getTrainingsByUserID(id, reqId);
            }

            modelAndView.addObject("profile", profile);
            modelAndView.addObject("trainings", trainings);
            
            return modelAndView;
        } catch (CoreException e) {
            
            return null;
        }
    }

    @GetMapping(value = "/training/{id}")
    public ModelAndView getDitailTrainingView(
        HttpSession session,
        @PathVariable int id)
    {
        try {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("common/training");

            RuntimeProfile profile = getProfile(session);

            Training training = trainingService.getTrainingByID(id, profile.getUser().getID());
            Approach[] approachs = trainingService.getApproachByTrainingID(id, profile.getUser().getID());

            modelAndView.addObject("profile", profile);
            modelAndView.addObject("training", training);
            modelAndView.addObject("approachs", approachs);
            
            return modelAndView;
        } catch (CoreException e) {
            
            return null;
        }
    }

    @GetMapping(value = "/training/{id}/delete")
    public RedirectView removeTraining(
        HttpSession session, 
        @PathVariable int id
        )
    {

        try {
            RuntimeProfile profile = getProfile(session);

            int reqId = profile.getUser().getID();

            trainingService.removeTraining(id, reqId);

            return new RedirectView("/user/" + reqId + "/trainings" );

        } catch (CoreException e) {

            return new RedirectView("/");
        }
    }

    @GetMapping(value = "/approach/{id}/delete")
    public RedirectView removeApproach(
        HttpSession session, 
        @PathVariable int id
        )
    {

        try {
            RuntimeProfile profile = getProfile(session);
            Approach a = trainingService.getApproachByID(id, profile.getUser().getID());

            trainingService.removeApproach(id, profile.getUser().getID());

            return new RedirectView("/training/" + a.GetTrainingID());

        } catch (CoreException e) {
            return new RedirectView("/");
        }
    }

    @GetMapping(value = "/approach/{id}")
    public ModelAndView getApproachView(
        HttpSession session,
        @RequestParam(name ="withError", defaultValue = "false") String withError,
        @RequestParam(name ="legend", defaultValue = "") String errLegend,
        @RequestParam(name ="message", defaultValue = "") String errMessage, 
        @PathVariable int id)
    {
        try {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("common/update_approach");

            RuntimeProfile profile = getProfile(session);

            modelAndView.addObject("withError", Boolean.valueOf(withError));
            modelAndView.addObject("legend", errLegend);
            modelAndView.addObject("message", errMessage);

            Approach approach = trainingService.getApproachByID(id, profile.getUser().getID());
            modelAndView.addObject("approach", approach);
            
            return modelAndView;
        } catch (CoreException e) {
            
            return null;
        }
    }

    @PostMapping(value = "/approach/{id}")
    public RedirectView updateApproach(
        HttpSession session, 
        RedirectAttributes attributes,
        @ModelAttribute("form") OneFieldForm form,
        @PathVariable int id
        )
    {

        try {
            int amount = Integer.valueOf(form.getField1());
            RuntimeProfile profile = getProfile(session);
            int reqId = profile.getUser().getID();

            if (amount < 1) {
                throw new NumberFormatException();
            }

            Approach approach = trainingService.getApproachByID(id, reqId);
            approach.setAmount(amount);
            approach.setCompleted(true);
            trainingService.alterApproach(approach, reqId);

            return new RedirectView("/training/" + approach.GetTrainingID());

        } catch (NumberFormatException e) {
            attributes.addAttribute("withError", true);
            attributes.addAttribute("legend", "Неверный формат ввода");
            attributes.addAttribute("message", "Количество должно быть целым положительным числом");

            return new RedirectView("/approach/" + id);
        } catch (CoreException e) {
            attributes.addAttribute("withError", true);
            attributes.addAttribute("legend", "Операция не выполнена.");
            attributes.addAttribute("message", "Попробуйте еще.");

            return new RedirectView("/approach/" + id);
        }
    }

    @GetMapping(value = "/user/{id}/training")
    public RedirectView createTraining(
        HttpSession session, 
        @PathVariable int id
        )
    {

        try {
            RuntimeProfile profile = getProfile(session);

            int tId = trainingService.createTraining(id, profile.getUser().getID());

            return new RedirectView("/training/" + tId);

        } catch (CoreException e) {

            return new RedirectView("/");
        }
    }

    @GetMapping(value = "/training/{id}/approach")
    public ModelAndView getApproachCreateForm(
        HttpSession session,
        @PathVariable int id,
        @RequestParam(name ="withError", defaultValue = "false") String withError,
        @RequestParam(name ="legend", defaultValue = "") String errLegend,
        @RequestParam(name ="message", defaultValue = "") String errMessage)
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("common/create_approach_form");

        modelAndView.addObject("withError", Boolean.valueOf(withError));
        modelAndView.addObject("legend", errLegend);
        modelAndView.addObject("message", errMessage);

        try {
            modelAndView.addObject("exerciseTypes", exerciseTypeService.getExerciseTypeList());
        } catch (CoreException e) {}
        
        modelAndView.addObject("trainingId", id);

        return modelAndView;
    }

    @PostMapping(value = "/training/{id}/approach")
    public RedirectView createApproach(
        HttpSession session, 
        RedirectAttributes attributes,
        @PathVariable int id,
        @ModelAttribute("form") TwoFieldsForm form
        )
    {
        try {
            int amount = Integer.valueOf(form.getField2());

            if (amount < 1) {
                throw new NumberFormatException();
            }

            ExerciseType exerciseType = exerciseTypeMap(form.getField1());
            
            if (exerciseType == null) {
                throw new NumberFormatException();
            }

            RuntimeProfile profile = getProfile(session);
            int reqId = profile.getUser().getID();
            int approachId = trainingService.createApproach(id, reqId);

            Approach approach = trainingService.getApproachByID(approachId, reqId);
            approach.setAmount(amount);
            approach.setType(exerciseType);
            trainingService.alterApproach(approach, reqId);

            return new RedirectView("/training/" + id);

        } catch (NumberFormatException e) {
            attributes.addAttribute("withError", true);
            attributes.addAttribute("legend", "Неверный формат ввода числа");
            attributes.addAttribute("message", "Введите целое положительное число");

            return new RedirectView("/training/" + id + "/approach");
        } catch (CoreException e) {
            attributes.addAttribute("withError", true);
            attributes.addAttribute("legend", "Операция не выполнена.");
            attributes.addAttribute("message", "Попробуйте еще.");

            return new RedirectView("/training/" + id + "/approach");
        }
    }

    @GetMapping(value = "/training/{id}/finish")
    public RedirectView finishTraining(
        HttpSession session, 
        @PathVariable int id
        )
    {

        try {
            RuntimeProfile profile = getProfile(session);
            int reqId = profile.getUser().getID();

            Training training = trainingService.getTrainingByID(id, reqId);
            training.SetCompleted(true);
            trainingService.alterTraining(training, reqId);

            return new RedirectView("/user/" + training.GetHolderUserID() + "/trainings" );

        } catch (CoreException e) {

            return new RedirectView("/");
        }
    }

    @GetMapping(value = "/user/{id}/requests")
    public Object showUserRequestsList(
        HttpSession session, 
        @PathVariable int id
        )
    {
        try {
            RuntimeProfile profile = getProfile(session);
            int reqId = profile.getUser().getID();

            Request[] requests = null;

            if (profile.getUser().getRole() == Role.SignedUser) {
                requests = requestService.getRequestsBySignedUserId(id, reqId);
            }

            if (profile.getUser().getRole() == Role.Trainer) {
                requests = requestService.getRequestsByTrainerId(id, reqId);
            }

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("common/user_requests_list_wiew");
            modelAndView.addObject("profile", profile);
            modelAndView.addObject("requests", requests);

            return modelAndView;

        } catch (CoreException e) {
            return new RedirectView("/");
        }
    }

    @GetMapping(value = "/request/{id}/delete")
    public RedirectView removeRequest(
        HttpSession session, 
        @PathVariable int id
        )
    {

        try {
            RuntimeProfile profile = getProfile(session);
            Request request = requestService.getRequest(id, profile.getUser().getID());

            if (request == null) {
                throw new CoreException();
            }

            requestService.removeRequest(id, profile.getUser().getID());

            return new RedirectView("/user/" + profile.getUser().getID() + "/requests");

        } catch (CoreException e) {
            return new RedirectView("/");
        }
    }

    @GetMapping(value = "/user/{id}/request/create")
    public ModelAndView getCreateRequestForm(
        HttpSession session, 
        @PathVariable int id,
        @RequestParam(name ="withError", defaultValue = "false") String withError,
        @RequestParam(name ="legend", defaultValue = "") String errLegend,
        @RequestParam(name ="message", defaultValue = "") String errMessage) 
    {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("common/create_request_form");
        modelAndView.addObject("id", id);

        RuntimeProfile profile = getProfile(session);
        modelAndView.addObject("profile", profile);

        User[] trainers = new User[]{};

        try {
            trainers = userService.getTrainers(profile.getUser().getID());
        } catch (CoreException e) {}

        modelAndView.addObject("trainers", trainers);

        modelAndView.addObject("withError", Boolean.valueOf(withError));
        modelAndView.addObject("legend", errLegend);
        modelAndView.addObject("message", errMessage);
        
        return modelAndView;
    }

    @PostMapping(value = "/user/{id}/request/create")
    public RedirectView postCreateRequestForm(
        HttpSession session, 
        RedirectAttributes attributes,
        @PathVariable int id,
        @ModelAttribute("form") TwoFieldsForm form
        )
    {

        try {
            int trainerId = Integer.valueOf(form.getField1());
            RuntimeProfile profile = getProfile(session);
            String message = form.getField2();

            User trainer = userService.getUser(trainerId, trainerId);
            if (trainer == null) { throw new CoreException(); }
            int requestId = requestService.createRequest(profile.getUser().getID());
            Request request = requestService.getRequest(requestId, profile.getUser().getID());

            request.setUserFromId(id);
            request.setUserToId(trainerId);
            request.setSatisfied(false);
            request.setMessage(message);

            requestService.alterRequest(request, profile.getUser().getID());
            
            return new RedirectView("/user/" + profile.getUser().getID() + "/requests");

        } catch (NumberFormatException e) {
            attributes.addAttribute("withError", true);
            attributes.addAttribute("legend", "Неверный формат ввода");
            attributes.addAttribute("message", "'" + form.getField1() + "' не является числом");

            return new RedirectView("/user/" + id + "/request/create");
        } catch (CoreException e) {
            attributes.addAttribute("withError", true);
            attributes.addAttribute("legend", "Что-то пошло не так");
            attributes.addAttribute("message", "Попробуйте позже");

            return new RedirectView("/user/" + id + "/request/create");
        }
    }

}
