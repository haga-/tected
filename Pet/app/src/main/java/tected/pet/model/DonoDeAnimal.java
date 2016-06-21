package tected.pet.model;

import io.realm.RealmList;

/**
 * Created by erick on 18/06/16.
 */
public class DonoDeAnimal extends Usuario {
    private String nome, email;
    private RealmList<Animal> animais;

    public DonoDeAnimal(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public DonoDeAnimal(){
        nome = email = null;
        animais = null;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public RealmList<Animal> getAnimais() {
        return animais;
    }
}
