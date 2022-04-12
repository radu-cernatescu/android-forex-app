package ca.senecacollege.dps924.assignment4_forexapp;

public class Currency {

    private String three_letter_form;
    private String long_form;

    Currency(String three_letter_form, String long_form) {
        this.three_letter_form = three_letter_form;
        this.long_form = long_form;
    }

    @Override
    public String toString() {
        return this.three_letter_form + ": " + this.long_form;
    }

    public String getLongForm() {
        return this.long_form;
    }

    public String getShortForm() {
        return this.three_letter_form;
    }
}
