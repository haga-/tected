package tected.pet;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by erick on 16/05/16.
 */
public class Cadastro extends RealmObject {
    private String nomePet;
    private String racaPet;
    private String generoPet;
    private String caracteristicasPet;
    private String nomeDono;
    private String enderecoDono;
    private String ultimoEndereco;
    private String telefone;
    private double latitude,longitude;

    public Cadastro(){
        nomePet = racaPet = generoPet = caracteristicasPet = nomeDono = enderecoDono = ultimoEndereco = telefone = null;
        latitude = longitude = 0;
    }

    public Cadastro(String nomePet, String racaPet, String generoPet, String caracteristicasPet, String nomeDono, String enderecoDono, String ultimoEndereco, String telefone, double latitude, double longitude) {
        this.nomePet = nomePet;
        this.racaPet = racaPet;
        this.generoPet = generoPet;
        this.caracteristicasPet = caracteristicasPet;
        this.nomeDono = nomeDono;
        this.enderecoDono = enderecoDono;
        this.ultimoEndereco = ultimoEndereco;
        this.telefone = telefone;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNomePet() {
        return nomePet;
    }

    public String getRacaPet() {
        return racaPet;
    }

    public String getGeneroPet() {
        return generoPet;
    }

    public String getCaracteristicasPet() {
        return caracteristicasPet;
    }

    public String getNomeDono() {
        return nomeDono;
    }

    public String getEnderecoDono() {
        return enderecoDono;
    }

    public String getUltimoEndereco() {
        return ultimoEndereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
