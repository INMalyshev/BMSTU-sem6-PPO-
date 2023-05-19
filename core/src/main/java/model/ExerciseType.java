package model;

public enum ExerciseType {

    PullUp ("Подтягивания"),
    PushUp ("Отжимания"),
    Squat ("Присядания");

    private String title;

    ExerciseType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }
}
