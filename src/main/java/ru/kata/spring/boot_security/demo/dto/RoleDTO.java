package ru.kata.spring.boot_security.demo.dto;

public class RoleDTO {

    private Long id;
    private String name;

    public RoleDTO() {
    }

    public RoleDTO(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public RoleDTO(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
