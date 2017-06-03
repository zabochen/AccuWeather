package ua.ck.zabochen.accuweather.model.singleton;

import ua.ck.zabochen.accuweather.model.jackson.condition.Condition;

public class ConditionCity {

    private static ConditionCity conditionCity;
    private static Condition condition;

    private ConditionCity() {
    }

    public static ConditionCity getInstance() {
        if (conditionCity == null) {
            conditionCity = new ConditionCity();
        }
        return conditionCity;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Condition getCondition() {
        return condition;
    }

}
