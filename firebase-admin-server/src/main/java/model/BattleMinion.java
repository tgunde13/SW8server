package model;

/**
 * Created by Chres on 18-04-2017.
 */
public class BattleMinion extends Minion {
    int currentHP;
    int currentSpeed;
    int currentPower;

    public BattleMinion(Minion minion) {
        health = minion.health;

        currentHP = minion.health;
        currentSpeed = minion.speed;
        currentPower = minion.power;


    }

}
