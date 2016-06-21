package tected.pet.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * Created by erick on 20/06/16.
 */
public class TimelineItem extends RealmObject implements Parcelable {
    private String titulo, descricao, data, tipo, link;

    protected TimelineItem(Parcel in) {
        titulo = in.readString();
        descricao = in.readString();
        data = in.readString();
        tipo = in.readString();
        link = in.readString();
    }

    public TimelineItem(){
        titulo = descricao = data = tipo = link = null;
    }

    public static final Creator<TimelineItem> CREATOR = new Creator<TimelineItem>() {
        @Override
        public TimelineItem createFromParcel(Parcel in) {
            return new TimelineItem(in);
        }

        @Override
        public TimelineItem[] newArray(int size) {
            return new TimelineItem[size];
        }
    };

    public TimelineItem(String titulo, String descricao, String data, String tipo, String link) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.data = data;
        this.tipo = tipo;
        this.link = link;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titulo);
        dest.writeString(descricao);
        dest.writeString(data);
        dest.writeString(tipo);
        dest.writeString(link);
    }
}
