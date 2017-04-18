package model;

/**
 * Created by Chres on 18-04-2017.
 */
public class EBattleAvatar {

    EMinion eMinion;

    EBattleAvatar(EMinion eMinion){
        this.eMinion = eMinion;
    }

    EBattleAvatar(){
    }

    EMinion getEMinion(){ return eMinion; }
}
