package com.example.student_3.todolist.validators;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Student_3 on 09/11/2017.
 */

public class Validator<T> {

    private abstract static class Rule<T> {

        protected String message;

        public Rule(String message) {
            this.message = message;
        }

        public abstract boolean validate(T value);
    }

    private abstract static class CriteriaRule<T, C> extends Rule<T> {

        protected C criteria;

        public CriteriaRule(String message, C criteria) {
            super(message);
            this.criteria = criteria;
        }
    }

    private Set<Rule<T>> rules;
    private ArrayList<String> messages;

    private Validator() {
        rules = new HashSet<>();
        messages = new ArrayList<>();
    }

    public @Nullable String getLastMessage() {
        return messages.isEmpty() ? null : messages.get(messages.size() - 1);
    }

    public boolean validate(T value, String... fieldName) {
        boolean result = true;
        messages.clear();
        for (Rule<T> rule : rules) {
            if (!rule.validate(value)) {
                result = false;
                String message = rule.message;
                if (fieldName != null && fieldName.length > 0) {
                    message = String.format("%s %s", fieldName[0], rule.message);
                }
                messages.add(message);
            }
        }
        return result;
    }

    public static class StringValidatorBuilder {
        Validator<String> validator;
        private Rule<String> notEmptyRule;
        private Rule<String> notEmptyOrWhiteSpaceRule;
        private CriteriaRule<String, Integer> minLenghtRule;

        private Rule<String> getNotEmptyRule(String... messages) {
            String message = getMessageString(messages, "cannot be blank");
            if (notEmptyRule != null) {
                notEmptyRule.message = message;
            } else {
                notEmptyRule = new Rule<String>(message) {
                    @Override
                    public boolean validate(String value) {
                        return !TextUtils.isEmpty(value);
                    }
                };
            }
            return notEmptyRule;
        }

        private Rule<String> getNotEmptyOrWhiteSpaceRule(String... messages) {
            String message = getMessageString(messages, "cannot be blank");
            if (notEmptyOrWhiteSpaceRule != null) {
                notEmptyOrWhiteSpaceRule.message = message;
            } else {
                notEmptyOrWhiteSpaceRule = new Rule<String>(message) {
                    @Override
                    public boolean validate(String value) {
                        return !TextUtils.isEmpty(value) && !TextUtils.isEmpty(value.trim());
                    }
                };
            }
            return notEmptyOrWhiteSpaceRule;
        }

        private CriteriaRule<String, Integer> getMinLenghtRule(Integer value, String... messages) {
            String message = getMessageString(messages, String.format("must contain more than %d symbols", value));
            if(minLenghtRule != null){
                minLenghtRule.message = message;
                minLenghtRule.criteria = value;
            } else {
                minLenghtRule = new CriteriaRule<String, Integer>(message, value) {
                    @Override
                    public boolean validate(String value) {
                        return !TextUtils.isEmpty(value) && value.length() >= this.criteria;
                    }
                };
            }
            return minLenghtRule;
        }

        private String getMessageString(String[] messages, String defaultMessage) {
            String message = "";
            if(messages == null || messages.length == 0){
                message = defaultMessage;
            } else {
                message = messages[0];
            }
            return message;
        }

        public StringValidatorBuilder(){
            validator = new Validator<String>();
        }

        public StringValidatorBuilder setNotEmpty(String ... message){
            validator.rules.add(getNotEmptyRule(message));
            return this;
        }

        public StringValidatorBuilder setNotEmptyorWhiteSpace(String ... message){
            validator.rules.add(getNotEmptyOrWhiteSpaceRule(message));
            return this;
        }

        public StringValidatorBuilder setMinLength(int minLength, String ... message){
            validator.rules.add(getMinLenghtRule(minLength, message));
            return this;
        }

        public Validator<String> build(){
            return validator;
        }
    }

    public static class NumberValidatorBuilder<T extends Number & Comparable<? super T>> {
        Validator<T> validator;
        private CriteriaRule<T, T> minNumberRule;
        private CriteriaRule<T, T> maxNumberRule;
        private T maxNumber;
        private T minNumber;

