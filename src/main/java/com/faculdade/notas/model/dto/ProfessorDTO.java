package com.faculdade.notas.model.dto;

public class ProfessorDTO {

    private int indice;
    private String nomeProfessor;
    private String nomeMateria;
    private String[] rotulosNotasIniciais;

    public ProfessorDTO(int indice, String nomeProfessor, String nomeMateria, String[] rotulosNotasIniciais) {
        this.indice = indice;
        this.nomeProfessor = nomeProfessor;
        this.nomeMateria = nomeMateria;
        this.rotulosNotasIniciais = rotulosNotasIniciais;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

    public String getNomeProfessor() {
        return nomeProfessor;
    }

    public void setNomeProfessor(String nomeProfessor) {
        this.nomeProfessor = nomeProfessor;
    }

    public String getNomeMateria() {
        return nomeMateria;
    }

    public void setNomeMateria(String nomeMateria) {
        this.nomeMateria = nomeMateria;
    }

    public String[] getRotulosNotasIniciais() {
        return rotulosNotasIniciais;
    }

    public void setRotulosNotasIniciais(String[] rotulosNotasIniciais) {
        this.rotulosNotasIniciais = rotulosNotasIniciais;
    }
}
