package webControllers;

import java.util.Collections;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import model.ExerciseType;
import webModels.RuntimeProfile;

public class BaseController {

    protected RuntimeProfile getProfile(HttpSession session) {
        String profileAttrName = "profile";
        List<String> attrNames = Collections.list(session.getAttributeNames());

        if (!attrNames.contains(profileAttrName)) {
            session.setAttribute(profileAttrName, new RuntimeProfile());
        }

        return (RuntimeProfile) session.getAttribute(profileAttrName);
    }

    protected ExerciseType exerciseTypeMap(String title) {
        ExerciseType[] titles = ExerciseType.values();

        for (ExerciseType elem : titles) {
            if (elem.getTitle().equals(title)) {
                return elem;
            }
        }

        return null;

    }
    
}