        public NumberValidatorBuilder(){
            validator = new Validator<T>();
        }

        private CriteriaRule<T, T> getMinNumberRule(T value, String ... messages){
            String message = getMessageString(messages, String.format("must be bigger than %s", value));
            if(minNumberRule != null){
                minNumberRule.message = message;
                minNumberRule.criteria = value;
            } else {
                minNumberRule = new CriteriaRule<T, T>(message, value) {
                    @Override
                    public boolean validate(T value) {
                        return value.compareTo(criteria) >= 0;
                    }
                };
            }
            return minNumberRule;
        }

        private CriteriaRule<T, T> getMaxNumberRule(T value, String ... messages){
            String message = getMessageString(messages, String.format("must be smaller than %s", value));
            if(maxNumberRule != null){
                maxNumberRule.message = message;
                maxNumberRule.criteria = value;
            } else {
                maxNumberRule = new CriteriaRule<T, T>(message, value) {
                    @Override
                    public boolean validate(T value) {
                        return value.compareTo(criteria) <= 0;
                    }
                };
            }
            return maxNumberRule;
        }

        private String getMessageString(String[] messages, String defaultMessage) {
            String message = "";
            if(messages == null || messages.length == 0){
                message = defaultMessage;
            } else {
                message = messages[0];
            }
            return message;
        }

        public NumberValidatorBuilder setMinNumber(T minNumber, String ... message){
            if(this.maxNumber != null && this.maxNumber.compareTo(minNumber) <= 0){
                this.minNumber = this.maxNumber;
                message = message.length > 0 ? message : new String[]{String.format("must be exactly %s",
                        this.maxNumber)};
                validator.rules.add(getMinNumberRule(this.minNumber, message));
            } else {
                this.minNumber = minNumber;
            }
            validator.rules.add(getMinNumberRule(this.minNumber, message));
            return this;
        }

        public NumberValidatorBuilder setMaxNumber(T maxNumber, String ... message){
            if(this.minNumber != null && this.minNumber.compareTo(maxNumber) >= 0){
                this.maxNumber = this.minNumber;
                message = message.length > 0 ? message : new String[]{String.format("must be exactly %s",
                        this.maxNumber)};
                validator.rules.add(getMinNumberRule(this.maxNumber, message));
            } else {
                this.maxNumber = maxNumber;
            }
            validator.rules.add(getMaxNumberRule(this.maxNumber, message));
            return this;
        }

        public NumberValidatorBuilder setRange(T minNumber, T maxNumber, String ... messages){
            if(minNumber.compareTo(maxNumber) > 0){
                this.maxNumber = minNumber;
                this.minNumber = maxNumber;
            } else {
                this.maxNumber = maxNumber;
                this.minNumber = minNumber;
            }
            validator.rules.add(getMinNumberRule(this.minNumber, messages));
            validator.rules.add(getMaxNumberRule(this.maxNumber, messages));
            return this;
        }

        public Validator<? extends Number> build(){
            return validator;
        }
    }

    public static class DateValidatorBuilder {
        Validator<Date> validator;
        private Rule<Date> notExpiredRule;

        public DateValidatorBuilder() {
            validator = new Validator<>();
        }

        private Rule<Date> getNotExpiredRule(String... messages) {
            String message = getMessageString(messages, "the date has expired");
            if(notExpiredRule != null){
                notExpiredRule.message = message;
            } else {
                notExpiredRule = new Rule<Date>(message) {
                    @Override
                    public boolean validate(Date value) {
                        return new Date().before(value);
                    }
                };
            }
            return notExpiredRule;
        }

        public DateValidatorBuilder setNotExpiredRule(String... message) {
            validator.rules.add(getNotExpiredRule(message));
            return this;
        }

        public Validator<Date> build(){
            return validator;
        }

        private String getMessageString(String[] messages, String defaultMessage) {
            String message = "";
            if(messages == null || messages.length == 0){
                message = defaultMessage;
            } else {
                message = messages[0];
            }
            return message;
        }
    }
}