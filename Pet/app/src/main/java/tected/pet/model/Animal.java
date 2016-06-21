package tected.pet.model;

import io.realm.RealmObject;

/**
 * Created by erick on 18/06/16.
 */
public class Animal extends RealmObject {
    private String nome, caracteristicas;

    public Animal(String nome, String caracteristicas) {
        this.nome = nome;
        this.caracteristicas = caracteristicas;
    }

    public Animal(){
        nome = caracteristicas = null;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }
}
