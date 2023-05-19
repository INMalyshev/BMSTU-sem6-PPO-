package webModels;

public class CreateApproachPlanForm {
    private String formNumber;
    private String formString;

    public CreateApproachPlanForm(String formNumber, String formString) {
        this.formNumber = formNumber;
        this.formString = formString;
    }

    public String getFormNumber() {
        return formNumber;
    }

    public void setFormNumber(String formNumber) {
        this.formNumber = formNumber;
    }

    public String getFormString() {
        return formString;
    }

    public void setFormString(String formString) {
        this.formString = formString;
    }
}
